package com.deyo.hypixel.velocity.gamemanager;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.deyo.hypixel.commons.ServerType;
import com.deyo.hypixel.commons.protocol.objects.proxy.from.GivePlayersOriginTypeProtocol;
import com.deyo.hypixel.commons.protocol.objects.proxy.from.PlayerSwitchedProtocol;
import com.deyo.hypixel.commons.text.Text;
import com.deyo.hypixel.velocity.SkyBlockVelocity;
import com.deyo.hypixel.velocity.text.ProxyText;
import com.deyo.hypixel.commons.redis.RedisClient;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public record TransferHandler(Player player) {
	public static final Map<Player, ServerType> playersGoalServerType = new ConcurrentHashMap<>();
	private static final Map<Player, RegisteredServer> playersOriginServer = new ConcurrentHashMap<>();
	private static final Set<Player> disregard = ConcurrentHashMap.newKeySet();
	private static final Map<UUID, ServerType> afkOriginLobbyType = new ConcurrentHashMap<>();
	private static final Set<UUID> afkReturnInProgress = ConcurrentHashMap.newKeySet();

	public boolean isInLimbo() {
		return isInLimbo(player);
	}

	public static boolean isInLimbo(Player player) {
		return playersGoalServerType.containsKey(player);
	}

	public boolean isInAfkLimbo() {
		return isInAfkLimbo(player.getUniqueId());
	}

	public static boolean isInAfkLimbo(UUID playerUuid) {
		return afkOriginLobbyType.containsKey(playerUuid);
	}

	public static void clearPlayerState(UUID playerUuid) {
		afkOriginLobbyType.remove(playerUuid);
		afkReturnInProgress.remove(playerUuid);
	}

	public static void clearPlayer(Player player) {
		playersGoalServerType.remove(player);
		playersOriginServer.remove(player);
		disregard.remove(player);
		clearPlayerState(player.getUniqueId());
	}

	public void addToDisregard() {
		disregard.add(player);
	}

	public void removeFromDisregard() {
		disregard.remove(player);
	}

	public CompletableFuture<Boolean> sendToLimbo() {
		CompletableFuture<Boolean> future = new CompletableFuture<>();

		Thread.startVirtualThread(() -> {
			if (isInLimbo() && player.getCurrentServer()
				.map(server -> server.getServer().equals(SkyBlockVelocity.getLimboServer()))
				.orElse(false)) {
				future.complete(true);
				return;
			}

			if (player.getCurrentServer().isPresent()) {
				RegisteredServer previousServer = player.getCurrentServer().get().getServer();
				playersOriginServer.put(player, previousServer);
			}

			RegisteredServer limboServer = SkyBlockVelocity.getLimboServer();
			player.createConnectionRequest(limboServer).connectWithIndication();
			future.complete(true);
		});

		return future;
	}

	public CompletableFuture<Boolean> sendToLimboFromAfk(ServerType originType) {
		CompletableFuture<Boolean> future = new CompletableFuture<>();

		Thread.startVirtualThread(() -> {
			if (isInAfkLimbo() && player.getCurrentServer()
				.map(server -> server.getServer().equals(SkyBlockVelocity.getLimboServer()))
				.orElse(false)) {
				future.complete(true);
				return;
			}

			ServerType resolvedOriginType = resolveAfkOriginType(originType);
			afkOriginLobbyType.put(player.getUniqueId(), resolvedOriginType);

			if (player.getCurrentServer().isPresent()) {
				RegisteredServer previousServer = player.getCurrentServer().get().getServer();
				playersOriginServer.put(player, previousServer);
			}

			RegisteredServer limboServer = SkyBlockVelocity.getLimboServer();
			player.createConnectionRequest(limboServer).connectWithIndication();
			future.complete(true);
		});

		return future;
	}

	public void returnFromAfkLimbo() {
		Thread.startVirtualThread(() -> {
			UUID uuid = player.getUniqueId();
			if (!afkReturnInProgress.add(uuid)) {
				return;
			}

			try {
				if (!isInAfkLimbo()) {
					return;
				}

				if (player.getCurrentServer().isEmpty()
					|| !player.getCurrentServer().get().getServer().equals(SkyBlockVelocity.getLimboServer())) {
					return;
				}

				ServerType preferredType = afkOriginLobbyType.get(uuid);
				GameManager.GameServer destination = null;

				if (isLobbyType(preferredType)
					&& GameManager.hasType(preferredType)
					&& GameManager.isAnyEmpty(preferredType)) {
					destination = BalanceConfigurations.getServerFor(player, preferredType);
				}

				if (destination == null
					&& GameManager.hasType(ServerType.PROTOTYPE_LOBBY)
					&& GameManager.isAnyEmpty(ServerType.PROTOTYPE_LOBBY)) {
					destination = BalanceConfigurations.getServerFor(player, ServerType.PROTOTYPE_LOBBY);
				}

				if (destination == null) {
					player.sendMessage(Text.of("<c>There are no lobby servers available right now."));
					return;
				}

				afkOriginLobbyType.remove(uuid);
				player.sendMessage(Text.of("<7>Sending to server {}...", destination.displayName()));
				player.createConnectionRequest(destination.registeredServer()).connectWithIndication();
			} finally {
				afkReturnInProgress.remove(uuid);
			}
		});
	}

	public void previousServerIsFinished(RegisteredServer manualPick) {
		Thread.startVirtualThread(() -> {
			if (disregard.contains(player)) return;

			RegisteredServer originServer = playersOriginServer.get(player);
			ServerType originServerType = GameManager.getTypeFromRegisteredServer(originServer);

			UUID serverUUID = UUID.fromString(manualPick.getServerInfo().getName());
			UUID originServerUUID = UUID.fromString(originServer.getServerInfo().getName());

			RedisClient.requestServer(serverUUID,
					new GivePlayersOriginTypeProtocol(),
					new GivePlayersOriginTypeProtocol.Request(
							player.getUniqueId().toString(), originServerType.name()));

			playersGoalServerType.remove(player);
			playersOriginServer.remove(player);

			GameManager.GameServer manualPickAsGame = GameManager.getFromUUID(serverUUID);
			player.sendMessage(Text.of("<7>Sending to server {}...", manualPickAsGame.displayName()));
			player.createConnectionRequest(manualPick).connectWithIndication();

			RedisClient.requestServer(originServerUUID,
					new PlayerSwitchedProtocol(),
					new PlayerSwitchedProtocol.Request(player.getUniqueId().toString()));
		});
	}

	public void previousServerIsFinished() {
		Thread.startVirtualThread(() -> {
			if (disregard.contains(player) || !isInLimbo()) return;

			ServerType type = playersGoalServerType.get(player);
			if (type == null) {
				playersGoalServerType.remove(player);
				playersOriginServer.remove(player);
				return;
			}
			GameManager.GameServer server = BalanceConfigurations.getServerFor(player, type);

			if (server == null) {
				playersGoalServerType.remove(player);
				playersOriginServer.remove(player);
				ProxyText.disconnect(player, "<c>There are no Hypixel (type={}) servers available at the moment.", type.name());
				return;
			}

			RegisteredServer originServer = playersOriginServer.get(player);
			if (originServer == null) {
				playersGoalServerType.remove(player);
				return;
			}
			UUID originServerUUID = UUID.fromString(originServer.getServerInfo().getName());
			UUID sendingToServerUUID = server.internalID();
			ServerType originServerType = GameManager.getTypeFromRegisteredServer(originServer);

			RedisClient.requestServer(sendingToServerUUID,
					new GivePlayersOriginTypeProtocol(),
					new GivePlayersOriginTypeProtocol.Request(
							player.getUniqueId().toString(), originServerType.name()));

			playersOriginServer.remove(player);
			playersGoalServerType.remove(player);

			player.sendMessage(Text.of("<7>Sending to server {}...", server.displayName()));
			player.createConnectionRequest(server.registeredServer()).connectWithIndication();

			RedisClient.requestServer(originServerUUID,
					new PlayerSwitchedProtocol(),
					new PlayerSwitchedProtocol.Request(player.getUniqueId().toString()));
		});
	}

	public void transferTo(ServerType type) {
		if (type == null) {
			return;
		}

		GameManager.GameServer server = BalanceConfigurations.getServerFor(player, type);
		if (server == null) {
			playersGoalServerType.put(player, type);
			sendToLimbo();
			return;
		}

		player.sendMessage(Text.of("<7>Sending to server {}...", server.displayName()));
		transferTo(server.registeredServer());
	}

	public void forceRemoveFromLimbo() {
		playersGoalServerType.remove(player);
		playersOriginServer.remove(player);
		clearPlayerState(player.getUniqueId());
	}

	public CompletableFuture<Void> transferTo(RegisteredServer toTransferTo) {
		CompletableFuture<Void> future = new CompletableFuture<>();
		Thread.startVirtualThread(() -> {
			try {
				RegisteredServer originServer = playersOriginServer.get(player);
				if (originServer == null) {
					player.getCurrentServer().ifPresent(conn -> playersOriginServer.put(player, conn.getServer()));
					originServer = playersOriginServer.get(player);
				}
				ServerType originServerType = GameManager.getTypeFromRegisteredServer(originServer);

				playersGoalServerType.remove(player);
				playersOriginServer.remove(player);

				UUID serverUUID = UUID.fromString(toTransferTo.getServerInfo().getName());

				if (originServer != null && originServerType != null) {
					RedisClient.requestServer(serverUUID,
							new GivePlayersOriginTypeProtocol(),
							new GivePlayersOriginTypeProtocol.Request(
									player.getUniqueId().toString(), originServerType.name()));
				}

				player.createConnectionRequest(toTransferTo).connectWithIndication();
				future.complete(null);
			} catch (Exception e) {
				future.completeExceptionally(e);
			}
		});
		return future;
	}

	static ServerType resolveAfkOriginType(ServerType type) {
		return isLobbyType(type) ? type : ServerType.PROTOTYPE_LOBBY;
	}

	private static boolean isLobbyType(ServerType type) {
		return type != null && type.name().endsWith("_LOBBY");
	}
}

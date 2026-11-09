package com.deyo.hypixel.anticheat.loader.minestom;

import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerPacketEvent;
import net.minestom.server.event.player.PlayerPacketOutEvent;
import net.minestom.server.network.packet.client.ClientPacket;
import net.minestom.server.network.packet.client.common.ClientPongPacket;
import net.minestom.server.network.packet.server.SendablePacket;
import net.minestom.server.network.packet.server.ServerPacket;
import com.deyo.hypixel.anticheat.event.DeyoEventHandler;
import com.deyo.hypixel.anticheat.event.events.AnticheatPacketEvent;
import com.deyo.hypixel.anticheat.event.packet.*;
import com.deyo.hypixel.anticheat.loader.Loader;
import com.deyo.hypixel.anticheat.loader.LoaderPacketHandler;
import com.deyo.hypixel.anticheat.loader.managers.DeyoSchedulerManager;
import com.deyo.hypixel.anticheat.loader.minestom.packets.*;
import org.tinylog.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MinestomLoader extends Loader {
    @Override
    public DeyoSchedulerManager getSchedulerManager() {
        return new MinestomSchedulerManager();
    }

    @Override
    public List<UUID> getOnlinePlayers() {
        List<UUID> players = new ArrayList<>();
        MinecraftServer.getConnectionManager().getOnlinePlayers().forEach(player -> players.add(player.getUuid()));
        return players;
    }

    @Override
    public void onInitialize() {
        GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();

        registerPacketHandler(PositionAndRotationPacket.class,
                new MinestomHandlerPositionAndRotationPacket());
        registerPacketHandler(PositionPacket.class,
                new MinestomHandlerPositionPacket());
        registerPacketHandler(RotationPacket.class,
                new MinestomHandlerRotationPacket());
        registerPacketHandler(RequestPingPacket.class,
                new MinestomHandlerPingRequestPacket());
        registerPacketHandler(PingResponsePacket.class,
                new MinestomHandlerPingResponsePacket());
        registerPacketHandler(EntityActionPacket.class,
                new MinestomHandlerEntityActionPacket());
        registerPacketHandler(AbilitiesPacket.class,
                new MinestomHandlerAbilitiesPacket());
        registerPacketHandler(HeldItemChangePacket.class,
                new MinestomHandlerHeldItemChangePacket());
        registerPacketHandler(BlockDigPacket.class,
                new MinestomHandlerBlockDigPacket());
        registerPacketHandler(BlockPlacePacket.class,
                new MinestomHandlerBlockPlacePacket());
        registerPacketHandler(UseEntityPacket.class,
                new MinestomHandlerUseEntityPacket());
        registerPacketHandler(WindowClickPacket.class,
                new MinestomHandlerWindowClickPacket());
        registerPacketHandler(AnimationPacket.class,
                new MinestomHandlerAnimationPacket());
        registerPacketHandler(SteerVehiclePacket.class,
                new MinestomHandlerSteerVehiclePacket());
        registerPacketHandler(KnockbackPacket.class,
                new MinestomHandlerKnockbackPacket());

        globalEventHandler.addListener(PlayerPacketOutEvent.class, (event) -> {
            ServerPacket packet = event.getPacket();
            LoaderPacketHandler handler = getPacketHandler(packet.getClass());
            if (handler == null) return;

            DeyoPacket deyoPacket = handler.buildDeyoPacket(event.getPlayer().getUuid(), packet);
            if (deyoPacket == null) return;

            DeyoEventHandler.callEvent(new AnticheatPacketEvent(deyoPacket));
        });

        MinecraftServer.getPacketListenerManager().setPlayListener(
                ClientPongPacket.class, (packet, player) -> {
                    LoaderPacketHandler handler = getPacketHandler(ClientPongPacket.class);
                    DeyoPacket deyoPacket = handler.buildDeyoPacket(player.getUuid(), packet);
                    if (deyoPacket == null) return;

                    DeyoEventHandler.callEvent(new AnticheatPacketEvent(deyoPacket));
                });

        globalEventHandler.addListener(PlayerPacketEvent.class, (event) -> {
            ClientPacket packet = event.getPacket();
            LoaderPacketHandler handler = getPacketHandler(packet.getClass());
            if (handler == null) return;

            DeyoPacket deyoPacket = handler.buildDeyoPacket(event.getPlayer().getUuid(), packet);
            if (deyoPacket == null) return;

            DeyoEventHandler.callEvent(new AnticheatPacketEvent(deyoPacket));
        });
    }

    @Override
    public void sendPacket(UUID uuid, DeyoPacket packet) {
        Player player = MinecraftServer.getConnectionManager().getOnlinePlayerByUuid(uuid);
        if (player == null) return;

        try {
            player.sendPacket((SendablePacket) getPacketHandler(packet).buildLoaderPacket(uuid, packet));
        } catch (Exception e) {
            Logger.error(e, "Error when attempting to send packet {} to player {}",
                    packet.getClass().getSimpleName(), uuid);
        }
    }

    @Override
    public void sendMessage(UUID uuid, String message) {
        Player player = MinecraftServer.getConnectionManager().getOnlinePlayerByUuid(uuid);
        if (player == null) return;

        player.sendMessage(message);
    }
}

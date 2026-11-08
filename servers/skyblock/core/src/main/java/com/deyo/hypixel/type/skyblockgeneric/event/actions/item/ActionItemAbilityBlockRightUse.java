package com.deyo.hypixel.type.skyblockgeneric.event.actions.item;

import lombok.SneakyThrows;
import net.minestom.server.event.player.PlayerBlockInteractEvent;
import net.minestom.server.item.ItemStack;
import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.event.phase.EventPhase;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;
import com.deyo.hypixel.type.skyblockgeneric.item.SkyBlockItem;
import com.deyo.hypixel.type.skyblockgeneric.item.components.AbilityComponent;
import com.deyo.hypixel.type.skyblockgeneric.item.handlers.ability.RegisteredAbility;
import com.deyo.hypixel.type.skyblockgeneric.user.PlayerAbilityHandler;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

public class ActionItemAbilityBlockRightUse implements HypixelEventClass {
    @SneakyThrows
    @PhasedEvent(node = EventNodes.PLAYER, requireDataLoaded = true, phase = EventPhase.GAMEPLAY)
    public void run(PlayerBlockInteractEvent event) {
        ItemStack itemStack = event.getPlayer().getItemInMainHand();
        SkyBlockItem item = new SkyBlockItem(itemStack);
        SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();

        if (item.hasComponent(AbilityComponent.class)) {
            AbilityComponent abilityComponent = item.getComponent(AbilityComponent.class);
            RegisteredAbility ability;
            ability = abilityComponent.getAbility(RegisteredAbility.AbilityActivation.RIGHT_CLICK_BLOCK);
            if (ability != null) {
                if (!ability.getCost().canUse(player)) {
                    ability.getCost().onFail(player);
                    return;
                }

                PlayerAbilityHandler abilityHandler = player.getAbilityHandler();
                if (!abilityHandler.canUseAbility(item, ability.getCooldownTicks())) {
                    player.sendMessage("<c>This ability is on cooldown for {}s.",
                            Math.round((float) abilityHandler.getRemainingCooldown(item, ability.getCooldownTicks()) / 1000));
                    return;
                }

                if(ability.execute(player, item, event.getBlockPosition(), event.getBlockFace())){
                    abilityHandler.startAbilityCooldown(item);
                }
            }
        }
    }
}

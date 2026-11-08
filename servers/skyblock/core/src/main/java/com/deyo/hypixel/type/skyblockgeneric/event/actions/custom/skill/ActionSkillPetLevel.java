package com.deyo.hypixel.type.skyblockgeneric.event.actions.custom.skill;

import com.deyo.hypixel.commons.skyblock.item.attribute.attributes.ItemAttributePetData;
import com.deyo.hypixel.commons.text.Text;
import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.event.phase.EventPhase;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;
import com.deyo.hypixel.type.skyblockgeneric.event.custom.SkillUpdateEvent;
import com.deyo.hypixel.type.skyblockgeneric.item.SkyBlockItem;
import com.deyo.hypixel.type.skyblockgeneric.item.components.PetComponent;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

public class ActionSkillPetLevel implements HypixelEventClass {

    @PhasedEvent(node = EventNodes.CUSTOM, requireDataLoaded = false, phase = EventPhase.GAMEPLAY)
    public void run(SkillUpdateEvent event) {
        if (event.getNewValueRaw() <= event.getOldValueRaw()) return;
        double xp = event.getNewValueRaw() - event.getOldValueRaw();
        if (xp <= 0) return;

        SkyBlockPlayer player = event.getPlayer();
        SkyBlockItem enabledPet = player.getPetData().getEnabledPet();

        if (enabledPet == null) return;
        PetComponent pet = enabledPet.getComponent(PetComponent.class);
        if (pet.getSkillCategory() != event.getSkillCategory()) return;

        ItemAttributePetData.PetData petData = enabledPet.getAttributeHandler().getPetData();
        boolean hasLeveled = petData.addExperience(xp, enabledPet.getAttributeHandler().getRarity());

        if (hasLeveled) {
            player.sendMessage(
                    "<a>Your {}levelled up to level <9>{}<a>!",
                    Text.of("<color:{}>{} Pet ",
                            enabledPet.getAttributeHandler().getRarity().getColor(),
                            pet.getPetName()),
                    petData.getAsLevel(enabledPet.getAttributeHandler().getRarity())
            );
        }
    }
}

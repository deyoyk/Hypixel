package com.deyo.hypixel.type.hub.npcs;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.minestom.server.coordinate.Pos;
import com.deyo.hypixel.commons.text.Text;
import com.deyo.hypixel.type.generic.data.HypixelDataHandler;
import com.deyo.hypixel.type.generic.data.datapoints.DatapointString;
import com.deyo.hypixel.type.generic.entity.npc.HypixelNPC;
import com.deyo.hypixel.type.generic.entity.npc.configuration.HumanConfiguration;
import com.deyo.hypixel.type.generic.event.custom.NPCInteractEvent;
import com.deyo.hypixel.type.generic.user.HypixelPlayer;
import com.deyo.hypixel.type.skyblockgeneric.data.SkyBlockDataHandler;
import com.deyo.hypixel.type.skyblockgeneric.data.datapoints.DatapointMuseum;
import com.deyo.hypixel.type.skyblockgeneric.gui.inventories.museum.GUIYourMuseum;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.UUID;

public class NPCMuseumDisplay extends HypixelNPC {
    public NPCMuseumDisplay() {
        super(new MuseumHumanConfiguration());
    }

    private static class MuseumHumanConfiguration extends HumanConfiguration {
        private SkyBlockDataHandler skyblockHandler = null;
        private HypixelDataHandler dataHandler = null;

        @Override
        public String[] holograms(HypixelPlayer p) {
			SkyBlockPlayer player = (SkyBlockPlayer) p;
			DatapointMuseum.MuseumData data = player.getMuseumData();

            UUID currentlyViewing = data.getCurrentlyViewing().playerUuid();
            UUID profileUUID = data.getCurrentlyViewing().profileUuid();
			if (skyblockHandler == null) {
				skyblockHandler = SkyBlockDataHandler.getProfileOfOfflinePlayer(currentlyViewing, profileUUID);
			}
            if (dataHandler == null) {
                dataHandler = HypixelDataHandler.getOfOfflinePlayer(currentlyViewing);
            }
			Component colouredName = p.getColouredName();
			TextColor nameColour = colouredName.color();
			String username = PlainTextComponentSerializer.plainText().serialize(colouredName);
			String profileName = skyblockHandler.get(SkyBlockDataHandler.Data.PROFILE_NAME, DatapointString.class).getValue();

			return new String[]{
					Text.of("<color:{}>{}", nameColour, username).serialize(),
					Text.of("<e>Profile: <a>{}", profileName).serialize()
			};
		}

        @Override
        public String signature(HypixelPlayer p) {
            SkyBlockPlayer player = (SkyBlockPlayer) p;
            if (dataHandler == null) {
                DatapointMuseum.MuseumData data = player.getMuseumData();
                UUID currentlyViewing = data.getCurrentlyViewing().playerUuid();

                dataHandler = HypixelDataHandler.getOfOfflinePlayer(currentlyViewing);
            }
            return dataHandler.get(HypixelDataHandler.Data.SKIN_SIGNATURE, DatapointString.class).getValue();
        }

        @Override
        public String texture(HypixelPlayer p) {
            SkyBlockPlayer player = (SkyBlockPlayer) p;
            if (dataHandler == null) {
                DatapointMuseum.MuseumData data = player.getMuseumData();
                UUID currentlyViewing = data.getCurrentlyViewing().playerUuid();

                dataHandler = HypixelDataHandler.getOfOfflinePlayer(currentlyViewing);
            }
            return dataHandler.get(HypixelDataHandler.Data.SKIN_TEXTURE, DatapointString.class).getValue();
        }

        @Override
        public Pos position(HypixelPlayer player) {
            return new Pos(29.5, 70, 35.5, 180, 0);
        }

        @Override
        public boolean looking(HypixelPlayer player) {
            return false;
        }
    }

    @Override
    public void onClick(NPCInteractEvent e) {
        new GUIYourMuseum().open(e.player());
    }
}
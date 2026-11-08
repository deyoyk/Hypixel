package com.deyo.hypixel.type.skyblockgeneric.museum;

import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

public abstract class MuseumDisplay {
    public abstract MuseumDisplayEntityInformation display(SkyBlockPlayer player,
                                                           MuseumDisplays display,
                                                           boolean empty,
                                                           int position
    );
}
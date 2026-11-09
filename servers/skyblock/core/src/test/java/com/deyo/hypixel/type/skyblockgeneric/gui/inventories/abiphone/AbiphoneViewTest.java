package com.deyo.hypixel.type.skyblockgeneric.gui.inventories.abiphone;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AbiphoneViewTest {

    @Test
    void sortTypeCyclesThroughEveryMode() {
        assertEquals(AbiphoneView.SortType.ALPHABETICAL,
                AbiphoneView.nextSortType(AbiphoneView.SortType.FIRST_ADDED));
        assertEquals(AbiphoneView.SortType.LAST_CALLED,
                AbiphoneView.nextSortType(AbiphoneView.SortType.ALPHABETICAL));
        assertEquals(AbiphoneView.SortType.MOST_CALLED,
                AbiphoneView.nextSortType(AbiphoneView.SortType.LAST_CALLED));
        assertEquals(AbiphoneView.SortType.DO_NOT_DISTURB_FIRST,
                AbiphoneView.nextSortType(AbiphoneView.SortType.MOST_CALLED));
        assertEquals(AbiphoneView.SortType.FIRST_ADDED,
                AbiphoneView.nextSortType(AbiphoneView.SortType.DO_NOT_DISTURB_FIRST));
    }
}

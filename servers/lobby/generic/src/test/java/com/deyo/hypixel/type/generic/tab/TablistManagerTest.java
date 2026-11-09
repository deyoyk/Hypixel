package com.deyo.hypixel.type.generic.tab;

import com.deyo.hypixel.commons.text.Text;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TablistManagerTest {

    @Test
    void unchangedEntriesDoNotTriggerARebuild() {
        List<TablistModule.TablistEntry> entries = List.of(
                new TablistModule.TablistEntry(Text.of("<a>Hello"), TablistSkinRegistry.GRAY),
                new TablistModule.TablistEntry(Text.of("<b>World"), TablistSkinRegistry.GREEN)
        );

        assertFalse(TablistManager.entriesChanged(entries, List.copyOf(entries)));
    }

    @Test
    void changedOrReorderedEntriesTriggerARebuild() {
        TablistModule.TablistEntry first =
                new TablistModule.TablistEntry(Text.of("<a>Hello"), TablistSkinRegistry.GRAY);
        TablistModule.TablistEntry second =
                new TablistModule.TablistEntry(Text.of("<b>World"), TablistSkinRegistry.GREEN);

        assertTrue(TablistManager.entriesChanged(
                List.of(first, second),
                List.of(second, first)));
        assertTrue(TablistManager.entriesChanged(
                List.of(first),
                List.of(new TablistModule.TablistEntry(Text.of("<c>Changed"), TablistSkinRegistry.GRAY))));
    }
}

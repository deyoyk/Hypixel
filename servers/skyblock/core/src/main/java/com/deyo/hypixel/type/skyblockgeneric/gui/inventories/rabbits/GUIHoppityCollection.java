package com.deyo.hypixel.type.skyblockgeneric.gui.inventories.rabbits;

import lombok.Getter;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.inventory.click.Click;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import com.deyo.hypixel.commons.text.Text;
import com.deyo.hypixel.type.generic.gui.inventory.ItemStacks;
import com.deyo.hypixel.type.generic.gui.v2.Components;
import com.deyo.hypixel.type.generic.gui.v2.StatefulView;
import com.deyo.hypixel.type.generic.gui.v2.ViewConfiguration;
import com.deyo.hypixel.type.generic.gui.v2.ViewLayout;
import com.deyo.hypixel.type.generic.gui.v2.context.ViewContext;
import com.deyo.hypixel.type.skyblockgeneric.data.SkyBlockDataHandler;
import com.deyo.hypixel.type.skyblockgeneric.data.datapoints.DatapointChocolateFactory;
import com.deyo.hypixel.type.skyblockgeneric.rabbits.ChocolateRabbit;
import com.deyo.hypixel.type.skyblockgeneric.region.IslandType;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.*;
import java.util.stream.Collectors;

public class GUIHoppityCollection implements StatefulView<GUIHoppityCollection.State> {
    private static final String HOPPITY_TEXTURE = "b79e7f3341b672d9de6564cbaca052a6a723ea466a2e66af35ba1ba855f0d692";
    private static final String LOCATION_TEXTURE = "d7cc6687423d0570d556ac53e0676cb563bbdd9717cd8269bdebed6f6d4e7bf8";
    private static final String FOUND_RABBIT_TEXTURE = HOPPITY_TEXTURE;

    private static final int[] RABBIT_SLOTS = {
            10, 11, 12, 13, 14, 15, 16,
            19, 20, 21, 22, 23, 24, 25,
            28, 29, 30, 31, 32, 33, 34,
            37, 38, 39, 40, 41, 42, 43
    };

    private static final int RABBITS_PER_PAGE = RABBIT_SLOTS.length;
    private static final int TOTAL_RABBITS = ChocolateRabbit.values().length;
    private static final int PROGRESS_BAR_SEGMENTS = 25;
    private static final double PERCENT_PER_PROGRESS_SEGMENT = 100.0 / PROGRESS_BAR_SEGMENTS;
    private static final List<IslandType> LOCATION_FILTERS = Arrays.stream(IslandType.values())
            .filter(location -> Arrays.stream(ChocolateRabbit.values())
                    .anyMatch(rabbit -> rabbit.getLocation() == location))
            .toList();

    private static final int[] BORDER_SLOTS = {
            0, 1, 2, 3, 4, 5, 6, 7, 8,
            9, 17, 18, 26, 27, 35, 36, 44,
            45, 46, 47, 48, 49, 50, 51, 52, 53
    };

    public record State(int page, SortType sortType, FilterType filterType, IslandType locationFilter) {
    }

    @Override
    public State initialState() {
        return new State(1, SortType.A_TO_Z, FilterType.NONE, null);
    }

    @Override
    public ViewConfiguration<State> configuration() {
        return ViewConfiguration.withText((state, ctx) -> {
            SkyBlockPlayer player = (SkyBlockPlayer) ctx.player();
            DatapointChocolateFactory.ChocolateFactoryData data = player.getSkyblockDataHandler()
                    .get(SkyBlockDataHandler.Data.CHOCOLATE_FACTORY, DatapointChocolateFactory.class).getValue();
            List<ChocolateRabbit> rabbits = getFilteredAndSortedRabbits(
                    data.getFoundRabbits(), state.sortType(), state.filterType(), state.locationFilter());
            int totalPages = Math.max(1, (int) Math.ceil(rabbits.size() / (double) RABBITS_PER_PAGE));
            return Text.of("({}/{}) Hoppity's Collection", state.page(), totalPages);
        }, InventoryType.CHEST_6_ROW);
    }

    @Override
    public void layout(ViewLayout<State> layout, State state, ViewContext ctx) {
        SkyBlockPlayer player = (SkyBlockPlayer) ctx.player();
        DatapointChocolateFactory.ChocolateFactoryData data = player.getSkyblockDataHandler().get(SkyBlockDataHandler.Data.CHOCOLATE_FACTORY, DatapointChocolateFactory.class).getValue();
        Set<ChocolateRabbit> foundRabbits = data.getFoundRabbits();

        List<ChocolateRabbit> rabbits = getFilteredAndSortedRabbits(
                foundRabbits, state.sortType(), state.filterType(), state.locationFilter());
        int totalPages = Math.max(1, (int) Math.ceil(rabbits.size() / (double) RABBITS_PER_PAGE));
        for (int slot : BORDER_SLOTS) {
            layout.slot(slot, (s, c) -> ItemStacks.named(Material.BLACK_STAINED_GLASS_PANE, " "));
        }

        layout.slot(4, (s, c) -> createCollectionInfoItem((SkyBlockPlayer) c.player()));

        int startIndex = (state.page() - 1) * RABBITS_PER_PAGE;
        for (int i = 0; i < RABBIT_SLOTS.length; i++) {
            int rabbitIndex = startIndex + i;
            int slot = RABBIT_SLOTS[i];

            if (rabbitIndex < rabbits.size()) {
                ChocolateRabbit rabbit = rabbits.get(rabbitIndex);
                layout.slot(slot, (s, c) -> {
                    boolean found = foundRabbits.contains(rabbit);
                    return createRabbitItem(rabbit, found);
                });
            } else {
                layout.slot(slot, (s, c) -> ItemStack.AIR.builder());
            }
        }

        layout.slot(47, (s, c) -> {
            Text selected = s.locationFilter() == null
                    ? Text.of("<a>All Rabbits")
                    : s.locationFilter().getFormattedName();
            List<Text> lore = List.of(
                    Text.of("<7>Each rabbit has a specific location"),
                    Text.of("<7>on a specific island where it can be"),
                    Text.of("<7>found, just for fun."),
                    Text.empty(),
                    Text.of("<7>The <9>Hotspot <7>of a Rabbit means that"),
                    Text.of("<7>this season they have a <a>50% <7>higher"),
                    Text.of("<7>chance to be found on this specific"),
                    Text.of("<7>island."),
                    Text.empty(),
                    Text.of("<6>Resident <7>rabbits however, can <c>ONLY"),
                    Text.of("<7>be found on their respective islands."),
                    Text.empty(),
                    Text.of("<7>Currently selected: {}", selected),
                    Text.empty(),
                    Text.of("<b>Right-click to go backwards!"),
                    Text.of("<e>Click to cycle!")
            );
            return ItemStacks.head(LOCATION_TEXTURE, Text.of("<9>Rabbit Locations"), lore);
        }, (click, c) -> {
            boolean isRightClick = click.click() instanceof Click.Right;
            IslandType newFilter = cycleLocation(state.locationFilter(), isRightClick);
            c.session(State.class).setState(new State(1, state.sortType(), state.filterType(), newFilter));
        });

        Components.back(layout, 48, ctx);
        Components.close(layout, 49);

        layout.slot(50, (s, c) -> {
            List<Text> lore = new ArrayList<>();
            lore.add(Text.empty());
            for (SortType type : SortType.values()) {
                lore.add(Text.of(type == s.sortType() ? "<b>▶ {}" : "<7>  {}", type.getDisplayName()));
            }
            lore.add(Text.empty());
            lore.add(Text.of("<b>Right-click to go backwards!"));
            lore.add(Text.of("<e>Click to switch sort!"));

            return ItemStacks.item(Material.HOPPER, 1, Text.of("<a>Sort"), lore);
        }, (click, c) -> {
            boolean isRightClick = click.click() instanceof Click.Right;
            SortType newSort = isRightClick ? state.sortType().previous() : state.sortType().next();
            c.session(State.class).setState(new State(1, newSort, state.filterType(), state.locationFilter()));
        });

        layout.slot(51, (s, c) -> {
            List<Text> lore = new ArrayList<>();
            lore.add(Text.empty());
            for (FilterType type : FilterType.values()) {
                lore.add(Text.of(type == s.filterType() ? "<a>▶ {}" : "<7>  {}", type.getDisplayName()));
            }
            lore.add(Text.empty());
            lore.add(Text.of("<b>Right-click to go backwards!"));
            lore.add(Text.of("<e>Click to switch!"));

            return ItemStacks.item(Material.ENDER_EYE, 1, Text.of("<a>Filter"), lore);
        }, (click, c) -> {
            boolean isRightClick = click.click() instanceof Click.Right;
            FilterType newFilter = isRightClick ? state.filterType().previous() : state.filterType().next();
            c.session(State.class).setState(new State(1, state.sortType(), newFilter, state.locationFilter()));
        });

        if (state.page() < totalPages) {
            layout.slot(53, (s, c) -> ItemStacks.item(Material.ARROW, """
                    <a>Next Page
                    <e>Page {}""", s.page() + 1),
                    (click, c) -> c.session(State.class).setState(new State(state.page() + 1, state.sortType(), state.filterType(), state.locationFilter())));
        }

        if (state.page() > 1) {
            layout.slot(45, (s, c) -> ItemStacks.item(Material.ARROW, """
                    <a>Previous Page
                    <e>Page {}""", s.page() - 1),
                    (click, c) -> c.session(State.class).setState(new State(state.page() - 1, state.sortType(), state.filterType(), state.locationFilter())));
        }
    }

    private ItemStack.Builder createCollectionInfoItem(SkyBlockPlayer player) {
        DatapointChocolateFactory.ChocolateFactoryData data = player.getSkyblockDataHandler().get(SkyBlockDataHandler.Data.CHOCOLATE_FACTORY, DatapointChocolateFactory.class).getValue();
        Set<ChocolateRabbit> foundRabbits = data.getFoundRabbits();
        int found = foundRabbits.size();
        double percentage = (found / (double) TOTAL_RABBITS) * 100;

        int totalChocolate = 0;
        double totalMultiplier = 0;
        for (ChocolateRabbit rabbit : foundRabbits) {
            totalChocolate += rabbit.getChocolateBonus();
            totalMultiplier += rabbit.getMultiplierBonus();
        }

        List<Text> lore = new ArrayList<>(List.of(
                Text.of("<7>Help <a>Hoppity <7>find all of his <a>Chocolate"),
                Text.of("<a>Rabbits <7>during the <d>Hoppity's Hunt"),
                Text.of("<7>event!"),
                Text.empty(),
                Text.of("<7>The more unique <a>Chocolate Rabbits"),
                Text.of("<7>that you find, the more your"),
                Text.of("<6>Chocolate Factory <7>will produce!"),
                Text.empty(),
                Text.of("<7>Finding duplicate Rabbits grants"),
                Text.of("<a>+10% <7>extra <6>Chocolate <7>per duplicate,"),
                Text.of("<7>up to <a>+100%<7>!"),
                Text.empty(),
                Text.of("<7>Rabbits Found: <e>{}<6>%", String.format("%.1f", percentage)),
                createProgressBar(percentage, found),
                Text.empty()));
        if (totalChocolate > 0) {
            lore.add(Text.of("<6>+{} Chocolate per second", totalChocolate));
        }
        if (totalMultiplier > 0) {
            lore.add(Text.of("<6>+{} Chocolate Multiplier", String.format("%.3fx", totalMultiplier)));
        }

        return ItemStacks.head(HOPPITY_TEXTURE, 1, Text.of("<a>Hoppity's Collection"), lore);
    }

    private ItemStack.Builder createRabbitItem(ChocolateRabbit rabbit, boolean found) {
        List<Text> lore = new ArrayList<>();

        if (rabbit.getRarity() == ChocolateRabbit.RabbitRarity.LEGENDARY ||
                rabbit.getRarity() == ChocolateRabbit.RabbitRarity.DIVINE ||
                rabbit.getRarity() == ChocolateRabbit.RabbitRarity.MYTHIC) {
            lore.add(Text.of("<7>Grants <6>+{} Chocolate <7>per second",
                    String.format("%.2fx", rabbit.getMultiplierBonus())));
            lore.add(Text.of("<7>to your <6>Chocolate Factory<7>."));
        } else {
            lore.add(Text.of("<7>Grants <6>+{} Chocolate <7>and <6>{}", rabbit.getChocolateBonus(),
                    String.format("%.3fx", rabbit.getMultiplierBonus())));
            lore.add(Text.of("<6>Chocolate <7>per second to your"));
            lore.add(Text.of("<6>Chocolate Factory<7>."));
        }
        lore.add(Text.empty());

        if (rabbit.getObtainMethod() != null) {
            lore.add(Text.of("<7>{}", rabbit.getObtainMethod()));
            lore.add(Text.empty());
        }

        if (rabbit.getRequirement() != null) {
            lore.add(Text.of("<c>✖ <7>Requirement"));
            lore.add(Text.of("<7>{}", rabbit.getRequirement()));
            lore.add(Text.empty());
            if (!found) {
                lore.add(Text.of("<8>You cannot find this rabbit until you"));
                lore.add(Text.of("<8>meet the requirement!"));
                lore.add(Text.empty());
            }
        }

        if (!found) {
            lore.add(Text.of("<8>You have not found this rabbit yet!"));
        }

        if (rabbit.getLocation() != null) {
            lore.add(Text.empty());
            lore.add(Text.of("{}", rabbit.getResidentLabel()));
        }
        lore.add(Text.empty());
        lore.add(rabbit.getRarity().getFormattedName());

        Text name = rabbit.getFormattedName();
        if (found) {
            return ItemStacks.head(FOUND_RABBIT_TEXTURE, 1, name, lore);
        } else {
            return ItemStacks.item(Material.GRAY_DYE, 1, name, lore);
        }
    }

    static List<ChocolateRabbit> getFilteredAndSortedRabbits(Set<ChocolateRabbit> foundRabbits, SortType sortType,
                                                             FilterType filterType, IslandType locationFilter) {
        List<ChocolateRabbit> rabbits = Arrays.stream(ChocolateRabbit.values())
                .filter(rabbit -> locationFilter == null || rabbit.getLocation() == locationFilter)
                .filter(rabbit -> {
                    boolean found = foundRabbits.contains(rabbit);
                    return switch (filterType) {
                        case NONE -> true;
                        case FOUND -> found;
                        case NOT_FOUND -> !found;
                        case HAS_REQUIREMENT -> rabbit.getRequirement() != null;
                        case NO_REQUIREMENT -> rabbit.getRequirement() == null;
                    };
                })
                .collect(Collectors.toList());

        switch (sortType) {
            case A_TO_Z -> rabbits.sort(Comparator.comparing(ChocolateRabbit::getDisplayName));
            case Z_TO_A -> rabbits.sort(Comparator.comparing(ChocolateRabbit::getDisplayName).reversed());
            case HIGHEST_RARITY ->
                    rabbits.sort(Comparator.comparing((ChocolateRabbit r) -> r.getRarity().ordinal()).reversed());
            case LOWEST_RARITY -> rabbits.sort(Comparator.comparing(r -> r.getRarity().ordinal()));
        }

        return rabbits;
    }

    static IslandType cycleLocation(IslandType current, boolean backwards) {
        if (LOCATION_FILTERS.isEmpty()) return null;
        if (current == null) {
            return backwards
                    ? LOCATION_FILTERS.get(LOCATION_FILTERS.size() - 1)
                    : LOCATION_FILTERS.get(0);
        }
        int index = LOCATION_FILTERS.indexOf(current);
        if (index < 0) return null;
        int nextIndex = backwards ? index - 1 : index + 1;
        return nextIndex < 0 || nextIndex >= LOCATION_FILTERS.size() ? null : LOCATION_FILTERS.get(nextIndex);
    }

    private Text createProgressBar(double progress, int found) {
        int filled = (int) (progress / PERCENT_PER_PROGRESS_SEGMENT);
        int empty = PROGRESS_BAR_SEGMENTS - filled;

        return Text.of("<2><l><m>{}<f>{}<r> <e>{}<6>/<e>{}",
                " ".repeat(Math.max(0, filled)), " ".repeat(Math.max(0, empty)), found, TOTAL_RABBITS);
    }

    @Getter
    public enum SortType {
        A_TO_Z("A to Z"),
        Z_TO_A("Z to A"),
        HIGHEST_RARITY("Highest Rarity"),
        LOWEST_RARITY("Lowest Rarity");

        private final String displayName;

        SortType(String displayName) {
            this.displayName = displayName;
        }

        public SortType next() {
            SortType[] all = values();
            return all[(ordinal() + 1) % all.length];
        }

        public SortType previous() {
            SortType[] all = values();
            return all[(ordinal() - 1 + all.length) % all.length];
        }
    }

    @Getter
    public enum FilterType {
        NONE("None"),
        FOUND("Rabbits Found"),
        NOT_FOUND("Rabbits Not Found"),
        HAS_REQUIREMENT("Has Requirement"),
        NO_REQUIREMENT("No Requirement");

        private final String displayName;

        FilterType(String displayName) {
            this.displayName = displayName;
        }

        public FilterType next() {
            FilterType[] all = values();
            return all[(ordinal() + 1) % all.length];
        }

        public FilterType previous() {
            FilterType[] all = values();
            return all[(ordinal() - 1 + all.length) % all.length];
        }
    }
}

package com.deyo.hypixel.type.skyblockgeneric.item.components;

import lombok.Getter;
import com.deyo.hypixel.commons.StringUtility;
import com.deyo.hypixel.commons.text.Text;
import com.deyo.hypixel.commons.skyblock.item.ItemType;
import com.deyo.hypixel.commons.skyblock.item.attribute.attributes.ItemAttributeMinionData;
import com.deyo.hypixel.type.skyblockgeneric.item.SkyBlockItem;
import com.deyo.hypixel.type.skyblockgeneric.item.SkyBlockItemComponent;
import com.deyo.hypixel.type.skyblockgeneric.item.crafting.ShapedRecipe;
import com.deyo.hypixel.type.skyblockgeneric.item.crafting.SkyBlockRecipe;
import com.deyo.hypixel.type.skyblockgeneric.item.handlers.lore.LoreConfig;
import com.deyo.hypixel.type.skyblockgeneric.minion.MinionIngredient;
import com.deyo.hypixel.type.skyblockgeneric.minion.MinionRecipe;
import com.deyo.hypixel.type.skyblockgeneric.minion.MinionRegistry;
import com.deyo.hypixel.type.skyblockgeneric.minion.SkyBlockMinion;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public class MinionComponent extends SkyBlockItemComponent {
    private MinionRegistry minionRegistry;
    private ItemType baseItem;

    public MinionComponent(String minionType, String baseItem,
                           boolean isByDefaultCraftable, List<MinionIngredient> ingredients) {
        this.minionRegistry = MinionRegistry.valueOf(minionType);
        this.baseItem = ItemType.get(baseItem);

        addInheritedComponent(new SkullHeadComponent((item -> {
            int tier = item.getAttributeHandler().getMinionData().tier();

            return minionRegistry.asSkyBlockMinion().getTiers().get(tier - 1).texture();
        })));
        addInheritedComponent(new PlaceEventComponent("minion"));
        addInheritedComponent(new TrackedUniqueComponent());
        addInheritedComponent(new LoreUpdateComponent(
                new LoreConfig((item, player) -> {
                    boolean mithrilInfusion = item.getAttributeHandler().isMithrilInfused();

                    int percentageSpeed = 0;
                    if (mithrilInfusion)
                        percentageSpeed += 10;

                    return getLore(item, percentageSpeed);
                }, (item, player) -> Text.of("<9>{} {:roman}", getMinionRegistry().getDisplay(),
                        item.getAttributeHandler().getMinionData().tier()).serialize()), true
        ));

        List<SkyBlockRecipe<?>> toReturn = new ArrayList<>();

        getMinionRegistry().asSkyBlockMinion().getTiers().forEach(tier -> {
            if (!tier.craftable()) return; // Skip non-craftable tiers

            List<String> pattern = new ArrayList<>(Arrays.asList(
                    "AAA",
                    "ABA",
                    "AAA"
            ));

            SkyBlockItem item = new SkyBlockItem(getMinionRegistry().getItemType());
            item.getAttributeHandler().setMinionData(new ItemAttributeMinionData.MinionData(tier.tier(), 0));

            ShapedRecipe recipe = new ShapedRecipe(
                    SkyBlockRecipe.RecipeType.MINION,
                    item,
                    MinionRecipe.fromNumber(tier.tier() - 1).getRecipeFunction().apply(new MinionRecipe.MinionRecipeData(
                            ingredients,
                            this.baseItem,
                            getMinionRegistry().getItemType()
                    )),
                    pattern
            );
            recipe.addExtraRequirement('B', (minionItem) -> {
                if (minionItem.hasComponent(MinionComponent.class))
                    return minionItem.getAttributeHandler().getMinionData().tier() == Math.max(1, tier.tier() - 1);
                return true;
            });

            toReturn.add(recipe);
        });

        addInheritedComponent(new CraftableComponent(toReturn.toArray(new SkyBlockRecipe[0]), isByDefaultCraftable));
    }

    public static List<String> getLore(SkyBlockItem item, int percentageSpeed) {
        MinionRegistry minionRegistry = item.getComponent(MinionComponent.class).getMinionRegistry();

        List<String> lore = new ArrayList<>(Arrays.asList(
                "<7>Place this minion and it will start",
                "<7>generating and mining " + StringUtility.toNormalCase(minionRegistry.name()) + "!",
                "<7>Requires an open area to place",
                "<7>" + StringUtility.toNormalCase(minionRegistry.name()) + ". Minions also work",
                "<7>you are offline!",
                ""
        ));

        SkyBlockMinion minion = item.getAttributeHandler().getMinionType().asSkyBlockMinion();
        ItemAttributeMinionData.MinionData data = item.getAttributeHandler().getMinionData();
        SkyBlockMinion.MinionTier tier = minion.getTiers().get(data.tier() - 1);

        double timeBetweenActions = tier.timeBetweenActions() / (1. + percentageSpeed/100.);

        final DecimalFormat formattter = new DecimalFormat("#.##");

        lore.add("<7>Time Between Actions: <a>" + formattter.format(timeBetweenActions) + "s");
        lore.add("<7>Max Storage: <e>" + tier.storage());
        lore.add("<7>Resources Generated: <b>" + data.generatedResources());

        lore.add(" ");
        lore.add("<9><l>RARE");
        return lore;
    }
}

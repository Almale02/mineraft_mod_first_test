package net.seconddanad.first_test.villager;

import com.google.common.collect.ImmutableSet;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.fabricmc.fabric.api.object.builder.v1.villager.VillagerProfessionBuilder;
import net.fabricmc.fabric.api.object.builder.v1.world.poi.PointOfInterestHelper;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.VillagerProfession;
import net.minecraft.world.poi.PointOfInterestType;
import net.seconddanad.first_test.FirstTest;

public class ModVillagers {

    public static final PointOfInterestType TARGETING_POI = regiserPOI("targeting_poi", Blocks.TARGET);
    public static final VillagerProfession TARGETER = registerProfession(
            "targeter",
            RegistryKey.of(
                    Registries.POINT_OF_INTEREST_TYPE.getKey(),
                    new Identifier(FirstTest.MOD_ID, "targeting_poi")));

    public static void registerVillagers() {
        registerTrades();
    }
    public static PointOfInterestType regiserPOI(String name, Block block) {
        return PointOfInterestHelper.register(new Identifier(FirstTest.MOD_ID, name),
                1, 1, ImmutableSet.copyOf(block.getStateManager().getStates()));
    }
    public static VillagerProfession registerProfession(String name, RegistryKey<PointOfInterestType> type) {
        return Registry.register(
                Registries.VILLAGER_PROFESSION,
                new Identifier(FirstTest.MOD_ID, name),
                VillagerProfessionBuilder.create()
                        .id(new Identifier(FirstTest.MOD_ID,name))
                        .workstation(type)
                        .build()
        );
    }

    public static void registerTrades(){
        TradeOfferHelper.registerVillagerOffers(TARGETER, 1, factories -> {
            factories.add(((entity, random) -> new TradeOffer(
                    new ItemStack(Items.EMERALD, 2),
                    new ItemStack(Items.TARGET, 3),
                    5, 8, .002f
            )));
            factories.add(((entity, random) -> new TradeOffer(
                    new ItemStack(Items.TARGET, 3),
                    new ItemStack(Items.EMERALD, 2),
                    5, 8, .002f
            )));
        });
    }
}

package net.seconddanad.first_test;

import net.fabricmc.api.ModInitializer;
import net.seconddanad.first_test.block.ModBlocks;
import net.seconddanad.first_test.block.entity.ModBlockEntity;
import net.seconddanad.first_test.item.ModItems;
import net.seconddanad.first_test.villager.ModVillagers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.bernie.geckolib.GeckoLib;


public class FirstTest implements ModInitializer {
    public static final String MOD_ID = "first_test";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        ModItems.registerModItems();
        ModBlocks.registerModBlocks();
        ModVillagers.registerVillagers();
        ModBlockEntity.registerBlockEntities();
        GeckoLib.initialize();
    }
}
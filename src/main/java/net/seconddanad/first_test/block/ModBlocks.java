package net.seconddanad.first_test.block;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.seconddanad.first_test.FirstTest;
import net.seconddanad.first_test.block.custom.TestBlock;

public class ModBlocks {

    public static final Block TEST_BLOCK = registerBlock(
            "test_block",
            new Identifier("ingredients"),
            new TestBlock(FabricBlockSettings.create()
                    .sounds(BlockSoundGroup.METAL)
            ));

    public static void registerModBlocks() {

    }

    private static Block registerBlock(String name, Identifier itemGroupIdentifier, Block block) {
        registerBlockItem(name, block, itemGroupIdentifier);
        return Registry.register(Registries.BLOCK, new Identifier(FirstTest.MOD_ID, name), block);
    }

    private static Item registerBlockItem(String name, Block block, Identifier itemGroupIdentifier) {
        BlockItem blockItem = new BlockItem(block, new FabricItemSettings());

        ItemGroupEvents.modifyEntriesEvent(RegistryKey.of(RegistryKeys.ITEM_GROUP, itemGroupIdentifier)).register(content -> {
            content.add(blockItem);
        });

        return Registry.register(
                Registries.ITEM, new Identifier(FirstTest.MOD_ID, name), blockItem);
    }
}

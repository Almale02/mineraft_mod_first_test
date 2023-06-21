package net.seconddanad.first_test.block.entity;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.seconddanad.first_test.block.ModBlocks;

import static net.seconddanad.first_test.FirstTest.MOD_ID;

public class ModBlockEntity {
    public static BlockEntityType<TestBlockEntity> TEST_BLOCK_ENTITY;
    public static void registerBlockEntities() {
        TEST_BLOCK_ENTITY = Registry.register(
                Registries.BLOCK_ENTITY_TYPE,
                new Identifier(MOD_ID, "test_block_entity"),
                FabricBlockEntityTypeBuilder.create(TestBlockEntity::new, ModBlocks.TEST_BLOCK).build()
        );
    }
}
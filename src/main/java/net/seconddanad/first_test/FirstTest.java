package net.seconddanad.first_test;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.seconddanad.first_test.block.ModBlocks;
import net.seconddanad.first_test.block.entity.TestBlockEntity;
import net.seconddanad.first_test.item.ModItems;
import net.seconddanad.first_test.villager.ModVillagers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class FirstTest implements ModInitializer {
    public static final String MOD_ID = "first_test";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final BlockEntityType<TestBlockEntity> TEST_BLOCK_ENTITY = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            new Identifier(MOD_ID, "test_block_entity"),
            FabricBlockEntityTypeBuilder.create(TestBlockEntity::new, ModBlocks.TEST_BLOCK).build()
    );
    @Override
    public void onInitialize() {
        ModItems.registerModItems();
        ModBlocks.registerModBlocks();
        ModVillagers.registerVillagers();
    }
}
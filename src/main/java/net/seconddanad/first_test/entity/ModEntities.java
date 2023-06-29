package net.seconddanad.first_test.entity;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.seconddanad.first_test.entity.custom.TestEntity;

import static net.seconddanad.first_test.FirstTest.MOD_ID;

public class ModEntities {
    public static final EntityType<TestEntity> TEST_ENTITY = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(MOD_ID, "test_entity"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, TestEntity::new)
                    .dimensions(EntityDimensions.fixed(1f, 1f)).build()
    );
}

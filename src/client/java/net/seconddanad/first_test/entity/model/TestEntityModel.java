package net.seconddanad.first_test.entity.model;

import net.minecraft.util.Identifier;
import net.seconddanad.first_test.entity.custom.TestEntity;
import software.bernie.geckolib.model.GeoModel;

import static net.seconddanad.first_test.FirstTest.MOD_ID;

public class TestEntityModel extends GeoModel<TestEntity> {
    @Override
    public Identifier getModelResource(TestEntity animatable) {
        return new Identifier(MOD_ID, "geo/test_entity.geo.json");
    }

    @Override
    public Identifier getTextureResource(TestEntity animatable) {
        return new Identifier(MOD_ID, "textures/entity/test_entity.png");
    }

    @Override
    public Identifier getAnimationResource(TestEntity animatable) {
        return null;
    }
}

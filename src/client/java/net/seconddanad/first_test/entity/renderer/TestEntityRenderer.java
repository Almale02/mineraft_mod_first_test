package net.seconddanad.first_test.entity.renderer;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;
import net.seconddanad.first_test.entity.custom.TestEntity;
import net.seconddanad.first_test.entity.model.TestEntityModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

import static net.seconddanad.first_test.FirstTest.MOD_ID;

public class TestEntityRenderer extends GeoEntityRenderer<TestEntity> {

    public TestEntityRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new TestEntityModel());
    }

    @Override
    public Identifier getTextureLocation(TestEntity animatable) {
        return new Identifier(MOD_ID, "textures/entity/test_entity.png");
    }
}

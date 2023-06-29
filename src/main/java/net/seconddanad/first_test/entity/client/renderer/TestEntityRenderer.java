package net.seconddanad.first_test.entity.client.renderer;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.seconddanad.first_test.entity.client.model.TestEntityModel;
import net.seconddanad.first_test.entity.custom.TestEntity;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
public class ExampleEntityRenderer extends GeoEntityRenderer<TestEntity> {
    public ExampleEntityRenderer(EntityRendererFactory.Context renderManager, GeoModel<TestEntity> model) {
        super(TestEntityModel, model);
    }
}

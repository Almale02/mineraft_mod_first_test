package net.seconddanad.first_test;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.seconddanad.first_test.entity.ModEntities;
import net.seconddanad.first_test.entity.renderer.TestEntityRenderer;

public class FirstTestClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(ModEntities.TEST_ENTITY, TestEntityRenderer::new);
    }
}

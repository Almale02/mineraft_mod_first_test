package net.seconddanad.first_test;

import net.fabricmc.api.ClientModInitializer;

public class FirstTestClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        FirstTest.LOGGER.info("Client is inited");
    }
}

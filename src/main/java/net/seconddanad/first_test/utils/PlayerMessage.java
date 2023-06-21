package net.seconddanad.first_test.utils;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

public class PlayerMessage {
    public static void sendMessageToPlayer(PlayerEntity player, String message) {
        player.sendMessage(Text.of(message));
    }
}

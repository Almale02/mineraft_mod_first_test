package net.seconddanad.first_test.item.custom;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class TestItem extends Item {
    public TestItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World eventWorld = context.getWorld();

        PlayerEntity eventPlayer = context.getPlayer();
        BlockPos eventBlockPos = context.getBlockPos();
        BlockState eventBlockState = eventWorld.getBlockState(eventBlockPos);

        if (!context.getWorld().isClient()){

            BlockEntity blockEntity = eventWorld.getBlockEntity(eventBlockPos);

            if (blockEntity instanceof SignBlockEntity) {
                Direction dir = eventBlockState.get(Properties.HORIZONTAL_FACING);
                sendMessageToPlayer(eventPlayer, String.valueOf(dir));

                SignBlockEntity sign = ((SignBlockEntity) blockEntity);

                sign.setText(sign.getText(true), false);
                sign.getBackText().withColor(sign.getFrontText().getColor());
                sign.getBackText().withGlowing(sign.getFrontText().isGlowing());
            } else {
                Direction dir = eventBlockState.get(Properties.HORIZONTAL_FACING);
                sendMessageToPlayer(eventPlayer, String.valueOf(dir));

                sendMessageToPlayer(eventPlayer, "not sign");
            }
            
        }
        return ActionResult.PASS;
    }

    @Override
    public ActionResult useOnEntity(ItemStack eventStack, PlayerEntity eventPlayer, LivingEntity eventEntity, Hand playerHand) {
        if (!eventPlayer.getWorld().isClient) {

        }

        return ActionResult.PASS;
    }

    private static void sendMessageToPlayer(PlayerEntity player, String message) {
        player.sendMessage(Text.of(message));
    }
}

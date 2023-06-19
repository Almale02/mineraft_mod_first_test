package net.seconddanad.first_test.item.custom;

import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.world.ServerWorld;
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

        BlockPos eventBlockPos = context.getBlockPos();
        BlockState eventBlockState = eventWorld.getBlockState(eventBlockPos);

        Direction lookingDirection = context.getSide().getOpposite();
        BlockPos offsetedPosition = eventBlockPos.offset(lookingDirection, 1);

        if (!context.getWorld().isClient()){


            BlockState blockEntity = eventWorld.getBlockState(eventBlockPos);

            sendMessageToPlayer(context.getPlayer(), blockEntity.toString());
        }
        return ActionResult.PASS;
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        if (!user.getWorld().isClient) {
            sendMessageToPlayer(user, "you killed " + entity.getUuid());
            ((ServerWorld) user.getWorld()).getEntity(entity.getUuid()).kill();
        }

        return ActionResult.PASS;
    }

    private static void sendMessageToPlayer(PlayerEntity player, String message) {
        player.sendMessage(Text.of(message));
    }
}

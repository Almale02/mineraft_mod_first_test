package net.seconddanad.first_test.item.custom;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
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
        BlockState eventBlockState = eventWorld.getBlockState(context.getBlockPos());
        Direction lookingDirection = context.getSide().getOpposite();

        BlockPos offsetedPosition = context.getBlockPos().offset(lookingDirection, 1);

        if (!context.getWorld().isClient()){
            if (eventWorld.getBlockState(offsetedPosition).equals(Blocks.AIR.getDefaultState())) {
                pushBlock(lookingDirection, context.getBlockPos(), eventWorld);
            } else {
                pullBlock(lookingDirection, context.getBlockPos(), eventWorld);
            }
        }
        return ActionResult.PASS;
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        if (!user.getWorld().isClient) {
            user.kill();
        }

        return ActionResult.PASS;
    }

    private static void sendMessageToPlayer(PlayerEntity player, String message) {
        player.sendMessage(Text.of(message));
    }

    private static void pullBlock(Direction lookingDirection, BlockPos blockPos, World world) {
        world.setBlockState(
                blockPos.offset(lookingDirection.getOpposite()),
                world.getBlockState(blockPos)
        );
        world.setBlockState(blockPos, Blocks.AIR.getDefaultState());
    }

    private static void pushBlock(Direction lookingDirection, BlockPos blockPos, World world) {
        world.setBlockState(
                blockPos.offset(lookingDirection),
                world.getBlockState(blockPos)
        );
        world.setBlockState(blockPos, Blocks.AIR.getDefaultState());
    }
}

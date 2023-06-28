package net.seconddanad.first_test.item.custom;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.seconddanad.DanadReferenceSystem.DRSReferenceHead;
import net.seconddanad.first_test.block.entity.TestBlockEntity;

public class TestItem extends Item {
    public TestItem(Settings settings) {
        super(settings);
    }
    private DRSReferenceHead refHead;
    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World eventWorld = context.getWorld();

        PlayerEntity eventPlayer = context.getPlayer();
        BlockPos eventBlockPos = context.getBlockPos();
        BlockState eventBlockState = eventWorld.getBlockState(eventBlockPos);
        BlockEntity eventBlockEntity = eventWorld.getBlockEntity(eventBlockPos);

        if (!context.getWorld().isClient()) {
            if (eventBlockEntity instanceof TestBlockEntity testBlockEntity) {
                this.refHead = new DRSReferenceHead(testBlockEntity);
                return ActionResult.PASS;
            } else {
                if (eventPlayer.isSneaking()) {
                    this.refHead.downToRandom(eventWorld).ifPresent(child -> {
                        BlockPos offset = child.getPos().offset(Direction.UP);
                        eventPlayer.teleport(offset.getX(), offset.getY(), offset.getZ());
                    });

                } else {
                    this.refHead.up(eventWorld).ifPresent(parent -> {
                        BlockPos offset = parent.getPos().offset(Direction.UP);
                        eventPlayer.teleport(offset.getX(), offset.getY(), offset.getZ());
                    });
                }
            }
        }
        return ActionResult.FAIL;
    }
    @Override
    public ActionResult useOnEntity(ItemStack eventStack, PlayerEntity eventPlayer, LivingEntity eventEntity, Hand playerHand) {
        if (!eventPlayer.getWorld().isClient) {

        }

        return ActionResult.PASS;
    }

}

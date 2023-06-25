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
import net.minecraft.world.World;
import net.seconddanad.first_test.block.entity.TestBlockEntity;

import static net.seconddanad.first_test.utils.PlayerMessage.sendMessageToPlayer;

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
        BlockEntity eventBlockEntity = eventWorld.getBlockEntity(eventBlockPos);

        if (!context.getWorld().isClient()){
            if (eventBlockEntity instanceof TestBlockEntity testBlockEntity) {
                testBlockEntity.getChildren().stream().findFirst().ifPresent(childReference -> {

                    childReference.cacheReference(eventWorld);

                    eventWorld.setBlockState(
                            childReference.getReferencePos(),
                            childReference.getCachedReferenceBlockState()
                                    .getBlock().getDefaultState()
                    );
                });
            } else {
                sendMessageToPlayer(eventPlayer, "not what expected");
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

}

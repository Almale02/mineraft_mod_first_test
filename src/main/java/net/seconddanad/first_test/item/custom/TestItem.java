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

        if (!context.getWorld().isClient()){
            BlockEntity blockEntity = eventWorld.getBlockEntity(eventBlockPos);

            if (blockEntity != null) {
                sendMessageToPlayer(eventPlayer, blockEntity.createNbt().toString());
            } else {
                sendMessageToPlayer(eventPlayer, "not block entity");
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

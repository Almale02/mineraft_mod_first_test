package net.seconddanad.first_test.item.custom;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
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
                //Direction dir = eventBlockState.get(Properties.FACING);
                sendMessageToPlayer(eventPlayer, "asdf");

                //sendMessageToPlayer(eventPlayer, dir.toString());
                SignBlockEntity sign = ((SignBlockEntity) blockEntity);

                sign.setText(sign.getText(true), false);
                sign.getBackText().withColor(sign.getFrontText().getColor());
                sign.getBackText().withGlowing(sign.getFrontText().isGlowing());
            } else {
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

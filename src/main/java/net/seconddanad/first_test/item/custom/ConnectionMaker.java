package net.seconddanad.first_test.item.custom;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.world.World;
import net.seconddanad.DanadReferenceSystem.DRSBlockEntity;
import net.seconddanad.DanadReferenceSystem.References.DRSConnectors;

public class ConnectionMaker extends Item {
    ConnectionMode connectionMode = ConnectionMode.PARENT;
    DRSBlockEntity parent;


    public ConnectionMaker(Settings settings) {
        super(settings);
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return parent != null;
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World eventWorld = context.getWorld();

        BlockEntity eventBlockEntity = eventWorld.getBlockEntity(context.getBlockPos());

        if (!eventWorld.isClient()) {
            switch (this.connectionMode) {
                case PARENT -> {
                    if (!(eventBlockEntity instanceof DRSBlockEntity blockEntity)) return ActionResult.FAIL;
                    this.parent = blockEntity;

                    this.connectionMode = ConnectionMode.CHILD;
                }
                case CHILD -> {
                    if (context.getPlayer().isSneaking()) {
                        if (!(eventBlockEntity instanceof DRSBlockEntity blockEntity)) return ActionResult.FAIL;
                        DRSConnectors.makeDoubleConnection(parent, blockEntity);
                    } else {
                        DRSConnectors.makeSingleConnection(this.parent, context.getBlockPos());
                    }
                    this.parent = null;
                    this.connectionMode = ConnectionMode.PARENT;
                }
            }
        }
        return super.useOnBlock(context);
    }

    enum ConnectionMode {
        PARENT,
        CHILD
    }
}

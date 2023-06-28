package net.seconddanad.first_test.item.custom;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.world.World;
import net.seconddanad.DanadReferenceSystem.DRSBlockEntity;
import net.seconddanad.DanadReferenceSystem.References.DRSConnectors;
import net.seconddanad.DanadReferenceSystem.References.DRSReference;

public class ConnectionRemover extends Item {
    ConnectionMode connectionMode = ConnectionMode.PARENT;
    DRSBlockEntity parent;


    public ConnectionRemover(Settings settings) {
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
            if (context.getPlayer().isSneaking()) {
                if (eventBlockEntity instanceof DRSBlockEntity drsBlockEntity) {
                    drsBlockEntity.debugConnections(context.getPlayer());
                    return ActionResult.SUCCESS;
                }
            }
            switch (this.connectionMode) {
                case PARENT -> {
                    if (!(eventBlockEntity instanceof DRSBlockEntity blockEntity)) return ActionResult.FAIL;
                    this.parent = blockEntity;

                    this.connectionMode = ConnectionMode.CHILD;
                }
                case CHILD -> {
                    if (!parent.hasConnectionWithPos(context.getBlockPos())) {
                        this.parent = null;
                        this.connectionMode = ConnectionMode.PARENT;

                        return ActionResult.FAIL;
                    }
                    DRSReference childRef = parent.getChildWithPos(context.getBlockPos()).get();

                    if (DRSConnectors.isDoubleConnection(parent.getPos(), childRef, eventWorld)) {
                        DRSConnectors.removeDoubleConnection(parent, ((DRSBlockEntity) eventBlockEntity));
                    } else {
                        DRSConnectors.removeSingleConnection(parent, context.getBlockPos());
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

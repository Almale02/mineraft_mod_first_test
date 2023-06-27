package net.seconddanad.DanadReferenceSystem.References;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.seconddanad.DanadReferenceSystem.DRSBlockEntity;

import java.util.Optional;

public class DRSConnectors {
    public static <
            A extends DRSBlockEntity,
            B extends DRSBlockEntity
            >
    void makeDoubleConnection(A parent, B child) {
        parent.addChild(new DRSReference(child.getPos()));
        child.setParent(new DRSReference(parent.getPos()));
    }
    public static <
            A extends DRSBlockEntity
            >
    void makeSingleConnection(A parent, BlockPos childPos) {
        parent.addChild(new DRSReference(childPos));
    }
    public static <
            A extends DRSBlockEntity,
            B extends DRSBlockEntity
            >
    void removeDoubleConnection(A parent, B child) {
        child.removeParent();
        parent.removeChildWithPos(child.getPos());
    }
    public static <
            A extends DRSBlockEntity
            >
    void removeSingleConnection(A parent, BlockPos childPos) {
        parent.removeChildWithPos(childPos);
    }
    public static boolean isDoubleConnection(BlockPos parentBlockPos, DRSReference childRef, World world) {
        Optional<DRSBlockEntity> optionalChild = childRef.getCachedReferenceDRSBlockEntityRef();
        childRef.cacheReference(world);

        if (optionalChild.isEmpty()) {
            return false;
        }
        Optional<DRSReference> optionalChildParentRef = optionalChild.get().getParent();

        if (optionalChildParentRef.isEmpty()) {
            return false;
        }
        return optionalChild.get().getParent().get().getReferencePos().equals(parentBlockPos);

    }
}
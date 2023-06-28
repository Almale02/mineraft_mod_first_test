package net.seconddanad.DanadReferenceSystem;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.seconddanad.DanadReferenceSystem.References.DRSReference;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Predicate;

public class DRSReferenceHead {
    DRSBlockEntity current;
    public DRSReferenceHead(@NotNull DRSBlockEntity current) {
        this.current = current;
    }


    public DRSBlockEntity get() {
        return current;
    }
    public void set(DRSBlockEntity blockEntity) {
        this.current = blockEntity;
    }
    public Optional<DRSBlockEntity> up(World world) {
        Optional<DRSReference> parentRef = current.getParent();
        if (parentRef.isEmpty()) {
            return Optional.empty();
        }

        parentRef.get().cacheReference(world);
        Optional<DRSBlockEntity> parentBlock = parentRef.get().getCachedReferenceDRSBlockEntityRef();
        if (parentBlock.isEmpty()) {
            return Optional.empty();
        }

        this.current = parentBlock.get();
        return parentBlock;
    }
    public Optional<DRSBlockEntity> downToFirst(World world) {
        Optional<DRSReference> childRef = current.getFirstChildPredicate(x -> true);
        if (childRef.isEmpty()) return Optional.empty();

        childRef.get().cacheReference(world);
        Optional<DRSBlockEntity> child = childRef.get().getCachedReferenceDRSBlockEntityRef();
        if (child.isEmpty()) return Optional.empty();

        this.current = child.get();
        return child;
    }
    public Optional<DRSBlockEntity> downToPos(World world, BlockPos pos) {
        Optional<DRSReference> childRef = current.getChildWithPos(pos);
        if (childRef.isEmpty()) return Optional.empty();

        childRef.get().cacheReference(world);
        Optional<DRSBlockEntity> child = childRef.get().getCachedReferenceDRSBlockEntityRef();
        if (child.isEmpty()) return Optional.empty();

        this.current = child.get();
        return child;

    }
    public Optional<DRSBlockEntity> downToFirstPredicate(World world, Predicate<DRSReference> predicate) {
        Optional<DRSReference> childRef = current.getFirstChildPredicate(predicate);
        if (childRef.isEmpty()) return Optional.empty();

        childRef.get().cacheReference(world);
        Optional<DRSBlockEntity> child = childRef.get().getCachedReferenceDRSBlockEntityRef();
        if (child.isEmpty()) return Optional.empty();

        this.current = child.get();
        return child;
    }
    public Optional<DRSBlockEntity> downToRandom(World world) {
        if (this.current.getChildren().isEmpty()) {
            return Optional.empty();
        }
        DRSReference childRef = this.current.getChildren().get(
                Random.create().nextBetween(
                        0,
                        this.current.getChildren().size() -1
                ));
        childRef.cacheReference(world);
        if (childRef.getCachedReferenceDRSBlockEntityRef().isEmpty()) {
            return Optional.empty();
        }
        this.current = childRef.getCachedReferenceDRSBlockEntityRef().get();
        return Optional.of(childRef.getCachedReferenceDRSBlockEntityRef().get());
    }
}

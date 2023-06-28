package net.seconddanad.DanadReferenceSystem;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.seconddanad.DanadReferenceSystem.References.DRSReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.LongConsumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static net.seconddanad.first_test.utils.PlayerMessage.sendMessageToPlayer;

public abstract class DRSBlockEntity extends BlockEntity {
    private DRSReference parent;
    private List<DRSReference> children = new ArrayList<>();


    public DRSBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
    @Override
    protected void writeNbt(NbtCompound nbt) {
        List<Long> childrenPosList = new ArrayList<>();
        this.getChildren().forEach(child -> childrenPosList.add(child.encodeReference()));

        this.getParent().ifPresent(parentReference -> {
            nbt.putLong("parent_reference", parentReference.encodeReference());
        });

        nbt.putLongArray("children_reference", childrenPosList);
        super.writeNbt(nbt);
    }
    @Override
    public void readNbt(NbtCompound nbt) {
        if (nbt.contains("parent_reference")) {
            this.setParent(new DRSReference(nbt.getLong("parent_reference")));
        }

        Arrays.stream(nbt.getLongArray("children_reference")).iterator().forEachRemaining((LongConsumer) val -> {
            this.children.add(new DRSReference(val));
        });
        super.readNbt(nbt);
    }


    public void debugConnections(PlayerEntity player) {
        this.getParent().ifPresent(parentRef -> {
            sendMessageToPlayer(player, "parent: " + parentRef.getReferencePos().toString());
        });
        this.getChildren().forEach(childRef -> {
            sendMessageToPlayer(player, "child: " +  childRef.getReferencePos().toString());
        });
    }
    public Optional<DRSReference> getChildWithPos(BlockPos pos) {
        return this.getFirstChildPredicate(child -> child.getReferencePos().equals(pos) );
    }
    public Optional<DRSReference> getFirstChildPredicate(Predicate<DRSReference> predicate) {
        return this.children.stream().filter(predicate).findFirst();
    }
    public Stream<DRSReference> getAllChildrenPredicate(Predicate<DRSReference> predicate) {
        return this.children.stream().filter(predicate);
    }
    public void addChild(DRSReference child) {
        this.children.add(child);
        this.markDirty();
    }
    public void removeLastChild() {
        this.children.remove(children.size() -1);
        this.markDirty();
    }
    public boolean removeChildWithPos(BlockPos pos) {
        boolean didRemove = this.children.removeIf(val -> val.getReferencePos().equals(pos));
        this.markDirty();
        return didRemove;
    }
    public boolean removeFirstParentPredicate(Predicate<DRSReference> predicate) {
        Optional<DRSReference> childOption = this.getFirstChildPredicate(predicate);
        if (childOption.isPresent()) {
            this.children.remove(childOption.get());
            this.markDirty();
            return true;
        }
        return false;
    }
    public void removeAllChildrenPredicate(Predicate<DRSReference> predicate) {
        this.children.removeIf(predicate);
        this.markDirty();
    }
    public void emptyChildren() {
        this.children = new ArrayList<>();
        this.markDirty();
    }
    public void removeParent() {
        this.parent = null;
        this.markDirty();
    }
    public boolean hasConnectionWithPos(BlockPos pos) {
        return this.getChildWithPos(pos).isPresent();
    }


    public Optional<DRSReference> getParent() {
        return Optional.ofNullable(parent);
    }
    public void setParent(DRSReference parent) {
        this.markDirty();
        this.parent = parent;
    }
    public List<DRSReference> getChildren() {
        return this.children;
    }
    public void setChildren(List<DRSReference> children) {
        this.markDirty();
        this.children = children;
    }
}

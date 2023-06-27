package net.seconddanad.DanadReferenceSystem;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
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
    }
    public void removeLastChild() {
        this.children.remove(children.size() -1);
    }
    public boolean removeChildWithPos(BlockPos pos) {
        return this.children.removeIf(val -> val.getReferencePos().equals(pos));
    }
    public boolean removeFirstParentPredicate(Predicate<DRSReference> predicate) {
        Optional<DRSReference> childOption = this.getFirstChildPredicate(predicate);
        if (childOption.isPresent()) {
            this.children.remove(childOption.get());
            return true;
        }
        return false;
    }
    public void removeAllChildrenPredicate(Predicate<DRSReference> predicate) {
        this.children.removeIf(predicate);
    }
    public void emptyChildren() {
        this.children = new ArrayList<>();
    }
    public void removeParent() {
        this.parent = null;
    }
    public boolean hasConnectionWithPos(BlockPos pos) {
        return this.getChildWithPos(pos).isPresent();
    }


    public Optional<DRSReference> getParent() {
        return Optional.ofNullable(parent);
    }
    public void setParent(DRSReference parent) {
        this.parent = parent;
    }
    public List<DRSReference> getChildren() {
        return this.children;
    }
    public void setChildren(List<DRSReference> children) {
        this.children = children;
    }
}

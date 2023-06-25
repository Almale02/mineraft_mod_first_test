package net.seconddanad.DanadReferenceSystem.References;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.LongConsumer;

public abstract class DRSBlockEntity extends BlockEntity {
    public DRSReference parent;
    public List<DRSReference> children = new ArrayList<>();


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

    public DRSBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
    public List<DRSReference> getChildren() {
        return this.children;
    }
    public void setChildren(List<DRSReference> children) {
        this.children = children;
    }
    public void addChild(DRSReference child) {
        this.children.add(child);
    }
    public void removeLastChild() {
        this.children.remove(children.size() -1);
    }
    public void removeChildWithPos(BlockPos pos) {
        this.children.removeIf(val -> val.getReferencePos().equals(pos));
    }
    public void emptyChildren() {
        this.children = new ArrayList<>();
    }
    public void removeParent() {
        this.parent = null;
    }


    public Optional<DRSReference> getParent() {
        return Optional.ofNullable(parent);
    }
    public void setParent(DRSReference parent) {
        this.parent = parent;
    }
}

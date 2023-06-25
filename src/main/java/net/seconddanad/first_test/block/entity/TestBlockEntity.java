package net.seconddanad.first_test.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.seconddanad.DanadReferenceSystem.References.DRSBlockEntity;

public class TestBlockEntity extends DRSBlockEntity {
    public int testData = 0;
    public TestBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntity.TEST_BLOCK_ENTITY, pos, state);
        //DRSConnectors.makeSingleConnection(this, new BlockPos(new Vec3i(0, -55, 0)));
    }
    @Override
    protected void writeNbt(NbtCompound nbt) {
        nbt.putInt("test_data", this.testData);
        super.writeNbt(nbt);
    }
    @Override
    public void readNbt(NbtCompound nbt) {
        this.testData = nbt.getInt("test_data");
        super.readNbt(nbt);
    }
}

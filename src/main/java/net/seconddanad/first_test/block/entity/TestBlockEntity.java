package net.seconddanad.first_test.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.seconddanad.first_test.FirstTest;

public class TestBlockEntity extends BlockEntity {
    public int testData = 0;
    public TestBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntity.TEST_BLOCK_ENTITY, pos, state);
    }
    @Override
    protected void writeNbt(NbtCompound nbt) {
        nbt.putInt("test_data", this.testData);
        FirstTest.LOGGER.info("write: " + this.testData);
        super.writeNbt(nbt);
    }
    @Override
    public void readNbt(NbtCompound nbt) {
        this.testData = nbt.getInt("test_data");
        FirstTest.LOGGER.info("read: "+ nbt.getInt("test_data"));
        super.readNbt(nbt);
    }
}

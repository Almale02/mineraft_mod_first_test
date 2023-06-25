package net.seconddanad.DanadReferenceSystem.References;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DRSReference {
    private BlockState cachedReferenceBlockState;
    private BlockEntity cachedReferenceBlockEntity;
    private BlockPos referencePos;

    public DRSReference(BlockPos referencePos) {
        this.referencePos = referencePos;
    }
    public DRSReference(long referencePosAsLong) {
        decodeReference(referencePosAsLong);
    }

    public long encodeReference() {
        return referencePos.asLong();
    }
    private void decodeReference(long data) {
        this.referencePos = BlockPos.fromLong(data);
    }
    public void cacheReference(World world) {
        this.cachedReferenceBlockState = world.getBlockState(this.referencePos);
        this.cachedReferenceBlockEntity = world.getBlockEntity(this.referencePos);
    }

    public BlockState getCachedReferenceBlockState() {
        return cachedReferenceBlockState;
    }
    public BlockEntity getCachedReferenceBlockEntity() {
        return cachedReferenceBlockEntity;
    }
    public BlockPos getReferencePos() {
        return referencePos;
    }
}

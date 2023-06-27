package net.seconddanad.DanadReferenceSystem.References;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.seconddanad.DanadReferenceSystem.DRSBlockEntity;

import java.util.Optional;

public class DRSReference {
    private BlockState cachedReferenceBlockStateRef;
    private DRSBlockEntity cachedReferenceDRSBlockEntityRef;
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
        this.cachedReferenceBlockStateRef = world.getBlockState(this.referencePos);
        this.cachedReferenceDRSBlockEntityRef = (DRSBlockEntity) world.getBlockEntity(this.referencePos);
    }

    public BlockState getCachedReferenceBlockStateRef() {
        return cachedReferenceBlockStateRef;
    }
    public Optional<DRSBlockEntity> getCachedReferenceDRSBlockEntityRef() {
        return Optional.ofNullable(this.cachedReferenceDRSBlockEntityRef);
    }
    public BlockPos getReferencePos() {
        return referencePos;
    }
}

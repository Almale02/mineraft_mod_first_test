package net.seconddanad.first_test.entity.custom;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.WanderAroundGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.seconddanad.first_test.FirstTest;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Optional;

import static net.seconddanad.first_test.utils.PlayerMessage.sendMessageToPlayer;

public class TestEntity extends HostileEntity implements GeoEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public int food;

    public TestEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }
    public static DefaultAttributeContainer.Builder setAttributes() {
        return AnimalEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 10)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 1)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 5)
                .add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 0);
    }
    public float getPathfindingFavor(BlockPos pos, WorldView world) {
        return world.getPhototaxisFavor(pos);
    }
    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new MeleeAttackGoal(this, 1.2, false));
        this.goalSelector.add(2, new WanderAroundGoal(this, 1.3, 1));

        this.targetSelector.add(1, new ActiveTargetGoal<>(this, PigEntity.class, false));
        super.initGoals();
    }
    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        FirstTest.LOGGER.info("ran at write custom data with data " + this.food);
        nbt.putInt("food", this.food);
        super.writeCustomDataToNbt(nbt);
    }
    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        FirstTest.LOGGER.info("ran at read custom data with data " + nbt.getInt("food"));
        this.food = nbt.getInt("food");
        super.readCustomDataFromNbt(nbt);
    }
    @Override
    public void tickMovement() {
        super.tickMovement();
    }
    @Override
    public void tick() {
        Optional<? extends PlayerEntity> optionalPlayer = this.getWorld().getPlayers().stream().findFirst();
        if (optionalPlayer.isEmpty()) {
            return;
        }

        PlayerEntity player = optionalPlayer.get();
        if (!this.getWorld().isClient) {
            long time = this.getWorld().getTime();
            if (time % (20 * 5) == 0) {
                this.food = Math.max(this.food -1, 0);
                sendMessageToPlayer(player, String.valueOf(this.food));
            }
        }
        super.tick();
    }

    //ANIMATION//
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {

    }
    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}

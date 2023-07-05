package net.seconddanad.first_test.entity.custom;

import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.WanderAroundGoal;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.seconddanad.first_test.FirstTest;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;

public class TestEntity extends HostileEntity implements GeoEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public int food;
    public int  maxFood;
    public final int eatNumber = 10;

    public TestEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
        FirstTest.LOGGER.info("asdf");
    }

    public static DefaultAttributeContainer.Builder setAttributes() {
        return AnimalEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 10)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, (double) 1 / 2)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 5)
                .add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 0)
                .add(EntityAttributes.GENERIC_ATTACK_SPEED, 6)
                .add(TestEntityData.FOOD, 100);
    }
    public float getPathfindingFavor(BlockPos pos, WorldView world) {
        return world.getPhototaxisFavor(pos);
    }
    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new PickUpFoodGoal(this));
        this.goalSelector.add(1, new MeleeAttackGoal(this, 1.2 / 2, false));
        this.goalSelector.add(2, new WanderAroundGoal(this, 1.3 / 2));

        this.targetSelector.add(1, new ActiveTargetGoal<>(this, PigEntity.class, false));
        super.initGoals();
    }
    @Nullable
    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt) {
        this.maxFood = ((int) this.getAttributeValue(TestEntityData.FOOD));
        this.food =  this.maxFood;
        this.setStackInHand(Hand.MAIN_HAND, new ItemStack(Items.PORKCHOP));
        return super.initialize(world, difficulty, spawnReason, entityData, entityNbt);
    }
    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        nbt.putInt("food", this.food);
        super.writeCustomDataToNbt(nbt);
    }
    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        this.food = nbt.getInt("food");
        this.food = nbt.getInt("food");
        super.readCustomDataFromNbt(nbt);
    }
    @Override
    public void tickMovement() {
        super.tickMovement();
    }
    @Override
    public void tick() {
        if (!this.getWorld().isClient) {
            String name = "";
            long time = this.getWorld().getTime();

            if (time % 20 * 5 == 0 && this.food < 25) {
                while (this.getMainHandStack().getCount() > 0 && this.food < 100) {
                    this.food += Math.min(
                            this.eatNumber,
                            this.maxFood - this.food);
                    this.getMainHandStack().increment(-1);
                }
            }

            if (time % (10) == 0) {
                this.food = Math.max(this.food -1, 0);
            }
            name += " " + this.food + " " + this.getMainHandStack().getCount() + " " + this.getMainHandStack().isOf(Items.PORKCHOP);
            this.setCustomName(Text.of(name));
        }
        super.tick();
    }

    @Override
    protected ActionResult interactMob(PlayerEntity player, Hand hand) {
        if (player.getWorld().isClient || hand == Hand.OFF_HAND) return ActionResult.CONSUME;
        if (this.getMainHandStack().getCount() == 0) {
            this.setStackInHand(Hand.MAIN_HAND, new ItemStack(Items.COOKED_PORKCHOP));
        } else {
            this.getMainHandStack().increment(1);
        }
        return super.interactMob(player, hand);
    }

    //ANIMATION//
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {

    }
    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
    static class TestEntityData {
        public static final EntityAttribute FOOD = register("hunger.food", new ClampedEntityAttribute("attribute.name.hunger.food", 10, 0, 100).setTracked(true));

        private static EntityAttribute register(String id, EntityAttribute attribute) {
            return Registry.register(Registries.ATTRIBUTE, id, attribute);
        }
    }
    private static class PickUpFoodGoal extends Goal {
        Predicate<ItemEntity> IS_FOOD = item -> item.getStack().isOf(Items.PORKCHOP);
        List<ItemEntity> nearbyFood;
        TestEntity entity;
        public PickUpFoodGoal(TestEntity entity) {
            this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
            this.entity = entity;
        }
        @Override
        public boolean canStart() {
            nearbyFood = getNearbyFood();

            return !nearbyFood.isEmpty() && entity.getMainHandStack().getCount() < 64;
        }
        @Override
        public boolean shouldContinue() {
            nearbyFood = getNearbyFood();
            return !nearbyFood.isEmpty() && entity.getMainHandStack().getCount() < 64;
        }
        @Override
        public void start() {
            super.start();
        }
        @Override
        public void stop() {
            super.stop();
        }
        @Override
        public void tick() {
            nearbyFood = getNearbyFood();
            if (nearbyFood.isEmpty() || entity.getMainHandStack().getCount() == 64) {
                this.stop();
            } else {
                ItemEntity nextFood = nearbyFood.get(0);
                entity.lookAtEntity(nextFood, 60, 100);
                entity.getNavigation().startMovingTo(nextFood, 1.1f);
                if (entity.distanceTo(nextFood) <= 2) {
                    nextFood.kill();
                    entity.getMainHandStack().increment(Math.min(nextFood.getStack().getCount(), 64 - entity.getMainHandStack().getCount()));
                }
            }
        }
        private List<ItemEntity> getNearbyFood() {
            return this.entity.getWorld().getEntitiesByClass(ItemEntity.class, entity.getBoundingBox().expand(8), IS_FOOD);
        }
    }
}

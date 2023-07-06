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
import net.seconddanad.GoalPool.GoalPool;
import net.seconddanad.first_test.FirstTest;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import static net.seconddanad.first_test.utils.PlayerMessage.sendMessageToPlayer;

public class TestEntity extends HostileEntity implements GeoEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private WanderAroundGoal wanderAroundGoal;
    private MeleeAttackGoal meleeAttackGoal;
    private PickUpFoodGoal pickUpFoodGoal;
    public int food;
    public int maxFood;
    public final int eatNumber = 10;
    private GoalPool mainPool;
    private GoalPool waitingFoodPool;
    public boolean waitingFood = false;

    public TestEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
        FirstTest.LOGGER.info("asdf");
        maxFood = ((int) this.getAttributeValue(TestEntityData.FOOD));
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
        wanderAroundGoal = new WanderAroundGoal(this, .8);
        meleeAttackGoal = new MeleeAttackGoal(this, 1.2 / 2, false);
        pickUpFoodGoal = new PickUpFoodGoal(this);

        mainPool = new GoalPool();
        waitingFoodPool = new GoalPool();

        mainPool.addGoal(1, wanderAroundGoal);
        mainPool.addGoal(0, pickUpFoodGoal);
        //mainPool.addGoal(1, meleeAttackGoal);

        waitingFoodPool.addGoal(0, pickUpFoodGoal);
        waitingFoodPool.addGoal(1, meleeAttackGoal);

        mainPool.activateGoals(goalSelector);
        //waitingFoodPool.activateGoals(goalSelector);
        this.targetSelector.add(1, new ActiveTargetGoal<>(this, PigEntity.class, false));
        super.initGoals();
    }
    @Nullable
    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt) {
        this.food = this.maxFood;
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
        long time = this.getWorld().getTime();

        if (time % (10) == 0) {
            if (this.getVelocity().x != 0 && this.getVelocity().z != 0) {
                this.food = Math.max(this.food -1, 0);
            }
        }
        super.tickMovement();
    }
    @Override
    public void tick() {
        if (!this.getWorld().isClient) {
            Optional<? extends PlayerEntity> optionalPlayer = this.getWorld().getPlayers().stream().findFirst();
            if (getTarget() != null) {
                optionalPlayer.ifPresent(player -> sendMessageToPlayer(player, getTarget().getType().getTranslationKey()));
            }
            String name = "";
            long time = this.getWorld().getTime();
            /////////
            if (food < 25 && this.getMainHandStack().getCount() == 0) {
                if (!waitingFood) {
                    waitingFood = true;
                    //goalSelector.getGoals().removeIf(x -> true);
                    goalSelector.remove(wanderAroundGoal);
                    goalSelector.remove(meleeAttackGoal);
                    goalSelector.add(0, pickUpFoodGoal);
                    goalSelector.add(1, meleeAttackGoal);
                    waitingFoodPool.activateGoals(goalSelector);
                }
            } else {
                if (waitingFood) {
                    waitingFood = false;
                    //goalSelector.getGoals().removeIf(x -> true); // this is the problem and also up there. TODO: create GoalPoolManager should remove and add like the example below which works.
                    goalSelector.remove(pickUpFoodGoal);
                    goalSelector.remove(meleeAttackGoal);
                    goalSelector.add(0, pickUpFoodGoal);
                    goalSelector.add(1, wanderAroundGoal);
                    mainPool.activateGoals(goalSelector);
                }
            }
            ///////////////
            if (time % 20 * 5 == 0 && this.food < 25) {
                while (this.getMainHandStack().isOf(Items.PORKCHOP) && this.food < 100) {
                    this.food += Math.min(
                            this.eatNumber,
                            this.maxFood - this.food);
                    this.getMainHandStack().increment(-1);
                }
            }

            name += " " + this.food +
                    " " + this.getMainHandStack().getCount() +
                    " " + Arrays.toString(goalSelector.getRunningGoals().toArray()) +
                    " " + waitingFood +
                    " " + goalSelector.getGoals().toArray().length +
                    " " + goalSelector.getGoals().stream().findFirst().get().getGoal().toString();

                    this.setCustomName(Text.of(name));
        }
        super.tick();
    }

    @Override
    protected ActionResult interactMob(PlayerEntity player, Hand hand) {
        if (player.getWorld().isClient || hand == Hand.OFF_HAND) return ActionResult.CONSUME;
        food = 30;
        if (waitingFood) {
            waitingFood = false;
            mainPool.activateGoals(goalSelector);
        } else {
            waitingFood = true;
            waitingFoodPool.activateGoals(goalSelector);
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
            Optional<? extends PlayerEntity> optionalPlayer = entity.getWorld().getPlayers().stream().findFirst();
            optionalPlayer.ifPresent(player -> sendMessageToPlayer(player,
                    String.valueOf(!nearbyFood.isEmpty() && entity.getMainHandStack().getCount() < 64)));


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
                    if (!entity.getMainHandStack().isOf(Items.PORKCHOP)) {
                        entity.setStackInHand(Hand.MAIN_HAND, new ItemStack(Items.PORKCHOP));
                    }
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

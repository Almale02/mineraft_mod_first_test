package net.seconddanad.first_test.entity.custom;

import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
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
import java.util.Optional;

import static net.seconddanad.first_test.utils.PlayerMessage.sendMessageToPlayer;

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
            name += " " + this.food + " " + this.getMainHandStack().getCount();
            this.setCustomName(Text.of(name));
        }
        super.tick();
    }

    @Override
    protected ActionResult interactMob(PlayerEntity player, Hand hand) {
        if (player.getWorld().isClient || hand == Hand.OFF_HAND) return ActionResult.CONSUME;
        sendMessageToPlayer(player, "food level =" + this.food);
        sendMessageToPlayer(player, "egg =" + this.getMainHandStack().getCount());
        if (this.getMainHandStack().getCount() == 0) {
            this.setStackInHand(Hand.MAIN_HAND, new ItemStack(Items.EGG));
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
}

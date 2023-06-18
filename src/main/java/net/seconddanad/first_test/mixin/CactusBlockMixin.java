package net.seconddanad.first_test.mixin;

import net.minecraft.block.CactusBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(CactusBlock.class)
public class CactusBlockMixin {
	@Redirect(
			method = "onEntityCollision",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/entity/Entity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"))
	public boolean onEntityCollision(Entity entity, DamageSource source, float amount) {
		if (!entity.isAlive()) {
			return false;
		}
		LivingEntity eventLivingEntity = (LivingEntity) entity;

		entity.damage(source, (float)Math.floor(eventLivingEntity.getHealth() /2));

		return false;
	}
}
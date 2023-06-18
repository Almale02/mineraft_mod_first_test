package net.seconddanad.first_test.mixin;

import net.minecraft.sound.MusicSound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MusicSound.class)
public class MusicSoundMixin {
    @Inject(method = "getMinDelay", at = @At("RETURN"), cancellable = true)
    private void getMinDelay(CallbackInfoReturnable<Integer> ci) {
        ci.setReturnValue(1);
    }

    @Inject(method = "getMaxDelay", at = @At("RETURN"), cancellable = true)
    public void getMaxDelay(CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(5);
    }
}
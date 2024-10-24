package concerrox.minecraft.effecticularity.core.mixin.water;

import concerrox.minecraft.effecticularity.core.EffecticularityConfiguration;
import concerrox.minecraft.effecticularity.core.world.SplashSpawner;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntitySplashMixin {
    @Shadow
    private Level level;

    @Inject(method = "doWaterSplashEffect", at = @At("TAIL"))
    protected void onSwimmingStart(CallbackInfo callbackInfo) {
        if (level.isClientSide && EffecticularityConfiguration.splashes.get()) {
            SplashSpawner.trySpawnSplash((Entity) (Object) this);
        }
    }
}
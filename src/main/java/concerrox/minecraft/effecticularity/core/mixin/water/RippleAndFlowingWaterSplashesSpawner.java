package concerrox.minecraft.effecticularity.core.mixin.water;

import concerrox.minecraft.effecticularity.core.EffecticularityConfiguration;
import concerrox.minecraft.effecticularity.core.utils.EffectiveUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.WaterFluid;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WaterFluid.class)
public class RippleAndFlowingWaterSplashesSpawner {
    @Unique
    private static boolean effecticularity_1_20_1$shouldSplash(Level world, BlockPos pos) {
        if (EffecticularityConfiguration.getCONFIG().getFlowingWaterSplashingDensity().get() > 0) {
            FluidState fluidState = world.getFluidState(pos);
            if (!fluidState.isSource() & fluidState.getOwnHeight() >= 0.77) {
                BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
                for (Direction direction : Direction.values()) {
                    if (direction != Direction.DOWN && world.getBlockState(mutable.setWithOffset(pos, direction)).isAir()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Unique
    private static boolean effecticularity_1_20_1$shouldRipple(Level world, BlockPos pos) {
        if (EffecticularityConfiguration.getCONFIG().getRainRippleDensity().get() > 0) {
            FluidState fluidState = world.getFluidState(pos);
            return fluidState.isSource() && world.isRaining() && world.getBlockState(pos.offset(0, 1, 0)).isAir();
        }
        return false;
    }

    @Inject(method = "animateTick", at = @At("HEAD"))
    protected void effective$splashAndRainRipples(Level p_230606_, BlockPos p_230607_, FluidState p_230608_, RandomSource p_230609_, CallbackInfo ci) {
        // flowing water splashes
        if (effecticularity_1_20_1$shouldSplash(p_230606_, p_230607_.above())) {
            Vec3 vec3d = p_230608_.getFlow(p_230606_, p_230607_);
            for (int i = 0; i <= p_230609_.nextInt(EffecticularityConfiguration.getCONFIG().getFlowingWaterSplashingDensity().get()); i++) {
                p_230606_.addParticle(ParticleTypes.SPLASH, p_230607_.getX() + .5 + p_230609_.nextGaussian() / 2f, p_230607_.getY() + 1 + p_230609_.nextFloat(), p_230607_.getZ() + .5 + p_230609_.nextGaussian() / 2f, vec3d.x * p_230609_.nextFloat(), p_230609_.nextFloat() / 10f, vec3d.z * p_230609_.nextFloat());
            }
        }

        // still water rain ripples
        if (effecticularity_1_20_1$shouldRipple(p_230606_, p_230607_)) {
            if (p_230609_.nextInt(10) <= EffecticularityConfiguration.getCONFIG().getRainRippleDensity().get()) {
                if (p_230606_.getBiome(p_230607_).value().getPrecipitationAt(p_230607_) == Biome.Precipitation.RAIN && p_230606_.canSeeSkyFromBelowWater(p_230607_)) {
                    EffectiveUtils.spawnWaterEffect(p_230606_, Vec3.atCenterOf(p_230607_).add(p_230609_.nextFloat() - p_230609_.nextFloat(), .39f, p_230609_.nextFloat() - p_230609_.nextFloat()), 0f, 0f, 0f, EffectiveUtils.WaterEffectType.RIPPLE);
                }
            }
        }
    }
}
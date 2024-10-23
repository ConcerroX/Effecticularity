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
        var world = p_230606_;
        var pos = p_230607_;
        var state = p_230608_;
        var random = p_230609_;
        if (effecticularity_1_20_1$shouldSplash(world, pos.above())) {
            Vec3 vec3d = state.getFlow(world, pos);
            for (int i = 0; i <= random.nextInt(EffecticularityConfiguration.getCONFIG().getFlowingWaterSplashingDensity().get()); i++) {
                world.addParticle(ParticleTypes.SPLASH, pos.getX() + .5 + random.nextGaussian() / 2f, pos.getY() + 1 + random.nextFloat(), pos.getZ() + .5 + random.nextGaussian() / 2f, vec3d.x * random.nextFloat(), random.nextFloat() / 10f, vec3d.z * random.nextFloat());
            }
        }

        // still water rain ripples
        if (effecticularity_1_20_1$shouldRipple(world, pos)) {
            if (random.nextInt(10) <= EffecticularityConfiguration.getCONFIG().getRainRippleDensity().get()) {
                if (world.getBiome(pos).value().getPrecipitationAt(pos) == Biome.Precipitation.RAIN && world.canSeeSkyFromBelowWater(pos)) {
                    EffectiveUtils.spawnWaterEffect(world, Vec3.atCenterOf(pos).add(random.nextFloat() - random.nextFloat(), .39f, random.nextFloat() - random.nextFloat()), 0f, 0f, 0f, EffectiveUtils.WaterEffectType.RIPPLE);
                }
            }
        }
    }
}
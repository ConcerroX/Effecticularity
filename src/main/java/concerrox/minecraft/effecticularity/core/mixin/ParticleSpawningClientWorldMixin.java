package concerrox.minecraft.effecticularity.core.mixin;

import concerrox.minecraft.effecticularity.core.EffecticularityConfiguration;
import concerrox.minecraft.effecticularity.core.particle.FireflyParticle;
import concerrox.minecraft.effecticularity.core.particle.types.FireflyParticleType;
import concerrox.minecraft.effecticularity.core.registry.ModParticles;
import concerrox.minecraft.effecticularity.core.settings.FireflySpawnSetting;
import concerrox.minecraft.effecticularity.core.settings.SpawnSettings;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.WritableLevelData;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import team.lodestar.lodestone.handlers.RenderHandler;
import team.lodestar.lodestone.registry.common.particle.LodestoneParticleRegistry;
import team.lodestar.lodestone.systems.particle.builder.WorldParticleBuilder;
import team.lodestar.lodestone.systems.particle.data.GenericParticleData;
import team.lodestar.lodestone.systems.particle.data.color.ColorParticleData;
import team.lodestar.lodestone.systems.particle.render_types.LodestoneWorldParticleRenderType;

import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

@Mixin(ClientLevel.class)
public abstract class ParticleSpawningClientWorldMixin extends Level {
    @Shadow
    @Final
    private LevelRenderer levelRenderer;

    @Shadow protected abstract void spawnParticle(BlockPos p_104695_, ParticleOptions p_104696_, VoxelShape p_104697_, double p_104698_);

    @Shadow public abstract void addAlwaysVisibleParticle(@NotNull ParticleOptions p_104766_, double p_104767_, double p_104768_, double p_104769_, double p_104770_, double p_104771_, double p_104772_);

    protected ParticleSpawningClientWorldMixin(WritableLevelData properties, ResourceKey<Level> registryRef, RegistryAccess registryManager, Holder<DimensionType> dimensionEntry, Supplier<ProfilerFiller> profiler, boolean isClient, boolean debugWorld, long biomeAccess, int maxChainedNeighborUpdates) {
        super(properties, registryRef, registryManager, dimensionEntry, profiler, isClient, debugWorld, biomeAccess, maxChainedNeighborUpdates);
    }

    @Inject(method = "doAnimateTick", slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/biome/Biome;getAmbientParticle()Ljava/util/Optional;")), at = @At(value = "INVOKE", target = "Ljava/util/Optional;ifPresent(Ljava/util/function/Consumer;)V", ordinal = 0, shift = At.Shift.AFTER))
    private void doAnimateTick(int p_233613_, int p_233614_, int p_233615_, int p_233616_, RandomSource p_233617_, Block p_233618_, BlockPos.MutableBlockPos p_233619_, CallbackInfo ci) {
        BlockPos.MutableBlockPos pos = p_233619_.offset(Mth.floor(this.random.nextGaussian() * 50), Mth.floor(this.random.nextGaussian() * 10), Mth.floor(this.random.nextGaussian() * 50)).mutable();
        BlockPos.MutableBlockPos pos2 = pos.mutable();
        Holder<Biome> biome = this.getBiome(pos);

        // FIREFLIES
        if (EffecticularityConfiguration.getCONFIG().fireflyDensity.get() > 0) {
            FireflySpawnSetting fireflySpawnSetting = SpawnSettings.getFIREFLIES().get(biome.unwrapKey().get());
            if (fireflySpawnSetting != null) {
                if (random.nextFloat() * 250f <= fireflySpawnSetting.spawnChance() * EffecticularityConfiguration.getCONFIG().fireflyDensity.get() && pos.getY() > this.getSeaLevel()) {
                    for (int y = this.getSeaLevel(); y <= this.getSeaLevel() * 2; y++) {
                        pos.setY(y);
                        pos2.setY(y - 1);
                        boolean canSpawnFirefly = FireflyParticle.canFlyThroughBlock(this, pos, this.getBlockState(pos))
                                && !FireflyParticle.canFlyThroughBlock(this, pos2, this.getBlockState(pos2));

                        if (canSpawnFirefly) {
//                            this.addAlwaysVisibleParticle(new ParticleOptions() {
//                                @Override
//                                public @NotNull ParticleType<?> getType() {
//                                    return new FireflyParticleType();
//                                }
//
//                                @Override
//                                public void writeToNetwork(@NotNull FriendlyByteBuf friendlyByteBuf) {}
//                                @Override
//                                public @NotNull String writeToString() {
//                                    return "";
//                                }
//                            }, pos.getX(), pos.getY(), pos.getZ(), 0, 0, 0);
                            WorldParticleBuilder.create(ModParticles.INSTANCE.getFIREFLY().get())
//                                    .setScaleData(GenericParticleData.create(12f).build())
//                                    .setTransparencyData(GenericParticleData.create(1.0f, 1f).build())
//                                    .setColorData(ColorParticleData.create(fireflySpawnSetting.color(), fireflySpawnSetting.color()).build())
////                                    .setSpinData(SpinParticleData.create(0.2f,0.4f).setSpinOffset((level.getGameTime() * 0.2f) % 6.28f).setEasing(Easing.QUARTIC_IN).build())
//                                    .setLifetime(1000)
////                                    .addMotion(0,0.5,0)
////                                    .setRandomMotion(MAGNITUDE, MAGNITUDE)
//                                    .enableNoClip()
//                                    .setGravity(2.0F)

                                    .enableForcedSpawn()
                                    .setColorData(ColorParticleData.create(fireflySpawnSetting.color(), fireflySpawnSetting.color()).build())
                                    .setScaleData(GenericParticleData.create(0.05f + random.nextFloat() * 0.10f).build())
                                    .setLifetime(ThreadLocalRandom.current().nextInt(40, 120))
                                    .setRenderType(LodestoneWorldParticleRenderType.ADDITIVE)
                                    .spawn(this, pos.getX() + random.nextFloat(), pos.getY() + random.nextFloat() * 5f, pos.getZ() + random.nextFloat());
                            break;
                        }
                    }
                }
            }
        }

//        pos = p_233619_.offset(Mth.floor(this.random.nextGaussian() * 50), Mth.floor(this.random.nextGaussian() * 25), Mth.floor(this.random.nextGaussian() * 50)).mutable();

        // WILL O' WISP
//        if (EffectiveConfig.willOWispDensity > 0) {
//            if (biome.matchesKey(BiomeKeys.SOUL_SAND_VALLEY)) {
//                if (random.nextFloat() * 100f <= 0.01f * EffectiveConfig.willOWispDensity) {
//                    if (this.getBlockState(pos).isIn(BlockTags.SOUL_FIRE_BASE_BLOCKS)) {
//                        this.addParticle(Effective.WILL_O_WISP, pos.getX(), pos.getY(), pos.getZ(), 0, 0, 0);
//                    }
//                }
//            }
//        }

        // EYES IN THE DARK
//        if ((EffectiveConfig.eyesInTheDark == EffectiveConfig.EyesInTheDarkOptions.ALWAYS || (EffectiveConfig.eyesInTheDark == EffectiveConfig.EyesInTheDarkOptions.HALLOWEEN && LocalDate.now().getMonth() == Month.OCTOBER))
//                && random.nextFloat() <= 0.00002f) {
//            this.addParticle(Effective.EYES, (double) pos.getX() + 0.5, (double) pos.getY() + 0.5, (double) pos.getZ() + 0.5, 0.0D, 0.0D, 0.0D);
//        }
    }
}
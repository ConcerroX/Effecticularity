package concerrox.minecraft.effecticularity.core.world

import concerrox.minecraft.effecticularity.core.Effecticularity
import concerrox.minecraft.effecticularity.core.particle.contracts.SplashParticleInitialData
import concerrox.minecraft.effecticularity.core.particle.types.SplashParticleType
import concerrox.minecraft.effecticularity.core.registry.ModParticles
import concerrox.minecraft.effecticularity.core.utils.EffectiveUtils
import net.minecraft.core.BlockPos
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.projectile.FishingHook
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.material.Fluids
import net.minecraft.world.phys.Vec3
import kotlin.math.min
import kotlin.math.round
import kotlin.math.sqrt

class SplashSpawner {
    companion object {
        @JvmStatic
        fun trySpawnSplash(entity: Entity) {
            val topMostEntity = if (entity.isVehicle && entity.firstPassenger != null) entity.firstPassenger else entity
            if (topMostEntity !is FishingHook) {
                val amplifier = if (topMostEntity === entity) 0.2f else 0.9f
                val impactVelocity = topMostEntity!!.deltaMovement
                if (impactVelocity.length() < 0.3f) return // EffectiveConfig.splashThreshold) return
                val splashIntensity = min(1.0, computeSplashIntensity(impactVelocity, amplifier).toDouble()).toFloat()
                for (y in -10..9) {
                    if (isValidSplashPosition(entity, y)) {
                        val splashY = Math.round(entity.y) + y + 0.9f
                        entity.level().playLocalSound(
                            entity.x,
                            splashY.toDouble(),
                            entity.z,
                            if (topMostEntity is Player) SoundEvents.PLAYER_SPLASH else SoundEvents.GENERIC_SPLASH,
                            SoundSource.AMBIENT,
                            splashIntensity * 10f,
                            0.8f,
                            true
                        )
                        val data = SplashParticleInitialData(topMostEntity.bbWidth.toDouble(), impactVelocity.y())
                        spawnSplash(
                            entity.level(), entity.x, splashY.toDouble(), entity.z, data
                        )
                        break
                    }
                }

                spawnWaterEffects(entity)
            }
        }

        @JvmStatic
        private fun computeSplashIntensity(impactVelocity: Vec3, f: Float): Float {
            return sqrt(
                impactVelocity.x * impactVelocity.x * 0.2 + impactVelocity.y * impactVelocity.y + impactVelocity.z * impactVelocity.z * 0.2
            ).toFloat() * f
        }

        @JvmStatic
        private fun spawnWaterEffects(entity: Entity) {
            val random = entity.level().getRandom()

            for (j in 0 until (entity.bbWidth * 25f).toInt()) {
                EffectiveUtils.spawnWaterEffect(
                    entity.level(),
                    Vec3(
                        entity.x + random.nextGaussian() * entity.bbWidth / 5f,
                        entity.y,
                        entity.z + random.nextGaussian() * entity.bbWidth
                    ),
                    random.nextGaussian() / 15f,
                    (random.nextFloat() / 2.5f).toDouble(),
                    random.nextGaussian() / 15f,
                    EffectiveUtils.WaterEffectType.DROPLET
                )
            }
        }

        @JvmStatic
        private fun isValidSplashPosition(entity: Entity, yOffset: Int): Boolean {
            val pos = BlockPos.containing(entity.x, round(entity.y + yOffset), entity.z)
            val blockState = entity.level().getBlockState(pos)
            if (blockState.fluidState.fluidType == Fluids.WATER.fluidType) {
                if (blockState.fluidState.isSource) {
                    return entity.level().getBlockState(pos.above()).isAir
                }
            }
            return false
        }

        /**
         * Chooses between spawning a normal splash or glow splash depending on biome
         */
        @JvmStatic
        private fun spawnSplash(world: Level, x: Double, y: Double, z: Double, data: SplashParticleInitialData) {
            val splash = if (EffectiveUtils.isGlowingWater(
                    world, BlockPos.containing(x, y, z)
                )
            ) ModParticles.SPLASH.get() else ModParticles.SPLASH.get() //Effective.GLOW_SPLASH else ModParticles.SPLASH.get()
            world.addParticle(splash.setData(data), x, y, z, 0.0, 0.0, 0.0)
        }
    }

}
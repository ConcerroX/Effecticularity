package concerrox.minecraft.effecticularity.core.utils

import concerrox.minecraft.effecticularity.core.Effecticularity
import concerrox.minecraft.effecticularity.core.EffecticularityConfiguration
import concerrox.minecraft.effecticularity.core.registry.ModParticles
import net.minecraft.core.BlockPos
import net.minecraft.core.particles.SimpleParticleType
import net.minecraft.world.entity.animal.allay.Allay
import net.minecraft.world.level.Level
import net.minecraft.world.level.biome.Biomes
import net.minecraft.world.phys.Vec3

object EffectiveUtils {
//    fun isGoingFast(allayEntity: Allay): Boolean {
//        val velocity: Vec3 = allayEntity.deltaMovement
//        val speedRequired = 0.1f
//
//        return ((velocity.x() >= speedRequired || velocity.x() <= -speedRequired)
//                || (velocity.y() >= speedRequired || velocity.y() <= -speedRequired)
//                || (velocity.z() >= speedRequired || velocity.z() <= -speedRequired))
//    }

    @JvmStatic
    fun spawnWaterEffect(
        world: Level, pos: Vec3, velocityX: Double, velocityY: Double, velocityZ: Double, waterEffect: WaterEffectType
    ) {
        val particle = if (isGlowingWater(world, pos)) when (waterEffect) {
            WaterEffectType.DROPLET -> ModParticles.DROPLET.get()
            WaterEffectType.RIPPLE -> ModParticles.RIPPLE.get()
        } else when (waterEffect) {
            WaterEffectType.DROPLET -> ModParticles.DROPLET.get()//Effective.GLOW_DROPLET
            WaterEffectType.RIPPLE -> ModParticles.RIPPLE.get()//Effective.GLOW_RIPPLE
        }

        world.addParticle(particle, pos.x(), pos.y(), pos.z(), velocityX, velocityY, velocityZ)
    }

    private fun isGlowingWater(world: Level, pos: Vec3): Boolean {
        return isGlowingWater(world, BlockPos.containing(pos))
    }

    fun isGlowingWater(world: Level, pos: BlockPos): Boolean {
        return EffecticularityConfiguration.CONFIG.glowingPlankton.get() && isNightTime(world) && world.getBiome(pos)
            .`is`(Biomes.WARM_OCEAN)
    }

    fun isNightTime(world: Level): Boolean {
        return world.getSunAngle(world.dayTime.toFloat()) in 0.25965086..0.7403491
    }
//
//    fun getGlowingWaterColor(world: Level, pos: BlockPos): Color {
//        return Color(
//            Math.min(1, world.random.nextFloat() / 5f + world.getLightLevel(LightType.BLOCK, pos) / 15f),
//            Math.min(1, world.random.nextFloat() / 5f + world.getLightLevel(LightType.BLOCK, pos) / 15f),
//            1f
//        )
//    }
//
//    fun isInCave(world: Level, pos: BlockPos): Boolean {
//        return pos.y < world.getSeaLevel() && hasStoneAbove(world, pos)
//    }
//
//    fun isInOverworld(world: Level, pos: BlockPos?): Boolean {
//        return world.getBiome(pos).isIn(ConventionalBiomeTags.IN_OVERWORLD)
//    }

    // method to check if the player has a stone material type block above them, more reliable to detect caves compared to isSkyVisible
    // (okay nvm they removed materials we're using pickaxe mineable instead lmao oh god this is gonna be so unreliable)
//    fun hasStoneAbove(world: Level, pos: BlockPos): Boolean {
//        val mutable: BlockPos.Mutable = pos.mutableCopy()
//        val startY: Int = mutable.getY()
//        for (y in startY..startY + 100) {
//            mutable.setY(y)
//            if (world.getBlockState(mutable).isSolidBlock(world, pos) && world.getBlockState(mutable)
//                    .isIn(BlockTags.PICKAXE_MINEABLE)
//            ) {
//                return true
//            }
//        }
//        return false
//    }

    enum class WaterEffectType {
        DROPLET, RIPPLE,
    }
}
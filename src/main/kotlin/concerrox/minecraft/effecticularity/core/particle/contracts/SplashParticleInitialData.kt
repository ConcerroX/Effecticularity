package concerrox.minecraft.effecticularity.core.particle.contracts

import kotlin.math.abs

class SplashParticleInitialData(val width: Double, velocityY: Double) {
    val velocityY: Double = abs(velocityY)
}
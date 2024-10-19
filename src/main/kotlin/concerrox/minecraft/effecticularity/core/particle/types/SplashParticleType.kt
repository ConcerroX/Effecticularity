package concerrox.minecraft.effecticularity.core.particle.types

import concerrox.minecraft.effecticularity.core.particle.contracts.SplashParticleInitialData
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.core.particles.SimpleParticleType
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
class SplashParticleType(alwaysShow: Boolean) : SimpleParticleType(alwaysShow) {
    var initialData: SplashParticleInitialData? = null

    fun setData(target: SplashParticleInitialData): ParticleOptions {
        this.initialData = target
        return this
    }
}
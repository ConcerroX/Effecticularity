package concerrox.minecraft.effecticularity.core.particle

import net.minecraft.client.particle.ParticleEngine.SpriteParticleRegistration
import net.minecraft.client.particle.ParticleProvider
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.core.particles.ParticleType
import net.minecraftforge.registries.RegistryObject

interface EffecticularityParticleEngine {
    fun <O : ParticleOptions?, T : ParticleType<O>?> `effecticularity_1_20_1$registerSpriteSet`(
        type: RegistryObject<T>?,
        provider: ParticleProvider<O>?
    )

    fun <O : ParticleOptions?, T : ParticleType<O>?> `effecticularity_1_20_1$registerSpriteSet`(
        type: RegistryObject<T>?,
        registration: SpriteParticleRegistration<O>?
    )
}
package concerrox.minecraft.effecticularity.core.particle.types

import concerrox.minecraft.effecticularity.core.particle.FireflyParticle
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.Particle
import net.minecraft.client.particle.ParticleEngine
import net.minecraft.client.particle.ParticleProvider
import net.minecraft.client.particle.SpriteSet
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn
import team.lodestar.lodestone.systems.particle.world.options.WorldParticleOptions
import team.lodestar.lodestone.systems.particle.world.type.LodestoneWorldParticleType

@OnlyIn(Dist.CLIENT)
class FireflyParticleType : LodestoneWorldParticleType() {
    class Factory(private var sprite: SpriteSet) : ParticleProvider<WorldParticleOptions> {

        override fun createParticle(
            data: WorldParticleOptions,
            world: ClientLevel,
            x: Double,
            y: Double,
            z: Double,
            mx: Double,
            my: Double,
            mz: Double
        ): Particle {
            return FireflyParticle(world, data, sprite as ParticleEngine.MutableSpriteSet, x, y, z, mx, my, mz)
        }
    }
}
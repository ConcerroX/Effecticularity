package concerrox.minecraft.effecticularity.core.particle

import com.mojang.blaze3d.vertex.VertexConsumer
import net.minecraft.client.Camera
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.*
import net.minecraft.core.particles.SimpleParticleType
import net.minecraft.util.Mth
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn
import org.joml.Quaternionf
import org.joml.Vector3f

class RippleParticle(
    world: ClientLevel,
    x: Double,
    y: Double,
    z: Double,
    velocityX: Double,
    velocityY: Double,
    velocityZ: Double,
    private val spriteProvider: SpriteSet
) :
    TextureSheetParticle(world, x, y, z, velocityX, velocityY, velocityZ) {

    init {
        xd = 0.0
        yd = 0.0
        zd = 0.0

        val scaleAgeModifier = 1 + random.nextInt(10)
        quadSize *= 2f + random.nextFloat() / 10f * scaleAgeModifier
        lifetime = 10 + random.nextInt(scaleAgeModifier)
    }

    override fun getRenderType(): ParticleRenderType = ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT

    override fun tick() {
        xo = x
        yo = y
        zo = z
        if (age++ >= lifetime) {
            remove()
        }
    }

    override fun render(vertexConsumer: VertexConsumer, camera: Camera, tickDelta: Float) {
        setSpriteFromAge(spriteProvider)
        val cameraPos = camera.position
        val f = (Mth.lerp(tickDelta.toDouble(), xo, x) - cameraPos.x).toFloat()
        val g = (Mth.lerp(tickDelta.toDouble(), yo, y) - cameraPos.y).toFloat()
        val h = (Mth.lerp(tickDelta.toDouble(), zo, z) - cameraPos.z).toFloat()
        val quaternion2: Quaternionf
        if (roll == 0.0f) {
            quaternion2 = camera.rotation()
        } else {
            quaternion2 = Quaternionf(camera.rotation())
            val i = Mth.lerp(tickDelta, this.oRoll, this.roll)
            quaternion2.rotateZ(i)
        }

        val vec3f = Vector3f(-1.0f, -1.0f, 0.0f)
        vec3f.rotate(quaternion2)
        val vector3fs = arrayOf(
            Vector3f(-1.0f, -1.0f, 0.0f),
            Vector3f(-1.0f, 1.0f, 0.0f),
            Vector3f(1.0f, 1.0f, 0.0f),
            Vector3f(1.0f, -1.0f, 0.0f)
        )
        val j: Float = this.getQuadSize(tickDelta)

        for (k in 0..3) {
            val vector3f = vector3fs[k]
            vector3f.rotate(Quaternionf().rotateXYZ(Math.toRadians(90.0).toFloat(), 0f, 0f))
            vector3f.mul(j)
            vector3f.add(f, g, h)
        }

        val minU = u0
        val maxU = u1
        val minV = v0
        val maxV = v1
        val lightColor = getLightColor(tickDelta)

        vertexConsumer.vertex(vector3fs[0].x().toDouble(), vector3fs[0].y().toDouble(), vector3fs[0].z().toDouble())
            .uv(maxU, maxV).color(rCol, gCol, bCol, alpha).uv2(lightColor).endVertex()
        vertexConsumer.vertex(vector3fs[1].x().toDouble(), vector3fs[1].y().toDouble(), vector3fs[1].z().toDouble())
            .uv(maxU, minV).color(rCol, gCol, bCol, alpha).uv2(lightColor).endVertex()
        vertexConsumer.vertex(vector3fs[2].x().toDouble(), vector3fs[2].y().toDouble(), vector3fs[2].z().toDouble())
            .uv(minU, minV).color(rCol, gCol, bCol, alpha).uv2(lightColor).endVertex()
        vertexConsumer.vertex(vector3fs[3].x().toDouble(), vector3fs[3].y().toDouble(), vector3fs[3].z().toDouble())
            .uv(minU, maxV).color(rCol, gCol, bCol, alpha).uv2(lightColor).endVertex()
    }

    @OnlyIn(Dist.CLIENT)
    class DefaultFactory(private val spriteProvider: SpriteSet) : ParticleProvider<SimpleParticleType> {
        override fun createParticle(
            parameters: SimpleParticleType,
            world: ClientLevel,
            x: Double,
            y: Double,
            z: Double,
            velocityX: Double,
            velocityY: Double,
            velocityZ: Double
        ): Particle {
            return RippleParticle(world, x, y, z, velocityX, velocityY, velocityZ, spriteProvider)
        }
    }
}
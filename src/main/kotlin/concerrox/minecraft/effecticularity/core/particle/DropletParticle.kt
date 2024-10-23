package concerrox.minecraft.effecticularity.core.particle

import com.mojang.blaze3d.vertex.VertexConsumer
import concerrox.minecraft.effecticularity.core.extensions.*
import concerrox.minecraft.effecticularity.core.registry.ModParticles
import net.minecraft.client.Camera
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.*
import net.minecraft.core.particles.SimpleParticleType
import net.minecraft.util.Mth
import net.minecraft.world.level.block.Blocks
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn
import org.joml.Quaternionf
import org.joml.Vector3f
import kotlin.math.round

class DropletParticle(
    world: ClientLevel,
    x: Double,
    y: Double,
    z: Double,
    velocityX: Double,
    velocityY: Double,
    velocityZ: Double,
    spriteProvider: SpriteSet
) : TextureSheetParticle(world, x, y, z, velocityX, velocityY, velocityZ) {

    init {
        xd = velocityX
        yd = velocityY
        zd = velocityZ
        lifetime = 500
        quadSize = 0.05f
        setSpriteFromAge(spriteProvider)
    }

    override fun getRenderType(): ParticleRenderType = ParticleRenderType.PARTICLE_SHEET_OPAQUE

    override fun tick() {
        xo = x
        yo = y
        zo = z

        if (age++ >= lifetime) {
            remove()
        }
        if (onGround || (age > 5 && level.isBlock(x, y + yd, z, Blocks.WATER))) {
            remove()
        }

        if (level.isBlock(x, y + yd, z, Blocks.WATER) && level.isAir(x, y, z)) {
            for (i in 0 downTo -9) {
                val yWater = round(y) + i
                if (level.isBlock(x, yWater, z, Blocks.WATER)
                    && level.getBlockState(x, yWater, z).fluidState.isSource
                    && level.isAir(x, yWater + 1, z)
                ) {
                    level.addParticle(ModParticles.RIPPLE.get(), x, yWater + 0.9f, z, 0.0, 0.0, 0.0)
                    break
                }
            }
            remove()
        }

        xd *= 0.99f
        yd -= 0.05f
        zd *= 0.99f
        move(xd, yd, zd)
    }

    override fun render(vertexConsumer: VertexConsumer, camera: Camera, tickDelta: Float) {
        val cameraPos = camera.position
        val f = (Mth.lerp(tickDelta.toDouble(), xo, x) - cameraPos.x).toFloat()
        val g = (Mth.lerp(tickDelta.toDouble(), yo, y) - cameraPos.y).toFloat()
        val h = (Mth.lerp(tickDelta.toDouble(), zo, z) - cameraPos.z).toFloat()

        val quaternion2: Quaternionf
        if (roll == 0.0f) {
            quaternion2 = camera.rotation()
        } else {
            quaternion2 = Quaternionf(camera.rotation())
            val i = Mth.lerp(tickDelta, oRoll, roll)
            quaternion2.rotateZ(i)
        }

        val vector3f = Vector3f(-1.0f, -1.0f, 0.0f)
        vector3f.rotate(quaternion2)
        val vector3fs = arrayOf(
            Vector3f(-1.0f, -1.0f, 0.0f),
            Vector3f(-1.0f, 1.0f, 0.0f),
            Vector3f(1.0f, 1.0f, 0.0f),
            Vector3f(1.0f, -1.0f, 0.0f)
        )
        val quadSize = getQuadSize(tickDelta)

        for (k in 0..3) {
            val vector3f1 = vector3fs[k]
            vector3f1.rotate(quaternion2)
            vector3f1.mul(quadSize)
            vector3f1.add(f, g, h)
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
    class DefaultFactory(private val spriteSet: SpriteSet) : ParticleProvider<SimpleParticleType> {
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
            return DropletParticle(world, x, y, z, velocityX, velocityY, velocityZ, spriteSet)
        }
    }
}
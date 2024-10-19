package concerrox.minecraft.effecticularity.core.particle

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import concerrox.minecraft.effecticularity.core.Effecticularity
import concerrox.minecraft.effecticularity.core.particle.types.SplashParticleType
import concerrox.minecraft.effecticularity.core.registry.ModParticles
import concerrox.minecraft.effecticularity.core.render.entity.model.SplashBottomModel
import concerrox.minecraft.effecticularity.core.render.entity.model.SplashBottomRimModel
import concerrox.minecraft.effecticularity.core.render.entity.model.SplashModel
import concerrox.minecraft.effecticularity.core.render.entity.model.SplashRimModel
import net.minecraft.client.Camera
import net.minecraft.client.Minecraft
import net.minecraft.client.model.Model
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.Particle
import net.minecraft.client.particle.ParticleProvider
import net.minecraft.client.particle.ParticleRenderType
import net.minecraft.client.particle.SpriteSet
import net.minecraft.client.renderer.BiomeColors
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.texture.OverlayTexture
import net.minecraft.core.BlockPos
import net.minecraft.core.particles.SimpleParticleType
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.Mth
import net.minecraft.world.entity.Entity
import net.minecraft.world.phys.Vec3
import org.joml.Vector3f

open class SplashParticle protected constructor(world: ClientLevel, x: Double, y: Double, z: Double) :
    Particle(world, x, y, z) {
    var widthMultiplier: Float
    var heightMultiplier: Float
    var wave1End: Int
    var wave2Start: Int
    var wave2End: Int
    var waterColor: Int = -1
    var waveModel: Model = SplashModel<Entity>(Minecraft.getInstance().entityModels.bakeLayer(SplashModel.MODEL_LAYER))
    var waveBottomModel: Model = SplashBottomModel<Entity>(
        Minecraft.getInstance().entityModels.bakeLayer(SplashBottomModel.MODEL_LAYER)
    )
    var waveRimModel: Model =
        SplashRimModel<Entity>(Minecraft.getInstance().entityModels.bakeLayer(SplashRimModel.MODEL_LAYER))
    var waveBottomRimModel: Model = SplashBottomRimModel<Entity>(
        Minecraft.getInstance().entityModels.bakeLayer(SplashBottomRimModel.MODEL_LAYER)
    )

    init {
        this.gravity = 0.0f
        this.widthMultiplier = 0f
        this.heightMultiplier = 0f

        this.wave1End = 12
        this.wave2Start = 7
        this.wave2End = 24
    }

    override fun render(vertexConsumer: VertexConsumer, camera: Camera, tickDelta: Float) {
        // first splash
        if (age <= this.wave1End) {
            drawSplash(Math.round((this.age / wave1End.toFloat()) * MAX_FRAME), camera, tickDelta)
        }
        // second splash
        if (age >= this.wave2Start) {
            drawSplash(
                Math.round(((this.age - wave2Start).toFloat() / (this.wave2End - this.wave2Start).toFloat()) * MAX_FRAME),
                camera,
                tickDelta,
                Vector3f(0.5f, 2f, 0.5f)
            )
        }
    }

    override fun getRenderType(): ParticleRenderType {
        return ParticleRenderType.CUSTOM
    }

    private fun drawSplash(frame: Int, camera: Camera, tickDelta: Float, multiplier: Vector3f = Vector3f(1f, 1f, 1f)) {
        if (waterColor == -1) {
            waterColor = BiomeColors.getAverageWaterColor(level, BlockPos.containing(this.x, this.y, this.z))
        }
        val r = (waterColor shr 16 and 0xFF).toFloat() / 255.0f
        val g = (waterColor shr 8 and 0xFF).toFloat() / 255.0f
        val b = (waterColor and 0xFF).toFloat() / 255.0f

        val texture: ResourceLocation = Effecticularity.id(
            ("textures/entity/splash/splash_" + Mth.clamp(
                frame, 0, MAX_FRAME
            )).toString() + ".png"
        )
        val layer: RenderType = RenderType.entityTranslucent(texture)
        val rimTexture: ResourceLocation = Effecticularity.id(
            ("textures/entity/splash/splash_rim_" + Mth.clamp(
                frame, 0, MAX_FRAME
            )).toString() + ".png"
        )
        val rimLayer: RenderType = RenderType.entityTranslucent(rimTexture)

        // splash matrices
        val modelMatrix: PoseStack = getMatrixStackFromCamera(camera, tickDelta)
        modelMatrix.scale(
            widthMultiplier * multiplier.x(), -heightMultiplier * multiplier.y(), widthMultiplier * multiplier.z()
        )
        modelMatrix.translate(0.0, -1.0, 0.0)
        val modelBottomMatrix: PoseStack = getMatrixStackFromCamera(camera, tickDelta)
        modelBottomMatrix.scale(
            widthMultiplier * multiplier.x(), heightMultiplier * multiplier.y(), widthMultiplier * multiplier.z()
        )
        modelBottomMatrix.translate(0.0, 0.001, 0.0)

        // splash bottom matrices
        val modelRimMatrix: PoseStack = getMatrixStackFromCamera(camera, tickDelta)
        modelRimMatrix.scale(
            widthMultiplier * multiplier.x(), -heightMultiplier * multiplier.y(), widthMultiplier * multiplier.z()
        )
        modelRimMatrix.translate(0.0, -1.0, 0.0)
        val modelRimBottomMatrix: PoseStack = getMatrixStackFromCamera(camera, tickDelta)
        modelRimBottomMatrix.scale(
            widthMultiplier * multiplier.x(), heightMultiplier * multiplier.y(), widthMultiplier * multiplier.z()
        )
        modelRimBottomMatrix.translate(0.0, 0.001, 0.0)

        val light: Int = this.getLightColor(tickDelta)

        val immediate: MultiBufferSource.BufferSource = Minecraft.getInstance().renderBuffers().bufferSource()

        val modelConsumer: VertexConsumer = immediate.getBuffer(layer)
        waveModel.renderToBuffer(modelMatrix, modelConsumer, light, OverlayTexture.NO_OVERLAY, r, g, b, 0.9f)
        waveBottomModel.renderToBuffer(
            modelBottomMatrix, modelConsumer, light, OverlayTexture.NO_OVERLAY, r, g, b, 0.9f
        )

        val rimModelConsumer: VertexConsumer = immediate.getBuffer(rimLayer)
        waveRimModel.renderToBuffer(
            modelRimMatrix, rimModelConsumer, light, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f
        )
        waveBottomRimModel.renderToBuffer(
            modelRimBottomMatrix, rimModelConsumer, light, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f
        )

        immediate.endBatch()
    }

    private fun getMatrixStackFromCamera(camera: Camera, tickDelta: Float): PoseStack {
        val cameraPos: Vec3 = camera.position
        val x = (Mth.lerp(tickDelta.toDouble(), this.xo, this.x) - cameraPos.x()).toFloat()
        val y = (Mth.lerp(tickDelta.toDouble(), this.yo, this.y) - cameraPos.y()).toFloat()
        val z = (Mth.lerp(tickDelta.toDouble(), this.zo, this.z) - cameraPos.z()).toFloat()

        val matrixStack = PoseStack()
        matrixStack.translate(x, y, z)
        return matrixStack
    }

    override fun tick() {
        if (this.widthMultiplier == 0f) {
            this.remove()
        }

        this.xo = this.x
        this.yo = this.y
        this.zo = this.z

        this.widthMultiplier *= 1.03f

        if (age++ >= this.wave2End) {
            this.remove()
        }

        if (this.age == 1) {
            var i = 0
            while (i < this.widthMultiplier * 10f) {
                this.level.addParticle(
                    ModParticles.DROPLET.get(),
                    this.x + (random.nextGaussian() * this.widthMultiplier / 10f),
                    this.y,
                    this.z + (random.nextGaussian() * this.widthMultiplier / 10f),
                    random.nextGaussian() / 10f * this.widthMultiplier / 2.5f,
                    (random.nextFloat() / 10f + this.heightMultiplier / 2.8f).toDouble(),
                    random.nextGaussian() / 10f * this.widthMultiplier / 2.5f
                )
                i++
            }
        } else if (this.age == wave2Start) {
            var i = 0
            while (i < this.widthMultiplier * 5f) {
                this.level.addParticle(
                    ModParticles.DROPLET.get(),
                    this.x + (random.nextGaussian() * this.widthMultiplier / 10f * .5f),
                    this.y,
                    this.z + (random.nextGaussian() * this.widthMultiplier / 10f * .5f),
                    random.nextGaussian() / 10f * this.widthMultiplier / 5f,
                    (random.nextFloat() / 10f + this.heightMultiplier / 2.2f).toDouble(),
                    random.nextGaussian() / 10f * this.widthMultiplier / 5f
                )
                i++
            }
        }
    }

    //    @Environment(EnvType.CLIENT)
    class DefaultFactory(spriteProvider: SpriteSet) : ParticleProvider<SimpleParticleType> {
        override fun createParticle(
            p0: SimpleParticleType,
            level: ClientLevel,
            x: Double,
            y: Double,
            z: Double,
            xSpeed: Double,
            ySpeed: Double,
            zSpeed: Double
        ): Particle {
            val instance = SplashParticle(level, x, y, z)
            if (p0 is SplashParticleType && p0.initialData != null) {
                val width = p0.initialData!!.width.toFloat() * 2
                instance.widthMultiplier = width
                instance.heightMultiplier = p0.initialData!!.velocityY.toFloat() * width
                instance.wave1End = 10 + Math.round(width * 1.2f)
                instance.wave2Start = 6 + Math.round(width * 0.7f)
                instance.wave2End = 20 + Math.round(width * 2.4f)
            }
            return instance
        }
    }

    companion object {
        const val MAX_FRAME: Int = 12
    }
}
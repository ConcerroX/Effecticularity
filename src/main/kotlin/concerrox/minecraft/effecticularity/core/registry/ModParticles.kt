package concerrox.minecraft.effecticularity.core.registry

import concerrox.minecraft.effecticularity.core.Effecticularity
import concerrox.minecraft.effecticularity.core.particle.SplashParticle
import concerrox.minecraft.effecticularity.core.particle.types.FireflyParticleType
import concerrox.minecraft.effecticularity.core.particle.types.SplashParticleType
import concerrox.minecraft.effecticularity.core.render.entity.model.SplashBottomModel
import concerrox.minecraft.effecticularity.core.render.entity.model.SplashBottomRimModel
import concerrox.minecraft.effecticularity.core.render.entity.model.SplashModel
import concerrox.minecraft.effecticularity.core.render.entity.model.SplashRimModel
import net.minecraft.core.particles.ParticleType
import net.minecraft.core.particles.SimpleParticleType
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.client.event.EntityRenderersEvent
import net.minecraftforge.client.event.RegisterParticleProvidersEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.RegistryObject

@Mod.EventBusSubscriber(modid = Effecticularity.ID, bus = Bus.MOD, value = [Dist.CLIENT])
object ModParticles {
    val REGISTRY: DeferredRegister<ParticleType<*>> =
        DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, Effecticularity.ID)

    val SPLASH: RegistryObject<SplashParticleType> = REGISTRY.register("splash") { SplashParticleType(true) }
    val DROPLET: RegistryObject<SimpleParticleType> = REGISTRY.register("droplet") { SimpleParticleType(true) }
    val FIREFLY: RegistryObject<FireflyParticleType> = REGISTRY.register("firefly") { FireflyParticleType() }

    @SubscribeEvent
    fun particles(event: RegisterParticleProvidersEvent) {
        event.registerSpriteSet(SPLASH.get(), SplashParticle::DefaultFactory)
        event.registerSpriteSet(FIREFLY.get(), FireflyParticleType::Factory)
    }

    @SubscribeEvent
    fun modelLayers(event: EntityRenderersEvent.RegisterLayerDefinitions) {
        event.registerLayerDefinition(SplashModel.MODEL_LAYER, SplashModel.Companion::createBodyLayer)
        event.registerLayerDefinition(SplashBottomModel.MODEL_LAYER, SplashBottomModel.Companion::createBodyLayer)
        event.registerLayerDefinition(SplashRimModel.MODEL_LAYER, SplashRimModel.Companion::createBodyLayer)
        event.registerLayerDefinition(SplashBottomRimModel.MODEL_LAYER, SplashBottomRimModel.Companion::createBodyLayer)
    }
}
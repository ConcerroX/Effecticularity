package concerrox.minecraft.effecticularity.core.registry

import concerrox.minecraft.effecticularity.core.Effecticularity
import concerrox.minecraft.effecticularity.core.particle.DropletParticle
import concerrox.minecraft.effecticularity.core.particle.EffecticularityParticleEngine
import concerrox.minecraft.effecticularity.core.particle.RippleParticle
import concerrox.minecraft.effecticularity.core.particle.SplashParticle
import concerrox.minecraft.effecticularity.core.particle.types.FireflyParticleType
import concerrox.minecraft.effecticularity.core.particle.types.SplashParticleType
import concerrox.minecraft.effecticularity.core.render.entity.model.SplashBottomModel
import concerrox.minecraft.effecticularity.core.render.entity.model.SplashBottomRimModel
import concerrox.minecraft.effecticularity.core.render.entity.model.SplashModel
import concerrox.minecraft.effecticularity.core.render.entity.model.SplashRimModel
import net.minecraft.client.Minecraft
import net.minecraft.core.Registry
import net.minecraft.core.particles.ParticleType
import net.minecraft.core.particles.SimpleParticleType
import net.minecraft.resources.ResourceKey
import net.minecraftforge.client.event.EntityRenderersEvent
import net.minecraftforge.client.event.RegisterParticleProvidersEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.IForgeRegistry
import net.minecraftforge.registries.RegistryBuilder
import net.minecraftforge.registries.RegistryObject
import java.util.function.Supplier


@Mod.EventBusSubscriber(modid = Effecticularity.ID, bus = Bus.MOD)
object ModParticles {
    // Custom resource key of the particle type only for client
    private val RESOURCE_KEY_PARTICLE_TYPE: ResourceKey<Registry<ParticleType<*>>> =
        ResourceKey.createRegistryKey(Effecticularity.id("particle_type"))

    val REGISTRY: DeferredRegister<ParticleType<*>> =
        DeferredRegister.create(RESOURCE_KEY_PARTICLE_TYPE, Effecticularity.ID)

    val REGISTRY_CLIENT: Supplier<IForgeRegistry<ParticleType<*>>> = REGISTRY
        // Disable the syncing of the registry to prevent unregistered object on dedicated server
        .makeRegistry { RegistryBuilder<ParticleType<*>>().disableSaving().disableSync() }

    // Vanilla particles
    val SPLASH: RegistryObject<SplashParticleType> = REGISTRY.register("splash") { SplashParticleType(true) }
    val DROPLET: RegistryObject<SimpleParticleType> = REGISTRY.register("droplet") { SimpleParticleType(true) }
    val RIPPLE: RegistryObject<SimpleParticleType> = REGISTRY.register("ripple") { SimpleParticleType(true) }

    // Lodestone particles
    val FIREFLY: RegistryObject<FireflyParticleType> = REGISTRY.register("firefly") { FireflyParticleType() }

    @SubscribeEvent
    fun registerProviders(event: RegisterParticleProvidersEvent) {
        val engine = (Minecraft.getInstance().particleEngine as EffecticularityParticleEngine)

        // Vanilla particles
        engine.`effecticularity_1_20_1$registerSpriteSet`(SPLASH, SplashParticle::DefaultFactory)
        engine.`effecticularity_1_20_1$registerSpriteSet`(DROPLET, DropletParticle::DefaultFactory)
        engine.`effecticularity_1_20_1$registerSpriteSet`(RIPPLE, RippleParticle::DefaultFactory)

        // Lodestone particles
        engine.`effecticularity_1_20_1$registerSpriteSet`(FIREFLY, FireflyParticleType::Factory)
    }

    @SubscribeEvent
    fun modelLayers(event: EntityRenderersEvent.RegisterLayerDefinitions) {
        event.registerLayerDefinition(SplashModel.MODEL_LAYER, SplashModel.Companion::createBodyLayer)
        event.registerLayerDefinition(SplashBottomModel.MODEL_LAYER, SplashBottomModel.Companion::createBodyLayer)
        event.registerLayerDefinition(SplashRimModel.MODEL_LAYER, SplashRimModel.Companion::createBodyLayer)
        event.registerLayerDefinition(SplashBottomRimModel.MODEL_LAYER, SplashBottomRimModel.Companion::createBodyLayer)
    }
}
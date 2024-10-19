//package org.ladysnake.effective.cosmetics
//
//import com.google.common.collect.ImmutableMap
//import com.google.gson.*
//import com.google.gson.reflect.TypeToken
//import com.mojang.authlib.minecraft.client.MinecraftClient
//import net.fabricmc.api.ClientModInitializer
//import net.fabricmc.api.EnvType
//import net.fabricmc.api.Environment
//import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry
//import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry
//import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback
//import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes
//import net.minecraft.client.network.AbstractClientPlayerEntity
//import net.minecraft.client.render.entity.feature.FeatureRendererContext
//import net.minecraft.client.render.entity.model.PlayerEntityModel
//import net.minecraft.core.registries.Registries
//import net.minecraft.entity.player.PlayerEntity
//import net.minecraft.particle.DefaultParticleType
//import net.minecraft.registry.Registry
//import net.minecraft.util.Identifier
//import net.minecraft.world.World
//import net.minecraft.world.entity.EntityType
//import net.minecraft.world.level.Level
//import concerrox.minecraft.effecticularity.core.settings.SpawnSettings
//import org.ladysnake.effective.cosmetics.data.AuraData
//import org.ladysnake.effective.cosmetics.data.OverheadData
//import org.ladysnake.effective.cosmetics.data.PlayerCosmeticData
//import org.ladysnake.effective.cosmetics.particle.aura.*
//import org.ladysnake.effective.cosmetics.particle.pet.*
//import org.ladysnake.effective.cosmetics.render.entity.feature.OverheadFeatureRenderer
//import org.ladysnake.effective.cosmetics.render.entity.model.hat.*
//import org.ladysnake.effective.cosmetics.render.entity.model.pet.LanternModel
//import org.ladysnake.effective.cosmetics.render.entity.model.pet.PrideHeartModel
//import org.ladysnake.effective.cosmetics.render.entity.model.pet.WillOWispModel
//import java.io.IOException
//import java.io.InputStreamReader
//import java.lang.reflect.Type
//import java.net.URL
//import java.util.*
//import java.util.concurrent.CompletableFuture
////
////@Environment(EnvType.CLIENT)
////class EffectiveCosmetics : ClientModInitializer {
//    fun onInitializeClient() {
//        // get illuminations player cosmetics
////        loadPlayerCosmetics()
////
////        // register model layers
////        EntityModelLayerRegistry.registerModelLayer(CrownModel.MODEL_LAYER, CrownModel::getTexturedModelData)
////        EntityModelLayerRegistry.registerModelLayer(HornsModel.MODEL_LAYER, HornsModel::getTexturedModelData)
////        EntityModelLayerRegistry.registerModelLayer(HaloModel.MODEL_LAYER, HaloModel::getTexturedModelData)
////        EntityModelLayerRegistry.registerModelLayer(TiaraModel.MODEL_LAYER, TiaraModel::getTexturedModelData)
////        EntityModelLayerRegistry.registerModelLayer(
////            VoidheartTiaraModel.MODEL_LAYER,
////            VoidheartTiaraModel::getTexturedModelData
////        )
////        EntityModelLayerRegistry.registerModelLayer(WreathModel.MODEL_LAYER, WreathModel::getTexturedModelData)
////        EntityModelLayerRegistry.registerModelLayer(WillOWispModel.MODEL_LAYER, WillOWispModel::getTexturedModelData)
////        EntityModelLayerRegistry.registerModelLayer(LanternModel.MODEL_LAYER, LanternModel::getTexturedModelData)
////        EntityModelLayerRegistry.registerModelLayer(PrideHeartModel.MODEL_LAYER, PrideHeartModel::getTexturedModelData)
////
////        // aura particles
////        TWILIGHT_AURA = Registry.register(
////            Registries.PARTICLE_TYPE,
////            Identifier.of(MODID, "twilight_aura"),
////            FabricParticleTypes.simple(true)
////        )
////        ParticleFactoryRegistry.getInstance().register(TWILIGHT_AURA) { TwilightLegacyFireflyParticle.DefaultFactory() }
////        GHOSTLY_AURA = Registry.register(
////            Registries.PARTICLE_TYPE,
////            Identifier.of(MODID, "ghostly_aura"),
////            FabricParticleTypes.simple(true)
////        )
////        ParticleFactoryRegistry.getInstance().register(GHOSTLY_AURA) { GhostlyAuraParticle.DefaultFactory() }
////        CHORUS_AURA = Registry.register(
////            Registries.PARTICLE_TYPE,
////            Identifier.of(MODID, "chorus_aura"),
////            FabricParticleTypes.simple(true)
////        )
////        ParticleFactoryRegistry.getInstance().register(CHORUS_AURA) { ChorusAuraParticle.DefaultFactory() }
////        AUTUMN_LEAVES_AURA = Registry.register(
////            Registries.PARTICLE_TYPE,
////            Identifier.of(MODID, "autumn_leaves"),
////            FabricParticleTypes.simple(true)
////        )
////        ParticleFactoryRegistry.getInstance().register(AUTUMN_LEAVES_AURA) { AutumnLeavesParticle.DefaultFactory() }
////        SCULK_TENDRIL_AURA = Registry.register(
////            Registries.PARTICLE_TYPE,
////            Identifier.of(MODID, "sculk_tendril"),
////            FabricParticleTypes.simple(true)
////        )
////        ParticleFactoryRegistry.getInstance().register(SCULK_TENDRIL_AURA) { SculkTendrilParticle.DefaultFactory() }
////        SHADOWBRINGER_AURA = Registry.register(
////            Registries.PARTICLE_TYPE,
////            Identifier.of(MODID, "shadowbringer_aura"),
////            FabricParticleTypes.simple(true)
////        )
////        ParticleFactoryRegistry.getInstance().register(SHADOWBRINGER_AURA) { ShadowbringerParticle.DefaultFactory() }
////        GOLDENROD_AURA = Registry.register(
////            Registries.PARTICLE_TYPE,
////            Identifier.of(MODID, "goldenrod_aura"),
////            FabricParticleTypes.simple(true)
////        )
////        ParticleFactoryRegistry.getInstance().register(GOLDENROD_AURA) { GoldenrodAuraParticle.DefaultFactory() }
////        CONFETTI_AURA = Registry.register(
////            Registries.PARTICLE_TYPE,
////            Identifier.of(MODID, "confetti"),
////            FabricParticleTypes.simple(true)
////        )
////        ParticleFactoryRegistry.getInstance().register(CONFETTI_AURA) { ConfettiParticle.DefaultFactory() }
////        PRISMATIC_CONFETTI_AURA = Registry.register(
////            Registries.PARTICLE_TYPE,
////            Identifier.of(MODID, "prismatic_confetti"),
////            FabricParticleTypes.simple(true)
////        )
////        ParticleFactoryRegistry.getInstance()
////            .register(PRISMATIC_CONFETTI_AURA) { PrismaticConfettiParticle.DefaultFactory() }
////        PRISMARINE_AURA = Registry.register(
////            Registries.PARTICLE_TYPE,
////            Identifier.of(MODID, "prismarine_aura"),
////            FabricParticleTypes.simple(true)
////        )
////        ParticleFactoryRegistry.getInstance().register(PRISMARINE_AURA) { PrismarineAuraParticle.DefaultFactory() }
////
////        /*
////         PRIDE PETS
////  */
////        PRIDE_PET = Registry.register(
////            Registries.PARTICLE_TYPE,
////            Identifier.of(MODID, "pride_pet"),
////            FabricParticleTypes.simple(true)
////        )
////        ParticleFactoryRegistry.getInstance().register(PRIDE_PET) { fabricSpriteProvider ->
////            DefaultFactory(
////                fabricSpriteProvider,
////                Identifier.of(MODID, "textures/entity/pride_heart.png"),
////                1.0f,
////                1.0f,
////                1.0f
////            )
////        }
////        GAY_PRIDE_PET = Registry.register(
////            Registries.PARTICLE_TYPE,
////            Identifier.of(MODID, "gay_pride_pet"),
////            FabricParticleTypes.simple(true)
////        )
////        ParticleFactoryRegistry.getInstance().register(GAY_PRIDE_PET) { fabricSpriteProvider ->
////            DefaultFactory(
////                fabricSpriteProvider,
////                Identifier.of(MODID, "textures/entity/gay_pride_heart.png"),
////                1.0f,
////                1.0f,
////                1.0f
////            )
////        }
////        TRANS_PRIDE_PET = Registry.register(
////            Registries.PARTICLE_TYPE,
////            Identifier.of(MODID, "trans_pride_pet"),
////            FabricParticleTypes.simple(true)
////        )
////        ParticleFactoryRegistry.getInstance().register(TRANS_PRIDE_PET) { fabricSpriteProvider ->
////            DefaultFactory(
////                fabricSpriteProvider,
////                Identifier.of(MODID, "textures/entity/trans_pride_heart.png"),
////                1.0f,
////                1.0f,
////                1.0f
////            )
////        }
////        LESBIAN_PRIDE_PET = Registry.register(
////            Registries.PARTICLE_TYPE,
////            Identifier.of(MODID, "lesbian_pride_pet"),
////            FabricParticleTypes.simple(true)
////        )
////        ParticleFactoryRegistry.getInstance().register(LESBIAN_PRIDE_PET) { fabricSpriteProvider ->
////            DefaultFactory(
////                fabricSpriteProvider,
////                Identifier.of(MODID, "textures/entity/lesbian_pride_heart.png"),
////                1.0f,
////                1.0f,
////                1.0f
////            )
////        }
////        BI_PRIDE_PET = Registry.register(
////            Registries.PARTICLE_TYPE,
////            Identifier.of(MODID, "bi_pride_pet"),
////            FabricParticleTypes.simple(true)
////        )
////        ParticleFactoryRegistry.getInstance().register(BI_PRIDE_PET) { fabricSpriteProvider ->
////            DefaultFactory(
////                fabricSpriteProvider,
////                Identifier.of(MODID, "textures/entity/bi_pride_heart.png"),
////                1.0f,
////                1.0f,
////                1.0f
////            )
////        }
////        ACE_PRIDE_PET = Registry.register(
////            Registries.PARTICLE_TYPE,
////            Identifier.of(MODID, "ace_pride_pet"),
////            FabricParticleTypes.simple(true)
////        )
////        ParticleFactoryRegistry.getInstance().register(ACE_PRIDE_PET) { fabricSpriteProvider ->
////            DefaultFactory(
////                fabricSpriteProvider,
////                Identifier.of(MODID, "textures/entity/ace_pride_heart.png"),
////                1.0f,
////                1.0f,
////                1.0f
////            )
////        }
////        NB_PRIDE_PET = Registry.register(
////            Registries.PARTICLE_TYPE,
////            Identifier.of(MODID, "nb_pride_pet"),
////            FabricParticleTypes.simple(true)
////        )
////        ParticleFactoryRegistry.getInstance().register(NB_PRIDE_PET) { fabricSpriteProvider ->
////            DefaultFactory(
////                fabricSpriteProvider,
////                Identifier.of(MODID, "textures/entity/nb_pride_heart.png"),
////                1.0f,
////                1.0f,
////                1.0f
////            )
////        }
////        INTERSEX_PRIDE_PET = Registry.register(
////            Registries.PARTICLE_TYPE,
////            Identifier.of(MODID, "intersex_pride_pet"),
////            FabricParticleTypes.simple(true)
////        )
////        ParticleFactoryRegistry.getInstance().register(INTERSEX_PRIDE_PET) { fabricSpriteProvider ->
////            DefaultFactory(
////                fabricSpriteProvider,
////                Identifier.of(MODID, "textures/entity/intersex_pride_heart.png"),
////                1.0f,
////                1.0f,
////                1.0f
////            )
////        }
////        ARO_PRIDE_PET = Registry.register(
////            Registries.PARTICLE_TYPE,
////            Identifier.of(MODID, "aro_pride_pet"),
////            FabricParticleTypes.simple(true)
////        )
////        ParticleFactoryRegistry.getInstance().register(ARO_PRIDE_PET) { fabricSpriteProvider ->
////            DefaultFactory(
////                fabricSpriteProvider,
////                Identifier.of(MODID, "textures/entity/aro_pride_heart.png"),
////                1.0f,
////                1.0f,
////                1.0f
////            )
////        }
////        PAN_PRIDE_PET = Registry.register(
////            Registries.PARTICLE_TYPE,
////            Identifier.of(MODID, "pan_pride_pet"),
////            FabricParticleTypes.simple(true)
////        )
////        ParticleFactoryRegistry.getInstance().register(PAN_PRIDE_PET) { fabricSpriteProvider ->
////            DefaultFactory(
////                fabricSpriteProvider,
////                Identifier.of(MODID, "textures/entity/pan_pride_heart.png"),
////                1.0f,
////                1.0f,
////                1.0f
////            )
////        }
////        AGENDER_PRIDE_PET = Registry.register(
////            Registries.PARTICLE_TYPE,
////            Identifier.of(MODID, "agender_pride_pet"),
////            FabricParticleTypes.simple(true)
////        )
////        ParticleFactoryRegistry.getInstance().register(AGENDER_PRIDE_PET) { fabricSpriteProvider ->
////            DefaultFactory(
////                fabricSpriteProvider,
////                Identifier.of(MODID, "textures/entity/agender_pride_heart.png"),
////                1.0f,
////                1.0f,
////                1.0f
////            )
////        }
////        GENDERFLUID_PRIDE_PET = Registry.register(
////            Registries.PARTICLE_TYPE,
////            Identifier.of(MODID, "genderfluid_pride_pet"),
////            FabricParticleTypes.simple(true)
////        )
////        ParticleFactoryRegistry.getInstance().register(GENDERFLUID_PRIDE_PET) { fabricSpriteProvider ->
////            DefaultFactory(
////                fabricSpriteProvider,
////                Identifier.of(MODID, "textures/entity/genderfluid_pride_heart.png"),
////                1.0f,
////                1.0f,
////                1.0f
////            )
////        }
////
////        /*
////         WILL O' WISP PETS
////  */
////        WILL_O_WISP_PET = Registry.register(
////            Registries.PARTICLE_TYPE,
////            Identifier.of(MODID, "will_o_wisp_pet"),
////            FabricParticleTypes.simple(true)
////        )
////        ParticleFactoryRegistry.getInstance().register(WILL_O_WISP_PET) { fabricSpriteProvider ->
////            DefaultFactory(
////                fabricSpriteProvider,
////                Identifier.of(MODID, "textures/entity/will_o_wisp.png"),
////                1.0f,
////                1.0f,
////                1.0f,
////                1.0f,
////                1.0f,
////                1.0f
////            )
////        }
////        DISSOLUTION_WISP_PET = Registry.register(
////            Registries.PARTICLE_TYPE,
////            Identifier.of(MODID, "dissolution_wisp_pet"),
////            FabricParticleTypes.simple(true)
////        )
////        ParticleFactoryRegistry.getInstance().register(DISSOLUTION_WISP_PET) { PetParticle.DefaultFactory() }
//
//        /*
//         SPOOKY PETS
//  */
////        JACKO_PET = Registry.register(
////            Registries.PARTICLE_TYPE,
////            Identifier.of(MODID, "jacko_pet"),
////            FabricParticleTypes.simple(true)
////        )
////        ParticleFactoryRegistry.getInstance().register(JACKO_PET) { JackoParticle.DefaultFactory() }
////        PUMPKIN_SPIRIT_PET = Registry.register(
////            Registries.PARTICLE_TYPE,
////            Identifier.of(MODID, "pumpkin_spirit_pet"),
////            FabricParticleTypes.simple(true)
////        )
////        ParticleFactoryRegistry.getInstance().register(PUMPKIN_SPIRIT_PET) { fabricSpriteProvider ->
////            DefaultFactory(
////                fabricSpriteProvider,
////                Identifier.of(MODID, "textures/entity/pumpkin_spirit.png"),
////                1.0f,
////                0.95f,
////                0.0f,
////                0.0f,
////                -0.03f,
////                0.0f
////            )
////        }
////        POLTERGEIST_PET = Registry.register(
////            Registries.PARTICLE_TYPE,
////            Identifier.of(MODID, "poltergeist_pet"),
////            FabricParticleTypes.simple(true)
////        )
////        ParticleFactoryRegistry.getInstance().register(POLTERGEIST_PET) { fabricSpriteProvider ->
////            DefaultFactory(
////                fabricSpriteProvider,
////                Identifier.of(MODID, "textures/entity/poltergeist.png"),
////                1.0f,
////                1.0f,
////                1.0f,
////                0f,
////                0f,
////                0f
////            )
////        }
////        LANTERN_PET = Registry.register(
////            Registries.PARTICLE_TYPE,
////            Identifier.of(MODID, "lantern_pet"),
////            FabricParticleTypes.simple(true)
////        )
////        ParticleFactoryRegistry.getInstance().register(LANTERN_PET) { fabricSpriteProvider ->
////            DefaultFactory(
////                fabricSpriteProvider,
////                Identifier.of(MODID, "textures/entity/lantern.png"),
////                1.0f,
////                1.0f,
////                1.0f
////            )
////        }
////        SOUL_LANTERN_PET = Registry.register(
////            Registries.PARTICLE_TYPE,
////            Identifier.of(MODID, "soul_lantern_pet"),
////            FabricParticleTypes.simple(true)
////        )
////        ParticleFactoryRegistry.getInstance().register(SOUL_LANTERN_PET) { fabricSpriteProvider ->
////            DefaultFactory(
////                fabricSpriteProvider,
////                Identifier.of(MODID, "textures/entity/soul_lantern.png"),
////                1.0f,
////                1.0f,
////                1.0f
////            )
////        }
////        CRYING_LANTERN_PET = Registry.register(
////            Registries.PARTICLE_TYPE,
////            Identifier.of(MODID, "crying_lantern_pet"),
////            FabricParticleTypes.simple(true)
////        )
////        ParticleFactoryRegistry.getInstance().register(CRYING_LANTERN_PET) { fabricSpriteProvider ->
////            DefaultFactory(
////                fabricSpriteProvider,
////                Identifier.of(MODID, "textures/entity/crying_lantern.png"),
////                1.0f,
////                1.0f,
////                1.0f
////            )
////        }
////        SOOTHING_LANTERN_PET = Registry.register(
////            Registries.PARTICLE_TYPE,
////            Identifier.of(MODID, "soothing_lantern_pet"),
////            FabricParticleTypes.simple(true)
////        )
////        ParticleFactoryRegistry.getInstance().register(SOOTHING_LANTERN_PET) { fabricSpriteProvider ->
////            DefaultFactory(
////                fabricSpriteProvider,
////                Identifier.of(MODID, "textures/entity/soothing_lantern.png"),
////                1.0f,
////                1.0f,
////                1.0f
////            )
////        }
//
//        /*
//         CROWNS FEATURE RENDERER REGISTRATION
//  */
////        LivingEntityFeatureRendererRegistrationCallback.EVENT.register { entityType, entityRenderer, registrationHelper, context ->
////            if (entityType === EntityType.PLAYER) {
////                val playerRenderer: FeatureRendererContext<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> =
////                    entityRenderer as FeatureRendererContext<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>>
////                registrationHelper.register(OverheadFeatureRenderer(playerRenderer, context))
////            }
////        }
//
//        /*
//      Aura matching and spawn chances + overhead matching + crown matching
//      Currently set to default aura settings.
//      Uncomment settings related to auras in Config.java and change getDefaultAuraSettings to getAuraSettings to restore.
//  */
////        AURAS_DATA = ImmutableMap.builder<String, AuraData>()
////            .put("twilight", AuraData(TWILIGHT_AURA) { SpawnSettings.AURAS.get("twilight") })
////            .put("ghostly", AuraData(GHOSTLY_AURA) { SpawnSettings.AURAS.get("ghostly") })
////            .put("chorus", AuraData(CHORUS_AURA) { SpawnSettings.AURAS.get("chorus") })
////            .put("autumn_leaves", AuraData(AUTUMN_LEAVES_AURA) { SpawnSettings.AURAS.get("autumn_leaves") })
////            .put("sculk_tendrils", AuraData(SCULK_TENDRIL_AURA) { SpawnSettings.AURAS.get("sculk_tendrils") })
////            .put("shadowbringer_soul", AuraData(SHADOWBRINGER_AURA) { SpawnSettings.AURAS.get("shadowbringer_soul") })
////            .put("goldenrod", AuraData(GOLDENROD_AURA) { SpawnSettings.AURAS.get("goldenrod") })
////            .put("confetti", AuraData(CONFETTI_AURA) { SpawnSettings.AURAS.get("confetti") })
////            .put("prismatic_confetti", AuraData(PRISMATIC_CONFETTI_AURA) {
////                SpawnSettings.AURAS.get(
////                    "prismatic_confetti"
////                )
////            })
////            .put("prismarine", AuraData(PRISMARINE_AURA) { SpawnSettings.AURAS.get("prismarine") })
////            .build()
//
////        OVERHEADS_DATA = ImmutableMap.builder<String, OverheadData>()
////            .put("solar_crown", OverheadData({ CrownModel() }, "solar_crown"))
////            .put("frost_crown", OverheadData({ CrownModel() }, "frost_crown"))
////            .put("pyro_crown", OverheadData({ CrownModel() }, "pyro_crown"))
////            .put("chorus_crown", OverheadData({ CrownModel() }, "chorus_crown"))
////            .put("bloodfiend_crown", OverheadData({ CrownModel() }, "bloodfiend_crown"))
////            .put("dreadlich_crown", OverheadData({ CrownModel() }, "dreadlich_crown"))
////            .put("mooncult_crown", OverheadData({ CrownModel() }, "mooncult_crown"))
////            .put("deepsculk_horns", OverheadData({ HornsModel() }, "deepsculk_horns"))
////            .put("springfae_horns", OverheadData({ HornsModel() }, "springfae_horns"))
////            .put("voidheart_tiara", OverheadData({ VoidheartTiaraModel() }, "voidheart_tiara"))
////            .put("worldweaver_halo", OverheadData({ HaloModel() }, "worldweaver_halo"))
////            .put("summerbreeze_wreath", OverheadData({ WreathModel() }, "summerbreeze_wreath"))
////            .put("glowsquid_cult_crown", OverheadData({ TiaraModel() }, "glowsquid_cult_crown"))
////            .put("timeaspect_cult_crown", OverheadData({ TiaraModel() }, "timeaspect_cult_crown"))
////            .put("prismarine_crown", OverheadData({ CrownModel() }, "prismarine_crown"))
////            .build()
//
////        PETS_DATA = ImmutableMap.builder<String, DefaultParticleType>()
////            .put("pride", PRIDE_PET)
////            .put("gay_pride", GAY_PRIDE_PET)
////            .put("trans_pride", TRANS_PRIDE_PET)
////            .put("lesbian_pride", LESBIAN_PRIDE_PET)
////            .put("bi_pride", BI_PRIDE_PET)
////            .put("ace_pride", ACE_PRIDE_PET)
////            .put("nb_pride", NB_PRIDE_PET)
////            .put("intersex_pride", INTERSEX_PRIDE_PET)
////            .put("aro_pride", ARO_PRIDE_PET)
////            .put("pan_pride", PAN_PRIDE_PET)
////            .put("agender_pride", AGENDER_PRIDE_PET)
////            .put("genderfluid_pride", GENDERFLUID_PRIDE_PET)
////            .put("jacko", JACKO_PET)
////            .put("will_o_wisp", WILL_O_WISP_PET)
////            .put("dissolution_wisp", DISSOLUTION_WISP_PET)
////            .put("pumpkin_spirit", PUMPKIN_SPIRIT_PET)
////            .put("poltergeist", POLTERGEIST_PET)
////            .put("lantern", LANTERN_PET)
////            .put("soul_lantern", SOUL_LANTERN_PET)
////            .put("crying_lantern", CRYING_LANTERN_PET)
////            .put("soothing_lantern", SOOTHING_LANTERN_PET)
////            .build()
////    }
//
////    private class PlayerCosmeticDataParser : JsonDeserializer<PlayerCosmeticData> {
////        @Throws(JsonParseException::class)
////        override fun deserialize(
////            json: JsonElement,
////            typeOfT: Type,
////            context: JsonDeserializationContext
////        ): PlayerCosmeticData {
////            val jsonObject = json.asJsonObject
////            return PlayerCosmeticData(
////                jsonObject["aura"],
////                jsonObject["color"],
////                jsonObject["color2"],
////                jsonObject["overhead"],
////                jsonObject["pet"]
////            )
////        }
////    }
//
//    companion object {
//        const val MODID: String = "effective_cosmetics"
//
//        // illuminations constants
////        const val EYES_VANISHING_DISTANCE: Int = 5
////        val COSMETICS_GSON: Gson =
////            GsonBuilder().registerTypeAdapter(PlayerCosmeticData::class.java, PlayerCosmeticDataParser()).create()
////
////        // register overhead models
////        val COSMETIC_SELECT_TYPE: Type = object : TypeToken<Map<UUID?, PlayerCosmeticData?>?>() {
////        }.type
//
//        // illuminations cosmetics
//        private const val COSMETICS_URL = "https://doctor4t.ladysnake.org/illuminations-data"
////        var AURAS_DATA: ImmutableMap<String, AuraData>? = null
////        var PETS_DATA: ImmutableMap<String, DefaultParticleType>? = null
////        var OVERHEADS_DATA: ImmutableMap<String, OverheadData>? = null
//
//        // auras
////        var TWILIGHT_AURA: DefaultParticleType? = null
////        var GHOSTLY_AURA: DefaultParticleType? = null
////        var CHORUS_AURA: DefaultParticleType? = null
////        var AUTUMN_LEAVES_AURA: DefaultParticleType? = null
////        var SCULK_TENDRIL_AURA: DefaultParticleType? = null
////        var SHADOWBRINGER_AURA: DefaultParticleType? = null
////        var GOLDENROD_AURA: DefaultParticleType? = null
////        var CONFETTI_AURA: DefaultParticleType? = null
////        var PRISMATIC_CONFETTI_AURA: DefaultParticleType? = null
////        var PRISMARINE_AURA: DefaultParticleType? = null
//
//         pets
////        var PRIDE_PET: DefaultParticleType? = null
////        var GAY_PRIDE_PET: DefaultParticleType? = null
////        var TRANS_PRIDE_PET: DefaultParticleType? = null
////        var JACKO_PET: DefaultParticleType? = null
////        var LESBIAN_PRIDE_PET: DefaultParticleType? = null
////        var BI_PRIDE_PET: DefaultParticleType? = null
////        var ACE_PRIDE_PET: DefaultParticleType? = null
////        var NB_PRIDE_PET: DefaultParticleType? = null
////        var INTERSEX_PRIDE_PET: DefaultParticleType? = null
////        var ARO_PRIDE_PET: DefaultParticleType? = null
////        var PAN_PRIDE_PET: DefaultParticleType? = null
////        var AGENDER_PRIDE_PET: DefaultParticleType? = null
////        var GENDERFLUID_PRIDE_PET: DefaultParticleType? = null
////        var WILL_O_WISP_PET: DefaultParticleType? = null
////        var DISSOLUTION_WISP_PET: DefaultParticleType? = null
////        var PUMPKIN_SPIRIT_PET: DefaultParticleType? = null
////        var POLTERGEIST_PET: DefaultParticleType? = null
////        var LANTERN_PET: DefaultParticleType? = null
////        var SOUL_LANTERN_PET: DefaultParticleType? = null
////        var CRYING_LANTERN_PET: DefaultParticleType? = null
////        var SOOTHING_LANTERN_PET: DefaultParticleType? = null
////
////        private var PLAYER_COSMETICS: Map<UUID, PlayerCosmeticData> = emptyMap<UUID, PlayerCosmeticData>()
////
////        fun getCosmeticData(player: PlayerEntity): PlayerCosmeticData? {
////            return PLAYER_COSMETICS[player.getUuid()]
////        }
//
////        fun loadPlayerCosmetics() {
////            // get illuminations player cosmetics
////            CompletableFuture.supplyAsync<Map<UUID, PlayerCosmeticData>?> {
////                try {
////                    InputStreamReader(URL(COSMETICS_URL).openStream())
////                        .use { reader ->
////                            return@supplyAsync COSMETICS_GSON.fromJson<Map<UUID, PlayerCosmeticData>>(
////                                reader,
////                                COSMETIC_SELECT_TYPE
////                            )
////                        }
////                } catch (exception: IOException) {
////                    exception.printStackTrace()
////                }
////                null
////            }.exceptionally { throwable: Throwable ->
////                throwable.printStackTrace()
////                null
////            }.thenAcceptAsync({ playerData: Map<UUID, PlayerCosmeticData>? ->
////                if (playerData != null) {
////                    PLAYER_COSMETICS = playerData
////                } else {
////                    PLAYER_COSMETICS =
////                        emptyMap<UUID, PlayerCosmeticData>()
////                }
////            }, MinecraftClient.getInstance())
////        }
//
//        fun isNightTime(world: Level): Boolean {
//            return world.getSunAngle(world.dayTime.toFloat()) >= 0.25965086 && world.getSunAngle(world.dayTime.toFloat()) <= 0.7403491
//        }
//    }
//}
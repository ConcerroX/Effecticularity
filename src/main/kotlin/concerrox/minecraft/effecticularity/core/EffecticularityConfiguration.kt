package concerrox.minecraft.effecticularity.core

import net.minecraftforge.common.ForgeConfigSpec

class EffecticularityConfiguration(builder: ForgeConfigSpec.Builder) {
    companion object {
        @JvmStatic
        var CONFIG: EffecticularityConfiguration
        @JvmStatic
        var CONFIG_SPEC: ForgeConfigSpec

        @JvmStatic
        lateinit var splashes: ForgeConfigSpec.BooleanValue
        @JvmStatic
        lateinit var splashThreshold: ForgeConfigSpec.DoubleValue
        @JvmStatic
        lateinit var flowingWaterSplashingDensity: ForgeConfigSpec.IntValue
        @JvmStatic
        lateinit var cascades: ForgeConfigSpec.BooleanValue
        @JvmStatic
        lateinit var cascadeCloudDensity: ForgeConfigSpec.DoubleValue
        @JvmStatic
        lateinit var cascadeMistDensity: ForgeConfigSpec.IntValue
        @JvmStatic
        lateinit var shouldFlowingWaterSpawnParticlesOnFirstTick: ForgeConfigSpec.BooleanValue
        @JvmStatic
        lateinit var lapisBlockUpdateParticleChance: ForgeConfigSpec.IntValue
        @JvmStatic
        lateinit var rainRippleDensity: ForgeConfigSpec.IntValue
        @JvmStatic
        lateinit var glowingPlankton: ForgeConfigSpec.BooleanValue
        @JvmStatic
        lateinit var underwaterOpenChestBubbles: ForgeConfigSpec.BooleanValue

        init {
            val configured = ForgeConfigSpec.Builder().configure(::EffecticularityConfiguration)
            CONFIG = configured.left
            CONFIG_SPEC = configured.right
        }
    }


//    var underwaterChestsOpenRandomly: ForgeConfigSpec.IntValue

    var fireflyDensity: ForgeConfigSpec.DoubleValue

    init {
        builder.push("visuals")
        splashes = builder.comment("waterEffects").define("clientBoolean", true)
        splashThreshold = builder.defineInRange("splashThreshold", 0.3, 0.0, 5.0)
        flowingWaterSplashingDensity = builder.defineInRange("flowingWaterSplashingDensity", 50, 0, 100)
        cascades = builder.define("cascades", true)
        cascadeCloudDensity = builder.defineInRange("cascadeCloudDensity", 2.5, 0.0, 5.0)
        cascadeMistDensity  = builder.defineInRange("cascadeMistDensity", 1, 0, 5)
        shouldFlowingWaterSpawnParticlesOnFirstTick = builder.define("shouldFlowingWaterSpawnParticlesOnFirstTick", true)
        lapisBlockUpdateParticleChance  = builder.defineInRange("lapisBlockUpdateParticleChance", 1, 0, 10)
        rainRippleDensity  = builder.defineInRange("rainRippleDensity", 5, 0, 10)
        glowingPlankton = builder.define("glowingPlankton", true)
        underwaterOpenChestBubbles = builder.define("underwaterOpenChestBubbles", true)

        fireflyDensity = builder.comment("illuminatedEffects")
            .translation(Effecticularity.ID + ".config.fireflyDensity")
            .defineInRange("fireflyDensity", 1.0, 0.0, 10.0)

        builder.pop()
    }
}
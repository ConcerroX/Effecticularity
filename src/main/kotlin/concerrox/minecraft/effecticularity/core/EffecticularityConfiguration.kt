package concerrox.minecraft.effecticularity.core

import net.minecraftforge.common.ForgeConfigSpec

class EffecticularityConfiguration(builder: ForgeConfigSpec.Builder) {
    companion object {
        @JvmStatic
        var CONFIG: EffecticularityConfiguration
        @JvmStatic
        var CONFIG_SPEC: ForgeConfigSpec

        init {
            val configured = ForgeConfigSpec.Builder().configure(::EffecticularityConfiguration)
            CONFIG = configured.left
            CONFIG_SPEC = configured.right
        }
    }

    var splashes: ForgeConfigSpec.BooleanValue
    var splashThreshold: ForgeConfigSpec.DoubleValue
    var flowingWaterSplashingDensity: ForgeConfigSpec.IntValue
    var cascades: ForgeConfigSpec.BooleanValue
    var cascadeCloudDensity: ForgeConfigSpec.DoubleValue
    var cascadeMistDensity: ForgeConfigSpec.IntValue
    var shouldFlowingWaterSpawnParticlesOnFirstTick: ForgeConfigSpec.BooleanValue
    var lapisBlockUpdateParticleChance: ForgeConfigSpec.IntValue
    var rainRippleDensity: ForgeConfigSpec.IntValue
    var glowingPlankton: ForgeConfigSpec.BooleanValue
    var underwaterOpenChestBubbles: ForgeConfigSpec.BooleanValue
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
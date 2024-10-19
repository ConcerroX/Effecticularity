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

    @JvmField
    var splashes: ForgeConfigSpec.BooleanValue
    @JvmField
    var fireflyDensity: ForgeConfigSpec.DoubleValue

    init {
        builder.push("Visuals")

        splashes = builder.comment("An example boolean in the client config")
            .translation(Effecticularity.ID + ".config.clientBoolean")
            .define("clientBoolean", true)
        fireflyDensity = builder.comment("illuminatedEffects")
            .translation(Effecticularity.ID + ".config.fireflyDensity")
            .defineInRange("fireflyDensity", 1.0, 0.0, 10.0)

        builder.pop()
    }
}
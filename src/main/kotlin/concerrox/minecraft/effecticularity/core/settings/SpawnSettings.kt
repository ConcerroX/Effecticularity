package concerrox.minecraft.effecticularity.core.settings

import com.google.common.collect.ImmutableMap
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.biome.Biomes
import java.awt.Color


class SpawnSettings {
//    val AURAS: ImmutableMap<String, AuraSettings> = ImmutableMap.builder<String, AuraSettings>()
//        .put("twilight", AuraSettings(0.1f, 1))
//        .put("ghostly", AuraSettings(0.1f, 1))
//        .put("chorus", AuraSettings(0.1f, 1))
//        .put("autumn_leaves", AuraSettings(0.3f, 1))
//        .put("sculk_tendrils", AuraSettings(0.1f, 1))
//        .put("shadowbringer_soul", AuraSettings(0.1f, 1))
//        .put("goldenrod", AuraSettings(0.4f, 1))
//        .put("confetti", AuraSettings(0.1f, 1))
//        .put("prismatic_confetti", AuraSettings(0.1f, 1))
//        .put("prismarine", AuraSettings(0.1f, 1))
//        .build()
    companion object {
    @JvmStatic
    var LOW: Float = 0.01f
    @JvmStatic
    var MEDIUM: Float = 0.05f
    @JvmStatic
    var HIGH: Float = 0.1f
    @JvmStatic
    val FIREFLIES: ImmutableMap<ResourceKey<Biome>, FireflySpawnSetting> =
        ImmutableMap.builder<ResourceKey<Biome>, FireflySpawnSetting>()
            .put(Biomes.PLAINS, FireflySpawnSetting(LOW, Color(0x91BD59)))
            .put(Biomes.SUNFLOWER_PLAINS, FireflySpawnSetting(LOW, Color(0x91BD59)))
            .put(Biomes.OLD_GROWTH_PINE_TAIGA, FireflySpawnSetting(LOW, Color(0xBFFF00)))
            .put(Biomes.OLD_GROWTH_SPRUCE_TAIGA, FireflySpawnSetting(LOW, Color(0xBFFF00)))
            .put(Biomes.TAIGA, FireflySpawnSetting(LOW, Color(0xBFFF00)))
            .put(Biomes.FOREST, FireflySpawnSetting(MEDIUM, Color(0xBFFF00)))
            .put(Biomes.FLOWER_FOREST, FireflySpawnSetting(MEDIUM, Color(0xFF7FED)))
            .put(Biomes.BIRCH_FOREST, FireflySpawnSetting(MEDIUM, Color(0xE4FF00)))
            .put(Biomes.DARK_FOREST, FireflySpawnSetting(MEDIUM, Color(0x006900)))
            .put(Biomes.OLD_GROWTH_BIRCH_FOREST, FireflySpawnSetting(MEDIUM, Color(0xE4FF00)))
            .put(Biomes.JUNGLE, FireflySpawnSetting(MEDIUM, Color(0x00FF21)))
            .put(Biomes.SPARSE_JUNGLE, FireflySpawnSetting(MEDIUM, Color(0x00FF21)))
            .put(Biomes.BAMBOO_JUNGLE, FireflySpawnSetting(MEDIUM, Color(0x00FF21)))
            .put(Biomes.LUSH_CAVES, FireflySpawnSetting(MEDIUM, Color(0xF2B646)))
            .put(Biomes.SWAMP, FireflySpawnSetting(HIGH, Color(0xBFFF00)))
            .put(Biomes.MANGROVE_SWAMP, FireflySpawnSetting(HIGH, Color(0xBFFF00)))
            .build()
    }

}
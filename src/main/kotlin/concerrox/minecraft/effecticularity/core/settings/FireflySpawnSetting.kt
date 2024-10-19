package concerrox.minecraft.effecticularity.core.settings

import java.awt.Color

@JvmRecord
data class FireflySpawnSetting // spawn chance being the chance percent of a firefly spawning per tick
    (
    val spawnChance: Float,
    val color: Color
)
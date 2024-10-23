package concerrox.minecraft.effecticularity.core

import concerrox.minecraft.effecticularity.core.registry.ModParticles
import net.minecraft.resources.ResourceLocation
import net.minecraftforge.fml.ModLoadingContext
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.config.ModConfig
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import thedarkcolour.kotlinforforge.forge.MOD_BUS

@Mod(Effecticularity.ID)
object Effecticularity {

    const val ID = "effecticularity"
    private val LOGGER: Logger = LogManager.getLogger(ID)

    init {
        ModParticles.REGISTRY.register(MOD_BUS)
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, EffecticularityConfiguration.CONFIG_SPEC)

//        runForDist(
//            clientTarget = {
//                MOD_BUS.addListener(Effecticularity::onClientSetup)
//            },
//            serverTarget = {
//                MOD_BUS.addListener(Effecticularity::onServerSetup)
//            })
    }

    fun id(identifier: String): ResourceLocation {
        return ResourceLocation(ID, identifier)
    }

//    private fun onClientSetup(event: FMLClientSetupEvent) {
//        Satin.INSTANCE.onClientSetup(event)
//    }
//
//    private fun onServerSetup(event: FMLDedicatedServerSetupEvent) {
//
//    }

    @JvmStatic
    fun cout(string: String) {
        LOGGER.log(Level.INFO, string)
    }
}
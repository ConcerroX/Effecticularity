package org.ladysnake.satin;

import concerrox.minecraft.effecticularity.core.Effecticularity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ladysnake.satin.api.event.ResolutionChangeEventHandler;
import org.ladysnake.satin.api.event.WorldRendererReloadEventHandler;
import org.ladysnake.satin.impl.ReloadableShaderEffectManager;

public class Satin {

    public static final String MOD_ID = "satin";
    public static final Logger LOGGER = LogManager.getLogger("Satin");
    public static final Satin INSTANCE = new Satin();

    public static boolean areShadersDisabled() {
        return false;
    }

    public Satin() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    public void onClientSetup(FMLClientSetupEvent event) {
        ResolutionChangeEventHandler.register(ReloadableShaderEffectManager.INSTANCE);
        WorldRendererReloadEventHandler.register(ReloadableShaderEffectManager.INSTANCE);
    }

}

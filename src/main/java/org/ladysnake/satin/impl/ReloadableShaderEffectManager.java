/*
 * Satin
 * Copyright (C) 2019-2024 Ladysnake
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; If not, see <https://www.gnu.org/licenses>.
 */
package org.ladysnake.satin.impl;

import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import net.minecraft.client.Minecraft;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.client.renderer.LevelRenderer;
import com.mojang.blaze3d.platform.Window;
import net.minecraft.server.packs.resources.ResourceProvider;
import net.minecraft.resources.ResourceLocation;
import org.ladysnake.satin.Satin;
import org.ladysnake.satin.api.event.ResolutionChangeEventHandler;
import org.ladysnake.satin.api.event.WorldRendererReloadEventHandler;
import org.ladysnake.satin.api.managed.ManagedCoreShader;
import org.ladysnake.satin.api.managed.ManagedShaderEffect;
import org.ladysnake.satin.api.managed.ShaderEffectManager;

import java.util.Set;
import java.util.function.Consumer;

/**
 * A {@link ShaderEffectManager} that is reloaded along with resources
 *
 * @see ShaderEffectManager
 * @see ResettableManagedShaderEffect
 */
public final class ReloadableShaderEffectManager implements ShaderEffectManager, ResolutionChangeEventHandler.Callback, WorldRendererReloadEventHandler.Callback {
    public static final ReloadableShaderEffectManager INSTANCE = new ReloadableShaderEffectManager();

    private final Set<ResettableManagedShaderBase<?>> managedShaders = new ReferenceOpenHashSet<>();

    /**
     * Manages a post-processing shader loaded from a json definition file
     *
     * @param location the location of the json within your mod's assets
     * @return a lazily initialized shader effect
     */
    @Override
    public ManagedShaderEffect manage(ResourceLocation location) {
        return manage(location, s -> { });
    }

    /**
     * Manages a post-processing shader loaded from a json definition file
     *
     * @param location            the location of the json within your mod's assets
     * @param initCallback a block ran once the shader effect is initialized
     * @return a lazily initialized screen shader
     */
    @Override
    public ManagedShaderEffect manage(ResourceLocation location, Consumer<ManagedShaderEffect> initCallback) {
        ResettableManagedShaderEffect ret = new ResettableManagedShaderEffect(location, initCallback);
        managedShaders.add(ret);
        return ret;
    }

    @Override
    public ManagedCoreShader manageCoreShader(ResourceLocation location) {
        return manageCoreShader(location, DefaultVertexFormat.NEW_ENTITY);
    }

    @Override
    public ManagedCoreShader manageCoreShader(ResourceLocation location, VertexFormat vertexFormat) {
        return manageCoreShader(location, vertexFormat, (s) -> {});
    }

    @Override
    public ManagedCoreShader manageCoreShader(ResourceLocation location, VertexFormat vertexFormat, Consumer<ManagedCoreShader> initCallback) {
        ResettableManagedCoreShader ret = new ResettableManagedCoreShader(location, vertexFormat, initCallback);
        managedShaders.add(ret);
        return ret;
    }

    /**
     * Removes a shader from the global list of managed shaders,
     * making it not respond to resource reloading and screen resizing.
     * This also calls {@link ResettableManagedShaderEffect#release()} to release the shader's resources.
     * A <code>ManagedShaderEffect</code> object cannot be used after it has been disposed of.
     *
     * @param shader the shader to stop managing
     * @see ResettableManagedShaderEffect#release()
     */
    @Override
    public void dispose(ManagedShaderEffect shader) {
        shader.release();
        managedShaders.remove(shader);
    }

    @Override
    public void dispose(ManagedCoreShader shader) {
        shader.release();
        managedShaders.remove(shader);
    }

    public void reload(ResourceProvider shaderResources) {
        for (ResettableManagedShaderBase<?> ss : managedShaders) {
            ss.initializeOrLog(shaderResources);
        }
    }

    @Override
    public void onResolutionChanged(int newWidth, int newHeight) {
        runShaderSetup(newWidth, newHeight);
    }

    @Override
    public void onRendererReload(LevelRenderer renderer) {
        Window window = Minecraft.getInstance().getWindow();
        runShaderSetup(window.getWidth(), window.getHeight());
    }

    private void runShaderSetup(int newWidth, int newHeight) {
        if (!Satin.areShadersDisabled() && !managedShaders.isEmpty()) {
            for (ResettableManagedShaderBase<?> ss : managedShaders) {
                if (ss.isInitialized()) {
                    ss.setup(newWidth, newHeight);
                }
            }
        }
    }
}
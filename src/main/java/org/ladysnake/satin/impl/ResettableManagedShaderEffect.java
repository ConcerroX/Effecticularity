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

import com.google.common.base.Preconditions;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import com.mojang.blaze3d.pipeline.RenderTarget;
import net.minecraft.client.renderer.EffectInstance;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.server.packs.resources.ResourceProvider;
import net.minecraft.resources.ResourceLocation;
import org.apiguardian.api.API;
import org.joml.Matrix4f;
import org.ladysnake.satin.Satin;
import org.ladysnake.satin.api.managed.ManagedFramebuffer;
import org.ladysnake.satin.api.managed.ManagedShaderEffect;
import org.ladysnake.satin.api.managed.ShaderEffectManager;
import org.ladysnake.satin.api.managed.uniform.SamplerUniformV2;
import org.ladysnake.satin.api.util.ShaderPrograms;
import org.ladysnake.satin.mixin.client.AccessiblePassesShaderEffect;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import static org.apiguardian.api.API.Status.INTERNAL;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;

/**
 * A {@link ManagedShaderEffect} that can be {@link #setup} several times in its lifetime,
 * triggering an initialization callback.
 *
 * @see ReloadableShaderEffectManager
 * @see ManagedShaderEffect
 * @since 1.0.0
 */
public final class ResettableManagedShaderEffect extends ResettableManagedShaderBase<PostChain> implements ManagedShaderEffect {

    /**Callback to run once each time the shader effect is initialized*/
    private final Consumer<ManagedShaderEffect> initCallback;
    private final Map<String, FramebufferWrapper> managedTargets;
    private final Map<String, ManagedSamplerUniformV2> managedSamplers = new HashMap<>();

    /**
     * Creates a new shader effect. <br>
     * <b>Users should obtain instanced of this class through {@link ShaderEffectManager}</b>
     *
     * @param location         the location of a shader effect JSON definition file
     * @param initCallback code to run in {@link #setup(int, int)}
     * @see ReloadableShaderEffectManager#manage(ResourceLocation)
     * @see ReloadableShaderEffectManager#manage(ResourceLocation, Consumer)
     */
    @API(status = INTERNAL)
    public ResettableManagedShaderEffect(ResourceLocation location, Consumer<ManagedShaderEffect> initCallback) {
        super(location);
        this.initCallback = initCallback;
        this.managedTargets = new HashMap<>();
    }

    @Nullable
    @Override
    public PostChain getShaderEffect() {
        return getShaderOrLog();
    }

    @Override
    public void initialize() throws IOException {
        super.initialize(Minecraft.getInstance().getResourceManager());
    }

    @Override
    protected PostChain parseShader(ResourceProvider resourceFactory, Minecraft mc, ResourceLocation location) throws IOException {
        return new PostChain(mc.getTextureManager(), mc.getResourceManager(), mc.getMainRenderTarget(), location);
    }

    @Override
    public void setup(int windowWidth, int windowHeight) {
        Preconditions.checkNotNull(shader);
        this.shader.resize(windowWidth, windowHeight);

        for (ManagedUniformBase uniform : this.getManagedUniforms()) {
            setupUniform(uniform, shader);
        }

        for (FramebufferWrapper buf : this.managedTargets.values()) {
            buf.findTarget(this.shader);
        }

        this.initCallback.accept(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void render(float tickDelta) {
        PostChain sg = this.getShaderEffect();
        if (sg != null) {
            RenderSystem.disableBlend();
            RenderSystem.disableDepthTest();
            RenderSystem.resetTextureMatrix();
            sg.process(tickDelta);
            Minecraft.getInstance().getMainRenderTarget().bindWrite(true);
            RenderSystem.disableBlend();
            RenderSystem.blendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA); // restore blending
            RenderSystem.enableDepthTest();
        }
    }

    @Override
    public ManagedFramebuffer getTarget(String name) {
        return this.managedTargets.computeIfAbsent(name, n -> {
            FramebufferWrapper ret = new FramebufferWrapper(n);
            if (this.shader != null) {
                ret.findTarget(this.shader);
            }
            return ret;
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setUniformValue(String uniformName, int value) {
        this.findUniform1i(uniformName).set(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setUniformValue(String uniformName, int value0, int value1) {
        this.findUniform2i(uniformName).set(value0, value1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setUniformValue(String uniformName, int value0, int value1, int value2) {
        this.findUniform3i(uniformName).set(value0, value1, value2);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setUniformValue(String uniformName, int value0, int value1, int value2, int value3) {
        this.findUniform4i(uniformName).set(value0, value1, value2, value3);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setUniformValue(String uniformName, float value) {
        this.findUniform1f(uniformName).set(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setUniformValue(String uniformName, float value0, float value1) {
        this.findUniform2f(uniformName).set(value0, value1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setUniformValue(String uniformName, float value0, float value1, float value2) {
        this.findUniform3f(uniformName).set(value0, value1, value2);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setUniformValue(String uniformName, float value0, float value1, float value2, float value3) {
        this.findUniform4f(uniformName).set(value0, value1, value2, value3);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setUniformValue(String uniformName, Matrix4f value) {
        this.findUniformMat4(uniformName).set(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setSamplerUniform(String samplerName, AbstractTexture texture) {
        this.findSampler(samplerName).set(texture);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setSamplerUniform(String samplerName, RenderTarget textureFbo) {
        this.findSampler(samplerName).set(textureFbo);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setSamplerUniform(String samplerName, int textureName) {
        this.findSampler(samplerName).set(textureName);
    }

    @Override
    public SamplerUniformV2 findSampler(String samplerName) {
        return manageUniform(this.managedSamplers, ManagedSamplerUniformV2::new, samplerName, "sampler");
    }

    public void setupDynamicUniforms(Runnable dynamicSetBlock) {
        this.setupDynamicUniforms(0, dynamicSetBlock);
    }

    public void setupDynamicUniforms(int index, Runnable dynamicSetBlock) {
        AccessiblePassesShaderEffect sg = (AccessiblePassesShaderEffect) this.getShaderEffect();
        if (sg != null) {
            EffectInstance sm = sg.getPasses().get(index).getEffect();
            ShaderPrograms.useShader(sm.getId());
            dynamicSetBlock.run();
            ShaderPrograms.useShader(0);
        }
    }

    @Override
    protected boolean setupUniform(ManagedUniformBase uniform, PostChain shader) {
        return uniform.findUniformTargets(((AccessiblePassesShaderEffect) shader).getPasses());
    }

    @Override
    protected void logInitError(IOException e) {
        Satin.LOGGER.error("Could not create screen shader {}", this.getLocation(), e);
    }

    private @Nullable PostChain getShaderOrLog() {
        if (!this.isInitialized() && !this.isErrored()) {
            this.initializeOrLog(Minecraft.getInstance().getResourceManager());
        }
        return this.shader;
    }
}
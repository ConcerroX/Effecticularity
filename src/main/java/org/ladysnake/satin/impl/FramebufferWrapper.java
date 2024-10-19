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

import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;
import com.mojang.blaze3d.pipeline.RenderTarget;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.client.renderer.RenderType;
import org.ladysnake.satin.Satin;
import org.ladysnake.satin.api.managed.ManagedFramebuffer;

import javax.annotation.Nullable;

public final class FramebufferWrapper implements ManagedFramebuffer {
    private final RenderLayerSupplier renderLayerSupplier;
    private final String name;
    @Nullable
    private RenderTarget wrapped;

    FramebufferWrapper(String name) {
        this.name = name;
        this.renderLayerSupplier = RenderLayerSupplier.framebuffer(this.name + System.identityHashCode(this), () -> this.beginWrite(false), () -> Minecraft.getInstance().getMainRenderTarget().bindWrite(false));
    }

    void findTarget(@Nullable PostChain shaderEffect) {
        if (shaderEffect == null) {
            this.wrapped = null;
        } else {
            this.wrapped = shaderEffect.getTempTarget(this.name);
            if (this.wrapped == null) {
                Satin.LOGGER.warn("No target framebuffer found with name {} in shader {}", this.name, shaderEffect.getName());
            }
        }
    }

    public String getName() {
        return name;
    }

    @Nullable
    @Override
    public RenderTarget getFramebuffer() {
        return wrapped;
    }

    @Override
    public void copyDepthFrom(RenderTarget buffer) {
        if (this.wrapped != null) {
            this.wrapped.copyDepthFrom(buffer);
        }
    }

    @Override
    public void beginWrite(boolean updateViewport) {
        if (this.wrapped != null) {
            this.wrapped.bindWrite(updateViewport);
        }
    }

    @Override
    public void draw() {
        Window window = Minecraft.getInstance().getWindow();
        this.draw(window.getWidth(), window.getHeight(), true);
    }

    @Override
    public void draw(int width, int height, boolean disableBlend) {
        if (this.wrapped != null) {
            this.wrapped.createBuffers(width, height, disableBlend);
        }
    }

    @Override
    public void clear() {
        clear(Minecraft.ON_OSX);
    }

    @Override
    public void clear(boolean swallowErrors) {
        if (this.wrapped != null) {
            this.wrapped.clear(swallowErrors);
        }
    }

    @Override
    public RenderType getRenderLayer(RenderType baseLayer) {
        return this.renderLayerSupplier.getRenderLayer(baseLayer);
    }
}
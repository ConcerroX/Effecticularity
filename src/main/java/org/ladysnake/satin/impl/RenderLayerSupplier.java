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

import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderStateShard;
import com.mojang.blaze3d.vertex.VertexFormat;
import org.jetbrains.annotations.Nullable;
import org.ladysnake.satin.mixin.client.render.RenderPhaseAccessor;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class RenderLayerSupplier {
    private final Consumer<RenderType.CompositeState.CompositeStateBuilder> transform;
    private final Map<RenderType, RenderType> renderLayerCache = new HashMap<>();
    private final String uniqueName;
    private final @Nullable VertexFormat vertexFormat;

    public static RenderLayerSupplier framebuffer(String name, Runnable setupState, Runnable cleanupState) {
        RenderStateShard.OutputStateShard target = new RenderStateShard.OutputStateShard(
                name + "_target",
                setupState,
                cleanupState
        );
        return new RenderLayerSupplier(name, builder -> builder.setOutputState(target));
    }

    public static RenderLayerSupplier shader(String name, VertexFormat vertexFormat, Supplier<ShaderInstance> shaderSupplier) {
        RenderStateShard shader = Helper.makeShader(shaderSupplier);
        return new RenderLayerSupplier(name, vertexFormat, builder -> Helper.applyShader(builder, shader));
    }

    public RenderLayerSupplier(String name,  Consumer<RenderType.CompositeState.CompositeStateBuilder> transformer) {
        this(name, null, transformer);
    }

    public RenderLayerSupplier(String name, @Nullable VertexFormat vertexFormat, Consumer<RenderType.CompositeState.CompositeStateBuilder> transformer) {
        this.uniqueName = name;
        this.vertexFormat = vertexFormat;
        this.transform = transformer;
    }

    public RenderType getRenderLayer(RenderType baseLayer) {
        RenderType existing = this.renderLayerCache.get(baseLayer);
        if (existing != null) {
            return existing;
        }
        String newName = ((RenderPhaseAccessor) baseLayer).getName() + "_" + this.uniqueName;
        RenderType newLayer = RenderLayerDuplicator.copy(baseLayer, newName, this.vertexFormat, this.transform);
        this.renderLayerCache.put(baseLayer, newLayer);
        return newLayer;
    }

    /**
     * Big brain move right there
     */
    private static class Helper extends RenderStateShard {
        public static RenderStateShard makeShader(Supplier<ShaderInstance> shader) {
            return new ShaderStateShard(shader);
        }

        public static void applyShader(RenderType.CompositeState.CompositeStateBuilder builder, RenderStateShard shader) {
            builder.setShaderState((ShaderStateShard) shader);
        }

        private Helper(String name, Runnable beginAction, Runnable endAction) {
            super(name, beginAction, endAction);
        }
    }
}
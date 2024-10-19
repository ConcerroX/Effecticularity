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
package org.ladysnake.satin.api.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderStateShard;
import org.apiguardian.api.API;
import org.ladysnake.satin.impl.BlockRenderLayerRegistry;
import org.ladysnake.satin.impl.RenderLayerDuplicator;
import org.ladysnake.satin.mixin.client.render.RenderPhaseAccessor;

import java.util.function.Consumer;

import static org.apiguardian.api.API.Status.EXPERIMENTAL;

@API(status = EXPERIMENTAL, since = "1.4.0")
public final class RenderLayerHelper {

    /**
     * Gets the name of a {@link RenderStateShard}
     *
     * <p>This method can be used with {@link #copy(RenderType, String, Consumer)}
     * to make unique names based on the base layer's own name.
     *
     * @param phase a render phase or layer
     * @return the name that was passed to {@link RenderStateShard#RenderStateShard(String, Runnable, Runnable)}
     */
    @API(status = EXPERIMENTAL, since = "1.5.0")
    public static String getName(RenderStateShard phase) {
        return ((RenderPhaseAccessor) phase).getName();
    }

    /**
     * Copies a {@link RenderType} with all its parameters, but applies the given {@code phaseTransform} to the
     * copied parameters.
     *
     * <p>A typical use of this method is to create a new render layer with a different target (e.g. a target
     * that supports shaders). For example:
     * <pre>{@code RenderLayerHelper.copy(
     *      RenderLayer.getTranslucent(),
     *      "my mod:special_layer",
     *      builder -> builder.target(someShaderEnablingTarget)
     * );}</pre>
     *
     * @param existing       the render layer to duplicate
     * @param newName        the name of the new render layer
     * @param phaseTransform a transformation operation to apply to the new layer's {@linkplain RenderType.CompositeState.CompositeStateBuilder parameter builder}
     * @return a {@link RenderType} with the same base parameters as {@code existing} but modified according to {@code phaseTransform}
     * @throws IllegalArgumentException if {@code existing} is not a {@code MultiPhase} render layer
     */
    @API(status = EXPERIMENTAL, since = "1.4.0")
    public static RenderType copy(RenderType existing, String newName, Consumer<RenderType.CompositeState.CompositeStateBuilder> phaseTransform) {
        return RenderLayerDuplicator.copy(existing, newName, phaseTransform);
    }

    @API(status = EXPERIMENTAL, since = "1.4.0")
    public static RenderType.CompositeState copyPhaseParameters(RenderType existing, Consumer<RenderType.CompositeState.CompositeStateBuilder> phaseTransform) {
        return RenderLayerDuplicator.copyPhaseParameters(existing, phaseTransform);
    }

    /**
     * Register a custom RenderLayer for block rendering.
     *
     * <p><strong>Custom block render layers are usually not supported by alternative renderers.</strong>
     * Calling this method will have no visible effect if one of those is active.
     *
     * @throws IllegalStateException if this method is called after {@link Minecraft#renderBuffers()
     *                               buffer builders} have been initialized.
     * @deprecated this feature is likely to cause incompatibilities. Use at your own discretion.
     */
    @Deprecated
    @API(status = EXPERIMENTAL, since = "1.5.0")
    public static void registerBlockRenderLayer(RenderType layer) {
        BlockRenderLayerRegistry.INSTANCE.registerRenderLayer(layer);
    }
}
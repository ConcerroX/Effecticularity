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

import com.mojang.blaze3d.pipeline.RenderTarget;
import net.minecraft.client.renderer.EffectInstance;
import net.minecraft.client.renderer.texture.AbstractTexture;
import org.ladysnake.satin.api.managed.uniform.SamplerUniformV2;

import java.util.function.IntSupplier;

public final class ManagedSamplerUniformV2 extends ManagedSamplerUniformBase implements SamplerUniformV2 {
    public ManagedSamplerUniformV2(String name) {
        super(name);
    }

    @Override
    public void set(AbstractTexture texture) {
        set(texture::getId);
    }

    @Override
    public void set(RenderTarget textureFbo) {
        set(textureFbo::getColorTextureId);
    }

    @Override
    public void set(int textureName) {
        set(() -> textureName);
    }

    @Override
    protected void set(Object value) {
        this.set((IntSupplier) value);
    }

    @Override
    public void set(IntSupplier value) {
        SamplerAccess[] targets = this.targets;
        if (targets.length > 0 && this.cachedValue != value) {
            for (SamplerAccess target : targets) {
                ((EffectInstance) target).setSampler(this.name, value);
            }
            this.cachedValue = value;
        }
    }
}
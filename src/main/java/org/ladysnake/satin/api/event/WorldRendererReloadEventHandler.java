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
package org.ladysnake.satin.api.event;

import concerrox.minecraft.effecticularity.core.Effecticularity;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;


@Mod.EventBusSubscriber(modid = Effecticularity.ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class WorldRendererReloadEventHandler {

        private static final List<Callback> CALLBACKS = new ArrayList<>();

        public static void register(Callback callback) {
            CALLBACKS.add(callback);
        }

        @SubscribeEvent
        public static void onGuiScreenInit(RenderLevelStageEvent event) {
            for (Callback callback : CALLBACKS) {
                callback.onRendererReload(event.getLevelRenderer());
            }
        }

    @FunctionalInterface
    public interface Callback {
        void onRendererReload(LevelRenderer renderer);
    }

}
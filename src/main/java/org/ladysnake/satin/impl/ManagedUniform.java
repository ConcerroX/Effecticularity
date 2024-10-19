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

import com.mojang.blaze3d.shaders.Uniform;
import net.minecraft.client.renderer.PostPass;
import net.minecraft.client.renderer.ShaderInstance;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.ladysnake.satin.api.managed.uniform.Uniform1f;
import org.ladysnake.satin.api.managed.uniform.Uniform1i;
import org.ladysnake.satin.api.managed.uniform.Uniform2f;
import org.ladysnake.satin.api.managed.uniform.Uniform2i;
import org.ladysnake.satin.api.managed.uniform.Uniform3f;
import org.ladysnake.satin.api.managed.uniform.Uniform3i;
import org.ladysnake.satin.api.managed.uniform.Uniform4f;
import org.ladysnake.satin.api.managed.uniform.Uniform4i;
import org.ladysnake.satin.api.managed.uniform.UniformMat4;

import java.util.ArrayList;
import java.util.List;

public final class ManagedUniform extends ManagedUniformBase implements
        Uniform1i, Uniform2i, Uniform3i, Uniform4i,
        Uniform1f, Uniform2f, Uniform3f, Uniform4f,
        UniformMat4 {

    private static final Uniform[] NO_TARGETS = new Uniform[0];

    private final int count;

    private Uniform[] targets = NO_TARGETS;
    private int i0, i1, i2, i3;
    private float f0, f1, f2, f3;
    private boolean firstUpload = true;

    public ManagedUniform(String name, int count) {
        super(name);
        this.count = count;
    }

    @Override
    public boolean findUniformTargets(List<PostPass> shaders) {
        List<Uniform> list = new ArrayList<>();
        for (PostPass shader : shaders) {
            Uniform uniform = shader.getEffect().getUniform(this.name);

            if (uniform != null) {
                if (uniform.getCount() != this.count) {
                    throw new IllegalStateException("Mismatched number of values, expected " + this.count + " but JSON definition declares " + uniform.getCount());
                }
                list.add(uniform);
            }
        }

        if (list.size() > 0) {
            this.targets = list.toArray(new Uniform[0]);
            this.syncCurrentValues();
            return true;
        } else {
            this.targets = NO_TARGETS;
            return false;
        }
    }

    @Override
    public boolean findUniformTarget(ShaderInstance shader) {
        Uniform uniform = shader.getUniform(this.name);
        if (uniform != null) {
            this.targets = new Uniform[] {uniform};
            this.syncCurrentValues();
            return true;
        } else {
            this.targets = NO_TARGETS;
            return false;
        }
    }

    private void syncCurrentValues() {
        if (!this.firstUpload) {
            for (Uniform target : this.targets) {
                if (target.getIntBuffer() != null) {
                    target.set(i0, i1, i2, i3);
                } else {
                    assert target.getFloatBuffer() != null;
                    target.set(f0, f1, f2, f3);
                }
            }
        }
    }

    @Override
    public void set(int value) {
        Uniform[] targets = this.targets;
        int nbTargets = targets.length;
        if (nbTargets > 0) {
            if (firstUpload || i0 != value) {
                for (Uniform target : targets) {
                    target.set(value);
                }
                i0 = value;
                firstUpload = false;
            }
        }
    }

    @Override
    public void set(int value0, int value1) {
        Uniform[] targets = this.targets;
        int nbTargets = targets.length;
        if (nbTargets > 0) {
            if (firstUpload || i0 != value0 || i1 != value1) {
                for (Uniform target : targets) {
                    target.set(value0, value1);
                }
                i0 = value0;
                i1 = value1;
                firstUpload = false;
            }
        }
    }

    @Override
    public void set(int value0, int value1, int value2) {
        Uniform[] targets = this.targets;
        int nbTargets = targets.length;
        if (nbTargets > 0) {
            if (firstUpload || i0 != value0 || i1 != value1 || i2 != value2) {
                for (Uniform target : targets) {
                    target.set(value0, value1, value2);
                }
                i0 = value0;
                i1 = value1;
                i2 = value2;
                firstUpload = false;
            }
        }
    }

    @Override
    public void set(int value0, int value1, int value2, int value3) {
        Uniform[] targets = this.targets;
        int nbTargets = targets.length;
        if (nbTargets > 0) {
            if (firstUpload || i0 != value0 || i1 != value1 || i2 != value2 || i3 != value3) {
                for (Uniform target : targets) {
                    target.set(value0, value1, value2, value3);
                }
                i0 = value0;
                i1 = value1;
                i2 = value2;
                i3 = value3;
                firstUpload = false;
            }
        }
    }

    @Override
    public void set(float value) {
        Uniform[] targets = this.targets;
        int nbTargets = targets.length;
        if (nbTargets > 0) {
            if (firstUpload || f0 != value) {
                for (Uniform target : targets) {
                    target.set(value);
                }
                f0 = value;
                firstUpload = false;
            }
        }
    }

    @Override
    public void set(float value0, float value1) {
        Uniform[] targets = this.targets;
        int nbTargets = targets.length;
        if (nbTargets > 0) {
            if (firstUpload || f0 != value0 || f1 != value1) {
                for (Uniform target : targets) {
                    target.set(value0, value1);
                }
                f0 = value0;
                f1 = value1;
                firstUpload = false;
            }
        }
    }

    @Override
    public void set(Vector2f value) {
        set(value.x(), value.y());
    }

    @Override
    public void set(float value0, float value1, float value2) {
        Uniform[] targets = this.targets;
        int nbTargets = targets.length;
        if (nbTargets > 0) {
            if (firstUpload || f0 != value0 || f1 != value1 || f2 != value2) {
                for (Uniform target : targets) {
                    target.set(value0, value1, value2);
                }
                f0 = value0;
                f1 = value1;
                f2 = value2;
                firstUpload = false;
            }
        }
    }

    @Override
    public void set(Vector3f value) {
        set(value.x(), value.y(), value.z());
    }

    @Override
    public void set(float value0, float value1, float value2, float value3) {
        Uniform[] targets = this.targets;
        int nbTargets = targets.length;
        if (nbTargets > 0) {
            if (firstUpload || f0 != value0 || f1 != value1 || f2 != value2 || f3 != value3) {
                for (Uniform target : targets) {
                    target.set(value0, value1, value2, value3);
                }
                f0 = value0;
                f1 = value1;
                f2 = value2;
                f3 = value3;
                firstUpload = false;
            }
        }
    }

    @Override
    public void set(Vector4f value) {
        set(value.x(), value.y(), value.z(), value.w());
    }

    @Override
    public void set(Matrix4f value) {
        Uniform[] targets = this.targets;
        int nbTargets = targets.length;
        if (nbTargets > 0) {
            for (Uniform target : targets) {
                target.set(value);
            }
        }
    }

    @Override
    public void setFromArray(float[] values) {
        if (this.count != values.length) {
            throw new IllegalArgumentException("Mismatched values size, expected " + count + " but got " + values.length);
        }

        Uniform[] targets = this.targets;
        int nbTargets = targets.length;
        if (nbTargets > 0) {
            for (Uniform target : targets) {
                target.set(values);
            }
        }
    }
}
package concerrox.minecraft.effecticularity.core.mixin;

//import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
//import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import concerrox.minecraft.effecticularity.core.particle.EffecticularityParticleEngine;
import concerrox.minecraft.effecticularity.core.registry.ModParticles;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.RegistryObject;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Map;

@Mixin(ParticleEngine.class)
public class ParticleEngineMixin implements EffecticularityParticleEngine {

    @Shadow
    protected ClientLevel level;

    @Shadow
    @Final
    private Map<ResourceLocation, ParticleProvider<?>> providers;

    @Shadow
    @Final
    private Map<ResourceLocation, ParticleEngine.MutableSpriteSet> spriteSets;

    @Override
    public <O extends ParticleOptions, T extends ParticleType<O>> void effecticularity_1_20_1$registerSpriteSet(RegistryObject<T> type, ParticleProvider<O> provider) {
        assert type != null;
        providers.put(type.getId(), provider);
    }

    @Override
    public <O extends ParticleOptions, T extends ParticleType<O>> void effecticularity_1_20_1$registerSpriteSet(RegistryObject<T> type, ParticleEngine.SpriteParticleRegistration<O> registration) {
        ParticleEngine.MutableSpriteSet spriteSet = new ParticleEngine.MutableSpriteSet();
        assert type != null;
        spriteSets.put(type.getId(), spriteSet);
        assert registration != null;
        providers.put(type.getId(), registration.create(spriteSet));
    }

    @ModifyExpressionValue(method = "makeParticle", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/Registry;getKey(Ljava/lang/Object;)Lnet/minecraft/resources/ResourceLocation;"))
    private <T extends ParticleOptions> ResourceLocation remapParticles(ResourceLocation original, @Local(ordinal = 0, argsOnly = true) T options) {
        var registry = ModParticles.INSTANCE.getREGISTRY_CLIENT().get();
        if (registry != null && original == null) {
            return registry.getKey(options.getType());
        } else {
            return original;
        }
    }

//    @Inject(method = "makeParticle", at = @At("RETURN"))
//    @SuppressWarnings("deprecation")
//    private <T extends ParticleOptions> void makeParticle(T p_107396_, double p_107397_, double p_107398_, double p_107399_, double p_107400_, double p_107401_, double p_107402_, CallbackInfoReturnable<Particle> cb) {
//        ParticleProvider<T> provider = providers.get(BuiltInRegistries.PARTICLE_TYPE.getKey(p_107396_.getType()));
//        return provider == null ? null : provider.createParticle(p_107396_, level, p_107397_, p_107398_, p_107399_, p_107400_, p_107401_, p_107402_);
//    }

}
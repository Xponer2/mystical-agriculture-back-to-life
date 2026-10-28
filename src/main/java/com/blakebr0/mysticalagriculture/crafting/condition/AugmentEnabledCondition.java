package com.blakebr0.mysticalagriculture.crafting.condition;

import com.blakebr0.mysticalagriculture.registry.AugmentRegistry;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.xponer2.translator.recipe.ICondition;
import io.github.xponer2.translator.util.Codecs;

import net.minecraft.util.ResourceLocation;

import io.github.xponer2.translator.util.Codecs;

public class AugmentEnabledCondition implements ICondition {
    public static final MapCodec<AugmentEnabledCondition> CODEC = RecordCodecBuilder.mapCodec(builder ->
            builder.group(
                    Codecs.RESOURCE_LOCATION.fieldOf("augment").forGetter(condition -> condition.augment)
            ).apply(builder, AugmentEnabledCondition::new)
    );

    private final ResourceLocation augment;

    public AugmentEnabledCondition(ResourceLocation augment) {
        this.augment = augment;
    }

    @Override
    public boolean test(IContext context) {
        var augment = AugmentRegistry.getInstance().getAugmentById(this.augment);
        return augment != null && augment.isEnabled();
    }

    @Override
    public MapCodec<? extends ICondition> codec() {
        return CODEC;
    }
}

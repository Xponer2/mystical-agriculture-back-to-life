package com.blakebr0.mysticalagriculture.crafting.condition;

import com.blakebr0.mysticalagriculture.registry.CropRegistry;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.xponer2.translator.recipe.ICondition;
import io.github.xponer2.translator.util.Codecs;

import net.minecraft.util.ResourceLocation;

import io.github.xponer2.translator.util.Codecs;

public class CropEnabledCondition implements ICondition {
    public static final MapCodec<CropEnabledCondition> CODEC = RecordCodecBuilder.mapCodec(builder ->
            builder.group(
                    Codecs.RESOURCE_LOCATION.fieldOf("crop").forGetter(condition -> condition.crop)
            ).apply(builder, CropEnabledCondition::new)
    );

    private final ResourceLocation crop;

    public CropEnabledCondition(ResourceLocation crop) {
        this.crop = crop;
    }

    @Override
    public boolean test(IContext context) {
        var crop = CropRegistry.getInstance().getCropById(this.crop);
        return crop != null && crop.isEnabled();
    }

    @Override
    public MapCodec<? extends ICondition> codec() {
        return CODEC;
    }
}

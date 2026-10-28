package com.blakebr0.mysticalagriculture.world.modifiers;

import com.blakebr0.mysticalagriculture.config.ModConfigs;
import com.blakebr0.mysticalagriculture.world.PlacedOre;

public final class InferiumOreModifier extends OreModifier {
    public InferiumOreModifier(String biomes, PlacedOre feature) {
        super(biomes, feature);
    }

    @Override
    protected boolean isEnabled() {
        return ModConfigs.GENERATE_INFERIUM.get();
    }
}

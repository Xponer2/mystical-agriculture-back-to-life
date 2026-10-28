package com.blakebr0.mysticalagriculture.world.modifiers;

import com.blakebr0.mysticalagriculture.config.ModConfigs;
import com.blakebr0.mysticalagriculture.world.PlacedOre;

public final class SoulstoneModifier extends OreModifier {
    public SoulstoneModifier(String biomes, PlacedOre feature) {
        super(biomes, feature);
    }

    @Override
    protected boolean isEnabled() {
        return ModConfigs.GENERATE_SOULSTONE.get();
    }
}

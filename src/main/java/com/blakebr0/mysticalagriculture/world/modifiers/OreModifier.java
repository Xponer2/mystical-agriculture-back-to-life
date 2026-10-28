package com.blakebr0.mysticalagriculture.world.modifiers;

import com.blakebr0.mysticalagriculture.world.PlacedOre;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;

import java.util.Random;

/**
 * 1.12.2 replacement of a NeoForge biome modifier (data/mysticalagriculture/neoforge/biome_modifier) that adds a
 * placed feature. The 1.21 biome tags #is_overworld / #is_nether / #is_end are checked by the dimension type, like
 * the other 1.12.2 ore generators do.
 */
public abstract class OreModifier {
    private final DimensionType dimension;
    private final PlacedOre feature;

    protected OreModifier(String biomes, PlacedOre feature) {
        this.dimension = dimension(biomes);
        this.feature = feature;
    }

    private static DimensionType dimension(String biomes) {
        switch (biomes) {
            case "#is_overworld":
            case "#minecraft:is_overworld":
                return DimensionType.OVERWORLD;
            case "#is_nether":
            case "#minecraft:is_nether":
                return DimensionType.NETHER;
            case "#is_end":
            case "#minecraft:is_end":
                return DimensionType.THE_END;
            default:
                throw new IllegalStateException("unsupported biomes " + biomes);
        }
    }

    protected abstract boolean isEnabled();

    public void generate(World world, Random random, int chunkX, int chunkZ) {
        if (world.provider.getDimensionType() == this.dimension && this.isEnabled()) {
            this.feature.place(world, random, chunkX, chunkZ);
        }
    }

    @Override
    public String toString() {
        return this.feature + " in " + this.dimension.getName();
    }
}

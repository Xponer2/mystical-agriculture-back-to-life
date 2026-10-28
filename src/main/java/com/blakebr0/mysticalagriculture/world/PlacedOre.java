package com.blakebr0.mysticalagriculture.world;

import com.blakebr0.mysticalagriculture.MysticalAgriculture;
import com.blakebr0.mysticalagriculture.world.feature.OreConfig;
import com.blakebr0.mysticalagriculture.world.feature.SoulstoneFeature;
import com.google.gson.JsonObject;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.Random;

/**
 * A 1.21 placed feature (data/mysticalagriculture/worldgen/placed_feature) with its configured feature: veins per
 * chunk, at a random position in the chunk (in_square), in a uniform height range.
 */
public final class PlacedOre {
    private final String name;
    private final WorldGenerator feature;
    private final int count;
    private final int minY;
    private final int maxY;

    private PlacedOre(String name, WorldGenerator feature, int count, int minY, int maxY) {
        this.name = name;
        this.feature = feature;
        this.count = count;
        this.minY = minY;
        this.maxY = maxY;
    }

    public static PlacedOre load(String name) {
        var placed = WorldGenData.read("worldgen/placed_feature/" + name);
        var featureId = placed.get("feature").getAsString();
        var configured = WorldGenData.read("worldgen/configured_feature/" + WorldGenData.path(featureId));
        var config = OreConfig.fromJson(name, configured);
        if (config.targets.isEmpty()) {
            throw new IllegalStateException("no block to place in 1.12.2");
        }

        var type = configured.get("type").getAsString();
        WorldGenerator feature;
        switch (type) {
            case "minecraft:ore": {
                if (config.targets.size() > 1) {
                    MysticalAgriculture.LOGGER.warn("World generation {}: only the first of {} targets is placed", name, config.targets.size());
                }
                var target = config.targets.get(0);
                feature = new WorldGenMinable(target.state, config.size, target.target);
                break;
            }
            case "mysticalagriculture:soulstone":
                feature = new SoulstoneFeature(config);
                break;
            default:
                throw new IllegalStateException("unsupported feature type " + type);
        }

        int count = 1;
        int minY = 0;
        int maxY = 255;
        for (var element : placed.getAsJsonArray("placement")) {
            var modifier = element.getAsJsonObject();
            var modifierType = modifier.get("type").getAsString();
            switch (modifierType) {
                case "minecraft:count":
                    count = modifier.get("count").getAsInt();
                    break;
                case "minecraft:height_range": {
                    var height = modifier.getAsJsonObject("height");
                    if (!height.get("type").getAsString().equals("minecraft:uniform")) {
                        throw new IllegalStateException("unsupported height provider " + height.get("type"));
                    }
                    minY = absolute(height.getAsJsonObject("min_inclusive"));
                    maxY = absolute(height.getAsJsonObject("max_inclusive"));
                    break;
                }
                case "minecraft:in_square":
                case "minecraft:biome":
                    // the position in the chunk is always random, the biomes are checked by the modifier
                    break;
                default:
                    MysticalAgriculture.LOGGER.warn("World generation {}: placement {} ignored", name, modifierType);
            }
        }

        return new PlacedOre(name, feature, count, minY, maxY);
    }

    private static int absolute(JsonObject anchor) {
        if (!anchor.has("absolute")) {
            throw new IllegalStateException("unsupported vertical anchor " + anchor);
        }
        return anchor.get("absolute").getAsInt();
    }

    /**
     * Places the veins of one chunk. The 1.21 height range is cut to the 1.12.2 world (no blocks below y 0): the veins
     * per chunk stay the same, they are only placed in the part of the range that exists.
     */
    public void place(World world, Random random, int chunkX, int chunkZ) {
        int minY = Math.max(this.minY, 0);
        int maxY = Math.min(this.maxY, world.getHeight() - 1);
        if (minY > maxY) {
            return;
        }

        for (int i = 0; i < this.count; i++) {
            int x = chunkX * 16 + random.nextInt(16);
            int y = minY + random.nextInt(maxY - minY + 1);
            int z = chunkZ * 16 + random.nextInt(16);
            this.feature.generate(world, random, new BlockPos(x, y, z));
        }
    }

    @Override
    public String toString() {
        return this.name + " (" + this.count + " per chunk, y " + this.minY + ".." + this.maxY + ")";
    }
}

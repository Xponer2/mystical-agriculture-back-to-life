package com.blakebr0.mysticalagriculture.world.feature;

import com.blakebr0.mysticalagriculture.MysticalAgriculture;
import com.google.common.base.Predicate;
import com.google.gson.JsonObject;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStone;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockMatcher;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The ore configuration of a 1.21 configured feature (data/mysticalagriculture/worldgen/configured_feature): vein size
 * and the block placed for each replaceable block.
 */
public final class OreConfig {
    public final int size;
    public final List<Target> targets;

    private OreConfig(int size, List<Target> targets) {
        this.size = size;
        this.targets = Collections.unmodifiableList(targets);
    }

    public static OreConfig fromJson(String name, JsonObject json) {
        var config = json.getAsJsonObject("config");
        var targets = new ArrayList<Target>();
        for (var element : config.getAsJsonArray("targets")) {
            var target = element.getAsJsonObject();
            var stateName = target.getAsJsonObject("state").get("Name").getAsString();
            var block = ForgeRegistries.BLOCKS.getValue(io.github.xponer2.translator.util.ResourceLocations.parse(stateName));
            if (block == null || block == Blocks.AIR) {
                MysticalAgriculture.LOGGER.warn("World generation {}: unknown block {}", name, stateName);
                continue;
            }
            var predicate = predicate(target.getAsJsonObject("target"));
            if (predicate == null) {
                // the target exists only in 1.21 (deepslate)
                continue;
            }
            targets.add(new Target(predicate, block.getDefaultState()));
        }
        return new OreConfig(config.get("size").getAsInt(), targets);
    }

    /**
     * The 1.12.2 blocks of a 1.21 rule test, null if 1.12.2 has none of them.
     */
    private static Predicate<IBlockState> predicate(JsonObject json) {
        var type = json.get("predicate_type").getAsString();
        switch (type) {
            case "minecraft:tag_match": {
                var tag = json.get("tag").getAsString();
                if (tag.equals("minecraft:stone_ore_replaceables")) {
                    // stone, granite, diorite, andesite: the stone variants vanilla 1.12.2 ores replace
                    return state -> state != null && state.getBlock() == Blocks.STONE
                            && state.getValue(BlockStone.VARIANT).isNatural();
                }
                if (!tag.equals("minecraft:deepslate_ore_replaceables")) {
                    MysticalAgriculture.LOGGER.warn("World generation: unsupported block tag {}", tag);
                }
                return null;
            }
            case "minecraft:block_match": {
                Block block = ForgeRegistries.BLOCKS.getValue(io.github.xponer2.translator.util.ResourceLocations.parse(json.get("block").getAsString()));
                return block == null || block == Blocks.AIR ? null : BlockMatcher.forBlock(block);
            }
            default:
                MysticalAgriculture.LOGGER.warn("World generation: unsupported rule test {}", type);
                return null;
        }
    }

    public static final class Target {
        public final Predicate<IBlockState> target;
        public final IBlockState state;

        public Target(Predicate<IBlockState> target, IBlockState state) {
            this.target = target;
            this.state = state;
        }
    }
}

package com.blakebr0.mysticalagriculture.world;

import com.blakebr0.mysticalagriculture.MysticalAgriculture;
import com.blakebr0.mysticalagriculture.world.modifiers.InferiumOreModifier;
import com.blakebr0.mysticalagriculture.world.modifiers.OreModifier;
import com.blakebr0.mysticalagriculture.world.modifiers.ProsperityOreModifier;
import com.blakebr0.mysticalagriculture.world.modifiers.SoulstoneModifier;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.BiFunction;

/**
 * Generates what the 1.21 biome modifiers add (data/mysticalagriculture/neoforge/biome_modifier): inferium and
 * prosperity ore in the overworld, soulstone with soulium ore in the nether, with the 1.21 placement data.
 * Register it with {@link #register()} after the blocks are registered (init).
 */
public final class ModWorldGenerator implements IWorldGenerator {
    private final List<OreModifier> modifiers = new ArrayList<>();

    private ModWorldGenerator() {
        this.add("inferium_ore", InferiumOreModifier::new);
        this.add("prosperity_ore", ProsperityOreModifier::new);
        this.add("soulstone", SoulstoneModifier::new);
    }

    public static void register() {
        GameRegistry.registerWorldGenerator(new ModWorldGenerator(), 0);
    }

    private void add(String name, BiFunction<String, PlacedOre, OreModifier> factory) {
        try {
            var json = WorldGenData.read("neoforge/biome_modifier/" + name);
            var feature = PlacedOre.load(WorldGenData.path(json.get("feature").getAsString()));
            var modifier = factory.apply(json.get("biomes").getAsString(), feature);
            this.modifiers.add(modifier);
            MysticalAgriculture.LOGGER.debug("World generation: {}", modifier);
        } catch (RuntimeException e) {
            MysticalAgriculture.LOGGER.error("World generation {} is disabled", name, e);
        }
    }

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        for (var modifier : this.modifiers) {
            modifier.generate(world, random, chunkX, chunkZ);
        }
    }
}

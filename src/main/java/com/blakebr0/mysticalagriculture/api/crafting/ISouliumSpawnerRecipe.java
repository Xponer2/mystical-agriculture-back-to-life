package com.blakebr0.mysticalagriculture.api.crafting;

import java.util.Optional;
import java.util.Random;

import io.github.xponer2.translator.recipe.CraftingInput;
import io.github.xponer2.translator.recipe.Recipe;
import io.github.xponer2.translator.util.random.WeightedEntry;
import io.github.xponer2.translator.util.random.WeightedRandomList;

import io.github.xponer2.translator.registry.EntityType;

public interface ISouliumSpawnerRecipe extends Recipe<CraftingInput> {
    WeightedRandomList<WeightedEntry.Wrapper<EntityType<?>>> getEntityTypes();
    EntityType<?> getFirstEntityType();
    Optional<WeightedEntry.Wrapper<EntityType<?>>> getRandomEntityType(Random random);

    /**
     * Get the count for the ingredient at the requested index
     *
     * @param index the ingredient index
     * @return either the count or -1 if invalid
     */
    int getCount(int index);
}

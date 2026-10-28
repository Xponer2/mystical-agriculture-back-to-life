package com.blakebr0.mysticalagriculture.api.crafting;

import com.blakebr0.mysticalagriculture.api.soul.MobSoulType;
import io.github.xponer2.translator.recipe.CraftingInput;
import io.github.xponer2.translator.recipe.Recipe;

/**
 * Used to represent a Reprocessor recipe for the recipe type
 */
public interface ISoulExtractionRecipe extends Recipe<CraftingInput> {
    MobSoulType getMobSoulType();
    double getSouls();
}

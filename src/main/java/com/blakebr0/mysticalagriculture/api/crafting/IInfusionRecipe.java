package com.blakebr0.mysticalagriculture.api.crafting;

import io.github.xponer2.translator.recipe.CraftingInput;
import io.github.xponer2.translator.recipe.Recipe;

import net.minecraft.item.crafting.Ingredient;

/**
 * Used to represent an Infusion recipe for the recipe type
 */
public interface IInfusionRecipe extends Recipe<CraftingInput> {
    /**
     * The item that is placed on the Altar
     *
     * @return the altar ingredient
     */
    Ingredient getAltarIngredient();
}

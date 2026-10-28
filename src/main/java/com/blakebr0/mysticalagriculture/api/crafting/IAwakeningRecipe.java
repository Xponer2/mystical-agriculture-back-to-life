package com.blakebr0.mysticalagriculture.api.crafting;

import java.util.List;
import java.util.Map;

import io.github.xponer2.translator.recipe.CraftingInput;
import io.github.xponer2.translator.recipe.Recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;

/**
 * Used to represent an Awakening recipe for the recipe type
 */
public interface IAwakeningRecipe extends Recipe<CraftingInput> {
    /**
     * The item that is placed on the Altar
     *
     * @return the altar ingredient
     */
    Ingredient getAltarIngredient();

    /**
     * The list of essence items required for this recipe
     *
     * @return the list of essences
     */
    NonNullList<ItemStack> getEssences();

    /**
     * Gets a map of missing essences and the amount of each that is missing
     *
     * @param items the list essence vessel items
     * @return a map of missing essences -> amount missing
     */
    Map<ItemStack, Integer> getMissingEssences(List<ItemStack> items);

    /**
     * Checks if the given list of items has the required essences and stack sizes for this recipe
     * @param items the list essence vessel items
     * @return all required essences are provided
     */
    default boolean hasRequiredEssences(List<ItemStack> items) {
        return this.getMissingEssences(items).isEmpty();
    }
}

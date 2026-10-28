package com.blakebr0.mysticalagriculture.compat.jei;

import java.util.List;
import java.util.stream.Collectors;

import io.github.xponer2.translator.recipe.ingredient.Ingredients;
import com.blakebr0.mysticalagriculture.registry.CropRegistry;
import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

public class CruxRecipe {
    private static List<Block> farmlands;
    public final ItemStack seed;
    public final ItemStack crux;
    public final ItemStack essence;

    public CruxRecipe(ItemStack seed, ItemStack crux, ItemStack essence) {
        this.seed = seed;
        this.crux = crux;
        this.essence = essence;
    }

    public List<Ingredient> getIngredients() {
        return Lists.newArrayList(
                Ingredients.of(this.seed),
                Ingredients.of(farmlands.toArray(new Block[0])),
                Ingredients.of(this.crux)
        );
    }

    public static List<CruxRecipe> getGeneratedRecipes() {
        farmlands = CropRegistry.getInstance().getCrops()
                .stream()
                .map(c -> c.getTier().getFarmland())
                .distinct()
                .collect(Collectors.toList());

        return CropRegistry.getInstance().getCrops()
                .stream()
                .filter(c -> c.getCruxBlock() != null)
                .map(c -> new CruxRecipe(
                        new ItemStack(c.getSeedsItem()),
                        new ItemStack(c.getCruxBlock()),
                        new ItemStack(c.getEssenceItem())
                ))
                .collect(Collectors.toList());
    }
}

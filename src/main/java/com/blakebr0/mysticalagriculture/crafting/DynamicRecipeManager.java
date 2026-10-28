package com.blakebr0.mysticalagriculture.crafting;

import java.util.Optional;

import com.blakebr0.cucumber.event.RecipeManagerLoadingEvent;
import com.blakebr0.mysticalagriculture.MysticalAgriculture;
import com.blakebr0.mysticalagriculture.api.crop.Crop;
import com.blakebr0.mysticalagriculture.config.ModConfigs;
import com.blakebr0.mysticalagriculture.crafting.recipe.InfusionRecipe;
import com.blakebr0.mysticalagriculture.crafting.recipe.ReprocessorRecipe;
import com.blakebr0.mysticalagriculture.registry.CropRegistry;
import io.github.xponer2.translator.recipe.CraftingBookCategory;
import io.github.xponer2.translator.recipe.Recipe;
import io.github.xponer2.translator.recipe.RecipeHolder;
import io.github.xponer2.translator.recipe.ShapedRecipe;
import io.github.xponer2.translator.recipe.ShapedRecipePattern;
import io.github.xponer2.translator.recipe.ingredient.Ingredients;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class DynamicRecipeManager {
    public static final DynamicRecipeManager INSTANCE = new DynamicRecipeManager();

    @SubscribeEvent
    public void onRecipeManagerLoading(RecipeManagerLoadingEvent event) {
        for (var crop : CropRegistry.getInstance().getCrops()) {
            var seed = makeSeedRecipe(crop);
            var seedRegular = makeRegularSeedRecipe(crop);
            var reprocessor = makeReprocessorRecipe(crop);

            if (seed != null)
                event.addRecipe(seed);

            if (seedRegular != null)
                event.addRecipe(seedRegular);

            if (reprocessor != null)
                event.addRecipe(reprocessor);
        }
    }

    private static RecipeHolder<Recipe<?>> makeSeedRecipe(Crop crop) {
        if (!crop.isEnabled() || !crop.getRecipeConfig().isSeedInfusionRecipeEnabled())
            return null;

        var essenceItem = crop.getTier().getEssence();
        if (essenceItem == null)
            return null;

        var craftingSeedItem = crop.getType().getCraftingSeed();
        if (craftingSeedItem == null)
            return null;

        var material = crop.getCraftingMaterial();
        if (material == Ingredient.EMPTY)
            return null;

        var essence = Ingredients.of(essenceItem);
        var craftingSeed = Ingredients.of(craftingSeedItem);
        var inputs = NonNullList.from(Ingredient.EMPTY,
                material, essence, material, essence, material, essence, material, essence
        );

        var id = MysticalAgriculture.resource(crop.getNameWithSuffix("seeds_infusion"));
        var result = new ItemStack(crop.getSeedsItem());

        return new RecipeHolder<>(id, new InfusionRecipe(craftingSeed, inputs, result, false));
    }

    private static RecipeHolder<Recipe<?>> makeRegularSeedRecipe(Crop crop) {
        if (!crop.isEnabled() || !crop.getRecipeConfig().isSeedCraftingRecipeEnabled())
            return null;

        if (!ModConfigs.SEED_CRAFTING_RECIPES.get())
            return null;

        var essenceItem = crop.getTier().getEssence();
        if (essenceItem == null)
            return null;

        var craftingSeedItem = crop.getType().getCraftingSeed();
        if (craftingSeedItem == null)
            return null;

        var material = crop.getCraftingMaterial();
        if (material == Ingredient.EMPTY)
            return null;

        var essence = Ingredients.of(essenceItem);
        var craftingSeed = Ingredients.of(craftingSeedItem);
        var inputs = NonNullList.from(Ingredient.EMPTY,
                material, essence, material,
                essence, craftingSeed, essence,
                material, essence, material
        );

        var id = MysticalAgriculture.resource(crop.getNameWithSuffix("seeds_vanilla"));
        var pattern = new ShapedRecipePattern(3, 3, inputs, Optional.empty());
        var result = new ItemStack(crop.getSeedsItem());

        return new RecipeHolder<>(id, new ShapedRecipe("", CraftingBookCategory.MISC, pattern, result));
    }

    private static RecipeHolder<Recipe<?>> makeReprocessorRecipe(Crop crop) {
        if (!crop.isEnabled() || !crop.getRecipeConfig().isSeedReprocessorRecipeEnabled())
            return null;

        var input = Ingredients.of(crop.getSeedsItem());
        var id = MysticalAgriculture.resource(crop.getNameWithSuffix("seeds_reprocessor"));
        var result = new ItemStack(crop.getEssenceItem(), 2);

        return new RecipeHolder<>(id, new ReprocessorRecipe(input, result));
    }
}

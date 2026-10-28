package com.blakebr0.mysticalagriculture.init;

import com.blakebr0.mysticalagriculture.MysticalAgriculture;
import com.blakebr0.mysticalagriculture.api.crafting.IAwakeningRecipe;
import com.blakebr0.mysticalagriculture.api.crafting.IEnchanterRecipe;
import com.blakebr0.mysticalagriculture.api.crafting.IInfusionRecipe;
import com.blakebr0.mysticalagriculture.api.crafting.IReprocessorRecipe;
import com.blakebr0.mysticalagriculture.api.crafting.ISoulExtractionRecipe;
import com.blakebr0.mysticalagriculture.api.crafting.ISouliumSpawnerRecipe;
import io.github.xponer2.translator.recipe.RecipeType;
import io.github.xponer2.translator.registry.DeferredHolder;
import io.github.xponer2.translator.registry.DeferredRegister;

import io.github.xponer2.translator.util.Registries;

public final class ModRecipeTypes {
    public static final DeferredRegister<RecipeType<?>> REGISTRY = DeferredRegister.create(Registries.RECIPE_TYPE, MysticalAgriculture.MOD_ID);

    public static final DeferredHolder<RecipeType<?>, RecipeType<IInfusionRecipe>> INFUSION = REGISTRY.register("infusion", () -> RecipeType.simple(MysticalAgriculture.resource("infusion")));
    public static final DeferredHolder<RecipeType<?>, RecipeType<IAwakeningRecipe>> AWAKENING = REGISTRY.register("awakening", () -> RecipeType.simple(MysticalAgriculture.resource("awakening")));
    public static final DeferredHolder<RecipeType<?>, RecipeType<IEnchanterRecipe>> ENCHANTER = REGISTRY.register("enchanter", () -> RecipeType.simple(MysticalAgriculture.resource("enchanter")));
    public static final DeferredHolder<RecipeType<?>, RecipeType<IReprocessorRecipe>> REPROCESSOR = REGISTRY.register("reprocessor", () -> RecipeType.simple(MysticalAgriculture.resource("reprocessor")));
    public static final DeferredHolder<RecipeType<?>, RecipeType<ISoulExtractionRecipe>> SOUL_EXTRACTION = REGISTRY.register("soul_extraction", () -> RecipeType.simple(MysticalAgriculture.resource("soul_extraction")));
    public static final DeferredHolder<RecipeType<?>, RecipeType<ISouliumSpawnerRecipe>> SOULIUM_SPAWNER = REGISTRY.register("soulium_spawner", () -> RecipeType.simple(MysticalAgriculture.resource("soulium_spawner")));
}

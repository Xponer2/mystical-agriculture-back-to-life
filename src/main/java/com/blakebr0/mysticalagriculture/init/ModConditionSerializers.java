package com.blakebr0.mysticalagriculture.init;

import com.blakebr0.mysticalagriculture.MysticalAgriculture;
import com.blakebr0.mysticalagriculture.crafting.condition.AugmentEnabledCondition;
import com.blakebr0.mysticalagriculture.crafting.condition.CropEnabledCondition;
import com.blakebr0.mysticalagriculture.crafting.condition.CropHasMaterialCondition;
import com.blakebr0.mysticalagriculture.crafting.condition.SeedCraftingRecipesEnabledCondition;
import com.mojang.serialization.MapCodec;
import io.github.xponer2.translator.recipe.ICondition;
import io.github.xponer2.translator.registry.DeferredHolder;
import io.github.xponer2.translator.registry.DeferredRegister;
import io.github.xponer2.translator.registry.NeoForgeRegistries;

public final class ModConditionSerializers {
    public static final DeferredRegister<MapCodec<? extends ICondition>> REGISTRY = DeferredRegister.create(NeoForgeRegistries.CONDITION_SERIALIZERS, MysticalAgriculture.MOD_ID);

    public static final DeferredHolder<MapCodec<? extends ICondition>, MapCodec<? extends ICondition>> CROP_ENABLED = REGISTRY.register("crop_enabled", () -> CropEnabledCondition.CODEC);
    public static final DeferredHolder<MapCodec<? extends ICondition>, MapCodec<? extends ICondition>> AUGMENT_ENABLED = REGISTRY.register("augment_enabled", () -> AugmentEnabledCondition.CODEC);
    public static final DeferredHolder<MapCodec<? extends ICondition>, MapCodec<? extends ICondition>> CROP_HAS_MATERIAL = REGISTRY.register("crop_has_material", () -> CropHasMaterialCondition.CODEC);
    public static final DeferredHolder<MapCodec<? extends ICondition>, MapCodec<? extends ICondition>> SEED_CRAFTING_RECIPES_ENABLED = REGISTRY.register("seed_crafting_recipes_enabled", () -> SeedCraftingRecipesEnabledCondition.CODEC);
}

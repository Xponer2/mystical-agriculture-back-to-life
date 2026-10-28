package com.blakebr0.mysticalagriculture.crafting.condition;

import com.blakebr0.mysticalagriculture.config.ModConfigs;
import com.mojang.serialization.MapCodec;
import io.github.xponer2.translator.recipe.ICondition;

public class SeedCraftingRecipesEnabledCondition implements ICondition {
    public static final MapCodec<SeedCraftingRecipesEnabledCondition> CODEC = MapCodec.unit(SeedCraftingRecipesEnabledCondition::new);

    @Override
    public boolean test(IContext context) {
        return ModConfigs.SEED_CRAFTING_RECIPES.get();
    }

    @Override
    public MapCodec<? extends ICondition> codec() {
        return CODEC;
    }
}

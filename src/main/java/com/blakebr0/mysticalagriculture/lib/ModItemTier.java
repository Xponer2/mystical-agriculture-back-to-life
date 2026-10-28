package com.blakebr0.mysticalagriculture.lib;

import java.util.function.Supplier;

import io.github.xponer2.translator.item.Tier;
import io.github.xponer2.translator.recipe.ingredient.Ingredients;
import com.blakebr0.mysticalagriculture.api.MysticalAgricultureTags;
import com.blakebr0.mysticalagriculture.init.ModItems;
import com.google.common.base.Suppliers;

import net.minecraft.block.Block;
import net.minecraft.item.crafting.Ingredient;

import io.github.xponer2.translator.util.TagKey;

public enum ModItemTier implements Tier {
    INFERIUM(MysticalAgricultureTags.Blocks.INCORRECT_FOR_INFERIUM_TOOL, 2000, 9.0F, 4.0F, 12, () -> Ingredients.of(ModItems.INFERIUM_INGOT.get())),
    PRUDENTIUM(MysticalAgricultureTags.Blocks.INCORRECT_FOR_PRUDENTIUM_TOOL, 2800, 11.0F, 6.0F, 14, () -> Ingredients.of(ModItems.PRUDENTIUM_INGOT.get())),
    TERTIUM(MysticalAgricultureTags.Blocks.INCORRECT_FOR_TERTIUM_TOOL, 4000, 14.0F, 9.0F, 16, () -> Ingredients.of(ModItems.TERTIUM_INGOT.get())),
    IMPERIUM(MysticalAgricultureTags.Blocks.INCORRECT_FOR_IMPERIUM_TOOL, 6000, 19.0F, 13.0F, 18, () -> Ingredients.of(ModItems.IMPERIUM_INGOT.get())),
    SUPREMIUM(MysticalAgricultureTags.Blocks.INCORRECT_FOR_SUPREMIUM_TOOL, 0, 25.0F, 20.0F, 20, () -> Ingredients.of(ModItems.SUPREMIUM_INGOT.get())),
    AWAKENED_SUPREMIUM(MysticalAgricultureTags.Blocks.INCORRECT_FOR_AWAKENED_SUPREMIUM_TOOL, 0, 30.0F, 25.0F, 22, () -> Ingredients.of(ModItems.AWAKENED_SUPREMIUM_INGOT.get())),
    SOULIUM(MysticalAgricultureTags.Blocks.INCORRECT_FOR_SOULIUM_TOOL, 400, 5.0F, 3.0F, 15, () -> Ingredients.of(ModItems.SOULIUM_INGOT.get()));

    private final TagKey<Block> incorrectBlocksForDrops;
    private final int maxUses;
    private final float efficiency;
    private final float attackDamage;
    private final int enchantability;
    private final Supplier<Ingredient> repairMaterial;

    ModItemTier(TagKey<Block> incorrectBlocksForDrops, int maxUses, float efficiency, float attackDamage, int enchantability, Supplier<Ingredient> repairMaterial) {
        this.incorrectBlocksForDrops = incorrectBlocksForDrops;
        this.maxUses = maxUses;
        this.efficiency = efficiency;
        this.attackDamage = attackDamage;
        this.enchantability = enchantability;
        this.repairMaterial = Suppliers.memoize(repairMaterial::get);
    }

    @Override
    public TagKey<Block> getIncorrectBlocksForDrops() {
        return this.incorrectBlocksForDrops;
    }

    @Override
    public int getUses() {
        return this.maxUses;
    }

    @Override
    public float getSpeed() {
        return this.efficiency;
    }

    @Override
    public float getAttackDamageBonus() {
        return this.attackDamage;
    }

    @Override
    public int getEnchantmentValue() {
        return this.enchantability;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return this.repairMaterial.get();
    }
}

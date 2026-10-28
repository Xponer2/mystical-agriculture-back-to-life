package com.blakebr0.mysticalagriculture.crafting.ingredient;

import java.util.stream.Stream;

import com.blakebr0.mysticalagriculture.init.ModIngredientTypes;
import com.mojang.serialization.MapCodec;
import io.github.xponer2.translator.recipe.ingredient.ICustomIngredient;
import io.github.xponer2.translator.recipe.ingredient.IngredientType;
import org.jetbrains.annotations.Nullable;

import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;

import io.github.xponer2.translator.util.BuiltInRegistries;

public class HoeIngredient implements ICustomIngredient {
    public static final MapCodec<HoeIngredient> CODEC = MapCodec.unit(HoeIngredient::new);

    private ItemStack[] stacks;

    @Override
    public boolean test(@Nullable ItemStack stack) {
        if (stack != null) {
            if (this.stacks == null) {
                this.initMatchingStacks();
            }

            for (var itemstack : this.stacks) {
                if (itemstack.getItem() == stack.getItem()) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public Stream<ItemStack> getItems() {
        if (this.stacks == null) {
            this.initMatchingStacks();
        }

        return Stream.of(this.stacks);
    }

    @Override
    public boolean isSimple() {
        return true;
    }

    @Override
    public IngredientType<?> getType() {
        return ModIngredientTypes.ALL_HOES.get();
    }

    private void initMatchingStacks() {
        this.stacks = BuiltInRegistries.ITEM.entrySet()
                .stream()
                .filter(i -> i.getValue() instanceof ItemHoe)
                .map(i -> new ItemStack(i.getValue()))
                .toArray(ItemStack[]::new);
    }
}

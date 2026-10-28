package com.blakebr0.mysticalagriculture.api.lib;

import io.github.xponer2.translator.component.DataComponentMap;
import io.github.xponer2.translator.recipe.ingredient.DataComponentIngredient;
import io.github.xponer2.translator.recipe.ingredient.Ingredients;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

import io.github.xponer2.translator.tags.ItemTags;
import io.github.xponer2.translator.util.BuiltInRegistries;
import io.github.xponer2.translator.component.StackComponents;
import io.github.xponer2.translator.util.ResourceLocations;

public class LazyIngredient {
    public static final LazyIngredient EMPTY = new LazyIngredient(null, null, null) {
        @Override
        public Ingredient getIngredient() {
            return Ingredient.EMPTY;
        }
    };

    private final String id;
    private final DataComponentMap components;
    private final Type type;
    private Ingredient ingredient;

    private LazyIngredient(String id, Type type, DataComponentMap components) {
        this.id = id;
        this.type = type;
        this.components = components;
    }

    public static LazyIngredient item(String name) {
        return item(name, DataComponentMap.EMPTY);
    }

    public static LazyIngredient item(String name, DataComponentMap components) {
        return new LazyIngredient(name, Type.ITEM, components);
    }

    public static LazyIngredient tag(String name) {
        return new LazyIngredient(name, Type.TAG, DataComponentMap.EMPTY);
    }
    
    public String getId() {
        return this.id;
    }

    public boolean isItem() {
        return this.type == Type.ITEM;
    }

    public boolean isTag() {
        return this.type == Type.TAG;
    }

    public Ingredient getIngredient() {
        if (this.ingredient == null) {
            if (this.isTag()) {
                var tag = ItemTags.create(ResourceLocations.parse(this.id));
                this.ingredient = Ingredients.of(tag);
            } else if (this.isItem()) {
                var item = BuiltInRegistries.ITEM.get(ResourceLocations.parse(this.id));
                if (item != Items.AIR) {
                    if (this.components == null || this.components.isEmpty()) {
                        this.ingredient = Ingredients.of(item);
                    } else {
                        var stack = new ItemStack(item);
                        StackComponents.applyComponents(stack, this.components);
                        this.ingredient = DataComponentIngredient.of(false, stack);
                    }
                }
            }
        }

        return this.ingredient == null ? Ingredient.EMPTY : this.ingredient;
    }

    private enum Type {
        ITEM, TAG
    }
}

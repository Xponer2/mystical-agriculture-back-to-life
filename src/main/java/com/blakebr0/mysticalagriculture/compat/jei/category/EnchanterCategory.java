package com.blakebr0.mysticalagriculture.compat.jei.category;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.blakebr0.cucumber.util.Localizable;
import com.blakebr0.mysticalagriculture.MysticalAgriculture;
import com.blakebr0.mysticalagriculture.api.crafting.IEnchanterRecipe;
import com.blakebr0.mysticalagriculture.init.ModBlocks;
import com.google.common.collect.ImmutableList;
import io.github.xponer2.translator.jei.IRecipeLayoutBuilder;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.IGuiHelper;
import io.github.xponer2.translator.jei.IFocusGroup;
import io.github.xponer2.translator.jei.RecipeIngredientRole;
import io.github.xponer2.translator.jei.RecipeType;
import io.github.xponer2.translator.jei.IRecipeCategory;

import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.init.Items;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import io.github.xponer2.translator.util.Platform;

public class EnchanterCategory implements IRecipeCategory<IEnchanterRecipe> {
    private static final ResourceLocation TEXTURE = MysticalAgriculture.resource("textures/jei/enchanter.png");
    public static final RecipeType<IEnchanterRecipe> RECIPE_TYPE = RecipeType.create(MysticalAgriculture.MOD_ID, "enchanter", IEnchanterRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;

    public EnchanterCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 144, 26);
        this.icon = helper.createDrawableIngredient(new ItemStack(ModBlocks.ENCHANTER.get()));
    }

    @Override
    public RecipeType<IEnchanterRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public ITextComponent getTitle() {
        return Localizable.of("jei.category.mysticalagriculture.enchanter").build();
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, IEnchanterRecipe recipe, IFocusGroup focuses) {
        var level = Minecraft.getMinecraft().world;

        assert level != null;

        builder.addSlot(RecipeIngredientRole.INPUT, 1, 5).addItemStacks(createIngredientItemStack(recipe, 0));
        builder.addSlot(RecipeIngredientRole.INPUT, 23, 5).addItemStacks(createIngredientItemStack(recipe, 1));
        builder.addSlot(RecipeIngredientRole.INPUT, 63, 5).addItemStacks(createIngredientItemStack(recipe, 2));

        builder.addSlot(RecipeIngredientRole.OUTPUT, 123, 5).addItemStacks(createIngredientItemStack(recipe, 3));
    }

    private static List<ItemStack> createIngredientItemStack(IEnchanterRecipe recipe, int slot) {
        if (slot == 2) {
            return ImmutableList.of(new ItemStack(Items.BOOK));
        }

        var enchantment = recipe.getEnchantment();
        var maxLevel = enchantment.value().getMaxLevel();

        if (slot == 3) {
            return IntStream.rangeClosed(1, maxLevel)
                    .mapToObj(i -> ItemEnchantedBook.getEnchantedItemStack(new EnchantmentData(enchantment.value(), i)))
                    .collect(Collectors.toList());
        }

        var ingredients = recipe.getIngredients();

        return Arrays.stream(ingredients.get(slot).getMatchingStacks()).flatMap(stack ->
            IntStream.rangeClosed(1, maxLevel)
                    .mapToObj(i -> Platform.copyWithCount(stack, recipe.getCount(slot) * i))
        ).collect(Collectors.toList());
    }
}

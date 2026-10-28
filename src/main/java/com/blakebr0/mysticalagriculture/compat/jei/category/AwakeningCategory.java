package com.blakebr0.mysticalagriculture.compat.jei.category;

import java.util.ArrayList;
import java.util.List;

import io.github.xponer2.translator.registry.RegistryAccess;
import com.blakebr0.cucumber.util.Localizable;
import com.blakebr0.mysticalagriculture.MysticalAgriculture;
import com.blakebr0.mysticalagriculture.api.crafting.IAwakeningRecipe;
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
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class AwakeningCategory implements IRecipeCategory<IAwakeningRecipe> {
    private static final ResourceLocation TEXTURE = MysticalAgriculture.resource("textures/jei/infusion.png");
    public static final RecipeType<IAwakeningRecipe> RECIPE_TYPE = RecipeType.create(MysticalAgriculture.MOD_ID, "awakening", IAwakeningRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;

    public AwakeningCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 144, 81);
        this.icon = helper.createDrawableIngredient(new ItemStack(ModBlocks.AWAKENING_ALTAR.get()));
    }

    @Override
    public RecipeType<IAwakeningRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public ITextComponent getTitle() {
        return Localizable.of("jei.category.mysticalagriculture.awakening").build();
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
    public void setRecipe(IRecipeLayoutBuilder builder, IAwakeningRecipe recipe, IFocusGroup focuses) {
        var level = Minecraft.getMinecraft().world;

        assert level != null;

        var inputs = toItemStackLists(recipe);
        var output = recipe.getResultItem(RegistryAccess.INSTANCE);

        builder.addSlot(RecipeIngredientRole.INPUT, 33, 33).addItemStacks(inputs.get(0));
        builder.addSlot(RecipeIngredientRole.INPUT, 7, 7).addItemStacks(inputs.get(1));
        builder.addSlot(RecipeIngredientRole.INPUT, 33, 1).addItemStacks(inputs.get(2));
        builder.addSlot(RecipeIngredientRole.INPUT, 59, 7).addItemStacks(inputs.get(3));
        builder.addSlot(RecipeIngredientRole.INPUT, 65, 33).addItemStacks(inputs.get(4));
        builder.addSlot(RecipeIngredientRole.INPUT, 59, 59).addItemStacks(inputs.get(5));
        builder.addSlot(RecipeIngredientRole.INPUT, 33, 64).addItemStacks(inputs.get(6));
        builder.addSlot(RecipeIngredientRole.INPUT, 7, 59).addItemStacks(inputs.get(7));
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 33).addItemStacks(inputs.get(8));

        builder.addSlot(RecipeIngredientRole.OUTPUT, 123, 33).addItemStack(output);
    }

    private static List<List<ItemStack>> toItemStackLists(IAwakeningRecipe recipe) {
        var result = new ArrayList<List<ItemStack>>(9);

        var input = recipe.getAltarIngredient();
        var ingredients = recipe.getIngredients();
        var essences = recipe.getEssences();

        result.add(ImmutableList.copyOf(input.getMatchingStacks()));

        for (int i = 0; i < ingredients.size(); i++) {
            // essence vessel ingredient
            if (i % 2 == 0) {
                result.add(ImmutableList.of(essences.get(Math.floorDiv(i, 2))));
            } else {
                result.add(ImmutableList.copyOf(ingredients.get(i).getMatchingStacks()));
            }
        }

        return result;
    }
}

package com.blakebr0.mysticalagriculture.compat.jei.category;

import com.blakebr0.cucumber.util.Localizable;
import com.blakebr0.mysticalagriculture.MysticalAgriculture;
import com.blakebr0.mysticalagriculture.compat.jei.CruxRecipe;
import com.blakebr0.mysticalagriculture.init.ModItems;
import io.github.xponer2.translator.jei.IRecipeLayoutBuilder;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.IGuiHelper;
import io.github.xponer2.translator.jei.IFocusGroup;
import io.github.xponer2.translator.jei.RecipeIngredientRole;
import io.github.xponer2.translator.jei.RecipeType;
import io.github.xponer2.translator.jei.IRecipeCategory;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class CruxCategory implements IRecipeCategory<CruxRecipe> {
    private static final ResourceLocation TEXTURE = MysticalAgriculture.resource("textures/jei/crux.png");
    public static final RecipeType<CruxRecipe> RECIPE_TYPE = RecipeType.create(MysticalAgriculture.MOD_ID, "crux", CruxRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;

    public CruxCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 80, 54);
        this.icon = helper.createDrawableIngredient(new ItemStack(ModItems.PROSPERITY_SEED_BASE.get()));
    }

    @Override
    public RecipeType<CruxRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public ITextComponent getTitle() {
        return Localizable.of("jei.category.mysticalagriculture.crux").build();
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
    public void setRecipe(IRecipeLayoutBuilder builder, CruxRecipe recipe, IFocusGroup focuses) {
        var inputs = recipe.getIngredients();
        var output = recipe.essence;

        builder.addSlot(RecipeIngredientRole.INPUT, 1, 1).addIngredients(inputs.get(0));
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 19).addIngredients(inputs.get(1));
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 37).addIngredients(inputs.get(2));

        builder.addSlot(RecipeIngredientRole.INPUT, 59, 20).addItemStack(output);
    }
}

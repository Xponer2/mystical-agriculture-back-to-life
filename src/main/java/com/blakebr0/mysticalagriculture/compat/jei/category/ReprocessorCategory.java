package com.blakebr0.mysticalagriculture.compat.jei.category;

import io.github.xponer2.translator.registry.RegistryAccess;
import com.blakebr0.cucumber.util.Localizable;
import com.blakebr0.mysticalagriculture.MysticalAgriculture;
import com.blakebr0.mysticalagriculture.api.crafting.IReprocessorRecipe;
import com.blakebr0.mysticalagriculture.init.ModBlocks;
import io.github.xponer2.translator.jei.IRecipeLayoutBuilder;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableAnimated;
import io.github.xponer2.translator.jei.IRecipeSlotsView;
import mezz.jei.api.IGuiHelper;
import io.github.xponer2.translator.jei.IFocusGroup;
import io.github.xponer2.translator.jei.RecipeIngredientRole;
import io.github.xponer2.translator.jei.RecipeType;
import io.github.xponer2.translator.jei.IRecipeCategory;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import io.github.xponer2.translator.client.GuiGraphics;

public class ReprocessorCategory implements IRecipeCategory<IReprocessorRecipe> {
    private static final ResourceLocation TEXTURE = MysticalAgriculture.resource("textures/jei/reprocessor.png");
    public static final RecipeType<IReprocessorRecipe> RECIPE_TYPE = RecipeType.create(MysticalAgriculture.MOD_ID, "reprocessor", IReprocessorRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawableAnimated arrow;

    public ReprocessorCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 82, 26);
        this.icon = helper.createDrawableIngredient(new ItemStack(ModBlocks.REPROCESSOR.get()));

        var arrow = helper.createDrawable(TEXTURE, 85, 0, 24, 17);

        this.arrow = helper.createAnimatedDrawable(arrow, 100, IDrawableAnimated.StartDirection.LEFT, false);
    }

    @Override
    public RecipeType<IReprocessorRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public ITextComponent getTitle() {
        return Localizable.of("jei.category.mysticalagriculture.reprocessor").build();
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
    public void draw(IReprocessorRecipe recipe, IRecipeSlotsView slots, GuiGraphics gfx, double mouseX, double mouseY) {
        this.arrow.draw(Minecraft.getMinecraft(), 24, 4);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, IReprocessorRecipe recipe, IFocusGroup focuses) {
        var level = Minecraft.getMinecraft().world;

        assert level != null;

        var inputs = recipe.getIngredients();
        var output = recipe.getResultItem(RegistryAccess.INSTANCE);

        builder.addSlot(RecipeIngredientRole.INPUT, 1, 5).addIngredients(inputs.get(0));

        builder.addSlot(RecipeIngredientRole.OUTPUT, 61, 5).addItemStack(output);
    }
}

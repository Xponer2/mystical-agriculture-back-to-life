package com.blakebr0.mysticalagriculture.compat.jei.category;

import io.github.xponer2.translator.registry.RegistryAccess;
import com.blakebr0.cucumber.util.Localizable;
import com.blakebr0.mysticalagriculture.MysticalAgriculture;
import com.blakebr0.mysticalagriculture.api.crafting.IInfusionRecipe;
import com.blakebr0.mysticalagriculture.init.ModBlocks;
import io.github.xponer2.translator.jei.IRecipeLayoutBuilder;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.IGuiHelper;
import io.github.xponer2.translator.jei.IFocusGroup;
import io.github.xponer2.translator.jei.RecipeIngredientRole;
import io.github.xponer2.translator.jei.RecipeType;
import io.github.xponer2.translator.jei.IRecipeCategory;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class InfusionCategory implements IRecipeCategory<IInfusionRecipe> {
    private static final ResourceLocation TEXTURE = MysticalAgriculture.resource("textures/jei/infusion.png");
    public static final RecipeType<IInfusionRecipe> RECIPE_TYPE = RecipeType.create(MysticalAgriculture.MOD_ID, "infusion", IInfusionRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;

    public InfusionCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 144, 81);
        this.icon = helper.createDrawableIngredient(new ItemStack(ModBlocks.INFUSION_ALTAR.get()));
    }

    @Override
    public RecipeType<IInfusionRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public ITextComponent getTitle() {
        return Localizable.of("jei.category.mysticalagriculture.infusion").build();
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
    public void setRecipe(IRecipeLayoutBuilder builder, IInfusionRecipe recipe, IFocusGroup focuses) {
        var level = Minecraft.getMinecraft().world;

        assert level != null;

        var input = recipe.getAltarIngredient();
        var inputs = recipe.getIngredients();
        var output = recipe.getResultItem(RegistryAccess.INSTANCE);

        builder.addSlot(RecipeIngredientRole.INPUT, 33, 33).addIngredients(input);

        var pedestals = (int) inputs.stream().filter(i -> i.getMatchingStacks().length > 0).count();

        switch (pedestals) {
            case 1 -> addSlot(builder, SlotPosition.NORTH, inputs.get(0));
            case 2 -> {
                addSlot(builder, SlotPosition.NORTH, inputs.get(0));
                addSlot(builder, SlotPosition.SOUTH, inputs.get(1));
            }
            case 3 -> {
                addSlot(builder, SlotPosition.WEST, inputs.get(0));
                addSlot(builder, SlotPosition.NORTH, inputs.get(1));
                addSlot(builder, SlotPosition.EAST, inputs.get(2));
            }
            case 4 -> {
                addSlot(builder, SlotPosition.NORTH, inputs.get(0));
                addSlot(builder, SlotPosition.EAST, inputs.get(1));
                addSlot(builder, SlotPosition.SOUTH, inputs.get(2));
                addSlot(builder, SlotPosition.WEST, inputs.get(3));
            }
            case 5 -> {
                addSlot(builder, SlotPosition.NORTH_WEST, inputs.get(0));
                addSlot(builder, SlotPosition.NORTH, inputs.get(1));
                addSlot(builder, SlotPosition.NORTH_EAST, inputs.get(2));
                addSlot(builder, SlotPosition.SOUTH_EAST, inputs.get(3));
                addSlot(builder, SlotPosition.SOUTH_WEST, inputs.get(4));
            }
            case 6 -> {
                addSlot(builder, SlotPosition.NORTH_WEST, inputs.get(0));
                addSlot(builder, SlotPosition.NORTH, inputs.get(1));
                addSlot(builder, SlotPosition.NORTH_EAST, inputs.get(2));
                addSlot(builder, SlotPosition.SOUTH_EAST, inputs.get(3));
                addSlot(builder, SlotPosition.SOUTH, inputs.get(4));
                addSlot(builder, SlotPosition.SOUTH_WEST, inputs.get(5));
            }
            case 7 -> {
                addSlot(builder, SlotPosition.WEST, inputs.get(0));
                addSlot(builder, SlotPosition.NORTH_WEST, inputs.get(1));
                addSlot(builder, SlotPosition.NORTH, inputs.get(2));
                addSlot(builder, SlotPosition.NORTH_EAST, inputs.get(3));
                addSlot(builder, SlotPosition.EAST, inputs.get(4));
                addSlot(builder, SlotPosition.SOUTH_EAST, inputs.get(5));
                addSlot(builder, SlotPosition.SOUTH_WEST, inputs.get(6));
            }
            case 8 -> {
                addSlot(builder, SlotPosition.NORTH_WEST, inputs.get(0));
                addSlot(builder, SlotPosition.NORTH, inputs.get(1));
                addSlot(builder, SlotPosition.NORTH_EAST, inputs.get(2));
                addSlot(builder, SlotPosition.EAST, inputs.get(3));
                addSlot(builder, SlotPosition.SOUTH_EAST, inputs.get(4));
                addSlot(builder, SlotPosition.SOUTH, inputs.get(5));
                addSlot(builder, SlotPosition.SOUTH_WEST, inputs.get(6));
                addSlot(builder, SlotPosition.WEST, inputs.get(7));
            }
        }

        builder.addSlot(RecipeIngredientRole.OUTPUT, 123, 33).addItemStack(output);
    }

    private static void addSlot(IRecipeLayoutBuilder builder, SlotPosition position, Ingredient ingredient) {
        builder.addSlot(RecipeIngredientRole.INPUT, position.x, position.y).addIngredients(ingredient);
    }

    private enum SlotPosition {
        NORTH_WEST(7, 7),
        NORTH(33, 1),
        NORTH_EAST(59, 7),
        EAST(65, 33),
        SOUTH_EAST(59, 59),
        SOUTH(33, 64),
        SOUTH_WEST(7, 59),
        WEST(1, 33);

        final int x;
        final int y;

        SlotPosition(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}

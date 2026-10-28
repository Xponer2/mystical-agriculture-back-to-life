package com.blakebr0.mysticalagriculture.compat.jei.category;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import io.github.xponer2.translator.component.DataComponents;
import com.blakebr0.cucumber.util.Formatting;
import com.blakebr0.cucumber.util.Localizable;
import com.blakebr0.mysticalagriculture.MysticalAgriculture;
import com.blakebr0.mysticalagriculture.api.crafting.ISouliumSpawnerRecipe;
import com.blakebr0.mysticalagriculture.init.ModBlocks;
import com.blakebr0.mysticalagriculture.lib.ModTooltips;
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
import net.minecraft.entity.EntityList;
import net.minecraft.init.Items;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import io.github.xponer2.translator.client.GuiGraphics;
import io.github.xponer2.translator.util.Platform;
import io.github.xponer2.translator.component.StackComponents;

public class SouliumSpawnerCategory implements IRecipeCategory<ISouliumSpawnerRecipe> {
    private static final ResourceLocation TEXTURE = MysticalAgriculture.resource("textures/jei/soulium_spawner.png");
    public static final RecipeType<ISouliumSpawnerRecipe> RECIPE_TYPE = RecipeType.create(MysticalAgriculture.MOD_ID, "soulium_spawner", ISouliumSpawnerRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawableAnimated arrow;

    public SouliumSpawnerCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 82, 26);
        this.icon = helper.createDrawableIngredient(new ItemStack(ModBlocks.REPROCESSOR.get()));

        var arrow = helper.createDrawable(TEXTURE, 85, 0, 24, 17);

        this.arrow = helper.createAnimatedDrawable(arrow, 100, IDrawableAnimated.StartDirection.LEFT, false);
    }

    @Override
    public RecipeType<ISouliumSpawnerRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public ITextComponent getTitle() {
        return Localizable.of("jei.category.mysticalagriculture.soulium_spawner").build();
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
    public void draw(ISouliumSpawnerRecipe recipe, IRecipeSlotsView slots, GuiGraphics gfx, double mouseX, double mouseY) {
        this.arrow.draw(Minecraft.getMinecraft(), 24, 4);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, ISouliumSpawnerRecipe recipe, IFocusGroup focuses) {
        var inputs = createInputsList(recipe);
        var outputs = createOutputsList(recipe);

        builder.addSlot(RecipeIngredientRole.INPUT, 1, 5).addItemStacks(inputs);

        var totalWeight = recipe.getEntityTypes().unwrap()
                .stream().mapToInt(w -> w.weight().asInt()).sum();

        builder.addSlot(RecipeIngredientRole.OUTPUT, 61, 5)
                .addItemStacks(outputs)
                .addRichTooltipCallback((slots, tooltip) -> slots.getDisplayedItemStack().ifPresent(stack -> {
                    var data = StackComponents.get(stack, DataComponents.CUSTOM_DATA);
                    if (data == null || !data.hasKey("Weight"))
                        return;

                    var weight = data.getInteger("Weight");
                    var chance = ((double) weight / (double) totalWeight) * 100D;

                    tooltip.add(ModTooltips.CHANCE.args(Formatting.percent(chance)).build());
                }));
    }

    private static List<ItemStack> createInputsList(ISouliumSpawnerRecipe recipe) {
        return recipe.getIngredients()
                .stream()
                .flatMap(i -> Arrays.stream(i.getMatchingStacks()))
                .map(s -> Platform.copyWithCount(s, recipe.getCount(0)))
                .collect(Collectors.toList());
    }

    private static List<ItemStack> createOutputsList(ISouliumSpawnerRecipe recipe) {
        var entries = recipe.getEntityTypes().unwrap();
        var outputs = new ArrayList<ItemStack>();

        for (var entry : entries) {
            // 1.12.2 has one spawn egg item, the entity goes in its "EntityTag"
            var id = entry.data().entry().getRegistryName();
            if (id == null || !EntityList.ENTITY_EGGS.containsKey(id))
                continue;

            var stack = new ItemStack(Items.SPAWN_EGG);
            ItemMonsterPlacer.applyEntityIdToItemStack(stack, id);

            var tag = StackComponents.get(stack, DataComponents.CUSTOM_DATA);
            tag.setInteger("Weight", entry.weight().asInt());
            StackComponents.set(stack, DataComponents.CUSTOM_DATA, tag);

            outputs.add(stack);
        }

        return outputs;
    }
}

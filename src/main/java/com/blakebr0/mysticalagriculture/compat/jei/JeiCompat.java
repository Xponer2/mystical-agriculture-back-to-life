package com.blakebr0.mysticalagriculture.compat.jei;

import io.github.xponer2.translator.jei.JeiCategory;
import io.github.xponer2.translator.recipe.RecipeManager;
import com.blakebr0.cucumber.helper.RecipeHelper;
import com.blakebr0.mysticalagriculture.MysticalAgriculture;
import com.blakebr0.mysticalagriculture.api.util.MobSoulUtils;
import com.blakebr0.mysticalagriculture.client.screen.EnchanterScreen;
import com.blakebr0.mysticalagriculture.client.screen.EssenceFurnaceScreen;
import com.blakebr0.mysticalagriculture.client.screen.ReprocessorScreen;
import com.blakebr0.mysticalagriculture.client.screen.SoulExtractorScreen;
import com.blakebr0.mysticalagriculture.client.screen.SouliumSpawnerScreen;
import com.blakebr0.mysticalagriculture.compat.jei.category.AwakeningCategory;
import com.blakebr0.mysticalagriculture.compat.jei.category.CruxCategory;
import com.blakebr0.mysticalagriculture.compat.jei.category.EnchanterCategory;
import com.blakebr0.mysticalagriculture.compat.jei.category.InfusionCategory;
import com.blakebr0.mysticalagriculture.compat.jei.category.ReprocessorCategory;
import com.blakebr0.mysticalagriculture.compat.jei.category.SoulExtractorCategory;
import com.blakebr0.mysticalagriculture.compat.jei.category.SouliumSpawnerCategory;
import com.blakebr0.mysticalagriculture.init.ModBlocks;
import com.blakebr0.mysticalagriculture.init.ModItems;
import com.blakebr0.mysticalagriculture.init.ModRecipeTypes;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.ISubtypeRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import net.minecraft.item.ItemStack;

// JEI 4.16: the categories are 1.21 style ones shown through JeiCategory; JEI calls register at load time, without a
// world, so the recipes are added without the 1.21 "level != null" check (they are loaded in postInit).
@JEIPlugin
public final class JeiCompat implements IModPlugin {
    private InfusionCategory infusion;
    private AwakeningCategory awakening;
    private EnchanterCategory enchanter;
    private ReprocessorCategory reprocessor;
    private SoulExtractorCategory soulExtractor;
    private SouliumSpawnerCategory souliumSpawner;
    private CruxCategory crux;

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        var guiHelper = registration.getJeiHelpers().getGuiHelper();

        this.infusion = new InfusionCategory(guiHelper);
        this.awakening = new AwakeningCategory(guiHelper);
        this.enchanter = new EnchanterCategory(guiHelper);
        this.reprocessor = new ReprocessorCategory(guiHelper);
        this.soulExtractor = new SoulExtractorCategory(guiHelper);
        this.souliumSpawner = new SouliumSpawnerCategory(guiHelper);
        this.crux = new CruxCategory(guiHelper);

        registration.addRecipeCategories(
                new JeiCategory<>(this.infusion, MysticalAgriculture.NAME),
                new JeiCategory<>(this.awakening, MysticalAgriculture.NAME),
                new JeiCategory<>(this.enchanter, MysticalAgriculture.NAME),
                new JeiCategory<>(this.reprocessor, MysticalAgriculture.NAME),
                new JeiCategory<>(this.soulExtractor, MysticalAgriculture.NAME),
                new JeiCategory<>(this.souliumSpawner, MysticalAgriculture.NAME),
                new JeiCategory<>(this.crux, MysticalAgriculture.NAME)
        );
    }

    @Override
    public void register(IModRegistry registry) {
        registry.addRecipeCatalyst(new ItemStack(ModBlocks.INFUSION_ALTAR.get()), InfusionCategory.RECIPE_TYPE.getUid());
        registry.addRecipeCatalyst(new ItemStack(ModBlocks.INFUSION_PEDESTAL.get()), InfusionCategory.RECIPE_TYPE.getUid());
        registry.addRecipeCatalyst(new ItemStack(ModBlocks.AWAKENING_ALTAR.get()), AwakeningCategory.RECIPE_TYPE.getUid());
        registry.addRecipeCatalyst(new ItemStack(ModBlocks.AWAKENING_PEDESTAL.get()), AwakeningCategory.RECIPE_TYPE.getUid());
        registry.addRecipeCatalyst(new ItemStack(ModBlocks.ESSENCE_VESSEL.get()), AwakeningCategory.RECIPE_TYPE.getUid());
        registry.addRecipeCatalyst(new ItemStack(ModBlocks.ENCHANTER.get()), EnchanterCategory.RECIPE_TYPE.getUid());
        registry.addRecipeCatalyst(new ItemStack(ModBlocks.FURNACE.get()), VanillaRecipeCategoryUid.SMELTING);
        registry.addRecipeCatalyst(new ItemStack(ModBlocks.REPROCESSOR.get()), ReprocessorCategory.RECIPE_TYPE.getUid());
        registry.addRecipeCatalyst(new ItemStack(ModBlocks.SOUL_EXTRACTOR.get()), SoulExtractorCategory.RECIPE_TYPE.getUid());
        registry.addRecipeCatalyst(new ItemStack(ModBlocks.SOULIUM_SPAWNER.get()), SouliumSpawnerCategory.RECIPE_TYPE.getUid());

        var manager = RecipeManager.get();

        JeiCategory.addRecipes(registry, this.infusion, RecipeHelper.byTypeValues(manager, ModRecipeTypes.INFUSION.get()));
        JeiCategory.addRecipes(registry, this.awakening, RecipeHelper.byTypeValues(manager, ModRecipeTypes.AWAKENING.get()));
        JeiCategory.addRecipes(registry, this.enchanter, RecipeHelper.byTypeValues(manager, ModRecipeTypes.ENCHANTER.get()));
        JeiCategory.addRecipes(registry, this.reprocessor, RecipeHelper.byTypeValues(manager, ModRecipeTypes.REPROCESSOR.get()));
        JeiCategory.addRecipes(registry, this.soulExtractor, RecipeHelper.byTypeValues(manager, ModRecipeTypes.SOUL_EXTRACTION.get()));
        JeiCategory.addRecipes(registry, this.souliumSpawner, RecipeHelper.byTypeValues(manager, ModRecipeTypes.SOULIUM_SPAWNER.get()));

        JeiCategory.addRecipes(registry, this.crux, CruxRecipe.getGeneratedRecipes());

        registry.addIngredientInfo(
                new ItemStack(ModItems.COGNIZANT_DUST.get()),
                VanillaTypes.ITEM,
                "jei.desc.mysticalagriculture.cognizant_dust"
        );

        registry.addRecipeClickArea(EnchanterScreen.class, 104, 41, 22, 15, EnchanterCategory.RECIPE_TYPE.getUid());
        registry.addRecipeClickArea(EssenceFurnaceScreen.class, 99, 52, 22, 15, VanillaRecipeCategoryUid.SMELTING);
        registry.addRecipeClickArea(ReprocessorScreen.class, 99, 52, 22, 15, ReprocessorCategory.RECIPE_TYPE.getUid());
        registry.addRecipeClickArea(SoulExtractorScreen.class, 99, 52, 22, 15, SoulExtractorCategory.RECIPE_TYPE.getUid());
        registry.addRecipeClickArea(SouliumSpawnerScreen.class, 99, 52, 22, 15, SouliumSpawnerCategory.RECIPE_TYPE.getUid());
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistry registry) {
        registry.registerSubtypeInterpreter(ModItems.SOUL_JAR.get(), stack -> {
            var type = MobSoulUtils.getType(stack);
            return type != null ? type.getEntityIds().toString() : ISubtypeRegistry.ISubtypeInterpreter.NONE;
        });
    }
}

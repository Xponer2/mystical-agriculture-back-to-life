package com.blakebr0.mysticalagriculture.crafting.recipe;

import com.blakebr0.mysticalagriculture.api.crafting.IReprocessorRecipe;
import com.blakebr0.mysticalagriculture.init.ModRecipeSerializers;
import com.blakebr0.mysticalagriculture.init.ModRecipeTypes;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.xponer2.translator.network.ByteBufCodecs;
import io.github.xponer2.translator.recipe.CraftingInput;
import io.github.xponer2.translator.recipe.RecipeMatcher;
import io.github.xponer2.translator.recipe.RecipeSerializer;
import io.github.xponer2.translator.recipe.RecipeType;
import io.github.xponer2.translator.registry.HolderLookup;
import io.github.xponer2.translator.util.Codecs;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

import io.github.xponer2.translator.network.RegistryFriendlyByteBuf;
import io.github.xponer2.translator.network.ByteBufCodecs;
import io.github.xponer2.translator.network.StreamCodec;
import io.github.xponer2.translator.util.Codecs;

public class ReprocessorRecipe implements IReprocessorRecipe {
    private final NonNullList<Ingredient> inputs;
    private final ItemStack result;

    public ReprocessorRecipe(Ingredient input, ItemStack result) {
        this.inputs = NonNullList.from(Ingredient.EMPTY, input);
        this.result = result;
    }

    @Override
    public boolean matches(CraftingInput inventory, World level) {
        if (this.inputs.size() != inventory.ingredientCount())
            return false;

        var inputs = NonNullList.<ItemStack>create();

        for (var i = 0; i < inventory.size(); i++) {
            var item = inventory.getItem(i);
            if (!item.isEmpty()) {
                inputs.add(item);
            }
        }

        return RecipeMatcher.findMatches(inputs, this.inputs) != null;
    }

    @Override
    public ItemStack assemble(CraftingInput inventory, HolderLookup.Provider provider) {
        return this.result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider) {
        return this.result;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return this.inputs;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.REPROCESSOR.get();
    }

    @Override
    public RecipeType<? extends IReprocessorRecipe> getType() {
        return ModRecipeTypes.REPROCESSOR.get();
    }

    public static class Serializer implements RecipeSerializer<ReprocessorRecipe> {
        public static final MapCodec<ReprocessorRecipe> CODEC = RecordCodecBuilder.mapCodec(builder ->
               builder.group(
                       Codecs.INGREDIENT_NONEMPTY.fieldOf("input").forGetter(recipe -> recipe.inputs.get(0)),
                       Codecs.ITEM_STACK.fieldOf("result").forGetter(recipe -> recipe.result)
               ).apply(builder, ReprocessorRecipe::new)
        );
        public static final StreamCodec<RegistryFriendlyByteBuf, ReprocessorRecipe> STREAM_CODEC = StreamCodec.of(
                ReprocessorRecipe.Serializer::toNetwork, ReprocessorRecipe.Serializer::fromNetwork
        );

        @Override
        public MapCodec<ReprocessorRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, ReprocessorRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        private static ReprocessorRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
            var input = ByteBufCodecs.INGREDIENT.decode(buffer);
            var result = ByteBufCodecs.ITEM_STACK.decode(buffer);

            return new ReprocessorRecipe(input, result);
        }

        private static void toNetwork(RegistryFriendlyByteBuf buffer, ReprocessorRecipe recipe) {
            ByteBufCodecs.INGREDIENT.encode(buffer, recipe.inputs.get(0));
            ByteBufCodecs.ITEM_STACK.encode(buffer, recipe.result);
        }
    }
}

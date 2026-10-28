package com.blakebr0.mysticalagriculture.crafting.recipe;

import java.util.function.BiFunction;

import com.blakebr0.mysticalagriculture.api.crafting.IInfusionRecipe;
import com.blakebr0.mysticalagriculture.init.ModRecipeSerializers;
import com.blakebr0.mysticalagriculture.init.ModRecipeTypes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
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
import io.github.xponer2.translator.component.StackComponents;

public class InfusionRecipe implements IInfusionRecipe {
    public static final int RECIPE_SIZE = 9;
    private final Ingredient input;
    private final NonNullList<Ingredient> inputs;
    private final ItemStack result;
    private final boolean transferComponents;
    // for CraftTweaker recipes
    private BiFunction<Integer, ItemStack, ItemStack> transformer;

    // the input is specified separately in JSON but is part of the ingredient list in practice
    public InfusionRecipe(Ingredient input, NonNullList<Ingredient> inputs, ItemStack result, boolean transferComponents) {
        this.input = input;
        this.inputs = inputs;
        this.result = result;
        this.transferComponents = transferComponents;
    }

    @Override
    public boolean matches(CraftingInput inventory, World level) {
        // -1 ingredient for the input item
        if (this.inputs.size() != inventory.ingredientCount() - 1)
            return false;

        var input = inventory.getItem(0);
        if (!this.input.test(input))
            return false;

        var inputs = NonNullList.<ItemStack>create();

        for (var i = 1; i < inventory.size(); i++) {
            var item = inventory.getItem(i);
            if (!item.isEmpty()) {
                inputs.add(item);
            }
        }

        return RecipeMatcher.findMatches(inputs, this.inputs) != null;
    }

    @Override
    public ItemStack assemble(CraftingInput inventory, HolderLookup.Provider lookup) {
        var stack = inventory.getItem(0);
        var result = this.result.copy();

        if (this.transferComponents) {
            StackComponents.applyComponents(result, StackComponents.getComponentsPatch(stack));
        }

        return result;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider lookup) {
        return this.result;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return this.inputs;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.INFUSION.get();
    }

    @Override
    public RecipeType<? extends IInfusionRecipe> getType() {
        return ModRecipeTypes.INFUSION.get();
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingInput inventory) {
        var remaining = NonNullList.withSize(inventory.size(), ItemStack.EMPTY);

        for (int i = 0; i < remaining.size(); ++i) {
            var item = inventory.getItem(i);
            if (item.getItem().hasContainerItem(item)) {
                remaining.set(i, item.getItem().getContainerItem(item));
            }
        }

        if (this.transformer != null) {
            var used = new boolean[remaining.size()];
            var inputs = NonNullList.<Ingredient>create();

            inputs.add(this.input);
            inputs.addAll(this.inputs);

            for (int i = 0; i < remaining.size(); i++) {
                var stack = inventory.getItem(i);
                for (int j = 0; j < inputs.size(); j++) {
                    var input = inputs.get(j);

                    if (!used[j] && input.test(stack)) {
                        var ingredient = this.transformer.apply(j, stack);

                        used[j] = true;
                        remaining.set(i, ingredient);

                        break;
                    }
                }
            }
        }

        return remaining;
    }

    @Override
    public Ingredient getAltarIngredient() {
        return this.input;
    }

    public void setTransformer(BiFunction<Integer, ItemStack, ItemStack> transformer) {
        this.transformer = transformer;
    }

    public static class Serializer implements RecipeSerializer<InfusionRecipe> {
        public static final MapCodec<InfusionRecipe> CODEC = RecordCodecBuilder.mapCodec(builder ->
                builder.group(
                        Codecs.INGREDIENT_NONEMPTY.fieldOf("input").forGetter(recipe -> recipe.input),
                        Codecs.INGREDIENT_NONEMPTY
                                .listOf()
                                .fieldOf("ingredients")
                                .flatXmap(
                                        field -> {
                                            var max = 8;
                                            var ingredients = field.toArray(new Ingredient[0]);
                                            if (ingredients.length == 0) {
                                                return DataResult.error("No ingredients for infusion recipe");
                                            } else {
                                                return ingredients.length > max
                                                        ? DataResult.error(String.format("Too many ingredients for infusion recipe. The maximum is: %s", max))
                                                        : DataResult.success(NonNullList.from(Ingredient.EMPTY, ingredients));
                                            }
                                        },
                                        DataResult::success
                                )
                                .forGetter(recipe -> recipe.inputs),
                        Codecs.ITEM_STACK.fieldOf("result").forGetter(recipe -> recipe.result),
                        Codec.BOOL.optionalFieldOf("transfer_components", false).forGetter(recipe -> recipe.transferComponents)
                ).apply(builder, InfusionRecipe::new)
        );
        public static final StreamCodec<RegistryFriendlyByteBuf, InfusionRecipe> STREAM_CODEC = StreamCodec.of(
                InfusionRecipe.Serializer::toNetwork, InfusionRecipe.Serializer::fromNetwork
        );

        @Override
        public MapCodec<InfusionRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, InfusionRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        private static InfusionRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
            var input = ByteBufCodecs.INGREDIENT.decode(buffer);
            int size = buffer.readVarInt();
            var inputs = NonNullList.withSize(size, Ingredient.EMPTY);

            for (int i = 0; i < size; i++) {
                inputs.set(i, ByteBufCodecs.INGREDIENT.decode(buffer));
            }

            var result = ByteBufCodecs.ITEM_STACK.decode(buffer);
            var transferComponents = buffer.readBoolean();

            return new InfusionRecipe(input, inputs, result, transferComponents);
        }

        private static void toNetwork(RegistryFriendlyByteBuf buffer, InfusionRecipe recipe) {
            ByteBufCodecs.INGREDIENT.encode(buffer, recipe.input);
            buffer.writeVarInt(recipe.inputs.size());

            for (var ingredient : recipe.inputs) {
                ByteBufCodecs.INGREDIENT.encode(buffer, ingredient);
            }

            ByteBufCodecs.ITEM_STACK.encode(buffer, recipe.result);
            buffer.writeBoolean(recipe.transferComponents);
        }
    }
}

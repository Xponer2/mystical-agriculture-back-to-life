package com.blakebr0.mysticalagriculture.crafting.recipe;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import io.github.xponer2.translator.component.StackComponents;
import io.github.xponer2.translator.network.ByteBufCodecs;
import io.github.xponer2.translator.recipe.CraftingInput;
import io.github.xponer2.translator.recipe.RecipeMatcher;
import io.github.xponer2.translator.recipe.RecipeSerializer;
import io.github.xponer2.translator.recipe.RecipeType;
import io.github.xponer2.translator.recipe.ingredient.Ingredients;
import io.github.xponer2.translator.registry.HolderLookup;
import io.github.xponer2.translator.util.Codecs;
import com.blakebr0.cucumber.helper.StackHelper;
import com.blakebr0.mysticalagriculture.api.crafting.IAwakeningRecipe;
import com.blakebr0.mysticalagriculture.init.ModRecipeSerializers;
import com.blakebr0.mysticalagriculture.init.ModRecipeTypes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

import io.github.xponer2.translator.network.RegistryFriendlyByteBuf;
import io.github.xponer2.translator.network.ByteBufCodecs;
import io.github.xponer2.translator.network.StreamCodec;
import io.github.xponer2.translator.util.Codecs;
import io.github.xponer2.translator.util.Platform;

public class AwakeningRecipe implements IAwakeningRecipe {
    public static final int RECIPE_SIZE = 9;

    private final Ingredient input;
    private final NonNullList<Ingredient> inputs;
    private final NonNullList<ItemStack> essences;
    private final ItemStack result;
    private final boolean transferComponents;
    // for CraftTweaker recipes
    private BiFunction<Integer, ItemStack, ItemStack> transformer;

    // the input is specified separately in JSON but is part of the ingredient list in practice
    public AwakeningRecipe(Ingredient input, NonNullList<Ingredient> inputs, NonNullList<ItemStack> essences, ItemStack result, boolean transferComponents) {
        this.input = input;
        this.essences = essences;
        this.result = result;
        this.transferComponents = transferComponents;

        var allInputs = NonNullList.withSize(8, Ingredient.EMPTY);

        allInputs.set(0, Ingredients.of(essences.get(0)));
        allInputs.set(1, inputs.get(0));
        allInputs.set(2, Ingredients.of(essences.get(1)));
        allInputs.set(3, inputs.get(1));
        allInputs.set(4, Ingredients.of(essences.get(2)));
        allInputs.set(5, inputs.get(2));
        allInputs.set(6, Ingredients.of(essences.get(3)));
        allInputs.set(7, inputs.get(3));

        this.inputs = allInputs;
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
    public ItemStack assemble(CraftingInput inventory, HolderLookup.Provider provider) {
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
    public ItemStack getResultItem(HolderLookup.Provider provider) {
        return this.result;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return this.inputs;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.AWAKENING.get();
    }

    @Override
    public RecipeType<? extends IAwakeningRecipe> getType() {
        return ModRecipeTypes.AWAKENING.get();
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingInput inventory) {
        var remaining = NonNullList.withSize(inventory.size(), ItemStack.EMPTY);
        // we need to track this separately since the recipe stores vessels and pedestals in alternating order,
        // while the recipe inventory stores them in sequential order
        var vesselIndex = 0;

        for (int i = 0; i < remaining.size(); i++) {
            var stack = inventory.getItem(i);

            // slot indexes 5 -> 8 are the essence vessels
            if (i > 4) {
                var input = this.inputs.get(vesselIndex);

                vesselIndex += 2;

                if (input.getMatchingStacks().length == 0)
                    continue;

                // the ingredient will have the same ItemStack instance as the essence
                // this *should* be the quickest way to find the exact essence in the recipe
                for (var essence : this.essences) {
                    if (input.getMatchingStacks()[0] == essence) {
                        remaining.set(i, StackHelper.shrink(stack, essence.getCount(), false));
                        break;
                    }
                }
            } else {
                if (stack.getItem().hasContainerItem(stack)) {
                    remaining.set(i, stack.getItem().getContainerItem(stack));
                }

                if (this.transformer != null) {
                    var used = new boolean[remaining.size()];
                    var inputs = NonNullList.<Ingredient>create();

                    inputs.add(this.input);
                    inputs.addAll(this.inputs);

                    for (int j = 0; j < inputs.size(); j += 2) {
                        var input = inputs.get(j);

                        if (!used[j] && input.test(stack)) {
                            var index = Math.floorDiv(i, 2);
                            var ingredient = this.transformer.apply(index, stack);

                            used[j] = true;
                            remaining.set(i, ingredient);

                            break;
                        }
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

    @Override
    public NonNullList<ItemStack> getEssences() {
        return this.essences;
    }

    @Override
    public Map<ItemStack, Integer> getMissingEssences(List<ItemStack> items) {
        var remaining = new ArrayList<>(this.essences);
        var missing = new LinkedHashMap<ItemStack, Integer>();

        for (var item : items) {
            for (var essence : remaining) {
                if (StackComponents.isSameItemSameComponents(item, essence)) {
                    var current = item.getCount();
                    var required = essence.getCount();

                    if (current < required) {
                        missing.put(essence, required - current);
                    }

                    remaining.remove(essence);

                    break;
                }
            }
        }

        for (var essence : remaining) {
            missing.put(essence, essence.getCount());
        }

        return missing;
    }

    public void setTransformer(BiFunction<Integer, ItemStack, ItemStack> transformer) {
        this.transformer = transformer;
    }

    public static class Serializer implements RecipeSerializer<AwakeningRecipe> {
        public static final MapCodec<AwakeningRecipe> CODEC = RecordCodecBuilder.mapCodec(builder ->
                builder.group(
                        Codecs.INGREDIENT_NONEMPTY.fieldOf("input").forGetter(recipe -> recipe.input),
                        Codecs.INGREDIENT_NONEMPTY
                                .listOf()
                                .fieldOf("ingredients")
                                .flatXmap(
                                        field -> {
                                            var max = 4;
                                            var ingredients = field.toArray(new Ingredient[0]);
                                            if (ingredients.length == 0) {
                                                return DataResult.error("No ingredients for awakening recipe");
                                            } else {
                                                if (ingredients.length > max) {
                                                    return DataResult.error(String.format("Too many ingredients for awakening recipe. The maximum is: %s", max));
                                                }

                                                var result = NonNullList.withSize(max, Ingredient.EMPTY);

                                                for (int i = 0; i < ingredients.length; i++) {
                                                    result.set(i, ingredients[i]);
                                                }

                                                return DataResult.success(result);
                                            }
                                        },
                                        DataResult::success
                                )
                                .forGetter(recipe -> recipe.inputs),
                        Codecs.ITEM_STACK
                                .listOf()
                                .fieldOf("essences")
                                .flatXmap(
                                        field -> {
                                            var max = 4;
                                            var stacks = field.toArray(new ItemStack[0]);
                                            if (stacks.length == 0) {
                                                return DataResult.error("No essences for awakening recipe");
                                            } else {
                                                if (stacks.length > max) {
                                                    return DataResult.error(String.format("Too many essences for awakening recipe. The maximum is: %s", max));
                                                }

                                                var result = NonNullList.withSize(max, ItemStack.EMPTY);

                                                for (int i = 0; i < stacks.length; i++) {
                                                    result.set(i, stacks[i]);
                                                }

                                                return DataResult.success(result);
                                            }
                                        },
                                        DataResult::success
                                )
                                .forGetter(recipe -> recipe.essences),
                        Codecs.ITEM_STACK.fieldOf("result").forGetter(recipe -> recipe.result),
                        Codec.BOOL.optionalFieldOf("transfer_components", false).forGetter(recipe -> recipe.transferComponents)
                ).apply(builder, AwakeningRecipe::new)
        );
        public static final StreamCodec<RegistryFriendlyByteBuf, AwakeningRecipe> STREAM_CODEC = StreamCodec.of(
                AwakeningRecipe.Serializer::toNetwork, AwakeningRecipe.Serializer::fromNetwork
        );

        @Override
        public MapCodec<AwakeningRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, AwakeningRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        private static AwakeningRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
            var input = ByteBufCodecs.INGREDIENT.decode(buffer);
            int size = buffer.readVarInt();
            var inputs = NonNullList.withSize(size, Ingredient.EMPTY);

            for (int i = 0; i < size; i++) {
                inputs.set(i, ByteBufCodecs.INGREDIENT.decode(buffer));
            }

            size = buffer.readVarInt();
            var essences = NonNullList.withSize(size, ItemStack.EMPTY);

            for (int i = 0; i < size; i++) {
                essences.set(i, ByteBufCodecs.OPTIONAL_ITEM_STACK.decode(buffer));
            }

            var result = ByteBufCodecs.ITEM_STACK.decode(buffer);
            var transferComponents = buffer.readBoolean();

            return new AwakeningRecipe(input, inputs, essences, result, transferComponents);
        }

        private static void toNetwork(RegistryFriendlyByteBuf buffer, AwakeningRecipe recipe) {
            ByteBufCodecs.INGREDIENT.encode(buffer, recipe.input);

            buffer.writeVarInt(4);

            // only send the non-vessel ingredients
            for (int i = 1; i <= 7; i += 2) {
                ByteBufCodecs.INGREDIENT.encode(buffer, recipe.inputs.get(i));
            }

            buffer.writeVarInt(4);

            for (int i = 0; i < 4; i++) {
                ByteBufCodecs.OPTIONAL_ITEM_STACK.encode(buffer, recipe.essences.get(i));
            }

            ByteBufCodecs.ITEM_STACK.encode(buffer, recipe.result);
            buffer.writeBoolean(recipe.transferComponents);
        }
    }
}

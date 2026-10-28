package com.blakebr0.mysticalagriculture.crafting.recipe;

import java.util.Arrays;
import java.util.stream.Collectors;

import io.github.xponer2.translator.network.ByteBufCodecs;
import io.github.xponer2.translator.recipe.CraftingInput;
import io.github.xponer2.translator.recipe.RecipeInput;
import io.github.xponer2.translator.recipe.RecipeMatcher;
import io.github.xponer2.translator.recipe.RecipeSerializer;
import io.github.xponer2.translator.recipe.RecipeType;
import io.github.xponer2.translator.recipe.ingredient.ICustomIngredient;
import io.github.xponer2.translator.registry.Holder;
import io.github.xponer2.translator.registry.HolderLookup;
import io.github.xponer2.translator.util.Codecs;
import com.blakebr0.cucumber.crafting.ingredient.IngredientWithCount;
import com.blakebr0.mysticalagriculture.api.crafting.IEnchanterRecipe;
import com.blakebr0.mysticalagriculture.init.ModRecipeSerializers;
import com.blakebr0.mysticalagriculture.init.ModRecipeTypes;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Items;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

import io.github.xponer2.translator.network.RegistryFriendlyByteBuf;
import io.github.xponer2.translator.network.StreamCodec;
import io.github.xponer2.translator.util.Platform;
import io.github.xponer2.translator.util.ItemStacks;

public class EnchanterRecipe implements IEnchanterRecipe {
    private final NonNullList<IngredientWithCount> inputs;
    private final Holder<Enchantment> enchantment;

    public EnchanterRecipe(NonNullList<IngredientWithCount> inputs, Holder<Enchantment> enchantment) {
        this.inputs = inputs;
        this.enchantment = enchantment;
    }

    @Override
    public boolean matches(CraftingInput inventory, World level) {
        if (this.inputs.size() != inventory.ingredientCount() - 1)
            return false;

        var inputs = NonNullList.<ItemStack>create();

        for (var i = 0; i < inventory.size() - 1; i++) {
            var item = inventory.getItem(i);
            if (!item.isEmpty()) {
                inputs.add(item);
            }
        }

        return RecipeMatcher.findMatches(inputs, this.inputs) != null;
    }

    @Override
    public ItemStack assemble(CraftingInput inventory, HolderLookup.Provider provider) {
        return this.assemble(inventory, provider, this.getMaxResultEnchantmentLevel(inventory));
    }

    @Override
    public ItemStack assemble(CraftingInput inventory, HolderLookup.Provider provider, int level) {
        var stack = inventory.getItem(2);

        var target = this.enchantment.value();

        if (target.canApply(stack)) {
            // 1.12.2 enchantments of a stack: a map, stored enchantments for books (1.21 getEnchantmentsForCrafting)
            var enchantments = EnchantmentHelper.getEnchantments(stack);
            var newLevel = level;

            for (var enchantment : enchantments.keySet()) {
                if (enchantment == target && enchantments.get(enchantment) >= newLevel)
                    return ItemStack.EMPTY;

                // as in 1.21 (Enchantment.areCompatible)
                if (enchantment != target && enchantment.isCompatibleWith(target))
                    return ItemStack.EMPTY;
            }

            enchantments.put(target, newLevel);

            var result = Platform.copyWithCount(stack, 1);

            EnchantmentHelper.setEnchantments(enchantments, result);

            return result;
        }

        if (ItemStacks.is(stack, Items.BOOK)) {
            return ItemEnchantedBook.getEnchantedItemStack(new EnchantmentData(target, level));
        }

        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider) {
        return ItemStack.EMPTY;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return this.inputs.stream()
                .map(ICustomIngredient::toVanilla)
                .collect(Collectors.toCollection(NonNullList::create));
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.ENCHANTER.get();
    }

    @Override
    public RecipeType<? extends IEnchanterRecipe> getType() {
        return ModRecipeTypes.ENCHANTER.get();
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingInput inventory) {
        return this.getRemainingItems(inventory, this.getMaxResultEnchantmentLevel(inventory));
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingInput inventory, int level) {
        var remaining = NonNullList.withSize(inventory.size(), ItemStack.EMPTY);

        for (int i = 0; i < 2; i++) {
            var stack = inventory.getItem(i);
            var count = this.inputs.get(i).getCount() * level;

            remaining.set(i, Platform.copyWithCount(stack, stack.getCount() - count));
        }

        var stack = inventory.getItem(2);
        if (stack.getCount() > 1) {
            remaining.set(2, Platform.copyWithCount(stack, stack.getCount() - 1));
        }

        return remaining;
    }

    @Override
    public Holder<Enchantment> getEnchantment() {
        return this.enchantment;
    }

    @Override
    public int getCount(int index) {
        if (index < 0 || index >= this.inputs.size())
            return -1;

        return this.inputs.get(index).getCount();
    }

    @Override
    public int getMaxResultEnchantmentLevel(RecipeInput inventory) {
        var levels = new int[this.inputs.size()];
        var taken = new boolean[this.inputs.size()];

        for (var i = 0; i < this.inputs.size(); i++) {
            var stack = inventory.getItem(i);
            var count = 0;

            for (var input : this.inputs) {
                if (taken[i])
                    continue;

                if (input.test(stack)) {
                    count = input.getCount();
                    break;
                }
            }

            if (count == 0)
                continue;

            taken[i] = true;

            var newLevel = stack.getCount() / count;

            if (levels[i] == 0 || newLevel < levels[i]) {
                levels[i] = Math.min(newLevel, this.enchantment.value().getMaxLevel());
            }
        }

        return Arrays.stream(levels).min().orElse(0);
    }

    public static class Serializer implements RecipeSerializer<EnchanterRecipe> {
        public static final MapCodec<EnchanterRecipe> CODEC = RecordCodecBuilder.mapCodec(builder ->
                builder.group(
                        IngredientWithCount.MAP_CODEC
                                .codec()
                                .listOf()
                                .fieldOf("ingredients")
                                .flatXmap(
                                        field -> {
                                            var max = 2;
                                            var ingredients = field.toArray(new IngredientWithCount[0]);
                                            if (ingredients.length == 0) {
                                                return DataResult.error("No ingredients for enchanter recipe");
                                            } else {
                                                return ingredients.length > max
                                                        ? DataResult.error(String.format("Too many ingredients for enchanter recipe. The maximum is: %s", max))
                                                        : DataResult.success(NonNullList.from(IngredientWithCount.EMPTY, ingredients));
                                            }
                                        },
                                        DataResult::success
                                )
                                .forGetter(recipe -> recipe.inputs),
                        Codecs.ENCHANTMENT.fieldOf("enchantment").forGetter(recipe -> recipe.enchantment)
                ).apply(builder, EnchanterRecipe::new)
        );
        public static final StreamCodec<RegistryFriendlyByteBuf, EnchanterRecipe> STREAM_CODEC = StreamCodec.of(
                EnchanterRecipe.Serializer::toNetwork, EnchanterRecipe.Serializer::fromNetwork
        );

        @Override
        public MapCodec<EnchanterRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, EnchanterRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        private static EnchanterRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
            var size = buffer.readVarInt();
            var inputs = NonNullList.withSize(size, IngredientWithCount.EMPTY);

            for (int i = 0; i < size; i++) {
                inputs.set(i, IngredientWithCount.STREAM_CODEC.decode(buffer));
            }

            var enchantment = ByteBufCodecs.holderRegistry(io.github.xponer2.translator.util.Registries.ENCHANTMENT).decode(buffer);

            return new EnchanterRecipe(inputs, enchantment);
        }

        private static void toNetwork(RegistryFriendlyByteBuf buffer, EnchanterRecipe recipe) {
            buffer.writeVarInt(recipe.inputs.size());

            for (var i = 0; i < recipe.inputs.size(); i++) {
                IngredientWithCount.STREAM_CODEC.encode(buffer, recipe.inputs.get(i));
            }

            ByteBufCodecs.holderRegistry(io.github.xponer2.translator.util.Registries.ENCHANTMENT).encode(buffer, recipe.enchantment);
        }
    }
}

package com.blakebr0.mysticalagriculture.crafting.recipe;

import com.blakebr0.mysticalagriculture.init.ModRecipeSerializers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.xponer2.translator.network.ByteBufCodecs;
import io.github.xponer2.translator.recipe.CraftingBookCategory;
import io.github.xponer2.translator.recipe.CraftingInput;
import io.github.xponer2.translator.recipe.RecipeSerializer;
import io.github.xponer2.translator.recipe.ShapedRecipePattern;
import io.github.xponer2.translator.recipe.ShapelessRecipe;
import io.github.xponer2.translator.util.Codecs;

import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;

import io.github.xponer2.translator.network.RegistryFriendlyByteBuf;
import io.github.xponer2.translator.network.ByteBufCodecs;
import io.github.xponer2.translator.network.StreamCodec;
import io.github.xponer2.translator.util.Codecs;

public class FarmlandTillRecipe extends ShapelessRecipe {
    private final ItemStack result;

    public FarmlandTillRecipe(String group, ItemStack result, NonNullList<Ingredient> inputs) {
        super(group, CraftingBookCategory.MISC, result, inputs);
        this.result = result;
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingInput inventory) {
        var remaining = super.getRemainingItems(inventory);

        for (int i = 0; i < inventory.size(); i++) {
            var stack = inventory.getItem(i);

            if (stack.getItem() instanceof ItemHoe) {
                var hoe = stack.copy();

                if (hoe.isItemStackDamageable()) {
                    var damage = hoe.getItemDamage() + 1;
                    hoe.setItemDamage(damage);
                    if (damage < hoe.getMaxDamage()) {
                        remaining.set(i, hoe);
                    }
                } else {
                    remaining.set(i, hoe);
                }
            }
        }

        return remaining;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.CRAFTING_FARMLAND_TILL.get();
    }

    public static class Serializer implements RecipeSerializer<FarmlandTillRecipe> {
        public static final MapCodec<FarmlandTillRecipe> CODEC = RecordCodecBuilder.mapCodec(builder ->
                builder.group(
                        Codec.STRING.optionalFieldOf("group", "").forGetter(ShapelessRecipe::getGroup),
                        Codecs.ITEM_STACK.fieldOf("result").forGetter(recipe -> recipe.result),
                        Codecs.INGREDIENT_NONEMPTY
                                .listOf()
                                .fieldOf("ingredients")
                                .flatXmap(
                                        field -> {
                                            Ingredient[] ingredients = field.toArray(new Ingredient[0]); // Neo skip the empty check and immediately create the array.
                                            if (ingredients.length == 0) {
                                                return DataResult.error("No ingredients for shapeless recipe");
                                            } else {
                                                return ingredients.length > ShapedRecipePattern.getMaxHeight() * ShapedRecipePattern.getMaxWidth()
                                                        ? DataResult.error(String.format("Too many ingredients for shapeless recipe. The maximum is: %s", ShapedRecipePattern.getMaxHeight() * ShapedRecipePattern.getMaxWidth()))
                                                        : DataResult.success(NonNullList.from(Ingredient.EMPTY, ingredients));
                                            }
                                        },
                                        DataResult::success
                                )
                                .forGetter(ShapelessRecipe::getIngredients)
                ).apply(builder, FarmlandTillRecipe::new)
        );
        public static final StreamCodec<RegistryFriendlyByteBuf, FarmlandTillRecipe> STREAM_CODEC = StreamCodec.of(
                FarmlandTillRecipe.Serializer::toNetwork, FarmlandTillRecipe.Serializer::fromNetwork
        );

        @Override
        public MapCodec<FarmlandTillRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, FarmlandTillRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        private static FarmlandTillRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
            var group = buffer.readUtf(32767);
            int size = buffer.readVarInt();
            var inputs = NonNullList.withSize(size, Ingredient.EMPTY);

            inputs.replaceAll(ignored -> ByteBufCodecs.INGREDIENT.decode(buffer));

            var result = ByteBufCodecs.ITEM_STACK.decode(buffer);

            return new FarmlandTillRecipe(group, result, inputs);
        }

        private static void toNetwork(RegistryFriendlyByteBuf buffer, FarmlandTillRecipe recipe) {
            buffer.writeUtf(recipe.getGroup());
            buffer.writeVarInt(recipe.getIngredients().size());

            for (var ingredient : recipe.getIngredients()) {
                ByteBufCodecs.INGREDIENT.encode(buffer, ingredient);
            }

            ByteBufCodecs.ITEM_STACK.encode(buffer, recipe.result);
        }
    }
}

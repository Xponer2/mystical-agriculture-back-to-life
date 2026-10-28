package com.blakebr0.mysticalagriculture.crafting.recipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import com.blakebr0.cucumber.crafting.ingredient.IngredientWithCount;
import com.blakebr0.mysticalagriculture.api.crafting.ISouliumSpawnerRecipe;
import com.blakebr0.mysticalagriculture.init.ModRecipeSerializers;
import com.blakebr0.mysticalagriculture.init.ModRecipeTypes;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.xponer2.translator.recipe.CraftingInput;
import io.github.xponer2.translator.recipe.RecipeSerializer;
import io.github.xponer2.translator.recipe.RecipeType;
import io.github.xponer2.translator.recipe.ingredient.ICustomIngredient;
import io.github.xponer2.translator.registry.HolderLookup;
import io.github.xponer2.translator.util.random.Weight;
import io.github.xponer2.translator.util.random.WeightedEntry;
import io.github.xponer2.translator.util.random.WeightedRandomList;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

import io.github.xponer2.translator.util.Registries;
import io.github.xponer2.translator.registry.EntityType;
import io.github.xponer2.translator.network.RegistryFriendlyByteBuf;
import io.github.xponer2.translator.network.ByteBufCodecs;
import io.github.xponer2.translator.network.StreamCodec;
import io.github.xponer2.translator.util.BuiltInRegistries;

public class SouliumSpawnerRecipe implements ISouliumSpawnerRecipe {
    private final NonNullList<IngredientWithCount> inputs;
    private final WeightedRandomList<WeightedEntry.Wrapper<EntityType<?>>> entityTypes;

    public SouliumSpawnerRecipe(IngredientWithCount input, WeightedRandomList<WeightedEntry.Wrapper<EntityType<?>>> entityTypes) {
        this.inputs = NonNullList.from(IngredientWithCount.EMPTY, input);
        this.entityTypes = entityTypes;
    }

    @Override
    public boolean matches(CraftingInput inventory, World level) {
        if (this.inputs.size() != inventory.ingredientCount())
            return false;

        var stack = inventory.getItem(0);
        return this.inputs.get(0).test(stack);
    }

    @Override
    public ItemStack assemble(CraftingInput inventory, HolderLookup.Provider lookup) {
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
        return ModRecipeSerializers.SOULIUM_SPAWNER.get();
    }

    @Override
    public RecipeType<? extends ISouliumSpawnerRecipe> getType() {
        return ModRecipeTypes.SOULIUM_SPAWNER.get();
    }

    @Override
    public WeightedRandomList<WeightedEntry.Wrapper<EntityType<?>>> getEntityTypes() {
        return this.entityTypes;
    }

    @Override
    public EntityType<?> getFirstEntityType() {
        return this.entityTypes.unwrap().get(0).data();
    }

    @Override
    public Optional<WeightedEntry.Wrapper<EntityType<?>>> getRandomEntityType(Random random) {
        return this.entityTypes.getRandom(random);
    }

    @Override
    public int getCount(int index) {
        if (index < 0 || index >= this.inputs.size())
            return -1;

        return this.inputs.get(index).getCount();
    }

    public static class Serializer implements RecipeSerializer<SouliumSpawnerRecipe> {
        private static final StreamCodec<RegistryFriendlyByteBuf, EntityType<?>> ENTITY_TYPE_STREAM_CODEC = ByteBufCodecs.registry(Registries.ENTITY_TYPE);
        public static final MapCodec<SouliumSpawnerRecipe> CODEC = RecordCodecBuilder.mapCodec(builder ->
                builder.group(
                        IngredientWithCount.MAP_CODEC.fieldOf("input").forGetter(recipe -> recipe.inputs.get(0)),
                        WeightedRandomList.codec(
                                RecordCodecBuilder.<WeightedEntry.Wrapper<EntityType<?>>>create(wrapper ->
                                        wrapper.group(
                                                BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("entity").forGetter(WeightedEntry.Wrapper::data),
                                                Weight.CODEC.fieldOf("weight").forGetter(WeightedEntry.Wrapper::weight)
                                        ).apply(wrapper, WeightedEntry.Wrapper::new)
                                )
                        ).fieldOf("entities").forGetter(recipe -> recipe.entityTypes)
                ).apply(builder, SouliumSpawnerRecipe::new)
        );
        public static final StreamCodec<RegistryFriendlyByteBuf, SouliumSpawnerRecipe> STREAM_CODEC = StreamCodec.of(
                SouliumSpawnerRecipe.Serializer::toNetwork, SouliumSpawnerRecipe.Serializer::fromNetwork
        );

        @Override
        public MapCodec<SouliumSpawnerRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, SouliumSpawnerRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        private static SouliumSpawnerRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
            var input = IngredientWithCount.STREAM_CODEC.decode(buffer);
            var entities = buffer.readVarInt();

            List<WeightedEntry.Wrapper<EntityType<?>>> entityTypes = new ArrayList<>();

            for (int i = 0; i < entities; i++) {
                var entityType = ENTITY_TYPE_STREAM_CODEC.decode(buffer);
                var entityTypeWeight = buffer.readVarInt();

                entityTypes.add(WeightedEntry.wrap(entityType, entityTypeWeight));
            }

            return new SouliumSpawnerRecipe(input, WeightedRandomList.create(entityTypes));
        }

        private static void toNetwork(RegistryFriendlyByteBuf buffer, SouliumSpawnerRecipe recipe) {
            IngredientWithCount.STREAM_CODEC.encode(buffer, recipe.inputs.get(0));

            buffer.writeVarInt(recipe.entityTypes.unwrap().size());

            for (var entityType : recipe.entityTypes.unwrap()) {
                ENTITY_TYPE_STREAM_CODEC.encode(buffer, entityType.data());
                buffer.writeVarInt(entityType.weight().asInt());
            }
        }
    }
}

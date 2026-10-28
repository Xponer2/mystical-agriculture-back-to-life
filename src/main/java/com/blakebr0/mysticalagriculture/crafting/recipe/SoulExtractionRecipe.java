package com.blakebr0.mysticalagriculture.crafting.recipe;

import com.blakebr0.cucumber.helper.StackHelper;
import com.blakebr0.mysticalagriculture.api.crafting.ISoulExtractionRecipe;
import com.blakebr0.mysticalagriculture.api.soul.MobSoulType;
import com.blakebr0.mysticalagriculture.api.util.MobSoulUtils;
import com.blakebr0.mysticalagriculture.init.ModItems;
import com.blakebr0.mysticalagriculture.init.ModRecipeSerializers;
import com.blakebr0.mysticalagriculture.init.ModRecipeTypes;
import com.blakebr0.mysticalagriculture.registry.MobSoulTypeRegistry;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.xponer2.translator.network.ByteBufCodecs;
import io.github.xponer2.translator.recipe.CraftingInput;
import io.github.xponer2.translator.recipe.RecipeSerializer;
import io.github.xponer2.translator.recipe.RecipeType;
import io.github.xponer2.translator.registry.HolderLookup;
import io.github.xponer2.translator.util.Codecs;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import io.github.xponer2.translator.network.RegistryFriendlyByteBuf;
import io.github.xponer2.translator.network.ByteBufCodecs;
import io.github.xponer2.translator.network.StreamCodec;
import io.github.xponer2.translator.util.Codecs;
import io.github.xponer2.translator.util.ItemStacks;

public class SoulExtractionRecipe implements ISoulExtractionRecipe {
    private final NonNullList<Ingredient> inputs;
    private final MobSoulType type;
    private final double souls;
    private final Result result;

    public SoulExtractionRecipe(Ingredient input, Result result) {
        this.inputs = NonNullList.from(Ingredient.EMPTY, input);
        this.type = MobSoulTypeRegistry.getInstance().getMobSoulTypeById(result.type);
        this.souls = result.souls;
        this.result = result;
    }

    @Override
    public boolean matches(CraftingInput inventory, World level) {
        if (inventory.size() < 3)
            return false;

        var input = inventory.getItem(0);
        if (!this.inputs.get(0).test(input))
            return false;

        var output = inventory.getItem(2);
        if (!ItemStacks.is(output, this.result.stack.getItem()))
            return false;

        return MobSoulUtils.canAddTypeToJar(output, this.type) && !MobSoulUtils.isJarFull(output);
    }

    @Override
    public ItemStack assemble(CraftingInput inventory, HolderLookup.Provider provider) {
        var stack = inventory.getItem(2);
        var jar = StackHelper.withSize(stack, 1, false);

        MobSoulUtils.addSoulsToJar(jar, this.type, this.souls);

        return jar;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider) {
        return this.result.stack;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return this.inputs;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.SOUL_EXTRACTION.get();
    }

    @Override
    public RecipeType<? extends ISoulExtractionRecipe> getType() {
        return ModRecipeTypes.SOUL_EXTRACTION.get();
    }

    @Override
    public MobSoulType getMobSoulType() {
        return this.type;
    }

    @Override
    public double getSouls() {
        return this.souls;
    }

    public static class Serializer implements RecipeSerializer<SoulExtractionRecipe> {
        public static final MapCodec<SoulExtractionRecipe> CODEC = RecordCodecBuilder.mapCodec(builder ->
                builder.group(
                        Codecs.INGREDIENT_NONEMPTY.fieldOf("input").forGetter(recipe -> recipe.inputs.get(0)),
                        Result.CODEC.fieldOf("result").forGetter(recipe -> recipe.result)
                ).apply(builder, SoulExtractionRecipe::new)
        );
        public static final StreamCodec<RegistryFriendlyByteBuf, SoulExtractionRecipe> STREAM_CODEC = StreamCodec.of(
                SoulExtractionRecipe.Serializer::toNetwork, SoulExtractionRecipe.Serializer::fromNetwork
        );

        @Override
        public MapCodec<SoulExtractionRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, SoulExtractionRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        private static SoulExtractionRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
            var input = ByteBufCodecs.INGREDIENT.decode(buffer);
            var result = Result.STREAM_CODEC.decode(buffer);

            return new SoulExtractionRecipe(input, result);
        }

        private static void toNetwork(RegistryFriendlyByteBuf buffer, SoulExtractionRecipe recipe) {
            ByteBufCodecs.INGREDIENT.encode(buffer, recipe.inputs.get(0));
            Result.STREAM_CODEC.encode(buffer, recipe.result);
        }
    }

    public static class Result {
        public static final MapCodec<Result> CODEC = RecordCodecBuilder.mapCodec(builder ->
               builder.group(
                       Codecs.RESOURCE_LOCATION.fieldOf("type").forGetter(result -> result.type),
                       Codec.DOUBLE.fieldOf("souls").forGetter(result -> result.souls)
               ).apply(builder, Result::new)
        );
        public static final StreamCodec<RegistryFriendlyByteBuf, Result> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.RESOURCE_LOCATION,
                result -> result.type,
                ByteBufCodecs.DOUBLE,
                result -> result.souls,
                Result::new
        );

        public final ResourceLocation type;
        public final double souls;
        public final ItemStack stack;

        public Result(ResourceLocation type, double souls) {
            this.type = type;
            this.souls = souls;
            this.stack = MobSoulUtils.getSoulJar(MobSoulTypeRegistry.getInstance().getMobSoulTypeById(type), souls, ModItems.SOUL_JAR.get());
        }
    }
}

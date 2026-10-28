package com.blakebr0.mysticalagriculture.crafting.ingredient;

import java.util.stream.Stream;

import io.github.xponer2.translator.component.DataComponentMap;
import io.github.xponer2.translator.recipe.ingredient.ICustomIngredient;
import io.github.xponer2.translator.recipe.ingredient.IngredientType;
import io.github.xponer2.translator.util.Codecs;
import io.github.xponer2.translator.util.StringRepresentable;
import com.blakebr0.mysticalagriculture.init.ModIngredientTypes;
import com.blakebr0.mysticalagriculture.registry.CropRegistry;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.jetbrains.annotations.Nullable;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

import io.github.xponer2.translator.util.Codecs;
import io.github.xponer2.translator.component.StackComponents;
import io.github.xponer2.translator.util.ItemStacks;

public class CropComponentIngredient implements ICustomIngredient {
    public static final MapCodec<CropComponentIngredient> CODEC = RecordCodecBuilder.mapCodec(builder ->
            builder.group(
                    Codecs.RESOURCE_LOCATION.fieldOf("crop").forGetter(ingredient -> ingredient.crop),
                    ComponentType.CODEC.fieldOf("component").forGetter(ingredient -> ingredient.type)
            ).apply(builder, CropComponentIngredient::new)
    );

    private final ResourceLocation crop;
    private final ComponentType type;

    private ItemStack[] stacks;

    public CropComponentIngredient(ResourceLocation crop, ComponentType type) {
        this.crop = crop;
        this.type = type;
    }

    @Override
    public boolean test(@Nullable ItemStack input) {
        if (input == null)
            return false;

        var stack = this.getItems().filter(s -> ItemStacks.is(input, s.getItem())).findFirst();

        return stack.filter(s -> this.testComponents(StackComponents.getComponents(s), input)).isPresent();
    }

    @Override
    public Stream<ItemStack> getItems() {
        if (stacks == null) {
            this.initMatchingStacks();
        }

        return Stream.of(this.stacks);
    }

    @Override
    public boolean isSimple() {
        return false;
    }

    @Override
    public IngredientType<?> getType() {
        return ModIngredientTypes.CROP_COMPONENT.get();
    }

    private void initMatchingStacks() {
        var crop = CropRegistry.getInstance().getCropById(this.crop);

        this.stacks = switch (this.type) {
            case ESSENCE -> new ItemStack[] { new ItemStack(crop.getTier().getEssence()) };
            case SEED -> new ItemStack[] { new ItemStack(crop.getType().getCraftingSeed()) };
            case MATERIAL -> crop.getCraftingMaterial().getMatchingStacks();
        };
    }

    private boolean testComponents(DataComponentMap components, ItemStack stack) {
        for (var entry : components) {
            var type = entry.type();
            var value = entry.value();
            if (!StackComponents.has(stack, type) || !value.equals(StackComponents.get(stack, type))) {
                return false;
            }
        }

        return true;
    }

    public static Ingredient of(ResourceLocation crop, ComponentType type) {
        return new CropComponentIngredient(crop, type).toVanilla();
    }

    public enum ComponentType implements StringRepresentable {
        ESSENCE("essence"),
        SEED("seed"),
        MATERIAL("material");

        public static final Codec<ComponentType> CODEC = StringRepresentable.fromEnum(ComponentType::values);

        public final String name;

        ComponentType(String name) {
            this.name = name;
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }
    }
}

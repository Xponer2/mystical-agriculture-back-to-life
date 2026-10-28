package com.blakebr0.mysticalagriculture.init;

import java.util.EnumMap;
import java.util.List;

import com.blakebr0.mysticalagriculture.MysticalAgriculture;
import com.google.common.collect.ImmutableList;
import io.github.xponer2.translator.item.ArmorMaterialData;
import io.github.xponer2.translator.item.ArmorType;
import io.github.xponer2.translator.recipe.ingredient.Ingredients;
import io.github.xponer2.translator.registry.DeferredHolder;
import io.github.xponer2.translator.registry.DeferredRegister;
import io.github.xponer2.translator.util.Util;

import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.crafting.Ingredient;

import io.github.xponer2.translator.util.Registries;

public final class ModArmorMaterials {
    public static final DeferredRegister<ArmorMaterialData> REGISTRY = DeferredRegister.create(Registries.ARMOR_MATERIAL, MysticalAgriculture.MOD_ID);

    public static final DeferredHolder<ArmorMaterialData, ArmorMaterialData> INFERIUM = REGISTRY.register("inferium", () -> new ArmorMaterialData(
            Util.make(new EnumMap<>(ArmorType.class), map -> {
                map.put(ArmorType.BOOTS, 4);
                map.put(ArmorType.LEGGINGS, 6);
                map.put(ArmorType.CHESTPLATE, 8);
                map.put(ArmorType.HELMET, 3);
            }),
            12, SoundEvents.ITEM_ARMOR_EQUIP_GOLD,
            () -> Ingredients.of(ModItems.INFERIUM_INGOT.get()),
            ImmutableList.of(
                    new ArmorMaterialData.Layer(MysticalAgriculture.resource("inferium"), "", false)
            ),
            2.0F, 0.0F
    ));
    public static final DeferredHolder<ArmorMaterialData, ArmorMaterialData> PRUDENTIUM = REGISTRY.register("prudentium", () -> new ArmorMaterialData(
            Util.make(new EnumMap<>(ArmorType.class), map -> {
                map.put(ArmorType.BOOTS, 4);
                map.put(ArmorType.LEGGINGS, 7);
                map.put(ArmorType.CHESTPLATE, 8);
                map.put(ArmorType.HELMET, 4);
            }),
            14, SoundEvents.ITEM_ARMOR_EQUIP_GOLD,
            () -> Ingredients.of(ModItems.PRUDENTIUM_INGOT.get()),
            ImmutableList.of(
                    new ArmorMaterialData.Layer(MysticalAgriculture.resource("prudentium"), "", false)
            ),
            2.25F, 0.0F
    ));
    public static final DeferredHolder<ArmorMaterialData, ArmorMaterialData> TERTIUM = REGISTRY.register("tertium", () -> new ArmorMaterialData(
            Util.make(new EnumMap<>(ArmorType.class), map -> {
                map.put(ArmorType.BOOTS, 5);
                map.put(ArmorType.LEGGINGS, 8);
                map.put(ArmorType.CHESTPLATE, 9);
                map.put(ArmorType.HELMET, 4);
            }),
            16, SoundEvents.ITEM_ARMOR_EQUIP_GOLD,
            () -> Ingredients.of(ModItems.TERTIUM_INGOT.get()),
            ImmutableList.of(
                    new ArmorMaterialData.Layer(MysticalAgriculture.resource("tertium"), "", false)
            ),
            2.5F, 0.0F
    ));
    public static final DeferredHolder<ArmorMaterialData, ArmorMaterialData> IMPERIUM = REGISTRY.register("imperium", () -> new ArmorMaterialData(
            Util.make(new EnumMap<>(ArmorType.class), map -> {
                map.put(ArmorType.BOOTS, 5);
                map.put(ArmorType.LEGGINGS, 8);
                map.put(ArmorType.CHESTPLATE, 9);
                map.put(ArmorType.HELMET, 5);
            }),
            18, SoundEvents.ITEM_ARMOR_EQUIP_GOLD,
            () -> Ingredients.of(ModItems.IMPERIUM_INGOT.get()),
            ImmutableList.of(
                    new ArmorMaterialData.Layer(MysticalAgriculture.resource("imperium"), "", false)
            ),
            2.75F, 0.0F
    ));
    public static final DeferredHolder<ArmorMaterialData, ArmorMaterialData> SUPREMIUM = REGISTRY.register("supremium", () -> new ArmorMaterialData(
            Util.make(new EnumMap<>(ArmorType.class), map -> {
                map.put(ArmorType.BOOTS, 6);
                map.put(ArmorType.LEGGINGS, 8);
                map.put(ArmorType.CHESTPLATE, 10);
                map.put(ArmorType.HELMET, 5);
            }),
            20, SoundEvents.ITEM_ARMOR_EQUIP_GOLD,
            () -> Ingredients.of(ModItems.SUPREMIUM_INGOT.get()),
            ImmutableList.of(
                    new ArmorMaterialData.Layer(MysticalAgriculture.resource("supremium"), "", false)
            ),
            3.0F, 0.0F
    ));
    public static final DeferredHolder<ArmorMaterialData, ArmorMaterialData> AWAKENED_SUPREMIUM = REGISTRY.register("awakened_supremium", () -> new ArmorMaterialData(
            Util.make(new EnumMap<>(ArmorType.class), map -> {
                map.put(ArmorType.BOOTS, 8);
                map.put(ArmorType.LEGGINGS, 10);
                map.put(ArmorType.CHESTPLATE, 12);
                map.put(ArmorType.HELMET, 6);
            }),
            22, SoundEvents.ITEM_ARMOR_EQUIP_GOLD,
            () -> Ingredients.of(ModItems.AWAKENED_SUPREMIUM_INGOT.get()),
            ImmutableList.of(
                    new ArmorMaterialData.Layer(MysticalAgriculture.resource("awakened_supremium"), "", false)
            ),
            3.5F, 0.0F
    ));
}

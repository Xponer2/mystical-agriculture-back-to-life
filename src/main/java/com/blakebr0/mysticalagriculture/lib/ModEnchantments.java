package com.blakebr0.mysticalagriculture.lib;

import com.blakebr0.mysticalagriculture.MysticalAgriculture;
import io.github.xponer2.translator.registry.DeferredHolder;

import net.minecraft.enchantment.Enchantment;

import io.github.xponer2.translator.util.Registries;

public final class ModEnchantments {
    public static final DeferredHolder<Enchantment, Enchantment> MYSTICAL_ENLIGHTENMENT = DeferredHolder.create(Registries.ENCHANTMENT, MysticalAgriculture.resource("mystical_enlightenment"));
    public static final DeferredHolder<Enchantment, Enchantment> SOUL_SIPHONER = DeferredHolder.create(Registries.ENCHANTMENT, MysticalAgriculture.resource("soul_siphoner"));
}

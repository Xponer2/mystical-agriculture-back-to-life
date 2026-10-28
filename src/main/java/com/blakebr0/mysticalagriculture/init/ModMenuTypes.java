package com.blakebr0.mysticalagriculture.init;

import com.blakebr0.mysticalagriculture.MysticalAgriculture;
import com.blakebr0.mysticalagriculture.container.EnchanterContainer;
import com.blakebr0.mysticalagriculture.container.EssenceFurnaceContainer;
import com.blakebr0.mysticalagriculture.container.HarvesterContainer;
import com.blakebr0.mysticalagriculture.container.ReprocessorContainer;
import com.blakebr0.mysticalagriculture.container.SoulExtractorContainer;
import com.blakebr0.mysticalagriculture.container.SouliumSpawnerContainer;
import com.blakebr0.mysticalagriculture.container.TinkeringTableContainer;
import io.github.xponer2.translator.menu.IContainerFactory;
import io.github.xponer2.translator.menu.MenuType;
import io.github.xponer2.translator.registry.DeferredHolder;
import io.github.xponer2.translator.registry.DeferredRegister;
import io.github.xponer2.translator.util.FeatureFlagSet;

import io.github.xponer2.translator.util.Registries;

public final class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> REGISTRY = DeferredRegister.create(Registries.MENU, MysticalAgriculture.MOD_ID);

    public static final DeferredHolder<MenuType<?>, MenuType<TinkeringTableContainer>> TINKERING_TABLE = REGISTRY.register("tinkering_table", () -> new MenuType<>((IContainerFactory<TinkeringTableContainer>) TinkeringTableContainer::create, FeatureFlagSet.of()));
    public static final DeferredHolder<MenuType<?>, MenuType<EnchanterContainer>> ENCHANTER = REGISTRY.register("enchanter", () -> new MenuType<>((IContainerFactory<EnchanterContainer>) EnchanterContainer::create, FeatureFlagSet.of()));
    public static final DeferredHolder<MenuType<?>, MenuType<EssenceFurnaceContainer>> FURNACE = REGISTRY.register("furnace", () -> new MenuType<>((IContainerFactory<EssenceFurnaceContainer>) EssenceFurnaceContainer::create, FeatureFlagSet.of()));
    public static final DeferredHolder<MenuType<?>, MenuType<ReprocessorContainer>> REPROCESSOR = REGISTRY.register("reprocessor", () -> new MenuType<>((IContainerFactory<ReprocessorContainer>) ReprocessorContainer::create, FeatureFlagSet.of()));
    public static final DeferredHolder<MenuType<?>, MenuType<SoulExtractorContainer>> SOUL_EXTRACTOR = REGISTRY.register("soul_extractor", () -> new MenuType<>((IContainerFactory<SoulExtractorContainer>) SoulExtractorContainer::create, FeatureFlagSet.of()));
    public static final DeferredHolder<MenuType<?>, MenuType<HarvesterContainer>> HARVESTER = REGISTRY.register("harvester", () -> new MenuType<>((IContainerFactory<HarvesterContainer>) HarvesterContainer::create, FeatureFlagSet.of()));
    public static final DeferredHolder<MenuType<?>, MenuType<SouliumSpawnerContainer>> SOULIUM_SPAWNER = REGISTRY.register("soulium_spawner", () -> new MenuType<>((IContainerFactory<SouliumSpawnerContainer>) SouliumSpawnerContainer::create, FeatureFlagSet.of()));
}

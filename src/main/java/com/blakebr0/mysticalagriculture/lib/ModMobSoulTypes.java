package com.blakebr0.mysticalagriculture.lib;

import java.util.Arrays;
import java.util.Set;

import com.blakebr0.mysticalagriculture.MysticalAgriculture;
import com.blakebr0.mysticalagriculture.api.registry.IMobSoulTypeRegistry;
import com.blakebr0.mysticalagriculture.api.soul.MobSoulType;
import com.google.common.collect.Sets;
import io.github.xponer2.translator.util.ModList;

import net.minecraft.util.ResourceLocation;

public final class ModMobSoulTypes {
    private static final boolean DEBUG = false;

    private static final Set<ResourceLocation> FISH_IDS = Sets.newHashSet(io.github.xponer2.translator.util.ResourceLocations.parse("minecraft:cod"), io.github.xponer2.translator.util.ResourceLocations.parse("minecraft:salmon"), io.github.xponer2.translator.util.ResourceLocations.parse("minecraft:tropical_fish"), io.github.xponer2.translator.util.ResourceLocations.parse("minecraft:pufferfish"));
    private static final Set<ResourceLocation> SLIME_IDS = Sets.newHashSet(io.github.xponer2.translator.util.ResourceLocations.parse("minecraft:slime"), io.github.xponer2.translator.util.ResourceLocations.parse("tconstruct:earth_slime"));
    private static final Set<ResourceLocation> ZOMBIE_IDS = Sets.newHashSet(io.github.xponer2.translator.util.ResourceLocations.parse("minecraft:zombie"), io.github.xponer2.translator.util.ResourceLocations.parse("minecraft:zombie_villager"), io.github.xponer2.translator.util.ResourceLocations.parse("minecraft:husk"), io.github.xponer2.translator.util.ResourceLocations.parse("minecraft:drowned"));
    private static final Set<ResourceLocation> SKELETON_IDS = Sets.newHashSet(io.github.xponer2.translator.util.ResourceLocations.parse("minecraft:skeleton"), io.github.xponer2.translator.util.ResourceLocations.parse("minecraft:stray"));
    private static final Set<ResourceLocation> SPIDER_IDS = Sets.newHashSet(io.github.xponer2.translator.util.ResourceLocations.parse("minecraft:spider"), io.github.xponer2.translator.util.ResourceLocations.parse("minecraft:cave_spider"));

    public static final MobSoulType PIG = new MobSoulType(MysticalAgriculture.resource("pig"), io.github.xponer2.translator.util.ResourceLocations.parse("minecraft:pig"), 8, 15771042);
    public static final MobSoulType CHICKEN = new MobSoulType(MysticalAgriculture.resource("chicken"), io.github.xponer2.translator.util.ResourceLocations.parse("minecraft:chicken"), 8, 10592673);
    public static final MobSoulType COW = new MobSoulType(MysticalAgriculture.resource("cow"), io.github.xponer2.translator.util.ResourceLocations.parse("minecraft:cow"), 8, 4470310);
    public static final MobSoulType SHEEP = new MobSoulType(MysticalAgriculture.resource("sheep"), io.github.xponer2.translator.util.ResourceLocations.parse("minecraft:sheep"), 8, 15198183);
    public static final MobSoulType SQUID = new MobSoulType(MysticalAgriculture.resource("squid"), io.github.xponer2.translator.util.ResourceLocations.parse("minecraft:squid"), 6, 2243405);
    public static final MobSoulType FISH = new MobSoulType(MysticalAgriculture.resource("fish"), FISH_IDS, "fish", 6, 12691306);
    public static final MobSoulType SLIME = new MobSoulType(MysticalAgriculture.resource("slime"), SLIME_IDS, "slime", 12, 5349438);
    public static final MobSoulType TURTLE = new MobSoulType(MysticalAgriculture.resource("turtle"), io.github.xponer2.translator.util.ResourceLocations.parse("minecraft:turtle"), 6, 44975);
    public static final MobSoulType ARMADILLO = new MobSoulType(MysticalAgriculture.resource("armadillo"), io.github.xponer2.translator.util.ResourceLocations.parse("minecraft:armadillo"), 6, 0xb67b76);
    public static final MobSoulType ZOMBIE = new MobSoulType(MysticalAgriculture.resource("zombie"), ZOMBIE_IDS, "zombie", 10, 7969893);
    public static final MobSoulType SKELETON = new MobSoulType(MysticalAgriculture.resource("skeleton"), SKELETON_IDS, "skeleton", 10, 12698049);
    public static final MobSoulType CREEPER = new MobSoulType(MysticalAgriculture.resource("creeper"), io.github.xponer2.translator.util.ResourceLocations.parse("minecraft:creeper"), 10, 894731);
    public static final MobSoulType SPIDER = new MobSoulType(MysticalAgriculture.resource("spider"), SPIDER_IDS, "spider", 10, 3419431);
    public static final MobSoulType PHANTOM = new MobSoulType(MysticalAgriculture.resource("phantom"), io.github.xponer2.translator.util.ResourceLocations.parse("minecraft:phantom"), 6, 0x374377);
    public static final MobSoulType RABBIT = new MobSoulType(MysticalAgriculture.resource("rabbit"), io.github.xponer2.translator.util.ResourceLocations.parse("minecraft:rabbit"), 6, 10051392);
    public static final MobSoulType BREEZE = new MobSoulType(MysticalAgriculture.resource("breeze"), io.github.xponer2.translator.util.ResourceLocations.parse("minecraft:breeze"), 4, 0x7982c7);
    public static final MobSoulType BLAZE = new MobSoulType(MysticalAgriculture.resource("blaze"), io.github.xponer2.translator.util.ResourceLocations.parse("minecraft:blaze"), 10, 16167425);
    public static final MobSoulType GHAST = new MobSoulType(MysticalAgriculture.resource("ghast"), io.github.xponer2.translator.util.ResourceLocations.parse("minecraft:ghast"), 4, 16382457);
    public static final MobSoulType ENDERMAN = new MobSoulType(MysticalAgriculture.resource("enderman"), io.github.xponer2.translator.util.ResourceLocations.parse("minecraft:enderman"), 8, 1447446);
    public static final MobSoulType WITHER = new MobSoulType(MysticalAgriculture.resource("wither_skeleton"), io.github.xponer2.translator.util.ResourceLocations.parse("minecraft:wither_skeleton"), 8, 1315860);

    // THERMAL SERIES
    public static final MobSoulType BLIZZ = new MobSoulType(MysticalAgriculture.resource("blizz"), io.github.xponer2.translator.util.ResourceLocations.parse("thermal:blizz"), 6, 0x7BD4FF);
    public static final MobSoulType BLITZ = new MobSoulType(MysticalAgriculture.resource("blitz"), io.github.xponer2.translator.util.ResourceLocations.parse("thermal:blitz"), 6, 0xECFEFC);
    public static final MobSoulType BASALZ = new MobSoulType(MysticalAgriculture.resource("basalz"), io.github.xponer2.translator.util.ResourceLocations.parse("thermal:basalz"), 6, 0x363840);

    public static void onRegisterMobSoulTypes(IMobSoulTypeRegistry registry) {
        registry.register(PIG);
        registry.register(CHICKEN);
        registry.register(COW);
        registry.register(SHEEP);
        registry.register(SQUID);
        registry.register(FISH);
        registry.register(SLIME);
        registry.register(notOn1122(TURTLE));
        registry.register(notOn1122(ARMADILLO));
        registry.register(ZOMBIE);
        registry.register(SKELETON);
        registry.register(CREEPER);
        registry.register(SPIDER);
        registry.register(notOn1122(PHANTOM));
        registry.register(RABBIT);
        registry.register(notOn1122(BREEZE));
        registry.register(BLAZE);
        registry.register(GHAST);
        registry.register(ENDERMAN);
        registry.register(WITHER);

        registry.register(withRequiredMods(BLIZZ, "thermal"));
        registry.register(withRequiredMods(BLITZ, "thermal"));
        registry.register(withRequiredMods(BASALZ, "thermal"));
    }

    private static MobSoulType withRequiredMods(MobSoulType type, String... mods) {
        if (DEBUG) return type;

        boolean enabled = Arrays.stream(mods).anyMatch(ModList.get()::isLoaded);
        return type.setEnabled(enabled);
    }

    // 1.12.2: mobs that don't exist in 1.12.2
    private static MobSoulType notOn1122(MobSoulType type) {
        return type.setEnabled(false);
    }
}

package com.blakebr0.mysticalagriculture.init;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import com.blakebr0.cucumber.item.BaseItem;
import com.blakebr0.cucumber.item.tool.BaseScytheItem;
import com.blakebr0.cucumber.item.tool.BaseSickleItem;
import com.blakebr0.mysticalagriculture.MysticalAgriculture;
import com.blakebr0.mysticalagriculture.api.crop.CropTier;
import com.blakebr0.mysticalagriculture.api.machine.MachineUpgradeTier;
import com.blakebr0.mysticalagriculture.item.EssenceItem;
import com.blakebr0.mysticalagriculture.item.EssenceWateringCanItem;
import com.blakebr0.mysticalagriculture.item.ExperienceCapsuleItem;
import com.blakebr0.mysticalagriculture.item.ExperienceDropletItem;
import com.blakebr0.mysticalagriculture.item.FertilizedEssenceItem;
import com.blakebr0.mysticalagriculture.item.InfusionCrystalItem;
import com.blakebr0.mysticalagriculture.item.MachineUpgradeItem;
import com.blakebr0.mysticalagriculture.item.MasterInfusionCrystalItem;
import com.blakebr0.mysticalagriculture.item.MysticalFertilizerItem;
import com.blakebr0.mysticalagriculture.item.SoulJarItem;
import com.blakebr0.mysticalagriculture.item.SouliumDaggerItem;
import com.blakebr0.mysticalagriculture.item.WandItem;
import com.blakebr0.mysticalagriculture.item.WateringCanItem;
import com.blakebr0.mysticalagriculture.item.armor.EssenceBootsItem;
import com.blakebr0.mysticalagriculture.item.armor.EssenceChestplateItem;
import com.blakebr0.mysticalagriculture.item.armor.EssenceHelmetItem;
import com.blakebr0.mysticalagriculture.item.armor.EssenceLeggingsItem;
import com.blakebr0.mysticalagriculture.item.tool.EssenceAxeItem;
import com.blakebr0.mysticalagriculture.item.tool.EssenceBowItem;
import com.blakebr0.mysticalagriculture.item.tool.EssenceCrossbowItem;
import com.blakebr0.mysticalagriculture.item.tool.EssenceFishingRodItem;
import com.blakebr0.mysticalagriculture.item.tool.EssenceHoeItem;
import com.blakebr0.mysticalagriculture.item.tool.EssencePickaxeItem;
import com.blakebr0.mysticalagriculture.item.tool.EssenceScytheItem;
import com.blakebr0.mysticalagriculture.item.tool.EssenceShearsItem;
import com.blakebr0.mysticalagriculture.item.tool.EssenceShovelItem;
import com.blakebr0.mysticalagriculture.item.tool.EssenceSickleItem;
import com.blakebr0.mysticalagriculture.item.tool.EssenceStaffItem;
import com.blakebr0.mysticalagriculture.item.tool.EssenceSwordItem;
import com.blakebr0.mysticalagriculture.lib.ModItemTier;
import com.blakebr0.mysticalagriculture.registry.AugmentRegistry;
import com.blakebr0.mysticalagriculture.registry.CropRegistry;
import io.github.xponer2.translator.item.Tiers;
import io.github.xponer2.translator.registry.DeferredHolder;
import io.github.xponer2.translator.registry.RegisterEvent;

import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import io.github.xponer2.translator.util.Registries;
import io.github.xponer2.translator.util.BuiltInRegistries;

public final class ModItems {
    public static final List<Supplier<ItemBlock>> BLOCK_ENTRIES = new ArrayList<>();
    public static final Map<DeferredHolder<Item, Item>, Supplier<Item>> ENTRIES = new LinkedHashMap<>();
    public static final Map<DeferredHolder<Item, Item>, Supplier<Item>> GEAR_ENTRIES = new LinkedHashMap<>();

    public static final DeferredHolder<Item, Item> PROSPERITY_SHARD = register("prosperity_shard");
    public static final DeferredHolder<Item, Item> INFERIUM_ESSENCE = register("inferium_essence", () -> new EssenceItem(CropTier.ONE));
    public static final DeferredHolder<Item, Item> PRUDENTIUM_ESSENCE = register("prudentium_essence", () -> new EssenceItem(CropTier.TWO));
    public static final DeferredHolder<Item, Item> TERTIUM_ESSENCE = register("tertium_essence", () -> new EssenceItem(CropTier.THREE));
    public static final DeferredHolder<Item, Item> IMPERIUM_ESSENCE = register("imperium_essence", () -> new EssenceItem(CropTier.FOUR));
    public static final DeferredHolder<Item, Item> SUPREMIUM_ESSENCE = register("supremium_essence", () -> new EssenceItem(CropTier.FIVE));
    public static final DeferredHolder<Item, Item> AWAKENED_SUPREMIUM_ESSENCE = register("awakened_supremium_essence");
    public static final DeferredHolder<Item, Item> PROSPERITY_INGOT = register("prosperity_ingot");
    public static final DeferredHolder<Item, Item> INFERIUM_INGOT = register("inferium_ingot");
    public static final DeferredHolder<Item, Item> PRUDENTIUM_INGOT = register("prudentium_ingot");
    public static final DeferredHolder<Item, Item> TERTIUM_INGOT = register("tertium_ingot");
    public static final DeferredHolder<Item, Item> IMPERIUM_INGOT = register("imperium_ingot");
    public static final DeferredHolder<Item, Item> SUPREMIUM_INGOT = register("supremium_ingot");
    public static final DeferredHolder<Item, Item> AWAKENED_SUPREMIUM_INGOT = register("awakened_supremium_ingot");
    public static final DeferredHolder<Item, Item> SOULIUM_INGOT = register("soulium_ingot");
    public static final DeferredHolder<Item, Item> PROSPERITY_NUGGET = register("prosperity_nugget");
    public static final DeferredHolder<Item, Item> INFERIUM_NUGGET = register("inferium_nugget");
    public static final DeferredHolder<Item, Item> PRUDENTIUM_NUGGET = register("prudentium_nugget");
    public static final DeferredHolder<Item, Item> TERTIUM_NUGGET = register("tertium_nugget");
    public static final DeferredHolder<Item, Item> IMPERIUM_NUGGET = register("imperium_nugget");
    public static final DeferredHolder<Item, Item> SUPREMIUM_NUGGET = register("supremium_nugget");
    public static final DeferredHolder<Item, Item> AWAKENED_SUPREMIUM_NUGGET = register("awakened_supremium_nugget");
    public static final DeferredHolder<Item, Item> SOULIUM_NUGGET = register("soulium_nugget");
    public static final DeferredHolder<Item, Item> PROSPERITY_GEMSTONE = register("prosperity_gemstone");
    public static final DeferredHolder<Item, Item> INFERIUM_GEMSTONE = register("inferium_gemstone");
    public static final DeferredHolder<Item, Item> PRUDENTIUM_GEMSTONE = register("prudentium_gemstone");
    public static final DeferredHolder<Item, Item> TERTIUM_GEMSTONE = register("tertium_gemstone");
    public static final DeferredHolder<Item, Item> IMPERIUM_GEMSTONE = register("imperium_gemstone");
    public static final DeferredHolder<Item, Item> SUPREMIUM_GEMSTONE = register("supremium_gemstone");
    public static final DeferredHolder<Item, Item> AWAKENED_SUPREMIUM_GEMSTONE = register("awakened_supremium_gemstone");
    public static final DeferredHolder<Item, Item> SOULIUM_GEMSTONE = register("soulium_gemstone");
    public static final DeferredHolder<Item, Item> PROSPERITY_SEED_BASE = register("prosperity_seed_base");
    public static final DeferredHolder<Item, Item> SOULIUM_SEED_BASE = register("soulium_seed_base");
    public static final DeferredHolder<Item, Item> SOUL_DUST = register("soul_dust");
    public static final DeferredHolder<Item, Item> SOULIUM_DUST = register("soulium_dust");
    public static final DeferredHolder<Item, Item> COGNIZANT_DUST = register("cognizant_dust");
    public static final DeferredHolder<Item, Item> SOULIUM_DAGGER = register("soulium_dagger", () -> new SouliumDaggerItem(ModItemTier.SOULIUM, SouliumDaggerItem.DaggerType.BASIC));
    public static final DeferredHolder<Item, Item> PASSIVE_SOULIUM_DAGGER = register("passive_soulium_dagger", () -> new SouliumDaggerItem(ModItemTier.SOULIUM, SouliumDaggerItem.DaggerType.PASSIVE));
    public static final DeferredHolder<Item, Item> HOSTILE_SOULIUM_DAGGER = register("hostile_soulium_dagger", () -> new SouliumDaggerItem(ModItemTier.SOULIUM, SouliumDaggerItem.DaggerType.HOSTILE));
    public static final DeferredHolder<Item, Item> CREATIVE_SOULIUM_DAGGER = register("creative_soulium_dagger", () -> new SouliumDaggerItem(ModItemTier.SOULIUM, SouliumDaggerItem.DaggerType.CREATIVE));
    public static final DeferredHolder<Item, Item> INFUSION_CRYSTAL = register("infusion_crystal", InfusionCrystalItem::new);
    public static final DeferredHolder<Item, Item> MASTER_INFUSION_CRYSTAL = register("master_infusion_crystal", MasterInfusionCrystalItem::new);
    public static final DeferredHolder<Item, Item> FERTILIZED_ESSENCE = register("fertilized_essence", FertilizedEssenceItem::new);
    public static final DeferredHolder<Item, Item> MYSTICAL_FERTILIZER = register("mystical_fertilizer", MysticalFertilizerItem::new);
    public static final DeferredHolder<Item, Item> AIR_AGGLOMERATIO = register("air_agglomeratio");
    public static final DeferredHolder<Item, Item> EARTH_AGGLOMERATIO = register("earth_agglomeratio");
    public static final DeferredHolder<Item, Item> WATER_AGGLOMERATIO = register("water_agglomeratio");
    public static final DeferredHolder<Item, Item> FIRE_AGGLOMERATIO = register("fire_agglomeratio");
    public static final DeferredHolder<Item, Item> NATURE_AGGLOMERATIO = register("nature_agglomeratio");
    public static final DeferredHolder<Item, Item> DYE_AGGLOMERATIO = register("dye_agglomeratio");
    public static final DeferredHolder<Item, Item> NETHER_AGGLOMERATIO = register("nether_agglomeratio");
    public static final DeferredHolder<Item, Item> CORAL_AGGLOMERATIO = register("coral_agglomeratio");
    public static final DeferredHolder<Item, Item> HONEY_AGGLOMERATIO = register("honey_agglomeratio");
    public static final DeferredHolder<Item, Item> PRISMARINE_AGGLOMERATIO = register("prismarine_agglomeratio");
    public static final DeferredHolder<Item, Item> END_AGGLOMERATIO = register("end_agglomeratio");
    public static final DeferredHolder<Item, Item> MYSTICAL_FLOWER_AGGLOMERATIO = register("mystical_flower_agglomeratio");
    public static final DeferredHolder<Item, Item> EXPERIENCE_DROPLET = register("experience_droplet", ExperienceDropletItem::new);
    public static final DeferredHolder<Item, Item> WAND = register("wand", WandItem::new);
    public static final DeferredHolder<Item, Item> BLANK_SKULL = register("blank_skull");
    public static final DeferredHolder<Item, Item> BLANK_RECORD = register("blank_record");
    public static final DeferredHolder<Item, Item> UNATTUNED_AUGMENT = register("unattuned_augment");
    public static final DeferredHolder<Item, Item> SOUL_JAR = register("soul_jar", SoulJarItem::new);
    public static final DeferredHolder<Item, Item> EXPERIENCE_CAPSULE = register("experience_capsule", ExperienceCapsuleItem::new);
    public static final DeferredHolder<Item, Item> WATERING_CAN = register("watering_can", () -> new WateringCanItem(3, 0.25));
    public static final DeferredHolder<Item, Item> DIAMOND_SICKLE = register("diamond_sickle", () -> new BaseSickleItem(Tiers.DIAMOND, 3));
    public static final DeferredHolder<Item, Item> DIAMOND_SCYTHE = register("diamond_scythe", () -> new BaseScytheItem(Tiers.DIAMOND, 3));
    public static final DeferredHolder<Item, Item> UPGRADE_BASE = register("upgrade_base");
    public static final DeferredHolder<Item, Item> INFERIUM_UPGRADE = register("inferium_upgrade", () -> new MachineUpgradeItem(MachineUpgradeTier.INFERIUM));
    public static final DeferredHolder<Item, Item> PRUDENTIUM_UPGRADE = register("prudentium_upgrade", () -> new MachineUpgradeItem(MachineUpgradeTier.PRUDENTIUM));
    public static final DeferredHolder<Item, Item> TERTIUM_UPGRADE = register("tertium_upgrade", () -> new MachineUpgradeItem(MachineUpgradeTier.TERTIUM));
    public static final DeferredHolder<Item, Item> IMPERIUM_UPGRADE = register("imperium_upgrade", () -> new MachineUpgradeItem(MachineUpgradeTier.IMPERIUM));
    public static final DeferredHolder<Item, Item> SUPREMIUM_UPGRADE = register("supremium_upgrade", () -> new MachineUpgradeItem(MachineUpgradeTier.SUPREMIUM));
    public static final DeferredHolder<Item, Item> AWAKENED_SUPREMIUM_UPGRADE = register("awakened_supremium_upgrade", () -> new MachineUpgradeItem(MachineUpgradeTier.AWAKENED_SUPREMIUM));

    public static final DeferredHolder<Item, Item> INFERIUM_SWORD = registerGear("inferium_sword", () -> new EssenceSwordItem(ModItemTier.INFERIUM, 1, 1));
    public static final DeferredHolder<Item, Item> INFERIUM_PICKAXE = registerGear("inferium_pickaxe", () -> new EssencePickaxeItem(ModItemTier.INFERIUM, 1, 1));
    public static final DeferredHolder<Item, Item> INFERIUM_SHOVEL = registerGear("inferium_shovel", () -> new EssenceShovelItem(ModItemTier.INFERIUM, 1, 1));
    public static final DeferredHolder<Item, Item> INFERIUM_AXE = registerGear("inferium_axe", () -> new EssenceAxeItem(ModItemTier.INFERIUM, 1, 1));
    public static final DeferredHolder<Item, Item> INFERIUM_HOE = registerGear("inferium_hoe", () -> new EssenceHoeItem(ModItemTier.INFERIUM, 1, 1));
    public static final DeferredHolder<Item, Item> INFERIUM_STAFF = registerGear("inferium_staff", () -> new EssenceStaffItem(1, 1));
    public static final DeferredHolder<Item, Item> INFERIUM_WATERING_CAN = registerGear("inferium_watering_can", () -> new EssenceWateringCanItem(3, 0.25, CropTier.ONE.getTextColor()));
    public static final DeferredHolder<Item, Item> INFERIUM_BOW = registerGear("inferium_bow", () -> new EssenceBowItem(ModItemTier.INFERIUM, 1, 1, 1.1F, 1.0F));
    public static final DeferredHolder<Item, Item> INFERIUM_CROSSBOW = registerGear("inferium_crossbow", () -> new EssenceCrossbowItem(ModItemTier.INFERIUM, 1, 1, 1.1F, 1.0F));
    public static final DeferredHolder<Item, Item> INFERIUM_SHEARS = registerGear("inferium_shears", () -> new EssenceShearsItem(ModItemTier.INFERIUM, 1, 1));
    public static final DeferredHolder<Item, Item> INFERIUM_FISHING_ROD = registerGear("inferium_fishing_rod", () -> new EssenceFishingRodItem(ModItemTier.INFERIUM, 1, 1));
    public static final DeferredHolder<Item, Item> INFERIUM_SICKLE = registerGear("inferium_sickle", () -> new EssenceSickleItem(ModItemTier.INFERIUM, 3, CropTier.ONE.getTextColor(), 1, 1));
    public static final DeferredHolder<Item, Item> INFERIUM_SCYTHE = registerGear("inferium_scythe", () -> new EssenceScytheItem(ModItemTier.INFERIUM, 3, CropTier.ONE.getTextColor(), 1, 1));
    public static final DeferredHolder<Item, Item> PRUDENTIUM_SWORD = registerGear("prudentium_sword", () -> new EssenceSwordItem(ModItemTier.PRUDENTIUM, 2, 1));
    public static final DeferredHolder<Item, Item> PRUDENTIUM_PICKAXE = registerGear("prudentium_pickaxe", () -> new EssencePickaxeItem(ModItemTier.PRUDENTIUM, 2, 1));
    public static final DeferredHolder<Item, Item> PRUDENTIUM_SHOVEL = registerGear("prudentium_shovel", () -> new EssenceShovelItem(ModItemTier.PRUDENTIUM, 2, 1));
    public static final DeferredHolder<Item, Item> PRUDENTIUM_AXE = registerGear("prudentium_axe", () -> new EssenceAxeItem(ModItemTier.PRUDENTIUM, 2, 1));
    public static final DeferredHolder<Item, Item> PRUDENTIUM_HOE = registerGear("prudentium_hoe", () -> new EssenceHoeItem(ModItemTier.PRUDENTIUM, 2, 1));
    public static final DeferredHolder<Item, Item> PRUDENTIUM_STAFF = registerGear("prudentium_staff", () -> new EssenceStaffItem(2, 1));
    public static final DeferredHolder<Item, Item> PRUDENTIUM_WATERING_CAN = registerGear("prudentium_watering_can", () -> new EssenceWateringCanItem(5, 0.30, CropTier.TWO.getTextColor()));
    public static final DeferredHolder<Item, Item> PRUDENTIUM_BOW = registerGear("prudentium_bow", () -> new EssenceBowItem(ModItemTier.PRUDENTIUM, 2, 1, 1.2F, 1.5F));
    public static final DeferredHolder<Item, Item> PRUDENTIUM_CROSSBOW = registerGear("prudentium_crossbow", () -> new EssenceCrossbowItem(ModItemTier.PRUDENTIUM, 2, 1, 1.2F, 1.5F));
    public static final DeferredHolder<Item, Item> PRUDENTIUM_SHEARS = registerGear("prudentium_shears", () -> new EssenceShearsItem(ModItemTier.PRUDENTIUM, 2, 1));
    public static final DeferredHolder<Item, Item> PRUDENTIUM_FISHING_ROD = registerGear("prudentium_fishing_rod", () -> new EssenceFishingRodItem(ModItemTier.PRUDENTIUM, 2, 1));
    public static final DeferredHolder<Item, Item> PRUDENTIUM_SICKLE = registerGear("prudentium_sickle", () -> new EssenceSickleItem(ModItemTier.PRUDENTIUM, 4, CropTier.TWO.getTextColor(), 2, 1));
    public static final DeferredHolder<Item, Item> PRUDENTIUM_SCYTHE = registerGear("prudentium_scythe", () -> new EssenceScytheItem(ModItemTier.PRUDENTIUM, 4, CropTier.TWO.getTextColor(), 2, 1));
    public static final DeferredHolder<Item, Item> TERTIUM_SWORD = registerGear("tertium_sword", () -> new EssenceSwordItem(ModItemTier.TERTIUM, 3, 1));
    public static final DeferredHolder<Item, Item> TERTIUM_PICKAXE = registerGear("tertium_pickaxe", () -> new EssencePickaxeItem(ModItemTier.TERTIUM, 3, 1));
    public static final DeferredHolder<Item, Item> TERTIUM_SHOVEL = registerGear("tertium_shovel", () -> new EssenceShovelItem(ModItemTier.TERTIUM, 3, 1));
    public static final DeferredHolder<Item, Item> TERTIUM_AXE = registerGear("tertium_axe", () -> new EssenceAxeItem(ModItemTier.TERTIUM, 3, 1));
    public static final DeferredHolder<Item, Item> TERTIUM_HOE = registerGear("tertium_hoe", () -> new EssenceHoeItem(ModItemTier.TERTIUM, 3, 1));
    public static final DeferredHolder<Item, Item> TERTIUM_STAFF = registerGear("tertium_staff", () -> new EssenceStaffItem(3, 1));
    public static final DeferredHolder<Item, Item> TERTIUM_WATERING_CAN = registerGear("tertium_watering_can", () -> new EssenceWateringCanItem(7, 0.35, CropTier.THREE.getTextColor()));
    public static final DeferredHolder<Item, Item> TERTIUM_BOW = registerGear("tertium_bow", () -> new EssenceBowItem(ModItemTier.TERTIUM, 3, 1, 1.35F, 2.0F));
    public static final DeferredHolder<Item, Item> TERTIUM_CROSSBOW = registerGear("tertium_crossbow", () -> new EssenceCrossbowItem(ModItemTier.TERTIUM, 3, 1, 1.35F, 2.0F));
    public static final DeferredHolder<Item, Item> TERTIUM_SHEARS = registerGear("tertium_shears", () -> new EssenceShearsItem(ModItemTier.TERTIUM, 3, 1));
    public static final DeferredHolder<Item, Item> TERTIUM_FISHING_ROD = registerGear("tertium_fishing_rod", () -> new EssenceFishingRodItem(ModItemTier.TERTIUM, 3, 1));
    public static final DeferredHolder<Item, Item> TERTIUM_SICKLE = registerGear("tertium_sickle", () -> new EssenceSickleItem(ModItemTier.TERTIUM, 5, CropTier.THREE.getTextColor(), 3, 1));
    public static final DeferredHolder<Item, Item> TERTIUM_SCYTHE = registerGear("tertium_scythe", () -> new EssenceScytheItem(ModItemTier.TERTIUM, 5, CropTier.THREE.getTextColor(), 3, 1));
    public static final DeferredHolder<Item, Item> IMPERIUM_SWORD = registerGear("imperium_sword", () -> new EssenceSwordItem(ModItemTier.IMPERIUM, 4, 1));
    public static final DeferredHolder<Item, Item> IMPERIUM_PICKAXE = registerGear("imperium_pickaxe", () -> new EssencePickaxeItem(ModItemTier.IMPERIUM, 4, 1));
    public static final DeferredHolder<Item, Item> IMPERIUM_SHOVEL = registerGear("imperium_shovel", () -> new EssenceShovelItem(ModItemTier.IMPERIUM, 4, 1));
    public static final DeferredHolder<Item, Item> IMPERIUM_AXE = registerGear("imperium_axe", () -> new EssenceAxeItem(ModItemTier.IMPERIUM, 4, 1));
    public static final DeferredHolder<Item, Item> IMPERIUM_HOE = registerGear("imperium_hoe", () -> new EssenceHoeItem(ModItemTier.IMPERIUM, 4, 1));
    public static final DeferredHolder<Item, Item> IMPERIUM_STAFF = registerGear("imperium_staff", () -> new EssenceStaffItem(4, 1));
    public static final DeferredHolder<Item, Item> IMPERIUM_WATERING_CAN = registerGear("imperium_watering_can", () -> new EssenceWateringCanItem(9, 0.40, CropTier.FOUR.getTextColor()));
    public static final DeferredHolder<Item, Item> IMPERIUM_BOW = registerGear("imperium_bow", () -> new EssenceBowItem(ModItemTier.IMPERIUM, 4, 1, 1.55F, 2.5F));
    public static final DeferredHolder<Item, Item> IMPERIUM_CROSSBOW = registerGear("imperium_crossbow", () -> new EssenceCrossbowItem(ModItemTier.IMPERIUM, 4, 1, 1.55F, 2.5F));
    public static final DeferredHolder<Item, Item> IMPERIUM_SHEARS = registerGear("imperium_shears", () -> new EssenceShearsItem(ModItemTier.IMPERIUM, 4, 1));
    public static final DeferredHolder<Item, Item> IMPERIUM_FISHING_ROD = registerGear("imperium_fishing_rod", () -> new EssenceFishingRodItem(ModItemTier.IMPERIUM, 4, 1));
    public static final DeferredHolder<Item, Item> IMPERIUM_SICKLE = registerGear("imperium_sickle", () -> new EssenceSickleItem(ModItemTier.IMPERIUM, 6, CropTier.FOUR.getTextColor(), 4, 1));
    public static final DeferredHolder<Item, Item> IMPERIUM_SCYTHE = registerGear("imperium_scythe", () -> new EssenceScytheItem(ModItemTier.IMPERIUM, 6, CropTier.FOUR.getTextColor(), 4, 1));
    public static final DeferredHolder<Item, Item> SUPREMIUM_SWORD = registerGear("supremium_sword", () -> new EssenceSwordItem(ModItemTier.SUPREMIUM, 5, 1));
    public static final DeferredHolder<Item, Item> SUPREMIUM_PICKAXE = registerGear("supremium_pickaxe", () -> new EssencePickaxeItem(ModItemTier.SUPREMIUM, 5, 1));
    public static final DeferredHolder<Item, Item> SUPREMIUM_SHOVEL = registerGear("supremium_shovel", () -> new EssenceShovelItem(ModItemTier.SUPREMIUM, 5, 1));
    public static final DeferredHolder<Item, Item> SUPREMIUM_AXE = registerGear("supremium_axe", () -> new EssenceAxeItem(ModItemTier.SUPREMIUM, 5, 1));
    public static final DeferredHolder<Item, Item> SUPREMIUM_HOE = registerGear("supremium_hoe", () -> new EssenceHoeItem(ModItemTier.SUPREMIUM, 5, 1));
    public static final DeferredHolder<Item, Item> SUPREMIUM_STAFF = registerGear("supremium_staff", () -> new EssenceStaffItem(5, 1));
    public static final DeferredHolder<Item, Item> SUPREMIUM_WATERING_CAN = registerGear("supremium_watering_can", () -> new EssenceWateringCanItem(11, 0.45, CropTier.FIVE.getTextColor()));
    public static final DeferredHolder<Item, Item> SUPREMIUM_BOW = registerGear("supremium_bow", () -> new EssenceBowItem(ModItemTier.SUPREMIUM, 5, 1, 1.80F, 3.0F));
    public static final DeferredHolder<Item, Item> SUPREMIUM_CROSSBOW = registerGear("supremium_crossbow", () -> new EssenceCrossbowItem(ModItemTier.SUPREMIUM, 5, 1, 1.80F, 3.0F));
    public static final DeferredHolder<Item, Item> SUPREMIUM_SHEARS = registerGear("supremium_shears", () -> new EssenceShearsItem(ModItemTier.SUPREMIUM, 5, 1));
    public static final DeferredHolder<Item, Item> SUPREMIUM_FISHING_ROD = registerGear("supremium_fishing_rod", () -> new EssenceFishingRodItem(ModItemTier.SUPREMIUM, 5, 1));
    public static final DeferredHolder<Item, Item> SUPREMIUM_SICKLE = registerGear("supremium_sickle", () -> new EssenceSickleItem(ModItemTier.SUPREMIUM, 7, CropTier.FIVE.getTextColor(), 5, 1));
    public static final DeferredHolder<Item, Item> SUPREMIUM_SCYTHE = registerGear("supremium_scythe", () -> new EssenceScytheItem(ModItemTier.SUPREMIUM, 7, CropTier.FIVE.getTextColor(), 5, 1));
    public static final DeferredHolder<Item, Item> AWAKENED_SUPREMIUM_SWORD = registerGear("awakened_supremium_sword", () -> new EssenceSwordItem(ModItemTier.AWAKENED_SUPREMIUM, 5, 2));
    public static final DeferredHolder<Item, Item> AWAKENED_SUPREMIUM_PICKAXE = registerGear("awakened_supremium_pickaxe", () -> new EssencePickaxeItem(ModItemTier.AWAKENED_SUPREMIUM, 5, 2));
    public static final DeferredHolder<Item, Item> AWAKENED_SUPREMIUM_SHOVEL = registerGear("awakened_supremium_shovel", () -> new EssenceShovelItem(ModItemTier.AWAKENED_SUPREMIUM, 5, 2));
    public static final DeferredHolder<Item, Item> AWAKENED_SUPREMIUM_AXE = registerGear("awakened_supremium_axe", () -> new EssenceAxeItem(ModItemTier.AWAKENED_SUPREMIUM, 5, 2));
    public static final DeferredHolder<Item, Item> AWAKENED_SUPREMIUM_HOE = registerGear("awakened_supremium_hoe", () -> new EssenceHoeItem(ModItemTier.AWAKENED_SUPREMIUM, 5, 2));
    public static final DeferredHolder<Item, Item> AWAKENED_SUPREMIUM_STAFF = registerGear("awakened_supremium_staff", () -> new EssenceStaffItem(5, 1));
    public static final DeferredHolder<Item, Item> AWAKENED_SUPREMIUM_WATERING_CAN = registerGear("awakened_supremium_watering_can", () -> new EssenceWateringCanItem(13, 0.50, CropTier.FIVE.getTextColor()));
    public static final DeferredHolder<Item, Item> AWAKENED_SUPREMIUM_BOW = registerGear("awakened_supremium_bow", () -> new EssenceBowItem(ModItemTier.AWAKENED_SUPREMIUM, 5, 2, 2.10F, 4.0F));
    public static final DeferredHolder<Item, Item> AWAKENED_SUPREMIUM_CROSSBOW = registerGear("awakened_supremium_crossbow", () -> new EssenceCrossbowItem(ModItemTier.AWAKENED_SUPREMIUM, 5, 2, 2.10F, 4.0F));
    public static final DeferredHolder<Item, Item> AWAKENED_SUPREMIUM_SHEARS = registerGear("awakened_supremium_shears", () -> new EssenceShearsItem(ModItemTier.AWAKENED_SUPREMIUM, 5, 2));
    public static final DeferredHolder<Item, Item> AWAKENED_SUPREMIUM_FISHING_ROD = registerGear("awakened_supremium_fishing_rod", () -> new EssenceFishingRodItem(ModItemTier.AWAKENED_SUPREMIUM, 5, 2));
    public static final DeferredHolder<Item, Item> AWAKENED_SUPREMIUM_SICKLE = registerGear("awakened_supremium_sickle", () -> new EssenceSickleItem(ModItemTier.AWAKENED_SUPREMIUM, 8, CropTier.FIVE.getTextColor(), 5, 2));
    public static final DeferredHolder<Item, Item> AWAKENED_SUPREMIUM_SCYTHE = registerGear("awakened_supremium_scythe", () -> new EssenceScytheItem(ModItemTier.AWAKENED_SUPREMIUM, 8, CropTier.FIVE.getTextColor(), 5, 2));
    public static final DeferredHolder<Item, Item> INFERIUM_HELMET = registerGear("inferium_helmet", () -> new EssenceHelmetItem(ModArmorMaterials.INFERIUM, 40, 1, 1));
    public static final DeferredHolder<Item, Item> INFERIUM_CHESTPLATE = registerGear("inferium_chestplate", () -> new EssenceChestplateItem(ModArmorMaterials.INFERIUM, 40, 1, 1));
    public static final DeferredHolder<Item, Item> INFERIUM_LEGGINGS = registerGear("inferium_leggings", () -> new EssenceLeggingsItem(ModArmorMaterials.INFERIUM, 40, 1, 1));
    public static final DeferredHolder<Item, Item> INFERIUM_BOOTS = registerGear("inferium_boots", () -> new EssenceBootsItem(ModArmorMaterials.INFERIUM, 40, 1, 1));
    public static final DeferredHolder<Item, Item> PRUDENTIUM_HELMET = registerGear("prudentium_helmet", () -> new EssenceHelmetItem(ModArmorMaterials.PRUDENTIUM, 60, 2, 1));
    public static final DeferredHolder<Item, Item> PRUDENTIUM_CHESTPLATE = registerGear("prudentium_chestplate", () -> new EssenceChestplateItem(ModArmorMaterials.PRUDENTIUM, 60, 2, 1));
    public static final DeferredHolder<Item, Item> PRUDENTIUM_LEGGINGS = registerGear("prudentium_leggings", () -> new EssenceLeggingsItem(ModArmorMaterials.PRUDENTIUM, 60, 2, 1));
    public static final DeferredHolder<Item, Item> PRUDENTIUM_BOOTS = registerGear("prudentium_boots", () -> new EssenceBootsItem(ModArmorMaterials.PRUDENTIUM, 60, 2, 1));
    public static final DeferredHolder<Item, Item> TERTIUM_HELMET = registerGear("tertium_helmet", () -> new EssenceHelmetItem(ModArmorMaterials.TERTIUM, 80, 3, 1));
    public static final DeferredHolder<Item, Item> TERTIUM_CHESTPLATE = registerGear("tertium_chestplate", () -> new EssenceChestplateItem(ModArmorMaterials.TERTIUM, 80, 3, 1));
    public static final DeferredHolder<Item, Item> TERTIUM_LEGGINGS = registerGear("tertium_leggings", () -> new EssenceLeggingsItem(ModArmorMaterials.TERTIUM, 80, 3, 1));
    public static final DeferredHolder<Item, Item> TERTIUM_BOOTS = registerGear("tertium_boots", () -> new EssenceBootsItem(ModArmorMaterials.TERTIUM, 80, 3, 1));
    public static final DeferredHolder<Item, Item> IMPERIUM_HELMET = registerGear("imperium_helmet", () -> new EssenceHelmetItem(ModArmorMaterials.IMPERIUM, 140, 4, 1));
    public static final DeferredHolder<Item, Item> IMPERIUM_CHESTPLATE = registerGear("imperium_chestplate", () -> new EssenceChestplateItem(ModArmorMaterials.IMPERIUM, 140, 4, 1));
    public static final DeferredHolder<Item, Item> IMPERIUM_LEGGINGS = registerGear("imperium_leggings", () -> new EssenceLeggingsItem(ModArmorMaterials.IMPERIUM, 140, 4, 1));
    public static final DeferredHolder<Item, Item> IMPERIUM_BOOTS = registerGear("imperium_boots", () -> new EssenceBootsItem(ModArmorMaterials.IMPERIUM, 140, 4, 1));
    public static final DeferredHolder<Item, Item> SUPREMIUM_HELMET = registerGear("supremium_helmet", () -> new EssenceHelmetItem(ModArmorMaterials.SUPREMIUM, 280, 5, 1));
    public static final DeferredHolder<Item, Item> SUPREMIUM_CHESTPLATE = registerGear("supremium_chestplate", () -> new EssenceChestplateItem(ModArmorMaterials.SUPREMIUM, 280, 5, 1));
    public static final DeferredHolder<Item, Item> SUPREMIUM_LEGGINGS = registerGear("supremium_leggings", () -> new EssenceLeggingsItem(ModArmorMaterials.SUPREMIUM, 280, 5, 1));
    public static final DeferredHolder<Item, Item> SUPREMIUM_BOOTS = registerGear("supremium_boots", () -> new EssenceBootsItem(ModArmorMaterials.SUPREMIUM, 280, 5, 1));
    public static final DeferredHolder<Item, Item> AWAKENED_SUPREMIUM_HELMET = registerGear("awakened_supremium_helmet", () -> new EssenceHelmetItem(ModArmorMaterials.AWAKENED_SUPREMIUM, 320,  5, 2));
    public static final DeferredHolder<Item, Item> AWAKENED_SUPREMIUM_CHESTPLATE = registerGear("awakened_supremium_chestplate", () -> new EssenceChestplateItem(ModArmorMaterials.AWAKENED_SUPREMIUM, 320,  5, 2));
    public static final DeferredHolder<Item, Item> AWAKENED_SUPREMIUM_LEGGINGS = registerGear("awakened_supremium_leggings", () -> new EssenceLeggingsItem(ModArmorMaterials.AWAKENED_SUPREMIUM, 320,  5, 2));
    public static final DeferredHolder<Item, Item> AWAKENED_SUPREMIUM_BOOTS = registerGear("awakened_supremium_boots", () -> new EssenceBootsItem(ModArmorMaterials.AWAKENED_SUPREMIUM, 320,  5, 2));

    @SubscribeEvent
    public void onRegisterItems(RegisterEvent event) {
        event.register(Registries.ITEM, registry -> {
            BLOCK_ENTRIES.stream().map(Supplier::get).forEach(item -> {
                var id = BuiltInRegistries.BLOCK.getKey(item.getBlock());
                registry.register(id, item);
            });

            ENTRIES.forEach((reg, item) -> {
                registry.register(reg.getId(), item.get());
            });

            CropRegistry.getInstance().onRegisterItems(registry);

            GEAR_ENTRIES.forEach((reg, item) -> {
                registry.register(reg.getId(), item.get());
            });

            AugmentRegistry.getInstance().onRegisterItems(registry);
        });
    }

    private static DeferredHolder<Item, Item> register(String name) {
        return register(name, BaseItem::new);
    }

    private static DeferredHolder<Item, Item> register(String name, Supplier<Item> item) {
        var id = MysticalAgriculture.resource(name);
        var holder = DeferredHolder.create(Registries.ITEM, id);
        ENTRIES.put(holder, item);
        return holder;
    }

    private static DeferredHolder<Item, Item> registerGear(String name, Supplier<? extends Item> item) {
        var id = MysticalAgriculture.resource(name);
        var holder = DeferredHolder.create(Registries.ITEM, id);
        GEAR_ENTRIES.put(holder, item::get);
        return holder;
    }
}
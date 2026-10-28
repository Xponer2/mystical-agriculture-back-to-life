package com.blakebr0.mysticalagriculture.init;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import com.blakebr0.cucumber.block.BaseBlock;
import com.blakebr0.cucumber.block.BaseGlassBlock;
import com.blakebr0.cucumber.block.BaseOreBlock;
import com.blakebr0.cucumber.block.BaseSlabBlock;
import com.blakebr0.cucumber.block.BaseStairsBlock;
import com.blakebr0.cucumber.block.BaseWallBlock;
import com.blakebr0.cucumber.item.BaseBlockItem;
import com.blakebr0.mysticalagriculture.MysticalAgriculture;
import com.blakebr0.mysticalagriculture.api.crop.CropTier;
import com.blakebr0.mysticalagriculture.block.AwakeningAltarBlock;
import com.blakebr0.mysticalagriculture.block.AwakeningPedestalBlock;
import com.blakebr0.mysticalagriculture.block.EnchanterBlock;
import com.blakebr0.mysticalagriculture.block.EssenceFurnaceBlock;
import com.blakebr0.mysticalagriculture.block.EssenceVesselBlock;
import com.blakebr0.mysticalagriculture.block.GrowthAcceleratorBlock;
import com.blakebr0.mysticalagriculture.block.HarvesterBlock;
import com.blakebr0.mysticalagriculture.block.InferiumCropBlock;
import com.blakebr0.mysticalagriculture.block.InfusedFarmlandBlock;
import com.blakebr0.mysticalagriculture.block.InfusionAltarBlock;
import com.blakebr0.mysticalagriculture.block.InfusionPedestalBlock;
import com.blakebr0.mysticalagriculture.block.ReprocessorBlock;
import com.blakebr0.mysticalagriculture.block.SoulExtractorBlock;
import com.blakebr0.mysticalagriculture.block.SouliumSpawnerBlock;
import com.blakebr0.mysticalagriculture.block.TinkeringTableBlock;
import com.blakebr0.mysticalagriculture.block.WitherproofBlock;
import com.blakebr0.mysticalagriculture.block.WitherproofGlassBlock;
import com.blakebr0.mysticalagriculture.lib.ModCrops;
import com.blakebr0.mysticalagriculture.registry.CropRegistry;
import io.github.xponer2.translator.registry.DeferredHolder;
import io.github.xponer2.translator.registry.RegisterEvent;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import io.github.xponer2.translator.util.Registries;

public final class ModBlocks {
    public static final Map<DeferredHolder<Block, Block>, Supplier<Block>> ENTRIES = new LinkedHashMap<>();

    public static final DeferredHolder<Block, Block> PROSPERITY_BLOCK = register("prosperity_block", () -> new BaseBlock(SoundType.STONE, 4.0F, 6.0F, true));
    public static final DeferredHolder<Block, Block> INFERIUM_BLOCK = register("inferium_block", () -> new BaseBlock(SoundType.STONE, 4.0F, 6.0F, true));
    public static final DeferredHolder<Block, Block> PRUDENTIUM_BLOCK = register("prudentium_block", () -> new BaseBlock(SoundType.STONE, 4.0F, 6.0F, true));
    public static final DeferredHolder<Block, Block> TERTIUM_BLOCK = register("tertium_block", () -> new BaseBlock(SoundType.STONE, 4.0F, 6.0F, true));
    public static final DeferredHolder<Block, Block> IMPERIUM_BLOCK = register("imperium_block", () -> new BaseBlock(SoundType.STONE, 4.0F, 5.0F, true));
    public static final DeferredHolder<Block, Block> SUPREMIUM_BLOCK = register("supremium_block", () -> new BaseBlock(SoundType.STONE, 4.0F, 6.0F, true));
    public static final DeferredHolder<Block, Block> AWAKENED_SUPREMIUM_BLOCK = register("awakened_supremium_block", () -> new BaseBlock(SoundType.STONE, 4.0F, 6.0F, true));
    public static final DeferredHolder<Block, Block> SOULIUM_BLOCK = register("soulium_block", () -> new BaseBlock(SoundType.STONE, 4.0F, 6.0F, true));
    public static final DeferredHolder<Block, Block> PROSPERITY_INGOT_BLOCK = register("prosperity_ingot_block", () -> new BaseBlock(SoundType.METAL, 5.0F, 6.0F, true));
    public static final DeferredHolder<Block, Block> INFERIUM_INGOT_BLOCK = register("inferium_ingot_block", () -> new BaseBlock(SoundType.METAL, 5.0F, 6.0F, true));
    public static final DeferredHolder<Block, Block> PRUDENTIUM_INGOT_BLOCK = register("prudentium_ingot_block", () -> new BaseBlock(SoundType.METAL, 5.0F, 6.0F, true));
    public static final DeferredHolder<Block, Block> TERTIUM_INGOT_BLOCK = register("tertium_ingot_block", () -> new BaseBlock(SoundType.METAL, 5.0F, 6.0F, true));
    public static final DeferredHolder<Block, Block> IMPERIUM_INGOT_BLOCK = register("imperium_ingot_block", () -> new BaseBlock(SoundType.METAL, 5.0F, 6.0F, true));
    public static final DeferredHolder<Block, Block> SUPREMIUM_INGOT_BLOCK = register("supremium_ingot_block", () -> new BaseBlock(SoundType.METAL, 5.0F, 6.0F, true));
    public static final DeferredHolder<Block, Block> AWAKENED_SUPREMIUM_INGOT_BLOCK = register("awakened_supremium_ingot_block", () -> new BaseBlock(SoundType.METAL, 5.0F, 6.0F, true));
    public static final DeferredHolder<Block, Block> SOULIUM_INGOT_BLOCK = register("soulium_ingot_block", () -> new BaseBlock(SoundType.METAL, 5.0F, 6.0F, true));
    public static final DeferredHolder<Block, Block> PROSPERITY_GEMSTONE_BLOCK = register("prosperity_gemstone_block", () -> new BaseBlock(SoundType.METAL, 5.0F, 6.0F, true));
    public static final DeferredHolder<Block, Block> INFERIUM_GEMSTONE_BLOCK = register("inferium_gemstone_block", () -> new BaseBlock(SoundType.METAL, 5.0F, 6.0F, true));
    public static final DeferredHolder<Block, Block> PRUDENTIUM_GEMSTONE_BLOCK = register("prudentium_gemstone_block", () -> new BaseBlock(SoundType.METAL, 5.0F, 6.0F, true));
    public static final DeferredHolder<Block, Block> TERTIUM_GEMSTONE_BLOCK = register("tertium_gemstone_block", () -> new BaseBlock(SoundType.METAL, 5.0F, 6.0F, true));
    public static final DeferredHolder<Block, Block> IMPERIUM_GEMSTONE_BLOCK = register("imperium_gemstone_block", () -> new BaseBlock(SoundType.METAL, 5.0F, 6.0F, true));
    public static final DeferredHolder<Block, Block> SUPREMIUM_GEMSTONE_BLOCK = register("supremium_gemstone_block", () -> new BaseBlock(SoundType.METAL, 5.0F, 6.0F, true));
    public static final DeferredHolder<Block, Block> AWAKENED_SUPREMIUM_GEMSTONE_BLOCK = register("awakened_supremium_gemstone_block", () -> new BaseBlock(SoundType.METAL, 5.0F, 6.0F, true));
    public static final DeferredHolder<Block, Block> SOULIUM_GEMSTONE_BLOCK = register("soulium_gemstone_block", () -> new BaseBlock(SoundType.METAL, 5.0F, 6.0F, true));
    public static final DeferredHolder<Block, Block> INFERIUM_FARMLAND = register("inferium_farmland", () -> new InfusedFarmlandBlock(CropTier.ONE));
    public static final DeferredHolder<Block, Block> PRUDENTIUM_FARMLAND = register("prudentium_farmland", () -> new InfusedFarmlandBlock(CropTier.TWO));
    public static final DeferredHolder<Block, Block> TERTIUM_FARMLAND = register("tertium_farmland", () -> new InfusedFarmlandBlock(CropTier.THREE));
    public static final DeferredHolder<Block, Block> IMPERIUM_FARMLAND = register("imperium_farmland", () -> new InfusedFarmlandBlock(CropTier.FOUR));
    public static final DeferredHolder<Block, Block> SUPREMIUM_FARMLAND = register("supremium_farmland", () -> new InfusedFarmlandBlock(CropTier.FIVE));
    public static final DeferredHolder<Block, Block> INFERIUM_GROWTH_ACCELERATOR = register("inferium_growth_accelerator", () -> new GrowthAcceleratorBlock(9, CropTier.ONE.getTextColor()));
    public static final DeferredHolder<Block, Block> PRUDENTIUM_GROWTH_ACCELERATOR = register("prudentium_growth_accelerator", () -> new GrowthAcceleratorBlock(18, CropTier.TWO.getTextColor()));
    public static final DeferredHolder<Block, Block> TERTIUM_GROWTH_ACCELERATOR = register("tertium_growth_accelerator", () -> new GrowthAcceleratorBlock(27, CropTier.THREE.getTextColor()));
    public static final DeferredHolder<Block, Block> IMPERIUM_GROWTH_ACCELERATOR = register("imperium_growth_accelerator", () -> new GrowthAcceleratorBlock(36, CropTier.FOUR.getTextColor()));
    public static final DeferredHolder<Block, Block> SUPREMIUM_GROWTH_ACCELERATOR = register("supremium_growth_accelerator", () -> new GrowthAcceleratorBlock(45, CropTier.FIVE.getTextColor()));
    public static final DeferredHolder<Block, Block> AWAKENED_SUPREMIUM_GROWTH_ACCELERATOR = register("awakened_supremium_growth_accelerator", () -> new GrowthAcceleratorBlock(54, CropTier.FIVE.getTextColor()));
    public static final DeferredHolder<Block, Block> PROSPERITY_ORE = register("prosperity_ore", () -> new BaseOreBlock(SoundType.STONE, 3.0F, 3.0F, 2, 5));
    public static final DeferredHolder<Block, Block> DEEPSLATE_PROSPERITY_ORE = register("deepslate_prosperity_ore", () -> new BaseOreBlock(SoundType.STONE, 4.5F, 3.0F, 2, 5));
    public static final DeferredHolder<Block, Block> INFERIUM_ORE = register("inferium_ore", () -> new BaseOreBlock(SoundType.STONE, 3.0F, 3.0F, 2, 5));
    public static final DeferredHolder<Block, Block> DEEPSLATE_INFERIUM_ORE = register("deepslate_inferium_ore", () -> new BaseOreBlock(SoundType.STONE, 4.5F, 3.0F, 2, 5));
    public static final DeferredHolder<Block, Block> SOULIUM_ORE = register("soulium_ore", () -> new BaseOreBlock(SoundType.STONE, 3.0F, 3.0F, 3, 7));
    public static final DeferredHolder<Block, Block> SOULSTONE = register("soulstone", () -> new BaseBlock(SoundType.STONE, 1.5F, 6.0F, true));
    public static final DeferredHolder<Block, Block> SOULSTONE_COBBLE = register("soulstone_cobble", () -> new BaseBlock(SoundType.STONE, 2.0F, 6.0F, true));
    public static final DeferredHolder<Block, Block> SOULSTONE_BRICKS = register("soulstone_bricks", () -> new BaseBlock(SoundType.STONE, 1.5F, 6.0F, true));
    public static final DeferredHolder<Block, Block> SOULSTONE_CRACKED_BRICKS = register("soulstone_cracked_bricks", () -> new BaseBlock(SoundType.STONE, 1.5F, 6.0F, true));
    public static final DeferredHolder<Block, Block> SOULSTONE_CHISELED_BRICKS = register("soulstone_chiseled_bricks", () -> new BaseBlock(SoundType.STONE, 1.5F, 6.0F, true));
    public static final DeferredHolder<Block, Block> SOULSTONE_SMOOTH = register("soulstone_smooth", () -> new BaseBlock(SoundType.STONE, 1.5F, 6.0F, true));
    public static final DeferredHolder<Block, Block> SOUL_GLASS = register("soul_glass", () -> new BaseGlassBlock(SoundType.GLASS, 0.3F, 0.3F));
    public static final DeferredHolder<Block, Block> SOULSTONE_SLAB = register("soulstone_slab", () -> new BaseSlabBlock(SOULSTONE));
    public static final DeferredHolder<Block, Block> SOULSTONE_COBBLE_SLAB = register("soulstone_cobble_slab", () -> new BaseSlabBlock(SOULSTONE_COBBLE));
    public static final DeferredHolder<Block, Block> SOULSTONE_BRICKS_SLAB = register("soulstone_bricks_slab", () -> new BaseSlabBlock(SOULSTONE_BRICKS));
    public static final DeferredHolder<Block, Block> SOULSTONE_SMOOTH_SLAB = register("soulstone_smooth_slab", () -> new BaseSlabBlock(SOULSTONE_SMOOTH));
    public static final DeferredHolder<Block, Block> SOULSTONE_STAIRS = register("soulstone_stairs", () -> new BaseStairsBlock(SOULSTONE.get().getDefaultState()));
    public static final DeferredHolder<Block, Block> SOULSTONE_COBBLE_STAIRS = register("soulstone_cobble_stairs", () -> new BaseStairsBlock(SOULSTONE_COBBLE.get().getDefaultState()));
    public static final DeferredHolder<Block, Block> SOULSTONE_BRICKS_STAIRS = register("soulstone_bricks_stairs", () -> new BaseStairsBlock(SOULSTONE_BRICKS.get().getDefaultState()));
    public static final DeferredHolder<Block, Block> SOULSTONE_COBBLE_WALL = register("soulstone_cobble_wall", () -> new BaseWallBlock(SOULSTONE_COBBLE));
    public static final DeferredHolder<Block, Block> SOULSTONE_BRICKS_WALL = register("soulstone_bricks_wall", () -> new BaseWallBlock(SOULSTONE_BRICKS));
    public static final DeferredHolder<Block, Block> WITHERPROOF_BLOCK = register("witherproof_block", WitherproofBlock::new);
    public static final DeferredHolder<Block, Block> WITHERPROOF_BRICKS = register("witherproof_bricks", WitherproofBlock::new);
    public static final DeferredHolder<Block, Block> WITHERPROOF_GLASS = register("witherproof_glass", WitherproofGlassBlock::new);
    public static final DeferredHolder<Block, Block> INFUSION_PEDESTAL = register("infusion_pedestal", InfusionPedestalBlock::new);
    public static final DeferredHolder<Block, Block> INFUSION_ALTAR = register("infusion_altar", InfusionAltarBlock::new);
    public static final DeferredHolder<Block, Block> AWAKENING_PEDESTAL = register("awakening_pedestal", AwakeningPedestalBlock::new);
    public static final DeferredHolder<Block, Block> AWAKENING_ALTAR = register("awakening_altar", AwakeningAltarBlock::new);
    public static final DeferredHolder<Block, Block> ESSENCE_VESSEL = register("essence_vessel", EssenceVesselBlock::new);
    public static final DeferredHolder<Block, Block> TINKERING_TABLE = register("tinkering_table", TinkeringTableBlock::new);
    public static final DeferredHolder<Block, Block> ENCHANTER = register("enchanter", EnchanterBlock::new);
    public static final DeferredHolder<Block, Block> MACHINE_FRAME = register("machine_frame", () -> new BaseBlock(SoundType.STONE, 1.5F, 6.0F, true));
    public static final DeferredHolder<Block, Block> FURNACE = register("furnace", EssenceFurnaceBlock::new);
    public static final DeferredHolder<Block, Block> REPROCESSOR = register("seed_reprocessor", ReprocessorBlock::new);
    public static final DeferredHolder<Block, Block> SOUL_EXTRACTOR = register("soul_extractor", SoulExtractorBlock::new);
    public static final DeferredHolder<Block, Block> HARVESTER = register("harvester", HarvesterBlock::new);
    public static final DeferredHolder<Block, Block> SOULIUM_SPAWNER = register("soulium_spawner", SouliumSpawnerBlock::new);

    public static final DeferredHolder<Block, Block> INFERIUM_CROP = registerNoItem("inferium_crop", () -> new InferiumCropBlock(ModCrops.INFERIUM));

    @SubscribeEvent
    public void onRegisterBlocks(RegisterEvent event) {
        event.register(Registries.BLOCK, registry -> {
            ENTRIES.forEach((reg, block) -> {
                registry.register(reg.getId(), block.get());
            });

            CropRegistry.getInstance().setAllowRegistration(true);
            CropRegistry.getInstance().onRegisterBlocks(registry);
            CropRegistry.getInstance().setAllowRegistration(false);
        });
    }

    private static DeferredHolder<Block, Block> register(String name, Supplier<Block> block) {
        return register(name, block, b -> () -> new BaseBlockItem(b.get()));
    }

    private static DeferredHolder<Block, Block> register(String name, Supplier<Block> block, Function<DeferredHolder<Block, Block>, Supplier<? extends ItemBlock>> item) {
        var holder = registerNoItem(name, block);
        ModItems.BLOCK_ENTRIES.add(() -> item.apply(holder).get());
        return holder;
    }

    public static DeferredHolder<Block, Block> registerNoItem(String name, Supplier<Block> block) {
        var id = MysticalAgriculture.resource(name);
        var holder = DeferredHolder.create(Registries.BLOCK, id);
        ENTRIES.put(holder, block);
        return holder;
    }
}

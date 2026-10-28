package com.blakebr0.mysticalagriculture.init;

import java.util.function.Supplier;

import com.blakebr0.mysticalagriculture.MysticalAgriculture;
import com.blakebr0.mysticalagriculture.tileentity.AwakeningAltarTileEntity;
import com.blakebr0.mysticalagriculture.tileentity.AwakeningPedestalTileEntity;
import com.blakebr0.mysticalagriculture.tileentity.EnchanterTileEntity;
import com.blakebr0.mysticalagriculture.tileentity.EssenceFurnaceTileEntity;
import com.blakebr0.mysticalagriculture.tileentity.EssenceVesselTileEntity;
import com.blakebr0.mysticalagriculture.tileentity.HarvesterTileEntity;
import com.blakebr0.mysticalagriculture.tileentity.InfusionAltarTileEntity;
import com.blakebr0.mysticalagriculture.tileentity.InfusionPedestalTileEntity;
import com.blakebr0.mysticalagriculture.tileentity.ReprocessorTileEntity;
import com.blakebr0.mysticalagriculture.tileentity.SoulExtractorTileEntity;
import com.blakebr0.mysticalagriculture.tileentity.SouliumSpawnerTileEntity;
import com.blakebr0.mysticalagriculture.tileentity.TinkeringTableTileEntity;
import io.github.xponer2.translator.registry.DeferredHolder;
import io.github.xponer2.translator.registry.DeferredRegister;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;

import io.github.xponer2.translator.util.Registries;
import io.github.xponer2.translator.registry.BlockEntityType;

public final class ModTileEntities {
    public static final DeferredRegister<BlockEntityType<?>> REGISTRY = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, MysticalAgriculture.MOD_ID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<InfusionPedestalTileEntity>> INFUSION_PEDESTAL = register("infusion_pedestal", InfusionPedestalTileEntity::new, () -> new Block[] { ModBlocks.INFUSION_PEDESTAL.get() });
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<InfusionAltarTileEntity>> INFUSION_ALTAR = register("infusion_altar", InfusionAltarTileEntity::new, () -> new Block[] { ModBlocks.INFUSION_ALTAR.get() });
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<AwakeningPedestalTileEntity>> AWAKENING_PEDESTAL = register("awakening_pedestal", AwakeningPedestalTileEntity::new, () -> new Block[] { ModBlocks.AWAKENING_PEDESTAL.get() });
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<AwakeningAltarTileEntity>> AWAKENING_ALTAR = register("awakening_altar", AwakeningAltarTileEntity::new, () -> new Block[] { ModBlocks.AWAKENING_ALTAR.get() });
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<EssenceVesselTileEntity>> ESSENCE_VESSEL = register("essence_vessel", EssenceVesselTileEntity::new, () -> new Block[] { ModBlocks.ESSENCE_VESSEL.get() });
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TinkeringTableTileEntity>> TINKERING_TABLE = register("tinkering_table", TinkeringTableTileEntity::new, () -> new Block[] { ModBlocks.TINKERING_TABLE.get() });
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<EnchanterTileEntity>> ENCHANTER = register("enchanter", EnchanterTileEntity::new, () -> new Block[] { ModBlocks.ENCHANTER.get() });
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<EssenceFurnaceTileEntity>> FURNACE = register("furnace", EssenceFurnaceTileEntity::new, () -> new Block[] { ModBlocks.FURNACE.get() });
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ReprocessorTileEntity>> REPROCESSOR = register("seed_reprocessor", ReprocessorTileEntity::new, () -> new Block[] { ModBlocks.REPROCESSOR.get() });
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SoulExtractorTileEntity>> SOUL_EXTRACTOR = register("soul_extractor", SoulExtractorTileEntity::new, () -> new Block[] { ModBlocks.SOUL_EXTRACTOR.get() });
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<HarvesterTileEntity>> HARVESTER = register("harvester", HarvesterTileEntity::new, () -> new Block[] { ModBlocks.HARVESTER.get() });
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SouliumSpawnerTileEntity>> SOULIUM_SPAWNER = register("soulium_spawner", SouliumSpawnerTileEntity::new, () -> new Block[] { ModBlocks.SOULIUM_SPAWNER.get() });

    private static <T extends TileEntity> DeferredHolder<BlockEntityType<?>, BlockEntityType<T>> register(String name, BlockEntityType.BlockEntitySupplier<T> tile, Supplier<Block[]> blocks) {
        return REGISTRY.register(name, () -> BlockEntityType.Builder.of(tile, blocks.get()).build(null));
    }
}

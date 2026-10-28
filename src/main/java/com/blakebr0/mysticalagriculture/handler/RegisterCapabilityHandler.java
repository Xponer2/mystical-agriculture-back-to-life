package com.blakebr0.mysticalagriculture.handler;

import com.blakebr0.mysticalagriculture.init.ModTileEntities;
import com.blakebr0.mysticalagriculture.tileentity.EssenceFurnaceTileEntity;
import com.blakebr0.mysticalagriculture.tileentity.ReprocessorTileEntity;
import com.blakebr0.mysticalagriculture.tileentity.SoulExtractorTileEntity;
import com.blakebr0.mysticalagriculture.tileentity.SouliumSpawnerTileEntity;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import io.github.xponer2.translator.capabilities.Capabilities;
import io.github.xponer2.translator.capabilities.RegisterCapabilitiesEvent;

public final class RegisterCapabilityHandler {
    @SubscribeEvent
    public void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ModTileEntities.AWAKENING_ALTAR.get(), (block, direction) -> block.getInventory());
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ModTileEntities.AWAKENING_PEDESTAL.get(), (block, direction) -> block.getInventory());
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ModTileEntities.ESSENCE_VESSEL.get(), (block, direction) -> block.getInventory());
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ModTileEntities.FURNACE.get(), EssenceFurnaceTileEntity::getSidedInventory);
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ModTileEntities.HARVESTER.get(), (block, direction) -> block.getInventory());
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ModTileEntities.INFUSION_ALTAR.get(), (block, direction) -> block.getInventory());
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ModTileEntities.INFUSION_PEDESTAL.get(), (block, direction) -> block.getInventory());
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ModTileEntities.REPROCESSOR.get(), ReprocessorTileEntity::getSidedInventory);
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ModTileEntities.SOUL_EXTRACTOR.get(), SoulExtractorTileEntity::getSidedInventory);
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ModTileEntities.SOULIUM_SPAWNER.get(), SouliumSpawnerTileEntity::getSidedInventory);

        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, ModTileEntities.FURNACE.get(), (block, direction) -> block.getEnergy());
        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, ModTileEntities.HARVESTER.get(), (block, direction) -> block.getEnergy());
        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, ModTileEntities.REPROCESSOR.get(), (block, direction) -> block.getEnergy());
        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, ModTileEntities.SOUL_EXTRACTOR.get(), (block, direction) -> block.getEnergy());
        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, ModTileEntities.SOULIUM_SPAWNER.get(), (block, direction) -> block.getEnergy());
    }
}

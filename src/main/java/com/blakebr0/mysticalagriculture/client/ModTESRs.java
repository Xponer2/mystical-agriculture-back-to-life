package com.blakebr0.mysticalagriculture.client;

import com.blakebr0.mysticalagriculture.client.tesr.AwakeningAltarRenderer;
import com.blakebr0.mysticalagriculture.client.tesr.AwakeningPedestalRenderer;
import com.blakebr0.mysticalagriculture.client.tesr.EnchanterRenderer;
import com.blakebr0.mysticalagriculture.client.tesr.EssenceVesselRenderer;
import com.blakebr0.mysticalagriculture.client.tesr.InfusionAltarRenderer;
import com.blakebr0.mysticalagriculture.client.tesr.InfusionPedestalRenderer;
import com.blakebr0.mysticalagriculture.client.tesr.SouliumSpawnerRenderer;
import com.blakebr0.mysticalagriculture.client.tesr.TinkeringTableRenderer;
import com.blakebr0.mysticalagriculture.init.ModTileEntities;
import io.github.xponer2.translator.client.EntityRenderersEvent;
import io.github.xponer2.translator.client.TesrAdapter;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public final class ModTESRs {
    @SubscribeEvent
    public void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        TesrAdapter.register(ModTileEntities.INFUSION_PEDESTAL.get(), InfusionPedestalRenderer::new);
        TesrAdapter.register(ModTileEntities.INFUSION_ALTAR.get(), InfusionAltarRenderer::new);
        TesrAdapter.register(ModTileEntities.TINKERING_TABLE.get(), TinkeringTableRenderer::new);
        TesrAdapter.register(ModTileEntities.ENCHANTER.get(), EnchanterRenderer::new);
        TesrAdapter.register(ModTileEntities.AWAKENING_PEDESTAL.get(), AwakeningPedestalRenderer::new);
        TesrAdapter.register(ModTileEntities.AWAKENING_ALTAR.get(), AwakeningAltarRenderer::new);
        TesrAdapter.register(ModTileEntities.ESSENCE_VESSEL.get(), EssenceVesselRenderer::new);
        TesrAdapter.register(ModTileEntities.SOULIUM_SPAWNER.get(), SouliumSpawnerRenderer::new);
    }
}

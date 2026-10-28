package com.blakebr0.mysticalagriculture.client;

import com.blakebr0.mysticalagriculture.client.screen.EnchanterScreen;
import com.blakebr0.mysticalagriculture.client.screen.EssenceFurnaceScreen;
import com.blakebr0.mysticalagriculture.client.screen.HarvesterScreen;
import com.blakebr0.mysticalagriculture.client.screen.ReprocessorScreen;
import com.blakebr0.mysticalagriculture.client.screen.SoulExtractorScreen;
import com.blakebr0.mysticalagriculture.client.screen.SouliumSpawnerScreen;
import com.blakebr0.mysticalagriculture.client.screen.TinkeringTableScreen;
import com.blakebr0.mysticalagriculture.init.ModMenuTypes;
import io.github.xponer2.translator.menu.MenuScreens.RegisterMenuScreensEvent;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public final class ModMenuScreens {
    @SubscribeEvent
    public void onRegisterMenuScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenuTypes.TINKERING_TABLE.get(), TinkeringTableScreen::new);
        event.register(ModMenuTypes.ENCHANTER.get(), EnchanterScreen::new);
        event.register(ModMenuTypes.FURNACE.get(), EssenceFurnaceScreen::new);
        event.register(ModMenuTypes.REPROCESSOR.get(), ReprocessorScreen::new);
        event.register(ModMenuTypes.SOUL_EXTRACTOR.get(), SoulExtractorScreen::new);
        event.register(ModMenuTypes.HARVESTER.get(), HarvesterScreen::new);
        event.register(ModMenuTypes.SOULIUM_SPAWNER.get(), SouliumSpawnerScreen::new);
    }
}

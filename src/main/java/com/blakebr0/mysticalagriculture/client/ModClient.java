package com.blakebr0.mysticalagriculture.client;

import com.blakebr0.mysticalagriculture.client.handler.AOEAugmentClientHandler;
import com.blakebr0.mysticalagriculture.client.handler.AugmentTooltipHandler;
import com.blakebr0.mysticalagriculture.client.handler.ColorHandler;
import com.blakebr0.mysticalagriculture.client.handler.GuiOverlayHandler;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * The client part of the 1.21 mod constructor and client setup (a class of its own, so the dedicated server never
 * loads the client classes).
 */
@SideOnly(Side.CLIENT)
public final class ModClient {
    private ModClient() { }

    // 1.21 mod constructor (client): colors, models, renderers, screens and client extensions are registered by the
    // Translator's events
    public static void preInit() {
        MinecraftForge.EVENT_BUS.register(new ColorHandler());
        MinecraftForge.EVENT_BUS.register(new ModelHandler());
        MinecraftForge.EVENT_BUS.register(new ModTESRs());
        MinecraftForge.EVENT_BUS.register(new ModMenuScreens());
        MinecraftForge.EVENT_BUS.register(new ModClientExtensions());
    }

    // 1.21 client setup
    public static void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new AugmentTooltipHandler());
        MinecraftForge.EVENT_BUS.register(new GuiOverlayHandler());
        MinecraftForge.EVENT_BUS.register(new AOEAugmentClientHandler());

        ModelHandler.onClientSetup(event);
    }
}

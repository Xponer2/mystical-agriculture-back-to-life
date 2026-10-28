package com.blakebr0.mysticalagriculture.client;

import com.blakebr0.cucumber.client.extensions.WateringCanClientItemExtensions;
import com.blakebr0.mysticalagriculture.init.ModItems;
import io.github.xponer2.translator.client.RegisterClientExtensionsEvent;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public final class ModClientExtensions {
    @SubscribeEvent
    public void onRegisterClientExtensions(RegisterClientExtensionsEvent event) {
        event.registerItem(WateringCanClientItemExtensions.INSTANCE,
                ModItems.WATERING_CAN.get(), ModItems.INFERIUM_WATERING_CAN.get(), ModItems.PRUDENTIUM_WATERING_CAN.get(),
                ModItems.TERTIUM_WATERING_CAN.get(), ModItems.IMPERIUM_WATERING_CAN.get(), ModItems.SUPREMIUM_WATERING_CAN.get(),
                ModItems.AWAKENED_SUPREMIUM_WATERING_CAN.get()
        );
    }
}

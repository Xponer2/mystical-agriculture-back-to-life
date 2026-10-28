package com.blakebr0.mysticalagriculture.network;

import com.blakebr0.mysticalagriculture.network.payloads.ExperienceCapsulePickupPayload;
import com.blakebr0.mysticalagriculture.network.payloads.ReloadIngredientCachePayload;
import com.blakebr0.mysticalagriculture.network.payloads.SyncEssenceVesselColorsPayload;
import com.blakebr0.mysticalagriculture.network.payloads.UpdateAOEAugmentOffsetPayload;
import io.github.xponer2.translator.network.RegisterPayloadHandlersEvent;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public final class NetworkHandler {
    @SubscribeEvent
    public void onRegisterPayloadsHandlers(RegisterPayloadHandlersEvent event) {
        var registrar = event.registrar("1");

        registrar.playToClient(ExperienceCapsulePickupPayload.TYPE, ExperienceCapsulePickupPayload.STREAM_CODEC, ExperienceCapsulePickupPayload::handleClient);
        registrar.playToClient(ReloadIngredientCachePayload.TYPE, ReloadIngredientCachePayload.STREAM_CODEC, ReloadIngredientCachePayload::handleClient);
        registrar.playToClient(SyncEssenceVesselColorsPayload.TYPE, SyncEssenceVesselColorsPayload.STREAM_CODEC, SyncEssenceVesselColorsPayload::handleClient);

        registrar.playToServer(UpdateAOEAugmentOffsetPayload.TYPE, UpdateAOEAugmentOffsetPayload.STREAM_CODEC, UpdateAOEAugmentOffsetPayload::handle);
    }
}

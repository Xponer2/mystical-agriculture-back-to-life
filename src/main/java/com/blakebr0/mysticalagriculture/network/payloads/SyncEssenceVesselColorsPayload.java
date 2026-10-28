package com.blakebr0.mysticalagriculture.network.payloads;

import java.util.Map;

import com.blakebr0.mysticalagriculture.MysticalAgriculture;
import com.blakebr0.mysticalagriculture.crafting.EssenceVesselColorManager;
import io.github.xponer2.translator.network.CustomPacketPayload;
import io.github.xponer2.translator.network.IPayloadContext;

import io.github.xponer2.translator.network.RegistryFriendlyByteBuf;
import io.github.xponer2.translator.network.StreamCodec;

public final class SyncEssenceVesselColorsPayload implements CustomPacketPayload {
    private final Map<String, Integer> colors;

    public SyncEssenceVesselColorsPayload(Map<String, Integer> colors) {
        this.colors = colors;
    }

    public Map<String, Integer> colors() {
        return colors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SyncEssenceVesselColorsPayload)) return false;
        SyncEssenceVesselColorsPayload that = (SyncEssenceVesselColorsPayload) o;
        return java.util.Objects.equals(colors, that.colors);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(colors);
    }

    @Override
    public String toString() {
        return "SyncEssenceVesselColorsPayload[" + "colors=" + colors + "]";
    }

    public static final Type<SyncEssenceVesselColorsPayload> TYPE = new Type<>(MysticalAgriculture.resource("sync_essence_vessel_colors"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncEssenceVesselColorsPayload> STREAM_CODEC = StreamCodec.of(
            SyncEssenceVesselColorsPayload::toNetwork, SyncEssenceVesselColorsPayload::fromNetwork
    );

    @Override
    public Type<SyncEssenceVesselColorsPayload> type() {
        return TYPE;
    }

    private static SyncEssenceVesselColorsPayload fromNetwork(RegistryFriendlyByteBuf buffer) {
        var colors = buffer.readMap(RegistryFriendlyByteBuf::readUtf, RegistryFriendlyByteBuf::readVarInt);
        return new SyncEssenceVesselColorsPayload(colors);
    }

    private static void toNetwork(RegistryFriendlyByteBuf buffer, SyncEssenceVesselColorsPayload payload) {
        buffer.writeMap(payload.colors, RegistryFriendlyByteBuf::writeUtf, RegistryFriendlyByteBuf::writeVarInt);
    }

    public static void handleClient(SyncEssenceVesselColorsPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            EssenceVesselColorManager.INSTANCE.setColors(payload.colors);
        });
    }
}

package com.blakebr0.mysticalagriculture.network.payloads;

import com.blakebr0.cucumber.util.Utils;
import com.blakebr0.mysticalagriculture.MysticalAgriculture;
import io.github.xponer2.translator.network.CustomPacketPayload;
import io.github.xponer2.translator.network.IPayloadContext;

import net.minecraft.init.SoundEvents;

import io.github.xponer2.translator.network.RegistryFriendlyByteBuf;
import io.github.xponer2.translator.network.StreamCodec;

public final class ExperienceCapsulePickupPayload implements CustomPacketPayload {
    public ExperienceCapsulePickupPayload() {
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof ExperienceCapsulePickupPayload;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash();
    }

    @Override
    public String toString() {
        return "ExperienceCapsulePickupPayload[]";
    }

    public static final Type<ExperienceCapsulePickupPayload> TYPE = new Type<>(MysticalAgriculture.resource("experience_pickup"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ExperienceCapsulePickupPayload> STREAM_CODEC = StreamCodec.unit(new ExperienceCapsulePickupPayload());

    @Override
    public Type<ExperienceCapsulePickupPayload> type() {
        return TYPE;
    }

    public static void handleClient(ExperienceCapsulePickupPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            context.player().playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 0.1F, (Utils.RANDOM.nextFloat() - Utils.RANDOM.nextFloat()) * 0.35F + 0.9F);
        });
    }
}

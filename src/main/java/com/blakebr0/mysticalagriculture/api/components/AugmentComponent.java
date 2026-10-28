package com.blakebr0.mysticalagriculture.api.components;

import java.util.List;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.xponer2.translator.util.Codecs;

import net.minecraft.util.ResourceLocation;

import io.github.xponer2.translator.network.RegistryFriendlyByteBuf;
import io.github.xponer2.translator.network.ByteBufCodecs;
import io.github.xponer2.translator.network.StreamCodec;
import io.github.xponer2.translator.util.Codecs;

public final class AugmentComponent {
    private final ResourceLocation id;
    private final int slot;

    public AugmentComponent(ResourceLocation id, int slot) {
        this.id = id;
        this.slot = slot;
    }

    public ResourceLocation id() {
        return id;
    }

    public int slot() {
        return slot;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AugmentComponent)) return false;
        AugmentComponent that = (AugmentComponent) o;
        return java.util.Objects.equals(id, that.id)
                && slot == that.slot;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(id, slot);
    }

    @Override
    public String toString() {
        return "AugmentComponent[" + "id=" + id + ", " + "slot=" + slot + "]";
    }

    public static final MapCodec<AugmentComponent> MAP_CODEC = RecordCodecBuilder.mapCodec(builder ->
            builder.group(
                    Codecs.RESOURCE_LOCATION.fieldOf("id").forGetter(AugmentComponent::id),
                    Codec.INT.fieldOf("slot").forGetter(AugmentComponent::slot)
            ).apply(builder, AugmentComponent::new)
    );
    public static final Codec<AugmentComponent> CODEC = MAP_CODEC.codec();
    public static final StreamCodec<RegistryFriendlyByteBuf, AugmentComponent> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.RESOURCE_LOCATION,
            AugmentComponent::id,
            ByteBufCodecs.INT,
            AugmentComponent::slot,
            AugmentComponent::new
    );
    public static final Codec<List<AugmentComponent>> EQUIPPED_CODEC = Codec.list(CODEC);
    public static final StreamCodec<RegistryFriendlyByteBuf, List<AugmentComponent>> EQUIPPED_STREAM_CODEC = STREAM_CODEC.apply(ByteBufCodecs.list());
}

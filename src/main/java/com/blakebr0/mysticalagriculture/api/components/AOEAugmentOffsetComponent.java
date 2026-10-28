package com.blakebr0.mysticalagriculture.api.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import io.github.xponer2.translator.network.RegistryFriendlyByteBuf;
import io.github.xponer2.translator.network.ByteBufCodecs;
import io.github.xponer2.translator.network.StreamCodec;

public final class AOEAugmentOffsetComponent {
    private final int horizontalOffset;
    private final int verticalOffset;

    public AOEAugmentOffsetComponent(int horizontalOffset, int verticalOffset) {
        this.horizontalOffset = horizontalOffset;
        this.verticalOffset = verticalOffset;
    }

    public int horizontalOffset() {
        return horizontalOffset;
    }

    public int verticalOffset() {
        return verticalOffset;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AOEAugmentOffsetComponent)) return false;
        AOEAugmentOffsetComponent that = (AOEAugmentOffsetComponent) o;
        return horizontalOffset == that.horizontalOffset
                && verticalOffset == that.verticalOffset;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(horizontalOffset, verticalOffset);
    }

    @Override
    public String toString() {
        return "AOEAugmentOffsetComponent[" + "horizontalOffset=" + horizontalOffset + ", " + "verticalOffset=" + verticalOffset + "]";
    }

    public static final AOEAugmentOffsetComponent DEFAULT = new AOEAugmentOffsetComponent(0, 0);
    public static final MapCodec<AOEAugmentOffsetComponent> MAP_CODEC = RecordCodecBuilder.mapCodec(builder ->
        builder.group(
            Codec.INT.fieldOf("horizontal_offset").forGetter(AOEAugmentOffsetComponent::horizontalOffset),
            Codec.INT.fieldOf("vertical_offset").forGetter(AOEAugmentOffsetComponent::verticalOffset)
        ).apply(builder, AOEAugmentOffsetComponent::new)
    );
    public static final Codec<AOEAugmentOffsetComponent> CODEC = MAP_CODEC.codec();
    public static final StreamCodec<RegistryFriendlyByteBuf, AOEAugmentOffsetComponent> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.INT,
        AOEAugmentOffsetComponent::horizontalOffset,
        ByteBufCodecs.INT,
        AOEAugmentOffsetComponent::verticalOffset,
        AOEAugmentOffsetComponent::new
    );

    public boolean isOffset() {
        return this.horizontalOffset != 0 || this.verticalOffset != 0;
    }
}

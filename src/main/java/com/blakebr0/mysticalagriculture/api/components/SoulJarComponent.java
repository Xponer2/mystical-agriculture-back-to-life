package com.blakebr0.mysticalagriculture.api.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.xponer2.translator.util.Codecs;

import net.minecraft.util.ResourceLocation;

import io.github.xponer2.translator.network.RegistryFriendlyByteBuf;
import io.github.xponer2.translator.network.ByteBufCodecs;
import io.github.xponer2.translator.network.StreamCodec;
import io.github.xponer2.translator.util.Codecs;

public final class SoulJarComponent {
    private final ResourceLocation type;
    private final double souls;

    public SoulJarComponent(ResourceLocation type, double souls) {
        this.type = type;
        this.souls = souls;
    }

    public ResourceLocation type() {
        return type;
    }

    public double souls() {
        return souls;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SoulJarComponent)) return false;
        SoulJarComponent that = (SoulJarComponent) o;
        return java.util.Objects.equals(type, that.type)
                && Double.compare(souls, that.souls) == 0;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(type, souls);
    }

    @Override
    public String toString() {
        return "SoulJarComponent[" + "type=" + type + ", " + "souls=" + souls + "]";
    }

    public static final MapCodec<SoulJarComponent> MAP_CODEC = RecordCodecBuilder.mapCodec(builder ->
            builder.group(
                    Codecs.RESOURCE_LOCATION.fieldOf("type").forGetter(SoulJarComponent::type),
                    Codec.DOUBLE.fieldOf("souls").forGetter(SoulJarComponent::souls)
            ).apply(builder, SoulJarComponent::new)
    );
    public static final Codec<SoulJarComponent> CODEC = MAP_CODEC.codec();
    public static final StreamCodec<RegistryFriendlyByteBuf, SoulJarComponent> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.RESOURCE_LOCATION,
            SoulJarComponent::type,
            ByteBufCodecs.DOUBLE,
            SoulJarComponent::souls,
            SoulJarComponent::new
    );
}

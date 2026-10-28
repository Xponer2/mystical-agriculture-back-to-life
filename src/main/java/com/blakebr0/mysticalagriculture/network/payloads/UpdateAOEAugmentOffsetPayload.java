package com.blakebr0.mysticalagriculture.network.payloads;

import com.blakebr0.mysticalagriculture.MysticalAgriculture;
import com.blakebr0.mysticalagriculture.api.components.AOEAugmentOffsetComponent;
import com.blakebr0.mysticalagriculture.api.util.AugmentUtils;
import com.blakebr0.mysticalagriculture.init.ModDataComponentTypes;
import io.github.xponer2.translator.network.CustomPacketPayload;
import io.github.xponer2.translator.network.IPayloadContext;
import io.netty.buffer.ByteBuf;

import net.minecraft.util.math.MathHelper;

import io.github.xponer2.translator.network.RegistryFriendlyByteBuf;
import io.github.xponer2.translator.network.StreamCodec;
import io.github.xponer2.translator.component.StackComponents;

public final class UpdateAOEAugmentOffsetPayload implements CustomPacketPayload {
    private final int horizontalOffsetChange;
    private final int verticalOffsetChange;

    public UpdateAOEAugmentOffsetPayload(int horizontalOffsetChange, int verticalOffsetChange) {
        this.horizontalOffsetChange = horizontalOffsetChange;
        this.verticalOffsetChange = verticalOffsetChange;
    }

    public int horizontalOffsetChange() {
        return horizontalOffsetChange;
    }

    public int verticalOffsetChange() {
        return verticalOffsetChange;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UpdateAOEAugmentOffsetPayload)) return false;
        UpdateAOEAugmentOffsetPayload that = (UpdateAOEAugmentOffsetPayload) o;
        return horizontalOffsetChange == that.horizontalOffsetChange
                && verticalOffsetChange == that.verticalOffsetChange;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(horizontalOffsetChange, verticalOffsetChange);
    }

    @Override
    public String toString() {
        return "UpdateAOEAugmentOffsetPayload[" + "horizontalOffsetChange=" + horizontalOffsetChange + ", " + "verticalOffsetChange=" + verticalOffsetChange + "]";
    }

    public static final Type<UpdateAOEAugmentOffsetPayload> TYPE = new Type<>(MysticalAgriculture.resource("update_aoe_offset"));

    public static final StreamCodec<RegistryFriendlyByteBuf, UpdateAOEAugmentOffsetPayload> STREAM_CODEC = StreamCodec.of(
            UpdateAOEAugmentOffsetPayload::toNetwork, UpdateAOEAugmentOffsetPayload::fromNetwork
    );

    @Override
    public Type<UpdateAOEAugmentOffsetPayload> type() {
        return TYPE;
    }

    private static UpdateAOEAugmentOffsetPayload fromNetwork(ByteBuf buf) {
        return new UpdateAOEAugmentOffsetPayload(buf.readInt(), buf.readInt());
    }

    private static void toNetwork(ByteBuf buf, UpdateAOEAugmentOffsetPayload payload) {
        buf.writeInt(payload.horizontalOffsetChange);
        buf.writeInt(payload.verticalOffsetChange);
    }

    public static void handle(UpdateAOEAugmentOffsetPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            var stack = context.player().getHeldItemMainhand();
            var offset = StackComponents.getOrDefault(stack, ModDataComponentTypes.AOE_AUGMENT_OFFSET, AOEAugmentOffsetComponent.DEFAULT);
            var range = AugmentUtils.getMaxAOEAugmentRange(stack);

            StackComponents.set(stack, ModDataComponentTypes.AOE_AUGMENT_OFFSET, new AOEAugmentOffsetComponent(
                    MathHelper.clamp(offset.horizontalOffset() + payload.horizontalOffsetChange, -range, range),
                    MathHelper.clamp(offset.verticalOffset() + payload.verticalOffsetChange, -range, range)));
        });
    }
}

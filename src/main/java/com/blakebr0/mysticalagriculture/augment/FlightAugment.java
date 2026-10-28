package com.blakebr0.mysticalagriculture.augment;

import java.util.EnumSet;

import com.blakebr0.mysticalagriculture.MysticalAgriculture;
import com.blakebr0.mysticalagriculture.api.lib.AbilityCache;
import com.blakebr0.mysticalagriculture.api.tinkering.Augment;
import com.blakebr0.mysticalagriculture.api.tinkering.AugmentType;
import io.github.xponer2.translator.entity.AttributeModifier;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import io.github.xponer2.translator.entity.AttributeInstance;
import io.github.xponer2.translator.entity.Attributes;

public class FlightAugment extends Augment {
    private static final ResourceLocation ATTRIBUTE_ID = MysticalAgriculture.resource("flight_augment");

    public FlightAugment(ResourceLocation id, int tier) {
        super(id, tier, EnumSet.of(AugmentType.CHESTPLATE), 0xCBD6D6, 0x556B6B);
    }

    @Override
    public void onPlayerTick(World level, EntityPlayer player, AbilityCache cache) {
        var abilities = player.capabilities;

        if (!cache.isCached(this, player)) {
            var flight = AttributeInstance.of(player, Attributes.CREATIVE_FLIGHT);
            if (flight == null)
                return;

            flight.addOrReplacePermanentModifier(new AttributeModifier(ATTRIBUTE_ID, 1, AttributeModifier.Operation.ADD_VALUE));

            cache.add(this, player, () -> {
                if (!abilities.isCreativeMode && !player.isSpectator()) {
                    abilities.isFlying = false;

                    if (player instanceof EntityPlayerMP serverPlayer) {
                        serverPlayer.sendPlayerAbilities();
                    }
                }

                flight.removeModifier(ATTRIBUTE_ID);
            });
        }
    }
}

package com.blakebr0.mysticalagriculture.augment;

import java.util.EnumSet;

import com.blakebr0.mysticalagriculture.MysticalAgriculture;
import com.blakebr0.mysticalagriculture.api.lib.AbilityCache;
import com.blakebr0.mysticalagriculture.api.tinkering.Augment;
import com.blakebr0.mysticalagriculture.api.tinkering.AugmentType;
import io.github.xponer2.translator.entity.AttributeModifier;
import io.github.xponer2.translator.entity.Attributes;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import io.github.xponer2.translator.entity.AttributeInstance;

public class StepAssistAugment extends Augment {
    private static final ResourceLocation ATTRIBUTE_ID = MysticalAgriculture.resource("step_assist_augment");

    public StepAssistAugment(ResourceLocation id, int tier) {
        super(id, tier, EnumSet.of(AugmentType.LEGGINGS, AugmentType.BOOTS), 0xFC4F00, 0x602600);
    }

    @Override
    public void onPlayerTick(World level, EntityPlayer player, AbilityCache cache) {
        if (!player.isSneaking() && !cache.isCached(this, player)) {
            var height = AttributeInstance.of(player, Attributes.STEP_HEIGHT);
            if (height == null)
                return;

            var hasStepAssist = height.getModifier(ATTRIBUTE_ID) != null;
            if (!hasStepAssist) {
                height.addPermanentModifier(new AttributeModifier(ATTRIBUTE_ID, 1, AttributeModifier.Operation.ADD_VALUE));
            }

            cache.add(this, player, () -> {
                height.removeModifier(ATTRIBUTE_ID);
            });
        }

        if (player.isSneaking() && cache.isCached(this, player)) {
            cache.remove(this, player);
        }
    }
}

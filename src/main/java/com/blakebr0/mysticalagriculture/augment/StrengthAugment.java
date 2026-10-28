package com.blakebr0.mysticalagriculture.augment;

import java.util.EnumSet;
import java.util.List;

import com.blakebr0.cucumber.helper.ColorHelper;
import com.blakebr0.mysticalagriculture.MysticalAgriculture;
import com.blakebr0.mysticalagriculture.api.tinkering.Augment;
import com.blakebr0.mysticalagriculture.api.tinkering.AugmentAttributeModifier;
import com.blakebr0.mysticalagriculture.api.tinkering.AugmentType;
import com.google.common.collect.ImmutableList;
import io.github.xponer2.translator.entity.AttributeModifier;
import io.github.xponer2.translator.entity.Attributes;
import io.github.xponer2.translator.entity.EquipmentSlotGroup;

import net.minecraft.util.ResourceLocation;

public class StrengthAugment extends Augment {
    private static final ResourceLocation ATTRIBUTE_ID = MysticalAgriculture.resource("strength_augment");
    private final List<AugmentAttributeModifier> attributes;

    public StrengthAugment(ResourceLocation id, int tier, int amplifier) {
        super(id, tier, EnumSet.of(AugmentType.SWORD), getColor(0xFFFD90, tier), getColor(0xCC8E27, tier));
        this.attributes = ImmutableList.of(
                new AugmentAttributeModifier(Attributes.ATTACK_DAMAGE, new AttributeModifier(ATTRIBUTE_ID, 5 * amplifier, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
        );
    }

    @Override
    public List<AugmentAttributeModifier> getAttributeModifiers() {
        return this.attributes;
    }

    private static int getColor(int color, int tier) {
        return ColorHelper.saturate(color, Math.min((float) tier / 5, 1));
    }
}

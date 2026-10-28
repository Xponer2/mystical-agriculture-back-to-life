package com.blakebr0.mysticalagriculture.api.tinkering;

import io.github.xponer2.translator.entity.Attribute;
import io.github.xponer2.translator.entity.AttributeModifier;
import io.github.xponer2.translator.entity.EquipmentSlotGroup;
import io.github.xponer2.translator.registry.Holder;

/**
 * Contains the necessary parameters to apply a new AttributeModifier to an item provided by an {@link Augment}
 * @param attribute the entity attribute
 * @param modifier the attribute modifier
 * @param slot the applicable slot group
 */
public final class AugmentAttributeModifier {
    private final Holder<Attribute> attribute;
    private final AttributeModifier modifier;
    private final EquipmentSlotGroup slot;

    public AugmentAttributeModifier(Holder<Attribute> attribute, AttributeModifier modifier, EquipmentSlotGroup slot) {
        this.attribute = attribute;
        this.modifier = modifier;
        this.slot = slot;
    }

    public Holder<Attribute> attribute() {
        return attribute;
    }

    public AttributeModifier modifier() {
        return modifier;
    }

    public EquipmentSlotGroup slot() {
        return slot;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AugmentAttributeModifier)) return false;
        AugmentAttributeModifier that = (AugmentAttributeModifier) o;
        return java.util.Objects.equals(attribute, that.attribute)
                && java.util.Objects.equals(modifier, that.modifier)
                && java.util.Objects.equals(slot, that.slot);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(attribute, modifier, slot);
    }

    @Override
    public String toString() {
        return "AugmentAttributeModifier[" + "attribute=" + attribute + ", " + "modifier=" + modifier + ", " + "slot=" + slot + "]";
    }
 }

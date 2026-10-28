package com.blakebr0.mysticalagriculture.augment;

import java.util.EnumSet;

import com.blakebr0.mysticalagriculture.api.tinkering.Augment;
import com.blakebr0.mysticalagriculture.api.tinkering.AugmentType;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class BlindnessResistanceAugment extends Augment {
    public BlindnessResistanceAugment(ResourceLocation id, int tier) {
        super(id, tier, EnumSet.of(AugmentType.HELMET), 0x500080, 0x270040);
    }

    @Override
    public void onArmorTick(ItemStack stack, World level, EntityPlayer player) {
        if (player.isPotionActive(MobEffects.BLINDNESS)) {
            player.removePotionEffect(MobEffects.BLINDNESS);
        }
    }
}

package com.blakebr0.mysticalagriculture.augment;

import java.util.EnumSet;

import com.blakebr0.mysticalagriculture.api.tinkering.Augment;
import com.blakebr0.mysticalagriculture.api.tinkering.AugmentType;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class SlownessResistanceAugment extends Augment {
    public SlownessResistanceAugment(ResourceLocation id, int tier) {
        super(id, tier, EnumSet.of(AugmentType.LEGGINGS), 0x333333, 0x111111);
    }

    @Override
    public void onArmorTick(ItemStack stack, World level, EntityPlayer player) {
        if (player.isPotionActive(MobEffects.SLOWNESS)) {
            player.removePotionEffect(MobEffects.SLOWNESS);
        }
    }
}

package com.blakebr0.mysticalagriculture.augment;

import java.util.EnumSet;

import com.blakebr0.mysticalagriculture.api.tinkering.Augment;
import com.blakebr0.mysticalagriculture.api.tinkering.AugmentType;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class NightVisionAugment extends Augment {
    public NightVisionAugment(ResourceLocation id, int tier) {
        super(id, tier, EnumSet.of(AugmentType.HELMET), 0xEEE050, 0x2B1E74);
    }

    @Override
    public void onArmorTick(ItemStack stack, World level, EntityPlayer player) {
        player.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 240, 0, true, false));
    }
}

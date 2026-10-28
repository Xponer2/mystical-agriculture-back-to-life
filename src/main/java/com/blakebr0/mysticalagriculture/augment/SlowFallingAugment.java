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

public class SlowFallingAugment extends Augment {
    public SlowFallingAugment(ResourceLocation id, int tier) {
        super(id, tier, EnumSet.of(AugmentType.BOOTS), 0xFFEFD1, 0xA5957A);
    }

    @Override
    public void onArmorTick(ItemStack stack, World level, EntityPlayer player) {
        if (!player.isSneaking()) {
            { if (player.motionY < -0.08D) player.motionY = -0.08D; player.fallDistance = 0.0F; }
        }
    }
}

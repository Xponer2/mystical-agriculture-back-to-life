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

public class FireResistanceAugment extends Augment {
    public FireResistanceAugment(ResourceLocation id, int tier) {
        super(id, tier, EnumSet.of(AugmentType.ARMOR), 0xFFAA3F, 0x623E4E);
    }

    @Override
    public void onArmorTick(ItemStack stack, World level, EntityPlayer player) {
        player.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 5, 0, true, false));

        if (player.isBurning()) {
            player.extinguish();
        }
    }
}

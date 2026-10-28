package com.blakebr0.mysticalagriculture.augment;

import java.util.EnumSet;

import com.blakebr0.mysticalagriculture.api.tinkering.Augment;
import com.blakebr0.mysticalagriculture.api.tinkering.AugmentType;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class NauseaResistanceAugment extends Augment {
    public NauseaResistanceAugment(ResourceLocation id, int tier) {
        super(id, tier, EnumSet.of(AugmentType.HELMET), 0x66BF00, 0x1F8057);
    }

    @Override
    public void onArmorTick(ItemStack stack, World level, EntityPlayer player) {
        if (player.isPotionActive(MobEffects.NAUSEA)) {
            player.removePotionEffect(MobEffects.NAUSEA);
        }
    }
}

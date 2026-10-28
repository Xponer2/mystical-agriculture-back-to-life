package com.blakebr0.mysticalagriculture.augment;

import java.util.EnumSet;

import com.blakebr0.cucumber.helper.ColorHelper;
import com.blakebr0.mysticalagriculture.api.tinkering.Augment;
import com.blakebr0.mysticalagriculture.api.tinkering.AugmentType;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class AbsorptionAugment extends Augment {
    private static final String PERSISTENT_DATA_KEY = "MA_AbsorptionAugment";
    private final int amplifier;

    public AbsorptionAugment(ResourceLocation id, int tier, int amplifier) {
        super(id, tier, EnumSet.of(AugmentType.ARMOR), getColor(0x7E95A5, tier), getColor(0x3AAAC4, tier));
        this.amplifier = amplifier;
    }

    @Override
    public void onArmorTick(ItemStack stack, World level, EntityPlayer player) {
        if (level.isRemote || player.isPotionActive(MobEffects.ABSORPTION))
            return;

        if (getLastApplicationTime(player) + 9600L < level.getTotalWorldTime()) {
            player.addPotionEffect(new PotionEffect(MobEffects.ABSORPTION, 9600, this.amplifier, true, false));
            setLastApplicationTime(player, level.getTotalWorldTime());
        }
    }

    private static int getColor(int color, int tier) {
        return ColorHelper.saturate(color, Math.min((float) tier / 5, 1));
    }

    private static long getLastApplicationTime(EntityPlayer player) {
        return player.getEntityData().getLong(PERSISTENT_DATA_KEY);
    }

    private static void setLastApplicationTime(EntityPlayer player, long time) {
        player.getEntityData().setLong(PERSISTENT_DATA_KEY, time);
    }
}

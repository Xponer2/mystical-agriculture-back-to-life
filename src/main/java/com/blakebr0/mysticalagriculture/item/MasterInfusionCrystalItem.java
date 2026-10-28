package com.blakebr0.mysticalagriculture.item;

import com.blakebr0.cucumber.item.BaseReusableItem;

import net.minecraft.item.ItemStack;

public class MasterInfusionCrystalItem extends BaseReusableItem {
    public MasterInfusionCrystalItem() {
        super(0);
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
        return true;
    }
}

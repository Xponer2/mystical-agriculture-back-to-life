package com.blakebr0.mysticalagriculture.api.machine;

import org.jetbrains.annotations.Nullable;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.ItemStackHandler;

/**
 * An implementation of {@link ItemStackHandler} for {@link IMachineUpgrade}s.
 */
public class MachineUpgradeItemStackHandler extends ItemStackHandler {
    @Override
    public int getSlotLimit(int slot) {
        return 1;
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        var item = stack.getItem();
        return item instanceof IMachineUpgrade;
    }

    /**
     * Gets the {@link MachineUpgradeTier} for the upgrade in this inventory, or null if empty
     * @return the machine upgrade tier
     */
    @Nullable
    public MachineUpgradeTier getUpgradeTier() {
        var item = this.getStackInSlot(0).getItem();
        if (item instanceof IMachineUpgrade upgrade)
            return upgrade.getTier();

        return null;
    }

    public NonNullList<ItemStack> getStacks() {
        return this.stacks;
    }
}

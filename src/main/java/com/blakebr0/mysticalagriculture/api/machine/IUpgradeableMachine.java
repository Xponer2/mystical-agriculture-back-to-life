package com.blakebr0.mysticalagriculture.api.machine;

import org.jetbrains.annotations.Nullable;

import net.minecraft.item.ItemStack;

/**
 * An interface for machine block entities that can be upgraded with {@link IMachineUpgrade}s.
 */
public interface IUpgradeableMachine {
    MachineUpgradeItemStackHandler getUpgradeInventory();

    /**
     * Gets the {@link MachineUpgradeTier} for the item in the inventory, or null if there's no upgrade
     * @return the machine upgrade tier
     */
    @Nullable
    default MachineUpgradeTier getMachineTier() {
        return getUpgradeInventory().getUpgradeTier();
    }

    /**
     * Applies the given upgrade to this machine
     * @param item the upgrade to apply
     * @return the current upgrade in the inventory
     */
    default ItemStack applyUpgrade(IMachineUpgrade item) {
        var inventory = getUpgradeInventory();
        var current = inventory.getStackInSlot(0);

        inventory.setStackInSlot(0, new ItemStack((net.minecraft.item.Item) item));
        
        return current;
    }

    /**
     * Checks if the tier of upgrade can be applied to this machine
     * @param tier the tier to upgrade to
     * @return can be applied
     */
    default boolean canApplyUpgrade(MachineUpgradeTier tier) {
        return getMachineTier() == null || getMachineTier().getValue() < tier.getValue();
    }
}

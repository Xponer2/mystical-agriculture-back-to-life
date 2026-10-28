package com.blakebr0.mysticalagriculture.api.machine;

import io.github.xponer2.translator.util.ItemLike;

/**
 * Implement this interface on Machine Upgrade items
 */
public interface IMachineUpgrade extends ItemLike {
    /**
     * Get the tier/group this machine upgrade belongs to
     * @return the tier of this machine upgrade
     */
    MachineUpgradeTier getTier();
}

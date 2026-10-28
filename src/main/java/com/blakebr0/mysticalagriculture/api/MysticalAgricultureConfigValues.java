package com.blakebr0.mysticalagriculture.api;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import org.jetbrains.annotations.ApiStatus;

public final class MysticalAgricultureConfigValues {
    private final DoubleSupplier inferiumDropChance;
    private final DoubleSupplier fertilizedEssenceDropChance;
    private final BooleanSupplier secondarySeedDrops;
    private final BooleanSupplier requiresEffectiveFarmland;
    private final BooleanSupplier enchantableSupremiumTools;
    private final BooleanSupplier unbreakableSupremiumArmor;
    private final BooleanSupplier fakePlayerWatering;

    @ApiStatus.Internal
    public MysticalAgricultureConfigValues(
            DoubleSupplier inferiumDropChance,
            DoubleSupplier fertilizedEssenceDropChance,
            BooleanSupplier secondarySeedDrops,
            BooleanSupplier requiresEffectiveFarmland,
            BooleanSupplier enchantableSupremiumTools,
            BooleanSupplier unbreakableSupremiumArmor,
            BooleanSupplier fakePlayerWatering
    ) {
        this.inferiumDropChance = inferiumDropChance;
        this.fertilizedEssenceDropChance = fertilizedEssenceDropChance;
        this.secondarySeedDrops = secondarySeedDrops;
        this.requiresEffectiveFarmland = requiresEffectiveFarmland;
        this.enchantableSupremiumTools = enchantableSupremiumTools;
        this.unbreakableSupremiumArmor = unbreakableSupremiumArmor;
        this.fakePlayerWatering = fakePlayerWatering;
    }

    public double getInferiumDropChance() {
        return this.inferiumDropChance.getAsDouble();
    }

    public double getFertilizedEssenceDropChance() {
        return this.fertilizedEssenceDropChance.getAsDouble();
    }

    public boolean isSecondarySeedDropsEnabled() {
        return this.secondarySeedDrops.getAsBoolean();
    }

    public boolean isRequiresEffectiveFarmlandEnabled() {
        return this.requiresEffectiveFarmland.getAsBoolean();
    }

    public boolean isEnchantableSupremiumToolsEnabled() {
        return this.enchantableSupremiumTools.getAsBoolean();
    }

    public boolean isUnbreakableSupremiumArmorEnabled() {
        return this.unbreakableSupremiumArmor.getAsBoolean();
    }

    public boolean isFakePlayerWateringEnabled() {
        return this.fakePlayerWatering.getAsBoolean();
    }
}

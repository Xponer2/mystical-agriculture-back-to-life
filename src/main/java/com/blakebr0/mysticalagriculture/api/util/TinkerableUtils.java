package com.blakebr0.mysticalagriculture.api.util;

import com.blakebr0.mysticalagriculture.api.crop.CropTier;
import com.blakebr0.mysticalagriculture.api.tinkering.ITinkerable;
import io.github.xponer2.translator.text.LiteralComponent;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class TinkerableUtils {
    private static final EntityEquipmentSlot[] ARMOR_EQUIPMENT_SLOTS = { EntityEquipmentSlot.HEAD, EntityEquipmentSlot.CHEST, EntityEquipmentSlot.LEGS, EntityEquipmentSlot.FEET };

    /**
     * Gets the {@link ITinkerable} instance from the provided item stack if applicable
     *
     * @param stack the item
     * @return the {@link ITinkerable} or null
     */
    public static ITinkerable getTinkerable(ItemStack stack) {
        var item = stack.getItem();
        return item instanceof ITinkerable tinkerable ? tinkerable : null;
    }

    /**
     * Gets the minimum {@link ITinkerable} tier for the player's equipped armor
     *
     * @param player the player
     * @return the minimum {@link ITinkerable} tier, or -1 if not wearing a full set
     */
    public static int getArmorSetMinimumTier(EntityPlayer player) {
        int tier = -1;

        for (int i = 0; i < 4; i++) {
            var stack = player.getItemStackFromSlot(ARMOR_EQUIPMENT_SLOTS[i]);
            var tinkerable = getTinkerable(stack);

            if (tinkerable == null)
                return -1;

            var tinkerableTier = tinkerable.getTinkerableTier();

            if (tinkerableTier > tier)
                tier = tinkerableTier;
        }

        return tier;
    }

    /**
     * Checks if the provided player has a full set of the minimum {@link ITinkerable} tier equipped
     *
     * @param player the player
     * @param tier   the {@link ITinkerable} tier
     * @return has the minimum tier equipped
     */
    public static boolean hasArmorSetMinimumTier(EntityPlayer player, int tier) {
        return getArmorSetMinimumTier(player) >= tier;
    }

    /**
     * Get the tooltip color for the provided int tier
     *
     * @param tier the tier
     * @return the color
     */
    public static TextFormatting getColorForTier(int tier) {
        return switch (tier) {
            case 1 -> CropTier.ONE.getTextColor();
            case 2 -> CropTier.TWO.getTextColor();
            case 3 -> CropTier.THREE.getTextColor();
            case 4 -> CropTier.FOUR.getTextColor();
            case 5 -> CropTier.FIVE.getTextColor();
            default -> TextFormatting.GRAY;
        };
    }

    /**
     * Gets the text component variant of the provided tier number for use in tooltips
     *
     * @param tier the tier
     * @return the formatted tier
     */
    public static ITextComponent getTooltipForTier(int tier) {
        return new LiteralComponent(String.valueOf(tier)).withStyle(getColorForTier(tier));
    }
}

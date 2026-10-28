package com.blakebr0.mysticalagriculture.api.farmland;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

import io.github.xponer2.translator.util.UseOnContext;

/**
 * Implement this on items that are used to convert vanilla farmland to essence farmland
 * Make sure to call {@link FarmlandConverter#convert} in your item's {@link Item#useOn(UseOnContext)}
 */
public interface IFarmlandConverter {
    /**
     * Gets the farmland block that this converter should convert farmland to
     * @return the converted farmland block
     */
    Block getConvertedFarmland();
}

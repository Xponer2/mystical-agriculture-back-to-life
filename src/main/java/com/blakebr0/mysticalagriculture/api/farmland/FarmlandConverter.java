package com.blakebr0.mysticalagriculture.api.farmland;

import com.blakebr0.mysticalagriculture.api.crop.ICropTierProvider;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFarmland;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.SoundCategory;

import io.github.xponer2.translator.util.UseOnContext;

public final class FarmlandConverter {
    /**
     * Call this using {@link Item#useOn(UseOnContext)} to allow default farmland conversion mechanics
     */
    public static EnumActionResult convert(IFarmlandConverter converter, UseOnContext context) {
        var pos = context.getClickedPos();
        var level = context.getLevel();
        var stack = context.getHeldItem();
        var player = context.getPlayer();
        var state = level.getBlockState(pos);
        var block = state.getBlock();

        if (block == Blocks.FARMLAND) {
            var newState = converter.getConvertedFarmland().getDefaultState().withProperty(BlockFarmland.MOISTURE, state.getValue(BlockFarmland.MOISTURE));

            level.setBlockState(pos, newState);
            level.playSound(player, pos, SoundEvents.BLOCK_SAND_BREAK, SoundCategory.BLOCKS, 1.0F, 1.0F);

            stack.shrink(1);

            return EnumActionResult.SUCCESS;
        } else if (block instanceof IEssenceFarmland farmland) {
            var item = stack.getItem();

            if (item instanceof ICropTierProvider provider) {
                var tier = provider.getTier();

                if (tier != farmland.getTier()) {
                    var newState = converter.getConvertedFarmland().getDefaultState().withProperty(BlockFarmland.MOISTURE, state.getValue(BlockFarmland.MOISTURE));

                    level.setBlockState(pos, newState);
                    level.playSound(player, pos, SoundEvents.BLOCK_SAND_BREAK, SoundCategory.BLOCKS, 1.0F, 1.0F);

                    stack.shrink(1);

                    if (Math.random() < 0.25) {
                        Block.spawnAsEntity(level, pos.up(), new ItemStack(farmland.getTier().getEssence()));
                    }

                    return EnumActionResult.SUCCESS;
                }
            }
        }

        return EnumActionResult.PASS;
    }
}

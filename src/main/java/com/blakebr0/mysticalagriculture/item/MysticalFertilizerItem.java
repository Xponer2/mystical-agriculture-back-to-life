package com.blakebr0.mysticalagriculture.item;

import java.util.List;

import io.github.xponer2.translator.block.BlockSource;
import io.github.xponer2.translator.block.OptionalDispenseItemBehavior;
import io.github.xponer2.translator.util.EventHooks;
import com.blakebr0.cucumber.item.BaseItem;
import com.blakebr0.mysticalagriculture.api.crop.ICropProvider;
import com.blakebr0.mysticalagriculture.lib.ModTooltips;

import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.IGrowable;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import io.github.xponer2.translator.util.UseOnContext;
import io.github.xponer2.translator.item.TooltipContext;

public class MysticalFertilizerItem extends BaseItem {
    public MysticalFertilizerItem() {
        super();

        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(this, new DispenserBehavior());
    }

    public EnumActionResult useOn(UseOnContext context) {
        var stack = context.getHeldItem();
        var pos = context.getClickedPos();
        var player = context.getPlayer();
        var level = context.getLevel();

        if (applyFertilizer(stack, level, pos, player)) {
            if (!level.isRemote) {
                level.playEvent(1505, pos, 0);
            }

            return EnumActionResult.SUCCESS;
        }

        return EnumActionResult.PASS;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<ITextComponent> tooltip, ITooltipFlag flag) {
        tooltip.add(ModTooltips.MYSTICAL_FERTILIZER.build());
    }

    public static boolean applyFertilizer(ItemStack stack, World level, BlockPos pos, EntityPlayer player) {
        var state = level.getBlockState(pos);

        {
            var event = EventHooks.fireBonemealEvent(player, level, pos, state, stack);
            if (event.isCanceled()) {
                return event.isSuccessful();
            }
        }

        var block = state.getBlock();

        if (block instanceof IGrowable growable && growable.canGrow(level, pos, state, level.isRemote)) {
            if (!level.isRemote) {
                var rand = level.rand;

                if (growable.canUseBonemeal(level, rand, pos, state) || canGrowResourceCrops(growable) || growable instanceof BlockSapling) {
                    var serverWorld = (WorldServer) level;

                    if (growable instanceof BlockCrops crop) {
                        level.setBlockState(pos, crop.withAge(crop.getMaxAge()), 2);
                    } else if (growable instanceof BlockSapling sapling) {
                        // generateTree fires the 1.12.2 sapling event itself
                        sapling.generateTree(serverWorld, pos, state, rand);
                    } else {
                        growable.grow(serverWorld, rand, pos, state);
                    }
                }

                stack.shrink(1);
            }

            return true;
        }

        return false;
    }

    private static boolean canGrowResourceCrops(IGrowable growable) {
        return growable instanceof ICropProvider cropGetter && cropGetter.getMysticalCrop().getTier().isFertilizable();
    }

    public static class DispenserBehavior extends OptionalDispenseItemBehavior {
        @Override
        protected ItemStack execute(BlockSource source, ItemStack stack) {
            this.setSuccess(true);

            var level = source.level();
            var pos = source.pos().offset(source.state().getValue(BlockDispenser.FACING));

            if (MysticalFertilizerItem.applyFertilizer(stack, level, pos, null)) {
                if (!level.isRemote) {
                    level.playEvent(2005, pos, 0);
                }
            } else {
                this.setSuccess(false);
            }

            return stack;
        }
    }

    @Override
    public EnumActionResult onItemUse(net.minecraft.entity.player.EntityPlayer player, net.minecraft.world.World world, net.minecraft.util.math.BlockPos pos,
            net.minecraft.util.EnumHand hand, net.minecraft.util.EnumFacing facing, float hitX, float hitY, float hitZ) {
        return this.useOn(io.github.xponer2.translator.util.UseOnContext.of(player, world, pos, hand, facing, hitX, hitY, hitZ));
    }
}

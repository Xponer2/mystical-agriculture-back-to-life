package com.blakebr0.mysticalagriculture.augment;

import java.util.EnumSet;

import com.blakebr0.cucumber.helper.ColorHelper;
import com.blakebr0.mysticalagriculture.api.tinkering.AOEAugment;
import com.blakebr0.mysticalagriculture.api.tinkering.AugmentType;

import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;

import io.github.xponer2.translator.util.UseOnContext;
import io.github.xponer2.translator.util.Platform;

public class PathingAOEAugment extends AOEAugment {
    public PathingAOEAugment(ResourceLocation id, int tier, int range) {
        super(id, tier, EnumSet.of(AugmentType.SHOVEL), getColor(0xAA8D4A, tier), getColor(0x856B3A, tier), range);
    }

    @Override
    public boolean onItemUse(UseOnContext context) {
        var player = context.getPlayer();

        if (player == null)
            return false;

        var level = context.getLevel();
        var pos = context.getClickedPos();

        var playedSound = false;

        if (path(context, pos)) {
            level.playSound(player, pos, SoundEvents.ITEM_SHOVEL_FLATTEN, SoundCategory.BLOCKS, 1.0F, 1.0F);

            playedSound = true;

            if (!player.isSneaking())
                return false;
        }

        if (player.isSneaking()) {
            var positions = getAOEBlocks(player.getHeldItemMainhand(), this.range, pos, EnumFacing.UP, player).iterator();

            while (positions.hasNext()) {
                var aoePos = positions.next();

                if (path(context, aoePos) && !playedSound) {
                    level.playSound(player, pos, SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS, 1.0F, 1.0F);

                    playedSound = true;
                }
            }
        }

        return true;
    }

    private static boolean path(UseOnContext context, BlockPos pos) {
        var level = context.getLevel();
        var direction = context.getClickedFace();

        if (direction != EnumFacing.DOWN && level.isAirBlock(pos.up())) {
            var modifiedState = Platform.toolModifiedState(level.getBlockState(pos), "SHOVEL_FLATTEN");
            if (modifiedState != null) {
                if (!level.isRemote) {
                    var stack = context.getHeldItem();
                    var player = context.getPlayer();

                    level.setBlockState(pos, modifiedState, 11);

                    if (player != null) {
                        stack.damageItem(1, player);
                    }
                }

                return true;
            }
        }

        return false;
    }

    private static int getColor(int color, int tier) {
        return ColorHelper.saturate(color, Math.min((float) tier / 5, 1));
    }
}

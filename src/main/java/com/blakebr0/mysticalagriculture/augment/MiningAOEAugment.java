package com.blakebr0.mysticalagriculture.augment;

import java.util.EnumSet;

import com.blakebr0.cucumber.helper.BlockHelper;
import com.blakebr0.cucumber.helper.ColorHelper;
import com.blakebr0.mysticalagriculture.api.tinkering.AOEAugment;
import com.blakebr0.mysticalagriculture.api.tinkering.AugmentType;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MiningAOEAugment extends AOEAugment {
    public MiningAOEAugment(ResourceLocation id, int tier, int range) {
        super(id, tier, EnumSet.of(AugmentType.PICKAXE, AugmentType.AXE, AugmentType.SHOVEL), getColor(0xD5FFF6, tier), getColor(0x0EBABD, tier), range);
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World level, IBlockState state, BlockPos pos, EntityLivingBase entity) {
        if (level.isRemote)
            return false;

        if (entity.isSneaking())
            return false;

        if (entity instanceof EntityPlayer player) {
            var trace = BlockHelper.rayTraceBlocks(level, player);
            var side = trace.sideHit;

            harvestAOEBlocks(stack, this.range, level, pos, side, player);
        }

        return false;
    }

    private static void harvestAOEBlocks(ItemStack stack, int radius, World level, BlockPos pos, EnumFacing side, EntityPlayer player) {
        var state = level.getBlockState(pos);
        var hardness = state.getBlockHardness(level, pos);

        if (radius > 0 && hardness >= 0.2F && canHarvestBlock(stack, state)) {
            getAOEBlocks(stack, radius, pos, side, player).forEach(aoePos -> {
                if (aoePos != pos) {
                    var aoeState = level.getBlockState(aoePos);

                    if (canHarvestBlock(stack, aoeState) && !aoeState.getBlock().hasTileEntity(aoeState) && aoeState.getBlockHardness(level, aoePos) <= hardness + 5.0F) {
                        BlockHelper.harvestAOEBlock(stack, level, (EntityPlayerMP) player, aoePos.toImmutable());
                    }
                }
            });
        }
    }

    private static boolean canHarvestBlock(ItemStack stack, IBlockState state) {
        return stack.canHarvestBlock(state) || (!!state.getMaterial().isToolNotRequired() && stack.getDestroySpeed(state) > 1.0F);
    }

    private static int getColor(int color, int tier) {
        return ColorHelper.saturate(color, Math.min((float) tier / 5, 1));
    }
}

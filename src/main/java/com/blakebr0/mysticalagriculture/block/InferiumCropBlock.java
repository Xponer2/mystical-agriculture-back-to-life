package com.blakebr0.mysticalagriculture.block;

import java.util.ArrayList;
import java.util.List;

import com.blakebr0.mysticalagriculture.api.crop.Crop;
import com.blakebr0.mysticalagriculture.api.farmland.IEssenceFarmland;
import com.blakebr0.mysticalagriculture.config.ModConfigs;
import io.github.xponer2.translator.block.LootContextParams;
import io.github.xponer2.translator.block.LootParams;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

public class InferiumCropBlock extends MysticalCropBlock {
    public InferiumCropBlock(Crop crop) {
        super(crop);
    }

    public List<ItemStack> getDrops(IBlockState state, LootParams.Builder builder) {
        int age = state.getValue(AGE);

        int crop = 0;
        int seed = 1;

        if (age == this.getMaxAge()) {
            crop = 1;

            var vec = builder.getOptionalParameter(LootContextParams.ORIGIN);

            if (vec != null) {
                var level = builder.getLevel();
                var pos = new BlockPos(vec);
                var below = level.getBlockState(pos.down()).getBlock();

                if (below instanceof IEssenceFarmland farmland) {
                    int tier = farmland.getTier().getValue();
                    crop = (int) ((0.5D * tier) + 0.5D);

                    if (tier > 1 && tier % 2 == 0 && Math.random() < 0.5D)
                        crop++;
                }

                double chance = this.getMysticalCrop().getSecondaryChance(below);
                if (ModConfigs.SECONDARY_SEED_DROPS.get() && Math.random() < chance)
                    seed = 2;
            }
        }

        List<ItemStack> drops = new ArrayList<>();

        if (crop > 0)
            drops.add(new ItemStack(this.getCrop(), crop));

        drops.add(new ItemStack(this.getSeed(), seed));

        return drops;
    }

    @Override
    public void getDrops(net.minecraft.util.NonNullList<ItemStack> drops, net.minecraft.world.IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        drops.addAll(this.getDrops(state, new io.github.xponer2.translator.block.LootParams.Builder(world, pos, this.harvesters.get(), state)));
    }
}

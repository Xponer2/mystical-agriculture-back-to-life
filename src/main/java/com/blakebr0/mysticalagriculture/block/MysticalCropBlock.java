package com.blakebr0.mysticalagriculture.block;

import java.util.Random;

import com.blakebr0.cucumber.util.Localizable;
import com.blakebr0.mysticalagriculture.api.crop.Crop;
import com.blakebr0.mysticalagriculture.api.crop.ICropProvider;
import com.blakebr0.mysticalagriculture.config.ModConfigs;
import com.blakebr0.mysticalagriculture.init.ModItems;
import com.blakebr0.mysticalagriculture.lib.ModCrops;

import net.minecraft.block.BlockCrops;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

// a 1.12.2 BlockCrops, so other mods treat it as a crop; ICropProvider#getCrop is getMysticalCrop in this port
// because BlockCrops#getCrop returns the crop item
public class MysticalCropBlock extends BlockCrops implements ICropProvider {
    private static final AxisAlignedBB SHAPE = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.25D, 1.0D);
    private final Crop crop;

    public MysticalCropBlock(Crop crop) {
        super();
        this.crop = crop;
        this.setHardness(0.0F);
        this.disableStats();
    }

    // 1.12.2: BlockBush#getPlantType calls every BlockCrops but the vanilla ones Plains (1.21: a crop)
    @Override
    public net.minecraftforge.common.EnumPlantType getPlantType(IBlockAccess world, BlockPos pos) {
        return net.minecraftforge.common.EnumPlantType.Crop;
    }

    @Override
    public boolean canUseBonemeal(World level, Random random, BlockPos pos, IBlockState state) {
        return false;
    }

    @Override
    public void updateTick(World level, BlockPos pos, IBlockState state, Random random) {
        if (!this.canGrow(level, pos)) {
            this.checkAndDropBlock(level, pos, state);
            return;
        }

        super.updateTick(level, pos, state, random);
    }

    @Override
    @SuppressWarnings("deprecation")
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess level, BlockPos pos) {
        return SHAPE;
    }

    @Override
    public String getLocalizedName() {
        return Localizable.of("block.mysticalagriculture.mystical_crop").args(this.crop.getDisplayName()).buildString();
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        int age = this.getAge(state);

        int crop = 0;
        int seed = 1;
        int fertilizer = 0;

        if (age == this.getMaxAge()) {
            crop = 1;

            var below = world.getBlockState(pos.down()).getBlock();
            double chance = this.crop.getSecondaryChance(below);

            if (Math.random() < chance)
                crop = 2;

            if (ModConfigs.SECONDARY_SEED_DROPS.get() && Math.random() < chance)
                seed = 2;

            double fertilizerChance = ModConfigs.FERTILIZED_ESSENCE_DROP_CHANCE.get();
            if (Math.random() < fertilizerChance)
                fertilizer = 1;
        }

        if (crop > 0)
            drops.add(new ItemStack(this.getCrop(), crop));

        drops.add(new ItemStack(this.getSeed(), seed));

        if (fertilizer > 0)
            drops.add(new ItemStack(ModItems.FERTILIZED_ESSENCE.get()));
    }

    @Override
    public void grow(World level, Random random, BlockPos pos, IBlockState state) {
        if (!this.canGrow(level, pos))
            return;

        super.grow(level, random, pos, state);
    }

    @Override
    public boolean canGrow(World level, BlockPos pos, IBlockState state, boolean isClient) {
        return this.canGrow(level, pos) && super.canGrow(level, pos, state, isClient);
    }

    @Override
    protected Item getSeed() {
        return this.crop.getSeedsItem();
    }

    @Override
    protected Item getCrop() {
        return this.crop.getEssenceItem();
    }

    @Override
    public Crop getMysticalCrop() {
        return this.crop;
    }

    private boolean canGrow(World level, BlockPos pos) {
        var crux = this.crop.getCruxBlock();

        if (crux != null) {
            var block = level.getBlockState(pos.down(2)).getBlock();
            if (block != crux)
                return false;
        }

        if (ModConfigs.REQUIRES_EFFECTIVE_FARMLAND.get() && this.crop != ModCrops.INFERIUM) {
            var farmland = level.getBlockState(pos.down()).getBlock();
            if (!this.crop.getTier().isEffectiveFarmland(farmland))
                return false;
        }

        var biomes = this.crop.getRequiredBiomes();

        if (!biomes.isEmpty()) {
            var biome = level.getBiome(pos).getRegistryName();
            return biome != null && biomes.contains(biome);
        }

        return true;
    }
}

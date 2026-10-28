package com.blakebr0.mysticalagriculture.item;

import com.blakebr0.cucumber.item.BaseItem;
import com.blakebr0.mysticalagriculture.api.crop.CropTier;
import com.blakebr0.mysticalagriculture.api.crop.ICropTierProvider;
import com.blakebr0.mysticalagriculture.api.farmland.FarmlandConverter;
import com.blakebr0.mysticalagriculture.api.farmland.IFarmlandConverter;
import com.blakebr0.mysticalagriculture.config.ModConfigs;

import net.minecraft.block.Block;
import net.minecraft.util.EnumActionResult;

import io.github.xponer2.translator.util.UseOnContext;

public class EssenceItem extends BaseItem implements IFarmlandConverter, ICropTierProvider {
    private final CropTier tier;

    public EssenceItem(CropTier tier) {
        super();
        this.tier = tier;
    }

    public EnumActionResult useOn(UseOnContext context) {
        if (!ModConfigs.ESSENCE_FARMLAND_CONVERSION.get())
            return EnumActionResult.PASS;

        return FarmlandConverter.convert(this, context);
    }

    @Override
    public Block getConvertedFarmland() {
        return this.tier.getFarmland();
    }

    @Override
    public CropTier getTier() {
        return this.tier;
    }

    @Override
    public EnumActionResult onItemUse(net.minecraft.entity.player.EntityPlayer player, net.minecraft.world.World world, net.minecraft.util.math.BlockPos pos,
            net.minecraft.util.EnumHand hand, net.minecraft.util.EnumFacing facing, float hitX, float hitY, float hitZ) {
        return this.useOn(io.github.xponer2.translator.util.UseOnContext.of(player, world, pos, hand, facing, hitX, hitY, hitZ));
    }
}

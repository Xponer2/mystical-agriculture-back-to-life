package com.blakebr0.mysticalagriculture.item;

import com.blakebr0.cucumber.item.BaseItem;
import com.blakebr0.mysticalagriculture.util.IActivatable;

import net.minecraft.util.EnumActionResult;

import io.github.xponer2.translator.util.UseOnContext;

public class WandItem extends BaseItem {
    public WandItem() {
        super(p -> p.stacksTo(1));
    }

    public EnumActionResult useOn(UseOnContext context) {
        var level = context.getLevel();
        var pos = context.getClickedPos();

        var tile = level.getTileEntity(pos);
        if (tile instanceof IActivatable activatable) {
            activatable.activate();

            return EnumActionResult.SUCCESS;
        }

        return EnumActionResult.PASS;
    }

    @Override
    public EnumActionResult onItemUse(net.minecraft.entity.player.EntityPlayer player, net.minecraft.world.World world, net.minecraft.util.math.BlockPos pos,
            net.minecraft.util.EnumHand hand, net.minecraft.util.EnumFacing facing, float hitX, float hitY, float hitZ) {
        return this.useOn(io.github.xponer2.translator.util.UseOnContext.of(player, world, pos, hand, facing, hitX, hitY, hitZ));
    }
}

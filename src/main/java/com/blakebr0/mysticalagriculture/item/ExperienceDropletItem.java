package com.blakebr0.mysticalagriculture.item;

import com.blakebr0.cucumber.item.BaseItem;
import com.blakebr0.cucumber.util.Utils;

import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ExperienceDropletItem extends BaseItem {
    public ExperienceDropletItem() {
        super();
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World level, EntityPlayer player, EnumHand hand) {
        var stack = player.getHeldItem(hand);
        int used = 0;

        if (!level.isRemote) {
            if (player.isSneaking()) {
                int xp = 0;
                for (int i = 0; i < stack.getCount(); i++) {
                    xp += Utils.randInt(8, 12);
                }

                var orb = new EntityXPOrb(level, player.posX, player.posY, player.posZ, xp);

                level.spawnEntity(orb);

                used = stack.getCount();
            } else {
                int xp = Utils.randInt(8, 12);
                var orb = new EntityXPOrb(level, player.posX, player.posY, player.posZ, xp);

                level.spawnEntity(orb);

                used = 1;
            }
        }

        if (!player.isCreative())
            stack.shrink(used);

        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }
}

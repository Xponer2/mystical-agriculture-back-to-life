package com.blakebr0.mysticalagriculture.item;

import java.util.List;

import com.blakebr0.cucumber.item.BaseItem;
import com.blakebr0.cucumber.util.Formatting;
import com.blakebr0.mysticalagriculture.api.util.ExperienceCapsuleUtils;
import com.blakebr0.mysticalagriculture.init.ModDataComponentTypes;
import com.blakebr0.mysticalagriculture.lib.ModTooltips;
import io.github.xponer2.translator.item.ItemPropertyFunction;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import io.github.xponer2.translator.item.TooltipContext;

public class ExperienceCapsuleItem extends BaseItem implements io.github.xponer2.translator.item.TooltipContext.HoverText {
    public ExperienceCapsuleItem() {
        super(p -> p
                .stacksTo(1)
                .component(ModDataComponentTypes.EXPERIENCE_CAPSULE, 0)
        );
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World level, EntityPlayer player, EnumHand hand) {
        var stack = player.getHeldItem(hand);
        var experience = ExperienceCapsuleUtils.getExperience(stack);

        if (player.isSneaking()) {
            if (experience > 0) {
                var xpToGive = Math.min(experience, getExperienceToGive(player));

                xpToGive -= ExperienceCapsuleUtils.removeExperienceFromCapsule(stack, xpToGive);

                giveExperiencePoints(player, xpToGive);

                return new ActionResult<>(EnumActionResult.SUCCESS, stack);
            }
        } else {
            if (experience < ExperienceCapsuleUtils.MAX_XP_POINTS && player.experienceTotal > 0) {
                var xpToTake = Math.min(ExperienceCapsuleUtils.MAX_XP_POINTS - experience, getExperienceToTake(player));

                xpToTake -= ExperienceCapsuleUtils.addExperienceToCapsule(stack, xpToTake);

                giveExperiencePoints(player, -xpToTake);

                return new ActionResult<>(EnumActionResult.SUCCESS, stack);
            }
        }

        return new ActionResult<>(EnumActionResult.PASS, stack);
    }

    public void appendHoverText(ItemStack stack, TooltipContext context, List<ITextComponent> tooltip, ITooltipFlag flag) {
        var experience = ExperienceCapsuleUtils.getExperience(stack);

        tooltip.add(ModTooltips.EXPERIENCE_CAPSULE.args(Formatting.number(experience), Formatting.number(ExperienceCapsuleUtils.MAX_XP_POINTS)).build());
    }

    public static ItemPropertyFunction getFillPropertyGetter() {
        return (stack, _level, _entity, _unused) -> {
            int experience = ExperienceCapsuleUtils.getExperience(stack);

            if (experience > 0) {
                double level = (double) experience / ExperienceCapsuleUtils.MAX_XP_POINTS;
                return (int) (level * 10);
            }

            return 0;
        };
    }

    private static int getExperienceToGive(EntityPlayer player) {
        int xpNeeded = player.xpBarCap();
        int extraXp = Math.round(xpNeeded * player.experience);

        return xpNeeded - extraXp;
    }

    private static int getExperienceToTake(EntityPlayer player) {
        // if they have progress towards the next level, then we give the player their progress
        // if they don't, then we need to give them everything in their current level
        var progress = Math.round(player.experience * 10F) / 10F;
        if (progress > 0.0F) {
            var xpNeeded = player.xpBarCap();
            return Math.round(xpNeeded * player.experience);
        } else {
            // decrease player level by 1 temporarily to get the experience needed for the current level
            player.experienceLevel--;

            var xpNeeded = player.xpBarCap();

            // set the players level back to where it was
            player.experienceLevel++;

            return xpNeeded;
        }
    }

    // copy of EntityPlayer#giveExperiencePoints
    private static void giveExperiencePoints(EntityPlayer player, int points) {
        player.experience += (float) points / (float) player.xpBarCap();
        player.experienceTotal = MathHelper.clamp(player.experienceTotal + points, 0, Integer.MAX_VALUE);

        while (player.experience < 0.0F) {
            float f = player.experience * (float) player.xpBarCap();
            if (player.experienceLevel > 0) {
                giveExperienceLevels(player, -1);
                player.experience = 1.0F + f / (float) player.xpBarCap();
            } else {
                giveExperienceLevels(player, -1);
                player.experience = 0.0F;
            }
        }

        while (player.experience >= 1.0F) {
            player.experience = (player.experience - 1.0F) * (float) player.xpBarCap();
            giveExperienceLevels(player, 1);
            player.experience /= (float) player.xpBarCap();
        }
    }

    // copy of EntityPlayer#giveExperienceLevels
    private static void giveExperienceLevels(EntityPlayer player, int levels) {
        player.experienceLevel += levels;

        if (player.experienceLevel < 0) {
            player.experienceLevel = 0;
            player.experience = 0.0F;
            player.experienceTotal = 0;
        }
    }

    @Override
    @net.minecraftforge.fml.relauncher.SideOnly(net.minecraftforge.fml.relauncher.Side.CLIENT)
    public void addInformation(ItemStack stack, @javax.annotation.Nullable net.minecraft.world.World world, java.util.List<String> tooltip, ITooltipFlag flag) {
        io.github.xponer2.translator.item.TooltipContext.addInformation(this, stack, world, tooltip, flag);
    }
}

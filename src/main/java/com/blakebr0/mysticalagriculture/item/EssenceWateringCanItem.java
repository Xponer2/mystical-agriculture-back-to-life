package com.blakebr0.mysticalagriculture.item;

import java.util.List;

import com.blakebr0.mysticalagriculture.init.ModDataComponentTypes;
import com.blakebr0.mysticalagriculture.lib.ModTooltips;
import io.github.xponer2.translator.tags.FluidTags;
import io.github.xponer2.translator.text.LiteralComponent;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import io.github.xponer2.translator.util.UseOnContext;
import io.github.xponer2.translator.component.StackComponents;
import io.github.xponer2.translator.text.MutableComponent;
import io.github.xponer2.translator.item.TooltipContext;

public class EssenceWateringCanItem extends WateringCanItem {
    private final TextFormatting textColor;

    public EssenceWateringCanItem(int range, double chance, TextFormatting textColor) {
        super(range, chance, p -> p.component(ModDataComponentTypes.WATERING_CAN_ACTIVE, false));
        this.textColor = textColor;
    }

    @Override
    public void onUpdate(ItemStack stack, World level, Entity entity, int slot, boolean selected) {
        var isActive = isActive(stack);

        if (selected && isActive && entity instanceof EntityPlayer player) {
            var trace = this.rayTrace(level, player, true);

            if (trace.typeOfHit == RayTraceResult.Type.BLOCK) {
                this.doWater(stack, level, player, trace.getBlockPos(), trace.sideHit);
            } else {
                stopPlayingSound(player);
            }
        }

        // we need to actively check if the watering can was playing the sound in any case where it's not actively
        // watering the ground
        if (!selected && isActive && entity instanceof EntityPlayer player) {
            stopPlayingSound(player);
        }
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
        return isActive(stack);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World level, EntityPlayer player, EnumHand hand) {
        var stack = player.getHeldItem(hand);
        var trace = this.rayTrace(level, player, true);

        if (trace.typeOfHit != RayTraceResult.Type.BLOCK) {
            if (isFilled(stack) && player.isSneaking()) {
                flipActive(stack);
            }

            return new ActionResult<>(EnumActionResult.PASS, stack);
        }

        if (isFilled(stack)) {
            return new ActionResult<>(EnumActionResult.PASS, stack);
        }

        var pos = trace.getBlockPos();
        var direction = trace.sideHit;

        if (level.isBlockModifiable(player, pos) && player.canPlayerEdit(pos.offset(direction), direction, stack)) {
            var fluid = level.getBlockState(pos);

            if (FluidTags.WATER.test(fluid)) {
                setFilled(stack, true);

                player.playSound(SoundEvents.ITEM_BUCKET_FILL, 1.0F, 1.0F);

                return new ActionResult<>(EnumActionResult.SUCCESS, stack);
            }
        }

        return new ActionResult<>(EnumActionResult.PASS, stack);
    }

    public EnumActionResult useOn(UseOnContext context) {
        var player = context.getPlayer();
        if (player == null)
            return EnumActionResult.FAIL;

        var hand = context.getHand();
        var stack = player.getHeldItem(hand);

        if (isActive(stack))
            return EnumActionResult.PASS;

        return super.onItemUse(context.getPlayer(), context.getLevel(), context.getClickedPos(), context.getHand(), context.getClickedFace(), context.hitX(), context.hitY(), context.hitZ());
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<ITextComponent> tooltip, ITooltipFlag advanced) {
        super.appendHoverText(stack, context, tooltip, advanced);

        var rangeString = String.valueOf(this.range);
        MutableComponent rangeNumber = new LiteralComponent(rangeString + "x" + rangeString).withStyle(this.textColor);

        tooltip.add(ModTooltips.TOOL_AREA.args(rangeNumber).build());
    }

    public static boolean isActive(ItemStack stack) {
        return StackComponents.getOrDefault(stack, ModDataComponentTypes.WATERING_CAN_ACTIVE, false);
    }

    public static void setActive(ItemStack stack, boolean active) {
        StackComponents.set(stack, ModDataComponentTypes.WATERING_CAN_ACTIVE, active);
    }

    public static void flipActive(ItemStack stack) {
        var current = isActive(stack);
        setActive(stack, !current);
    }

    @Override
    public EnumActionResult onItemUse(net.minecraft.entity.player.EntityPlayer player, net.minecraft.world.World world, net.minecraft.util.math.BlockPos pos,
            net.minecraft.util.EnumHand hand, net.minecraft.util.EnumFacing facing, float hitX, float hitY, float hitZ) {
        return this.useOn(io.github.xponer2.translator.util.UseOnContext.of(player, world, pos, hand, facing, hitX, hitY, hitZ));
    }
}

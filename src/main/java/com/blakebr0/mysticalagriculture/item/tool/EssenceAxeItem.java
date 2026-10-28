package com.blakebr0.mysticalagriculture.item.tool;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import com.blakebr0.cucumber.item.tool.BaseAxeItem;
import com.blakebr0.mysticalagriculture.api.components.AOEAugmentOffsetComponent;
import com.blakebr0.mysticalagriculture.api.tinkering.AugmentType;
import com.blakebr0.mysticalagriculture.api.tinkering.ITinkerable;
import com.blakebr0.mysticalagriculture.api.util.AugmentUtils;
import com.blakebr0.mysticalagriculture.config.ModConfigs;
import com.blakebr0.mysticalagriculture.init.ModDataComponentTypes;
import com.blakebr0.mysticalagriculture.lib.ModTooltips;
import io.github.xponer2.translator.component.DataComponents;
import io.github.xponer2.translator.component.Unbreakable;
import io.github.xponer2.translator.item.Tier;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

import io.github.xponer2.translator.util.UseOnContext;
import io.github.xponer2.translator.item.TooltipContext;

public class EssenceAxeItem extends BaseAxeItem implements ITinkerable, io.github.xponer2.translator.item.TooltipContext.HoverText {
    private static final EnumSet<AugmentType> TYPES = EnumSet.of(AugmentType.TOOL, AugmentType.AXE);
    private final int tinkerableTier;
    private final int slots;

    public EssenceAxeItem(Tier tier, int tinkerableTier, int slots) {
        super(tier, p -> {
            p.component(ModDataComponentTypes.EQUIPPED_AUGMENTS, new ArrayList<>(slots));
            p.component(ModDataComponentTypes.AOE_AUGMENT_OFFSET, new AOEAugmentOffsetComponent(0, 0));

            var uses = tier.getUses();
            if (uses == 0) {
                p.component(DataComponents.UNBREAKABLE, new Unbreakable(true));
            }

            p.durability(uses);

            return p;
        });
        this.tinkerableTier = tinkerableTier;
        this.slots = slots;
    }

    public EnumActionResult useOn(UseOnContext context) {
        var augments = AugmentUtils.getAugments(context.getHeldItem());
        var success = false;

        for (var augment : augments) {
            if (augment.onItemUse(context))
                success = true;
        }

        if (success)
            return EnumActionResult.SUCCESS;

        return super.onItemUse(context.getPlayer(), context.getLevel(), context.getClickedPos(), context.getHand(), context.getClickedFace(), context.hitX(), context.hitY(), context.hitZ());
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World level, EntityPlayer player, EnumHand hand) {
        var stack = player.getHeldItem(hand);
        var augments = AugmentUtils.getAugments(stack);
        var success = false;

        for (var augment : augments) {
            if (augment.onRightClick(stack, level, player, hand))
                success = true;
        }

        if (success)
            return new ActionResult<>(EnumActionResult.SUCCESS, stack);

        return new ActionResult<>(EnumActionResult.PASS, stack);
    }

    public EnumActionResult interactLivingEntity(ItemStack stack, EntityPlayer player, EntityLivingBase target, EnumHand hand) {
        var augments = AugmentUtils.getAugments(stack);
        var success = false;

        for (var augment : augments) {
            if (augment.onRightClickEntity(stack, player, target, hand))
                success = true;
        }

        return success ? EnumActionResult.SUCCESS : EnumActionResult.PASS;
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        var augments = AugmentUtils.getAugments(stack);
        var success = super.hitEntity(stack, target, attacker);

        for (var augment : augments) {
            if (augment.onHitEntity(stack, target, attacker))
                success = true;
        }

        return success;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World level, IBlockState state, BlockPos pos, EntityLivingBase entity) {
        var augments = AugmentUtils.getAugments(stack);
        var success = super.onBlockDestroyed(stack, level, state, pos, entity);

        for (var augment : augments) {
            if (augment.onBlockDestroyed(stack, level, state, pos, entity))
                success = true;
        }

        return success;
    }

    @Override
    public void onUpdate(ItemStack stack, World level, Entity entity, int slot, boolean isSelected) {
        for (var augment : AugmentUtils.getAugments(stack)) {
            augment.onInventoryTick(stack, level, entity, slot, isSelected);
        }
    }

    public void appendHoverText(ItemStack stack, TooltipContext context, List<ITextComponent> tooltip, ITooltipFlag flag) {
        tooltip.add(ModTooltips.getTooltipForTier(this.tinkerableTier));
        ModTooltips.addAugmentListToTooltip(tooltip, stack, this.slots);
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return ModConfigs.ENCHANTABLE_SUPREMIUM_TOOLS.get();
    }

    @Override
    public int getAugmentSlots() {
        return this.slots;
    }

    @Override
    public EnumSet<AugmentType> getAugmentTypes() {
        return TYPES;
    }

    @Override
    public int getTinkerableTier() {
        return this.tinkerableTier;
    }

    @Override
    public EnumActionResult onItemUse(net.minecraft.entity.player.EntityPlayer player, net.minecraft.world.World world, net.minecraft.util.math.BlockPos pos,
            net.minecraft.util.EnumHand hand, net.minecraft.util.EnumFacing facing, float hitX, float hitY, float hitZ) {
        return this.useOn(io.github.xponer2.translator.util.UseOnContext.of(player, world, pos, hand, facing, hitX, hitY, hitZ));
    }

    @Override
    public boolean itemInteractionForEntity(net.minecraft.item.ItemStack stack, net.minecraft.entity.player.EntityPlayer player,
            net.minecraft.entity.EntityLivingBase target, net.minecraft.util.EnumHand hand) {
        return this.interactLivingEntity(stack, player, target, hand) == EnumActionResult.SUCCESS;
    }

    @Override
    @net.minecraftforge.fml.relauncher.SideOnly(net.minecraftforge.fml.relauncher.Side.CLIENT)
    public void addInformation(ItemStack stack, @javax.annotation.Nullable net.minecraft.world.World world, java.util.List<String> tooltip, ITooltipFlag flag) {
        io.github.xponer2.translator.item.TooltipContext.addInformation(this, stack, world, tooltip, flag);
    }
}

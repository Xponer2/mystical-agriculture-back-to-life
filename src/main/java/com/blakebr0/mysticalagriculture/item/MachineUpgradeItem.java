package com.blakebr0.mysticalagriculture.item;

import java.util.List;

import com.blakebr0.cucumber.item.BaseItem;
import com.blakebr0.cucumber.lib.Tooltips;
import com.blakebr0.cucumber.util.Formatting;
import com.blakebr0.mysticalagriculture.api.machine.IMachineUpgrade;
import com.blakebr0.mysticalagriculture.api.machine.IUpgradeableMachine;
import com.blakebr0.mysticalagriculture.api.machine.MachineUpgradeTier;
import com.blakebr0.mysticalagriculture.lib.ModTooltips;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.text.ITextComponent;

import io.github.xponer2.translator.util.UseOnContext;
import io.github.xponer2.translator.util.Platform;
import io.github.xponer2.translator.item.TooltipContext;

public class MachineUpgradeItem extends BaseItem implements IMachineUpgrade, io.github.xponer2.translator.item.TooltipContext.HoverText {
    private final MachineUpgradeTier tier;

    public MachineUpgradeItem(MachineUpgradeTier tier) {
        super();
        this.tier = tier;
    }

    public EnumActionResult useOn(UseOnContext context) {
        var level = context.getLevel();
        var pos = context.getClickedPos();
        var tile = level.getTileEntity(pos);

        if (tile instanceof IUpgradeableMachine machine && machine.canApplyUpgrade(this.tier)) {
            var stack = context.getHeldItem();
            var remaining = machine.applyUpgrade(this);

            stack.shrink(1);

            if (!remaining.isEmpty()) {
                var item = new EntityItem(level, pos.getX(), pos.getY() + 1, pos.getZ(), remaining.copy());

                Platform.addFreshEntity(level, item);
            }

            return EnumActionResult.SUCCESS;
        }

        return EnumActionResult.FAIL;
    }

    public void appendHoverText(ItemStack stack, TooltipContext context, List<ITextComponent> tooltip, ITooltipFlag flag) {
        if (GuiScreen.isShiftKeyDown()) {
            var speed = Formatting.number(1 / this.tier.getOperationTimeMultiplier()).withStyle(this.tier.getTextColor());
            var fuelRate = Formatting.number(this.tier.getFuelUsageMultiplier()).withStyle(this.tier.getTextColor());
            var fuelCapacity = Formatting.number(this.tier.getFuelCapacityMultiplier()).withStyle(this.tier.getTextColor());
            var area = Formatting.number(this.tier.getAddedRange()).withStyle(this.tier.getTextColor());

            tooltip.add(ModTooltips.UPGRADE_SPEED.args(speed).build());
            tooltip.add(ModTooltips.UPGRADE_FUEL_RATE.args(fuelRate).build());
            tooltip.add(ModTooltips.UPGRADE_FUEL_CAPACITY.args(fuelCapacity).build());
            tooltip.add(ModTooltips.UPGRADE_AREA.args(area).build());
        } else {
            tooltip.add(Tooltips.HOLD_SHIFT_FOR_INFO.build());
        }
    }

    @Override
    public MachineUpgradeTier getTier() {
        return this.tier;
    }

    @Override
    public EnumActionResult onItemUse(net.minecraft.entity.player.EntityPlayer player, net.minecraft.world.World world, net.minecraft.util.math.BlockPos pos,
            net.minecraft.util.EnumHand hand, net.minecraft.util.EnumFacing facing, float hitX, float hitY, float hitZ) {
        return this.useOn(io.github.xponer2.translator.util.UseOnContext.of(player, world, pos, hand, facing, hitX, hitY, hitZ));
    }

    @Override
    @net.minecraftforge.fml.relauncher.SideOnly(net.minecraftforge.fml.relauncher.Side.CLIENT)
    public void addInformation(ItemStack stack, @javax.annotation.Nullable net.minecraft.world.World world, java.util.List<String> tooltip, ITooltipFlag flag) {
        io.github.xponer2.translator.item.TooltipContext.addInformation(this, stack, world, tooltip, flag);
    }
}

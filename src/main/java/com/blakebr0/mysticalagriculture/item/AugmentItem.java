package com.blakebr0.mysticalagriculture.item;

import java.util.List;

import com.blakebr0.cucumber.item.BaseItem;
import com.blakebr0.cucumber.util.Localizable;
import com.blakebr0.mysticalagriculture.api.tinkering.Augment;
import com.blakebr0.mysticalagriculture.api.tinkering.IAugmentProvider;
import com.blakebr0.mysticalagriculture.lib.ModTooltips;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import io.github.xponer2.translator.item.TooltipContext;

public class AugmentItem extends BaseItem implements IAugmentProvider, io.github.xponer2.translator.item.TooltipContext.HoverText {
    private final Augment augment;

    public AugmentItem(Augment augment) {
        super();
        this.augment = augment;
    }

    public ITextComponent getName(ItemStack stack) {
        return Localizable.of("item.mysticalagriculture.augment").args(this.augment.getDisplayName()).build();
    }

    public ITextComponent getDescription() {
        return this.getName(ItemStack.EMPTY);
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
        return this.augment.hasEffect();
    }

    public void appendHoverText(ItemStack stack, TooltipContext context, List<ITextComponent> tooltip, ITooltipFlag flag) {
        tooltip.add(ModTooltips.getTooltipForTier(this.augment.getTier()));
        tooltip.add(this.augment.getDescriptionDisplayText());

        if (this.augment.hasSetBonus()) {
            tooltip.add(ModTooltips.SET_BONUS.args(this.augment.getSetBonusDisplayText()).build());
        }

        if (flag.isAdvanced()) {
            tooltip.add(ModTooltips.AUGMENT_ID.args(this.augment.getId().toString()).color(TextFormatting.DARK_GRAY).build());
        }
    }

    @Override
    public Augment getAugment() {
        return this.augment;
    }

    @Override
    @net.minecraftforge.fml.relauncher.SideOnly(net.minecraftforge.fml.relauncher.Side.CLIENT)
    public void addInformation(ItemStack stack, @javax.annotation.Nullable net.minecraft.world.World world, java.util.List<String> tooltip, ITooltipFlag flag) {
        io.github.xponer2.translator.item.TooltipContext.addInformation(this, stack, world, tooltip, flag);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return this.getName(stack).getFormattedText();
    }
}

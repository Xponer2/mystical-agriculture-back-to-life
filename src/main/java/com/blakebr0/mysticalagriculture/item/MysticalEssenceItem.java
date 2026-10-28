package com.blakebr0.mysticalagriculture.item;

import java.util.List;

import com.blakebr0.cucumber.item.BaseItem;
import com.blakebr0.cucumber.util.Localizable;
import com.blakebr0.mysticalagriculture.MysticalAgriculture;
import com.blakebr0.mysticalagriculture.api.crop.Crop;
import com.blakebr0.mysticalagriculture.api.crop.ICropProvider;
import com.blakebr0.mysticalagriculture.lib.ModTooltips;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import io.github.xponer2.translator.item.TooltipContext;

public class MysticalEssenceItem extends BaseItem implements ICropProvider, io.github.xponer2.translator.item.TooltipContext.HoverText {
    private final Crop crop;

    public MysticalEssenceItem(Crop crop) {
        super();
        this.crop = crop;
    }

    public ITextComponent getName(ItemStack stack) {
        return Localizable.of("item.mysticalagriculture.mystical_essence").args(this.crop.getDisplayName()).build();
    }

    public ITextComponent getDescription() {
        return this.getName(ItemStack.EMPTY);
    }

    @Override
    public String getTranslationKey() {
        return Localizable.of("item.mysticalagriculture.mystical_essence").args(this.crop.getDisplayName()).buildString();
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
        return this.crop.hasEffect(stack) || super.hasEffect(stack);
    }

    public void appendHoverText(ItemStack stack, TooltipContext context, List<ITextComponent> tooltip, ITooltipFlag flag) {
        if (!this.crop.getModId().equals(MysticalAgriculture.MOD_ID)) {
            tooltip.add(ModTooltips.getAddedByTooltip(this.crop.getModId()));
        }
    }

    @Override
    public Crop getMysticalCrop() {
        return this.crop;
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

package com.blakebr0.mysticalagriculture.item;

import java.util.List;
import java.util.stream.Collectors;

import com.blakebr0.cucumber.util.Localizable;
import com.blakebr0.mysticalagriculture.MysticalAgriculture;
import com.blakebr0.mysticalagriculture.api.crop.Crop;
import com.blakebr0.mysticalagriculture.api.crop.ICropProvider;
import com.blakebr0.mysticalagriculture.lib.ModTooltips;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import io.github.xponer2.translator.text.LiteralComponent;
import io.github.xponer2.translator.item.TooltipContext;

public class MysticalSeedsItem extends ItemSeeds implements ICropProvider, io.github.xponer2.translator.item.TooltipContext.HoverText {
    private final Crop crop;

    public MysticalSeedsItem(Crop crop) {
        super(crop.getCropBlock(), net.minecraft.init.Blocks.FARMLAND);
        this.crop = crop;
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return this.getName(stack).getFormattedText();
    }

    public ITextComponent getName(ItemStack stack) {
        return Localizable.of("item.mysticalagriculture.mystical_seeds").args(this.crop.getDisplayName()).build();
    }

    public ITextComponent getDescription() {
        return this.getName(ItemStack.EMPTY);
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
        return this.crop.hasEffect(stack) || super.hasEffect(stack);
    }

    public void appendHoverText(ItemStack stack, TooltipContext level, List<ITextComponent> tooltip, ITooltipFlag flag) {
        var tier = this.crop.getTier().getDisplayName();

        tooltip.add(ModTooltips.TIER.args(tier).build());

        if (!this.crop.getModId().equals(MysticalAgriculture.MOD_ID)) {
            tooltip.add(ModTooltips.getAddedByTooltip(this.crop.getModId()));
        }

        var biomes = this.crop.getRequiredBiomes();

        if (!biomes.isEmpty()) {
            tooltip.add(ModTooltips.REQUIRED_BIOMES.build());

            var ids = biomes.stream()
                    .map(ResourceLocation::toString)
                    .map(s -> " - " + s)
                    .map(LiteralComponent::new)
                    .collect(Collectors.toList());

            tooltip.addAll(ids);
        }

        if (flag.isAdvanced()) {
            tooltip.add(ModTooltips.CROP_ID.args(this.crop.getId().toString()).color(TextFormatting.DARK_GRAY).build());
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
}

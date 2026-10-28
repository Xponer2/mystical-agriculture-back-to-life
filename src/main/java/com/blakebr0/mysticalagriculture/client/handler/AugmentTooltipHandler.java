package com.blakebr0.mysticalagriculture.client.handler;

import java.util.List;
import java.util.stream.Collectors;

import com.blakebr0.mysticalagriculture.api.tinkering.Augment;
import com.blakebr0.mysticalagriculture.api.tinkering.AugmentType;
import com.blakebr0.mysticalagriculture.init.ModItems;
import com.blakebr0.mysticalagriculture.item.AugmentItem;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

// 1.12.2 tooltips have no image components: an empty line is reserved under the name and the item icons are drawn
// on it after the text
public final class AugmentTooltipHandler {
    private static final int LINE_HEIGHT = 10;

    @SubscribeEvent
    public void onItemTooltip(ItemTooltipEvent event) {
        var stack = event.getItemStack();
        if (stack.getItem() instanceof AugmentItem item && event.getToolTip().size() > 0) {
            var stacks = getDisplayItemStacks(item.getAugment());
            var font = Minecraft.getMinecraft().fontRenderer;
            int spaces = (int) Math.ceil((stacks.size() * 9.0) / Math.max(1, font.getStringWidth(" ")));
            event.getToolTip().add(1, Strings.repeat(" ", spaces));
        }
    }

    @SubscribeEvent
    public void onTooltipText(RenderTooltipEvent.PostText event) {
        var mc = Minecraft.getMinecraft();
        var stack = event.getStack();
        if (mc.player == null || !(stack.getItem() instanceof AugmentItem item))
            return;

        var stacks = getDisplayItemStacks(item.getAugment());

        GlStateManager.pushMatrix();
        // the second line: the first one is followed by a 2 pixel gap
        GlStateManager.translate(event.getX(), event.getY() + LINE_HEIGHT + 2, 400);
        GlStateManager.scale(0.5F, 0.5F, 1.0F);
        RenderHelper.enableGUIStandardItemLighting();

        int drawn = 0;
        for (var display : stacks) {
            mc.getRenderItem().renderItemAndEffectIntoGUI(display, (drawn % 10) * 18, 0);
            drawn++;
        }

        RenderHelper.disableStandardItemLighting();
        GlStateManager.popMatrix();
    }

    private static List<ItemStack> getDisplayItemStacks(Augment augment) {
        var types = augment.getAugmentTypes();

        if (types.contains(AugmentType.TOOL)) {
            return ImmutableList.of(
                    new ItemStack(Items.DIAMOND_PICKAXE),
                    new ItemStack(Items.DIAMOND_SHOVEL),
                    new ItemStack(Items.DIAMOND_AXE),
                    new ItemStack(Items.DIAMOND_HOE),
                    new ItemStack(Items.SHEARS),
                    new ItemStack(Items.FISHING_ROD),
                    new ItemStack(ModItems.DIAMOND_SICKLE.get()),
                    new ItemStack(ModItems.DIAMOND_SCYTHE.get())
            );
        }

        if (types.contains(AugmentType.WEAPON)) {
            return ImmutableList.of(
                    new ItemStack(Items.DIAMOND_SWORD),
                    new ItemStack(Items.BOW),
                    // no vanilla crossbow in 1.12.2
                    new ItemStack(ModItems.INFERIUM_CROSSBOW.get()),
                    new ItemStack(ModItems.DIAMOND_SICKLE.get()),
                    new ItemStack(ModItems.DIAMOND_SCYTHE.get())
            );
        }

        if (types.contains(AugmentType.ARMOR)) {
            return ImmutableList.of(
                    new ItemStack(Items.DIAMOND_HELMET),
                    new ItemStack(Items.DIAMOND_CHESTPLATE),
                    new ItemStack(Items.DIAMOND_LEGGINGS),
                    new ItemStack(Items.DIAMOND_BOOTS)
            );
        }

        return types.stream()
                .map(AugmentTooltipHandler::getDisplayItemStackForAugmentType)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }

    private static ItemStack getDisplayItemStackForAugmentType(AugmentType type) {
        var item = switch (type) {
            case TOOL, WEAPON, ARMOR, STAFF -> null;
            case SWORD -> Items.DIAMOND_SWORD;
            case PICKAXE -> Items.DIAMOND_PICKAXE;
            case SHOVEL -> Items.DIAMOND_SHOVEL;
            case AXE -> Items.DIAMOND_AXE;
            case HOE -> Items.DIAMOND_HOE;
            case BOW -> Items.BOW;
            case CROSSBOW -> ModItems.INFERIUM_CROSSBOW.get();
            case SHEARS -> Items.SHEARS;
            case FISHING_ROD -> Items.FISHING_ROD;
            case SICKLE -> ModItems.DIAMOND_SICKLE.get();
            case SCYTHE -> ModItems.DIAMOND_SCYTHE.get();
            case HELMET -> Items.DIAMOND_HELMET;
            case CHESTPLATE -> Items.DIAMOND_CHESTPLATE;
            case LEGGINGS -> Items.DIAMOND_LEGGINGS;
            case BOOTS -> Items.DIAMOND_BOOTS;
        };

        return item == null ? ItemStack.EMPTY : new ItemStack(item);
    }
}

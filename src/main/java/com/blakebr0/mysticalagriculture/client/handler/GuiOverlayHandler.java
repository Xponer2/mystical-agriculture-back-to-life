package com.blakebr0.mysticalagriculture.client.handler;

import com.blakebr0.mysticalagriculture.api.crafting.IAwakeningRecipe;
import com.blakebr0.mysticalagriculture.lib.ModTooltips;
import com.blakebr0.mysticalagriculture.tileentity.AwakeningAltarTileEntity;
import com.blakebr0.mysticalagriculture.tileentity.EssenceVesselTileEntity;
import com.blakebr0.mysticalagriculture.tileentity.InfusionAltarTileEntity;
import io.github.xponer2.translator.registry.RegistryAccess;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import io.github.xponer2.translator.client.GuiGraphics;

public final class GuiOverlayHandler {
    @SubscribeEvent
    public void onPostRenderGui(RenderGameOverlayEvent.Post event) {
        var mc = Minecraft.getMinecraft();
        var level = mc.world;

        if (level == null)
            return;

        var gfx = new GuiGraphics(net.minecraft.client.Minecraft.getMinecraft());

        if (((Object) mc.objectMouseOver) instanceof RayTraceResult result && result.typeOfHit == RayTraceResult.Type.BLOCK) {
            var pos = result.getBlockPos();
            var tile = level.getTileEntity(pos);
            var stack = ItemStack.EMPTY;

            if (tile instanceof InfusionAltarTileEntity altar) {
                var recipe = altar.getActiveRecipe();
                if (recipe != null) {
                    stack = recipe.getResultItem(RegistryAccess.INSTANCE);
                }
            }

            if (tile instanceof AwakeningAltarTileEntity altar) {
                var recipe = altar.getActiveRecipe();
                if (recipe != null) {
                    stack = recipe.getResultItem(RegistryAccess.INSTANCE);

                    drawEssenceRequirements(gfx, recipe, altar);
                }
            }

            if (!stack.isEmpty()) {
                int x = new net.minecraft.client.gui.ScaledResolution(mc).getScaledWidth() / 2 - 11;
                int y = new net.minecraft.client.gui.ScaledResolution(mc).getScaledHeight() / 2 - 8;

                gfx.renderItem(stack, x + 26, y);
                gfx.renderItemDecorations(mc.fontRenderer, stack, x + 26, y);
                gfx.drawString(mc.fontRenderer, stack.getDisplayName(), x + 48, y + 5, 16383998);
            }
        }

        if (((Object) mc.objectMouseOver) instanceof RayTraceResult result && result.typeOfHit == RayTraceResult.Type.BLOCK) {
            var pos = result.getBlockPos();
            var tile = mc.world.getTileEntity(pos);

            if (tile instanceof EssenceVesselTileEntity vessel) {
                var stack = vessel.getInventory().getStackInSlot(0);

                if (!stack.isEmpty()) {
                    int x = new net.minecraft.client.gui.ScaledResolution(mc).getScaledWidth() / 2 - 11;
                    int y = new net.minecraft.client.gui.ScaledResolution(mc).getScaledHeight() / 2 - 8;

                    gfx.renderItem(stack, x + 26, y);
                    gfx.renderItemDecorations(mc.fontRenderer, stack, x + 26, y);
                    gfx.drawString(mc.fontRenderer, stack.getDisplayName(), x + 48, y + 5, 16383998);
                }
            }
        }
    }

    private static void drawEssenceRequirements(GuiGraphics gfx, IAwakeningRecipe recipe, AwakeningAltarTileEntity altar) {
        var mc = Minecraft.getMinecraft();

        int x = new net.minecraft.client.gui.ScaledResolution(mc).getScaledWidth() / 2 - 11;
        int y = new net.minecraft.client.gui.ScaledResolution(mc).getScaledHeight() / 2 - 4;
        var lineHeight = mc.fontRenderer.FONT_HEIGHT + 6;

        var hasMissingEssences = false;
        var xOffset = 0;

        var missingEssences = recipe.getMissingEssences(altar.getEssenceItems());

        for (var essence : missingEssences.entrySet()) {
            gfx.renderItem(essence.getKey(), x + 26 + xOffset, y + 2 * lineHeight);
            gfx.drawString(mc.fontRenderer, getEssenceDisplayName(essence.getKey(), essence.getValue()), x + 48 + xOffset, y + 5 + 2 * lineHeight, 16383998);
            xOffset += 56;
            hasMissingEssences = true;
        }

        if (hasMissingEssences) {
            gfx.drawString(mc.fontRenderer, ModTooltips.MISSING_ESSENCES.build(), x + 28, y + 5 + lineHeight, 16383998);
        }
    }

    private static String getEssenceDisplayName(ItemStack stack, int missing) {
        var required = stack.getCount();
        return required - missing + "/" + required;
    }
}

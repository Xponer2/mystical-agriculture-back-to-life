package com.blakebr0.mysticalagriculture.client.tesr;

import com.blakebr0.mysticalagriculture.tileentity.InfusionPedestalTileEntity;
import io.github.xponer2.translator.client.Axis;
import io.github.xponer2.translator.client.BlockEntityRenderer;
import io.github.xponer2.translator.client.BlockEntityRendererProvider;
import io.github.xponer2.translator.client.MultiBufferSource;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.item.ItemBlock;

import io.github.xponer2.translator.client.PoseStack;
import io.github.xponer2.translator.client.Rendering;

public class InfusionPedestalRenderer implements BlockEntityRenderer<InfusionPedestalTileEntity> {
    public InfusionPedestalRenderer(BlockEntityRendererProvider.Context context) { }

    @Override
    public void render(InfusionPedestalTileEntity tile, float v, PoseStack matrix, MultiBufferSource buffer, int i, int i1) {
        var stack = tile.getInventory().getStackInSlot(0);
        var minecraft = Minecraft.getMinecraft();

        if (!stack.isEmpty()) {
            matrix.pushPose();
            matrix.translate(0.5D, 1.2D, 0.5D);
            float scale = stack.getItem() instanceof ItemBlock ? 0.95F : 0.75F;
            matrix.scale(scale, scale, scale);
            double tick = System.currentTimeMillis() / 800.0D;
            matrix.translate(0.0D, Math.sin(tick % (2 * Math.PI)) * 0.065D, 0.0D);
            matrix.mulPose(Axis.YP.rotationDegrees((float) ((tick * 40.0D) % 360)));
            Rendering.renderStatic(stack, TransformType.GROUND, i, i1, matrix, buffer, minecraft.world, 0);
            matrix.popPose();
        }
    }
}


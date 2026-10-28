package com.blakebr0.mysticalagriculture.client.tesr;

import com.blakebr0.mysticalagriculture.block.TinkeringTableBlock;
import com.blakebr0.mysticalagriculture.tileentity.TinkeringTableTileEntity;
import io.github.xponer2.translator.client.Axis;
import io.github.xponer2.translator.client.BlockEntityRenderer;
import io.github.xponer2.translator.client.BlockEntityRendererProvider;
import io.github.xponer2.translator.client.MultiBufferSource;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;

import io.github.xponer2.translator.client.PoseStack;
import io.github.xponer2.translator.client.Rendering;

public class TinkeringTableRenderer implements BlockEntityRenderer<TinkeringTableTileEntity> {
    public TinkeringTableRenderer(BlockEntityRendererProvider.Context context) { }

    @Override
    public void render(TinkeringTableTileEntity tile, float v, PoseStack matrix, MultiBufferSource buffer, int i, int i1) {
        var level = tile.getWorld();
        if (level == null)
            return;

        var pos = tile.getPos();
        var state = level.getBlockState(pos);
        var stack = tile.getInventory().getStackInSlot(0);

        if (!stack.isEmpty() && state.getPropertyKeys().contains(TinkeringTableBlock.FACING)) {
            matrix.pushPose();
            matrix.translate(0.5D, 0.9D, 0.5D);
            float scale = 0.7F;
            matrix.scale(scale, scale, scale);
            int index = state.getValue(TinkeringTableBlock.FACING).getHorizontalIndex();
            matrix.mulPose(Axis.YP.rotationDegrees(-90 * index));
            matrix.mulPose(Axis.XP.rotationDegrees(90));
            Rendering.renderStatic(stack, TransformType.FIXED, i, i1, matrix, buffer, level, 0);
            matrix.popPose();
        }
    }
}

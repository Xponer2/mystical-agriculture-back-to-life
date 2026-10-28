package com.blakebr0.mysticalagriculture.client.tesr;

import com.blakebr0.mysticalagriculture.block.EnchanterBlock;
import com.blakebr0.mysticalagriculture.tileentity.EnchanterTileEntity;
import io.github.xponer2.translator.client.Axis;
import io.github.xponer2.translator.client.BlockEntityRenderer;
import io.github.xponer2.translator.client.BlockEntityRendererProvider;
import io.github.xponer2.translator.client.MultiBufferSource;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.util.EnumFacing;

import io.github.xponer2.translator.client.PoseStack;
import io.github.xponer2.translator.client.Rendering;

public class EnchanterRenderer implements BlockEntityRenderer<EnchanterTileEntity> {
    public EnchanterRenderer(BlockEntityRendererProvider.Context context) { }

    @Override
    public void render(EnchanterTileEntity tile, float v, PoseStack matrix, MultiBufferSource buffer, int i, int i1) {
        var level = tile.getWorld();
        if (level == null)
            return;

        var pos = tile.getPos();
        var state = level.getBlockState(pos);
        var stack = tile.getInventory().getStackInSlot(2);

        if (!stack.isEmpty() && state.getPropertyKeys().contains(EnchanterBlock.FACING)) {
            matrix.pushPose();
            matrix.translate(0.5D, 0.89D, 0.5D);

            float scale = 0.7F;

            matrix.scale(scale, scale, scale);

            var facing = state.getValue(EnchanterBlock.FACING);
            var axis = facing.getAxis();
            var axisDirection = facing.getAxisDirection().getOffset();

            if (axis == EnumFacing.Axis.X) {
                matrix.mulPose(Axis.ZP.rotationDegrees(158 * axisDirection));
            } else if (axis == EnumFacing.Axis.Z) {
                matrix.mulPose(Axis.XN.rotationDegrees(158 * axisDirection));
            }

            int index = facing.getHorizontalIndex();

            matrix.mulPose(Axis.YP.rotationDegrees(-90 * index));
            matrix.mulPose(Axis.XP.rotationDegrees(90));

            matrix.translate(0.0D, -0.08D, 0.0D);

            Rendering.renderStatic(stack, TransformType.FIXED, i, i1, matrix, buffer, level, 0);

            matrix.popPose();
        }
    }
}

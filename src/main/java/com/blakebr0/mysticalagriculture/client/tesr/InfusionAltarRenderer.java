package com.blakebr0.mysticalagriculture.client.tesr;

import com.blakebr0.cucumber.client.ModRenderTypes;
import com.blakebr0.mysticalagriculture.init.ModBlocks;
import com.blakebr0.mysticalagriculture.tileentity.InfusionAltarTileEntity;
import io.github.xponer2.translator.client.Axis;
import io.github.xponer2.translator.client.BlockEntityRenderer;
import io.github.xponer2.translator.client.BlockEntityRendererProvider;
import io.github.xponer2.translator.client.MultiBufferSource;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.math.AxisAlignedBB;

import io.github.xponer2.translator.client.PoseStack;
import io.github.xponer2.translator.client.ModelData;
import io.github.xponer2.translator.client.Rendering;

public class InfusionAltarRenderer implements BlockEntityRenderer<InfusionAltarTileEntity> {
    public InfusionAltarRenderer(BlockEntityRendererProvider.Context context) { }

    @Override
    public void render(InfusionAltarTileEntity tile, float v, PoseStack matrix, MultiBufferSource buffer, int i, int i1) {
        var minecraft = Minecraft.getMinecraft();
        var level = minecraft.world;
        if (level == null)
            return;

        var inventory = tile.getInventory();
        var stack = inventory.getStackInSlot(1).isEmpty() ? inventory.getStackInSlot(0) : inventory.getStackInSlot(1);

        if (!stack.isEmpty()) {
            matrix.pushPose();
            matrix.translate(0.5D, 1.1D, 0.5D);
            float scale = stack.getItem() instanceof ItemBlock ? 0.95F : 0.75F;
            matrix.scale(scale, scale, scale);
            double tick = System.currentTimeMillis() / 800.0D;
            matrix.translate(0.0D, Math.sin(tick % (2 * Math.PI)) * 0.065D, 0.0D);
            matrix.mulPose(Axis.YP.rotationDegrees((float) ((tick * 40.0D) % 360)));
            Rendering.renderStatic(stack, TransformType.GROUND, i, i1, matrix, buffer, level, 0);
            matrix.popPose();
        }

        var pos = tile.getPos();
        var builder = buffer.getBuffer(ModRenderTypes.GHOST);

        matrix.pushPose();
        matrix.translate(-pos.getX(), -pos.getY(), -pos.getZ());

        tile.getPedestalPositions().forEach(aoePos -> {
            if (level.isAirBlock(aoePos)) {
                matrix.pushPose();
                matrix.translate(aoePos.getX(), aoePos.getY(), aoePos.getZ());
                Rendering.renderBatched(ModBlocks.INFUSION_PEDESTAL.get().getDefaultState(), aoePos, level, matrix, builder, false, level.rand, ModelData.EMPTY, null);
                matrix.popPose();
            }
        });

        matrix.popPose();
    }

    @Override
    public boolean isGlobalRenderer(InfusionAltarTileEntity tile) {
        return true;
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox(InfusionAltarTileEntity tile) {
        return net.minecraft.tileentity.TileEntity.INFINITE_EXTENT_AABB;
    }
}

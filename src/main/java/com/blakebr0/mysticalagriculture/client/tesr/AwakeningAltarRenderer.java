package com.blakebr0.mysticalagriculture.client.tesr;

import com.blakebr0.cucumber.client.ModRenderTypes;
import com.blakebr0.mysticalagriculture.init.ModBlocks;
import com.blakebr0.mysticalagriculture.tileentity.AwakeningAltarTileEntity;
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

public class AwakeningAltarRenderer implements BlockEntityRenderer<AwakeningAltarTileEntity> {
    public AwakeningAltarRenderer(BlockEntityRendererProvider.Context context) { }

    @Override
    public void render(AwakeningAltarTileEntity tile, float v, PoseStack matrix, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        var minecraft = Minecraft.getMinecraft();
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
            Rendering.renderStatic(stack, TransformType.GROUND, combinedLight, combinedOverlay, matrix, buffer, minecraft.world, 0);
            matrix.popPose();
        }

        var pos = tile.getPos();
        var level = tile.getWorld();
        var builder = buffer.getBuffer(ModRenderTypes.GHOST);

        matrix.pushPose();
        matrix.translate(-pos.getX(), -pos.getY(), -pos.getZ());

        var positions = tile.getPedestalPositions();
        for (int i = 0; i < positions.size(); i++) {
            var aoePos = positions.get(i);

            if (level != null && level.isAirBlock(aoePos)) {
                matrix.pushPose();
                matrix.translate(aoePos.getX(), aoePos.getY(), aoePos.getZ());

                var state = i % 2 == 0
                        ? ModBlocks.AWAKENING_PEDESTAL.get().getDefaultState()
                        : ModBlocks.ESSENCE_VESSEL.get().getDefaultState();

                Rendering.renderBatched(state, aoePos, level, matrix, builder, false, level.rand, ModelData.EMPTY, null);

                matrix.popPose();
            }
        }

        matrix.popPose();
    }

    @Override
    public boolean isGlobalRenderer(AwakeningAltarTileEntity tile) {
        return true;
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox(AwakeningAltarTileEntity tile) {
        return net.minecraft.tileentity.TileEntity.INFINITE_EXTENT_AABB;
    }
}

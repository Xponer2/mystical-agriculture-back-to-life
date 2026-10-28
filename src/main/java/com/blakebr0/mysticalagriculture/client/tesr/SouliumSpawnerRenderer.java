package com.blakebr0.mysticalagriculture.client.tesr;

import com.blakebr0.mysticalagriculture.tileentity.SouliumSpawnerTileEntity;
import io.github.xponer2.translator.client.Axis;
import io.github.xponer2.translator.client.BlockEntityRenderer;
import io.github.xponer2.translator.client.BlockEntityRendererProvider;
import io.github.xponer2.translator.client.MultiBufferSource;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.math.MathHelper;

import io.github.xponer2.translator.client.PoseStack;
import io.github.xponer2.translator.util.Platform;
import io.github.xponer2.translator.client.Rendering;

public class SouliumSpawnerRenderer implements BlockEntityRenderer<SouliumSpawnerTileEntity> {
    private final RenderManager entityRenderer;

    public SouliumSpawnerRenderer(BlockEntityRendererProvider.Context context) {
        this.entityRenderer = context.getEntityRenderer();
    }

    @Override
    public void render(SouliumSpawnerTileEntity tile, float v, PoseStack matrix, MultiBufferSource buffer, int i, int i1) {
        matrix.pushPose();
        matrix.translate(0.5F, 0.0F, 0.5F);

        var displayEntity = tile.getDisplayEntity();
        if (displayEntity != null) {
            var entity = displayEntity.entity();

            float scale = 0.53125F;
            float bbMax = Math.max(entity.width, entity.height);

            if ((double) bbMax > 1.0D) {
                scale /= bbMax;
            }

            matrix.translate(0.0F, 0.4F, 0.0F);
            matrix.mulPose(Axis.YP.rotationDegrees((float) Platform.lerp(v, tile.getoSpin(), tile.getSpin()) * 10.0F));
            matrix.translate(0.0F, -0.2F, 0.0F);
            matrix.mulPose(Axis.XP.rotationDegrees(-30.0F));
            matrix.scale(scale, scale, scale);

            Rendering.renderEntity(entity, 0.0D, 0.0D, 0.0D, 0.0F, v, matrix, buffer, i);
        }

        matrix.popPose();
    }
}
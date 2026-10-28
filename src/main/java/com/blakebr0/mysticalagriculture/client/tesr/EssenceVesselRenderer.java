package com.blakebr0.mysticalagriculture.client.tesr;

import com.blakebr0.mysticalagriculture.MysticalAgriculture;
import com.blakebr0.mysticalagriculture.crafting.EssenceVesselColorManager;
import com.blakebr0.mysticalagriculture.tileentity.EssenceVesselTileEntity;
import io.github.xponer2.translator.client.Axis;
import io.github.xponer2.translator.client.BlockEntityRenderer;
import io.github.xponer2.translator.client.BlockEntityRendererProvider;
import io.github.xponer2.translator.client.MultiBufferSource;
import io.github.xponer2.translator.client.RenderType;
import io.github.xponer2.translator.client.VertexConsumer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;

import io.github.xponer2.translator.client.PoseStack;

public class EssenceVesselRenderer implements BlockEntityRenderer<EssenceVesselTileEntity> {
    private static final ResourceLocation VESSEL_CONTENT_TEXTURE = MysticalAgriculture.resource("block/essence_vessel_contents");

    public EssenceVesselRenderer(BlockEntityRendererProvider.Context context) { }

    @Override
    public void render(EssenceVesselTileEntity tile, float v, PoseStack matrix, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        var inventory = tile.getInventory();
        var stack = inventory.getStackInSlot(0);

        if (!stack.isEmpty()) {
            var builder = buffer.getBuffer(RenderType.solid());
            var sprite = Minecraft.getMinecraft()
                    .getTextureMapBlocks().getAtlasSprite(VESSEL_CONTENT_TEXTURE.toString());

            float fillPercentage = (float) stack.getCount() / (float) inventory.getSlotLimit(0);

            float filledAmount = 0.4f * fillPercentage;
            float textureOffset = ((16.0f - (11.0f * fillPercentage)) / 16.0F);

            matrix.pushPose();

            var color = EssenceVesselColorManager.INSTANCE.getColor(stack);

            // top
            addVertex(builder, matrix, 0.2f, 0.75f + filledAmount, 0.8f, sprite.getMinU(), sprite.getMaxV(), color, combinedLight);
            addVertex(builder, matrix, 0.8f, 0.75f + filledAmount, 0.8f, sprite.getMaxU(), sprite.getMaxV(), color, combinedLight);
            addVertex(builder, matrix, 0.8f, 0.75f + filledAmount, 0.2f, sprite.getMaxU(), sprite.getMinV(), color, combinedLight);
            addVertex(builder, matrix, 0.2f, 0.75f + filledAmount, 0.2f, sprite.getMinU(), sprite.getMinV(), color, combinedLight);

            matrix.pushPose();
            matrix.translate(0, 1, 1);
            matrix.mulPose(Axis.XP.rotationDegrees(180));

            // bottom
            addVertex(builder, matrix, 0.2f, 0.25f, 0.8f, sprite.getMinU(), sprite.getMaxV(), color, combinedLight);
            addVertex(builder, matrix, 0.8f, 0.25f, 0.8f, sprite.getMaxU(), sprite.getMaxV(), color, combinedLight);
            addVertex(builder, matrix, 0.8f, 0.25f, 0.2f, sprite.getMaxU(), sprite.getMinV(), color, combinedLight);
            addVertex(builder, matrix, 0.2f, 0.25f, 0.2f, sprite.getMinU(), sprite.getMinV(), color, combinedLight);

            matrix.popPose();

            matrix.pushPose();

            matrix.translate(1.2, 0.55, 0);
            matrix.mulPose(Axis.ZP.rotationDegrees(90));

            // west
            addVertex(builder, matrix, 0.2f, 1, 0.8f, sprite.getInterpolatedU(16.0 * (textureOffset)), sprite.getMaxV(), color, combinedLight);
            addVertex(builder, matrix, 0.2f + filledAmount, 1, 0.8f, sprite.getMaxU(), sprite.getMaxV(), color, combinedLight);
            addVertex(builder, matrix, 0.2f + filledAmount, 1, 0.2f, sprite.getMaxU(), sprite.getMinV(), color, combinedLight);
            addVertex(builder, matrix, 0.2f, 1, 0.2f, sprite.getInterpolatedU(16.0 * (textureOffset)), sprite.getMinV(), color, combinedLight);

            matrix.popPose();
            matrix.pushPose();

            matrix.translate(-0.2, 0.55, 1);
            matrix.mulPose(Axis.ZP.rotationDegrees(270));
            matrix.mulPose(Axis.YP.rotationDegrees(180));

            // east
            addVertex(builder, matrix, 0.2f, 1, 0.8f, sprite.getInterpolatedU(16.0 * (textureOffset)), sprite.getMaxV(), color, combinedLight);
            addVertex(builder, matrix, 0.2f + filledAmount, 1, 0.8f, sprite.getMaxU(), sprite.getMaxV(), color, combinedLight);
            addVertex(builder, matrix, 0.2f + filledAmount, 1, 0.2f, sprite.getMaxU(), sprite.getMinV(), color, combinedLight);
            addVertex(builder, matrix, 0.2f, 1, 0.2f, sprite.getInterpolatedU(16.0 * (textureOffset)), sprite.getMinV(), color, combinedLight);

            matrix.popPose();
            matrix.pushPose();

            matrix.translate(1, 0.55, -0.2);
            matrix.mulPose(Axis.XP.rotationDegrees(90));
            matrix.mulPose(Axis.YP.rotationDegrees(180));

            // south
            addVertex(builder, matrix, 0.2f, 1, 0.2f + filledAmount, sprite.getMinU(), sprite.getMaxV(), color, combinedLight);
            addVertex(builder, matrix, 0.8f, 1, 0.2f + filledAmount, sprite.getMaxU(), sprite.getMaxV(), color, combinedLight);
            addVertex(builder, matrix, 0.8f, 1, 0.2f, sprite.getMaxU(), sprite.getInterpolatedV(16.0 * (textureOffset)), color, combinedLight);
            addVertex(builder, matrix, 0.2f, 1, 0.2f, sprite.getMinU(), sprite.getInterpolatedV(16.0 * (textureOffset)), color, combinedLight);

            matrix.popPose();
            matrix.pushPose();

            matrix.translate(0, 0.55, 1.2);
            matrix.mulPose(Axis.XP.rotationDegrees(270));

            // north
            addVertex(builder, matrix, 0.2f, 1, 0.2f + filledAmount, sprite.getMinU(), sprite.getMaxV(), color, combinedLight);
            addVertex(builder, matrix, 0.8f, 1, 0.2f + filledAmount, sprite.getMaxU(), sprite.getMaxV(), color, combinedLight);
            addVertex(builder, matrix, 0.8f, 1, 0.2f, sprite.getMaxU(), sprite.getInterpolatedV(16.0 * (textureOffset)), color, combinedLight);
            addVertex(builder, matrix, 0.2f, 1, 0.2f, sprite.getMinU(), sprite.getInterpolatedV(16.0 * (textureOffset)), color, combinedLight);

            matrix.popPose();

            matrix.popPose();
        }
    }

    private static void addVertex(VertexConsumer renderer, PoseStack stack, float x, float y, float z, float u, float v, int color, int combinedLight) {
        renderer.addVertex(stack.last().pose(), x, y, z)
                .setColor(color)
                .setUv(u, v)
                .setLight(combinedLight)
                .setNormal(1, 0, 0);
    }
}


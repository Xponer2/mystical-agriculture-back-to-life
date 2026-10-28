package com.blakebr0.mysticalagriculture.client.handler;

import net.minecraft.util.math.RayTraceResult;

import com.blakebr0.mysticalagriculture.api.tinkering.AOEAugment;
import com.blakebr0.mysticalagriculture.api.tinkering.ITinkerable;
import com.blakebr0.mysticalagriculture.api.util.AugmentUtils;
import com.blakebr0.mysticalagriculture.network.payloads.UpdateAOEAugmentOffsetPayload;
import io.github.xponer2.translator.client.RenderType;
import io.github.xponer2.translator.network.PacketDistributor;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

public final class AOEAugmentClientHandler {
    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        var minecraft = Minecraft.getMinecraft();
        if (minecraft.world == null || minecraft.player == null)
            return;

        var stack = minecraft.player.getHeldItemMainhand();
        var item = stack.getItem();
        if (!(item instanceof ITinkerable))
            return;


        // do anything only if control is pressed
        if (!org.lwjgl.input.Keyboard.isKeyDown(org.lwjgl.input.Keyboard.KEY_RCONTROL) && !org.lwjgl.input.Keyboard.isKeyDown(org.lwjgl.input.Keyboard.KEY_LCONTROL))
            return;

        int range = AugmentUtils.getMaxAOEAugmentRange(stack);
        if (range == 0)
            return;

        int horizontalOffsetChange = 0;
        int verticalOffsetChange = 0;

        if (org.lwjgl.input.Keyboard.isKeyDown(org.lwjgl.input.Keyboard.KEY_LEFT)) {
            horizontalOffsetChange = -1;
        } else if (org.lwjgl.input.Keyboard.isKeyDown(org.lwjgl.input.Keyboard.KEY_RIGHT)) {
            horizontalOffsetChange = 1;
        } else if (org.lwjgl.input.Keyboard.isKeyDown(org.lwjgl.input.Keyboard.KEY_DOWN)) {
            verticalOffsetChange = -1;
        } else if (org.lwjgl.input.Keyboard.isKeyDown(org.lwjgl.input.Keyboard.KEY_UP)) {
            verticalOffsetChange = 1;
        }

        if (horizontalOffsetChange != 0 || verticalOffsetChange != 0) {
            PacketDistributor.sendToServer(new UpdateAOEAugmentOffsetPayload(horizontalOffsetChange, verticalOffsetChange));
        }
    }

    /**
     * This method highlights selected blocks that will be affected by AOE effect.
     * @param event The block highlight rendering event.
     */
    @SubscribeEvent
    public void onBlockHighlight(DrawBlockHighlightEvent event) {
        var player = event.getPlayer();
        var target = event.getTarget();
        if (player == null || target == null || target.typeOfHit != RayTraceResult.Type.BLOCK)
            return;

        var stack = player.getHeldItemMainhand();
        var item = stack.getItem();
        if (!(item instanceof ITinkerable))
            return;


        if (!org.lwjgl.input.Keyboard.isKeyDown(org.lwjgl.input.Keyboard.KEY_RCONTROL) && !org.lwjgl.input.Keyboard.isKeyDown(org.lwjgl.input.Keyboard.KEY_LCONTROL))
            return;

        int range = AugmentUtils.getMaxAOEAugmentRange(stack);
        if (range == 0)
            return;

        // the camera position (the world is drawn relative to it), like RenderGlobal#drawSelectionBox
        float partialTicks = event.getPartialTicks();
        double cameraX = player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks;
        double cameraY = player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks;
        double cameraZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks;

        float red = player.isSneaking() ? 1.0F : 0.0F;
        float green = player.isSneaking() ? 0.5F : 1.0F;
        float blue = 0.0F;
        float alpha = 0.7F;

        var direction = player.isSneaking() ? EnumFacing.UP : target.sideHit;

        net.minecraft.client.renderer.GlStateManager.enableBlend();
        net.minecraft.client.renderer.GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        net.minecraft.client.renderer.GlStateManager.glLineWidth(2.0F);
        net.minecraft.client.renderer.GlStateManager.disableTexture2D();
        net.minecraft.client.renderer.GlStateManager.depthMask(false);

        AOEAugment.getAOEBlocks(stack, range, target.getBlockPos(), direction, player).
            forEach(aoePos -> {
                var state = player.world.getBlockState(aoePos);
                if (state.getMaterial() == net.minecraft.block.material.Material.AIR)
                    return;

                // Contract the block box to the face player is looking at
                var box = new AxisAlignedBB(aoePos).contract(
                    -direction.getXOffset(),
                    -direction.getYOffset(),
                    -direction.getZOffset()
                ).offset(-cameraX, -cameraY, -cameraZ);

                RenderGlobal.drawSelectionBoundingBox(box, red, green, blue, alpha);
            });

        net.minecraft.client.renderer.GlStateManager.depthMask(true);
        net.minecraft.client.renderer.GlStateManager.enableTexture2D();
        net.minecraft.client.renderer.GlStateManager.disableBlend();
    }
}

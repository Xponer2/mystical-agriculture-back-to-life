package com.blakebr0.mysticalagriculture.client.screen;

import com.blakebr0.cucumber.client.screen.BaseContainerScreen;
import com.blakebr0.cucumber.client.screen.widget.EnergyBarWidget;
import com.blakebr0.cucumber.util.Formatting;
import com.blakebr0.mysticalagriculture.MysticalAgriculture;
import com.blakebr0.mysticalagriculture.container.SouliumSpawnerContainer;
import com.blakebr0.mysticalagriculture.tileentity.SouliumSpawnerTileEntity;
import io.github.xponer2.translator.client.Axis;
import io.github.xponer2.translator.text.LiteralComponent;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

import io.github.xponer2.translator.client.GuiGraphics;
import io.github.xponer2.translator.text.MutableComponent;
import io.github.xponer2.translator.client.Rendering;

public class SouliumSpawnerScreen extends BaseContainerScreen<SouliumSpawnerContainer> {
    private static final ResourceLocation BACKGROUND = MysticalAgriculture.resource("textures/gui/soulium_spawner.png");
    private SouliumSpawnerTileEntity tile;

    public SouliumSpawnerScreen(SouliumSpawnerContainer container, InventoryPlayer inv, ITextComponent title) {
        super(container, inv, title, BACKGROUND, 176, 194);
    }

    @Override
    protected void init() {
        super.init();

        int x = this.getGuiLeft();
        int y = this.getGuiTop();

        this.tile = this.getTileEntity();

        if (this.tile != null) {
            this.addRenderableWidget(new EnergyBarWidget(x + 7, y + 17, this.tile.getEnergy()));
        }
    }

    @Override
    protected void renderLabels(GuiGraphics gfx, int mouseX, int mouseY) {
        var title = this.getTitle().getUnformattedText();

        gfx.drawString(this.font, title, (this.xSize / 2 - this.font.getStringWidth(title) / 2), 6, 4210752, false);
        gfx.drawString(this.font, this.playerInventoryTitle, 8, (this.ySize - 96 + 2), 4210752, false);

        // TODO: "temporary" workaround for dynamic energy storage
        if (this.tile != null) {
            var tier = this.tile.getMachineTier();
            var energy = this.tile.getEnergy();

            energy.resetMaxEnergyStorage();

            if (tier != null) {
                energy.setMaxEnergyStorage((int) (this.tile.getEnergy().getMaxEnergyStored() * tier.getFuelCapacityMultiplier()));
            }
        }
    }

    @Override
    protected void renderBg(GuiGraphics gfx, float partialTicks, int mouseX, int mouseY) {
        super.renderBg(gfx, partialTicks, mouseX, mouseY);

        int x = this.getGuiLeft();
        int y = this.getGuiTop();

        if (this.getFuelItemValue() > 0) {
            int i = this.getBurnLeftScaled(13);
            gfx.blit(BACKGROUND, x + 31, y + 52 - i, 176, 12 - i, 14, i + 1);
        }

        if (this.getProgress() > 0) {
            int i2 = this.getProgressScaled(24);
            gfx.blit(BACKGROUND, x + 98, y + 51, 176, 14, i2 + 1, 16);
        }

        this.renderEntityPreview(gfx);

        if (isHoveringSlot(x + 134, y + 52, mouseX, mouseY)) {
            renderSlotHighlight(gfx, x + 134, y + 52, 0);
        }
    }

    @Override
    protected void renderTooltip(GuiGraphics gfx, int mouseX, int mouseY) {
        int x = this.getGuiLeft();
        int y = this.getGuiTop();

        super.renderTooltip(gfx, mouseX, mouseY);

        if (this.getFuelLeft() > 0 && mouseX > x + 30 && mouseX < x + 45 && mouseY > y + 39 && mouseY < y + 53) {
            gfx.renderTooltip(this.font, Formatting.energy(this.getFuelLeft()), mouseX, mouseY);
        }

        if (this.tile != null) {
            var displayEntity = this.tile.getDisplayEntity();

            if (displayEntity != null && isHoveringSlot(x + 134, y + 52, mouseX, mouseY)) {
                var entity = displayEntity.entity();
                var chance = displayEntity.chance();

                MutableComponent text = new LiteralComponent("").append(entity.getDisplayName()).append(String.format(" (%.2f%%)", chance));

                gfx.renderTooltip(this.font, text, mouseX, mouseY);
            }
        }
    }

    private SouliumSpawnerTileEntity getTileEntity() {
        var level = this.mc.world;

        if (level != null) {
            var tile = level.getTileEntity(this.getMenu().getPos());

            if (tile instanceof SouliumSpawnerTileEntity spawner) {
                return spawner;
            }
        }

        return null;
    }

    public int getProgress() {
        if (this.tile == null)
            return 0;

        return this.tile.getProgress();
    }

    public int getOperationTime() {
        if (this.tile == null)
            return 0;

        var tier = this.tile.getMachineTier();
        if (tier != null) {
            return (int) (this.tile.getOperationTime() * tier.getOperationTimeMultiplier());
        }

        return this.tile.getOperationTime();
    }

    public int getFuelLeft() {
        if (this.tile == null)
            return 0;

        return this.tile.getFuelLeft();
    }

    public int getFuelItemValue() {
        if (this.tile == null)
            return 0;

        return this.tile.getFuelItemValue();
    }

    public int getProgressScaled(int pixels) {
        int i = this.getProgress();
        int j = this.getOperationTime();
        return j != 0 && i != 0 ? i * pixels / j : 0;
    }

    public int getBurnLeftScaled(int pixels) {
        int i = this.getFuelLeft();
        int j = this.getFuelItemValue();
        return (int) (j != 0 && i != 0 ? (long) i * pixels / j : 0);
    }

    private void renderEntityPreview(GuiGraphics gfx) {
        if (this.tile == null)
            return;

        var displayEntity = this.tile.getDisplayEntity();
        if (displayEntity == null)
            return;

        var entity = displayEntity.entity();

        float scale = 16.0F;
        float bbMax = Math.max(entity.width, entity.height);

        if ((double) bbMax > 1.0D) {
            scale /= bbMax;
        }

        var matrix = gfx.pose();

        matrix.pushPose();

        matrix.translate(this.guiLeft + 142, this.guiTop + 68, 32.0F);
        matrix.mulPose(Axis.YP.rotationDegrees(135.0F));
        matrix.mulPose(Axis.XP.rotationDegrees(180.0F));
        matrix.scale(scale, scale, scale);

        var buffer = gfx.bufferSource();

        Rendering.renderEntity(entity, 0.0D, 0.0D, 0.0D, 0.0F, 1, matrix, buffer, 255);

        matrix.popPose();
    }

    private static boolean isHoveringSlot(int x, int y, int mouseX, int mouseY) {
        return mouseX > x - 1 && mouseX < x + 16 && mouseY > y - 1 && mouseY < y + 16;
    }
}

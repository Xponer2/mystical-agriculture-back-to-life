package com.blakebr0.mysticalagriculture.client.screen;

import com.blakebr0.cucumber.client.screen.BaseContainerScreen;
import com.blakebr0.cucumber.iface.IToggleableSlot;
import com.blakebr0.mysticalagriculture.MysticalAgriculture;
import com.blakebr0.mysticalagriculture.container.TinkeringTableContainer;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import io.github.xponer2.translator.client.GuiGraphics;

public class TinkeringTableScreen extends BaseContainerScreen<TinkeringTableContainer> {
    private static final ResourceLocation BACKGROUND = MysticalAgriculture.resource("textures/gui/tinkering_table.png");

    public TinkeringTableScreen(TinkeringTableContainer container, InventoryPlayer inv, ITextComponent title) {
        super(container, inv, title, BACKGROUND, 176, 197);
    }

    @Override
    protected void renderLabels(GuiGraphics gfx, int mouseX, int mouseY) {
        var title = this.getTitle().getUnformattedText();

        gfx.drawString(this.font, title, (this.xSize / 2 - this.font.getStringWidth(title) / 2), 6, 4210752, false);
        gfx.drawString(this.font, this.playerInventoryTitle, 8, (this.ySize - 96 + 2), 4210752, false);
    }

    @Override
    protected void renderBg(GuiGraphics gfx, float partialTicks, int mouseX, int mouseY) {
        super.renderBg(gfx, partialTicks, mouseX, mouseY);

        int x = this.getGuiLeft();
        int y = this.getGuiTop();

        for (var slot : this.menu.slots) {
            if (slot.isEnabled() && slot instanceof IToggleableSlot) {
                gfx.blit(BACKGROUND, x + slot.xPos, y + slot.yPos, 8, 115, 16, 16);
            }
        }
    }
}

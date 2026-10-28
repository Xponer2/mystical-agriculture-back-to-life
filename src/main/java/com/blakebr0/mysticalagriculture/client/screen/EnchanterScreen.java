package com.blakebr0.mysticalagriculture.client.screen;

import com.blakebr0.cucumber.client.screen.BaseContainerScreen;
import com.blakebr0.mysticalagriculture.MysticalAgriculture;
import com.blakebr0.mysticalagriculture.container.EnchanterContainer;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import io.github.xponer2.translator.client.GuiGraphics;

public class EnchanterScreen extends BaseContainerScreen<EnchanterContainer> {
    private static final ResourceLocation BACKGROUND = MysticalAgriculture.resource("textures/gui/enchanter.png");

    public EnchanterScreen(EnchanterContainer container, InventoryPlayer inv, ITextComponent title) {
        super(container, inv, title, BACKGROUND, 176, 177);
    }

    @Override
    protected void renderLabels(GuiGraphics gfx, int mouseX, int mouseY) {
        var title = this.getTitle().getUnformattedText();

        gfx.drawString(this.font, title, (this.xSize / 2 - this.font.getStringWidth(title) / 2), 6, 4210752, false);
        gfx.drawString(this.font, this.playerInventoryTitle, 8, (this.ySize - 96 + 2), 4210752, false);
    }
}

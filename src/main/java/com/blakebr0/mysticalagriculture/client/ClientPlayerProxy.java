package com.blakebr0.mysticalagriculture.client;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

// workaround for https://github.com/BlakeBr0/MysticalAgriculture/issues/722
// prevents loading the Minecraft class in ModTooltips
public final class ClientPlayerProxy {
    public static EntityPlayer getPlayer() {
        return Minecraft.getMinecraft().player;
    }
}

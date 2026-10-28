package com.blakebr0.mysticalagriculture.handler;

import java.util.ArrayList;
import java.util.List;

import io.github.xponer2.translator.network.PacketDistributor;
import com.blakebr0.mysticalagriculture.api.util.ExperienceCapsuleUtils;
import com.blakebr0.mysticalagriculture.item.ExperienceCapsuleItem;
import com.blakebr0.mysticalagriculture.network.payloads.ExperienceCapsulePickupPayload;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerPickupXpEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public final class ExperienceCapsuleHandler {
    @SubscribeEvent
    public void onPlayerPickupXp(PlayerPickupXpEvent event) {
        var orb = event.getOrb();
        var player = event.getEntityPlayer();
        var capsules = getExperienceCapsules(player);

        if (!capsules.isEmpty()) {
            for (var stack : capsules) {
                int remaining = ExperienceCapsuleUtils.addExperienceToCapsule(stack, orb.getXpValue());

                orb.xpValue = remaining;

                if (remaining == 0) {
                    orb.setDead();

                    PacketDistributor.sendToPlayer((EntityPlayerMP) player, new ExperienceCapsulePickupPayload());

                    event.setCanceled(true);
                    return;
                }
            }
        }
    }

    private static List<ItemStack> getExperienceCapsules(EntityPlayer player) {
        var items = new ArrayList<ItemStack>();

        var stack = player.getHeldItemOffhand();
        if (stack.getItem() instanceof ExperienceCapsuleItem)
            items.add(stack);

        player.inventory.mainInventory
                .stream()
                .filter(s -> s.getItem() instanceof ExperienceCapsuleItem)
                .forEach(items::add);

        return items;
    }
}

package com.blakebr0.mysticalagriculture.handler;

import com.blakebr0.mysticalagriculture.api.lib.AbilityCache;
import com.blakebr0.mysticalagriculture.api.util.AugmentUtils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public final class AugmentHandler {
    private static final AbilityCache ABILITY_CACHE = new AbilityCache();

    @SubscribeEvent
    public void onPlayerUpdate(TickEvent.PlayerTickEvent event) {
        // 1.21 PlayerTickEvent.Pre
        if (event.phase != TickEvent.Phase.START)
            return;

        var player = event.player;

        if (player.getHealth() > 0.0F) {
            var level = player.world;
            var augments = AugmentUtils.getArmorAugments(player);

            for (var augment : augments) {
                augment.onPlayerTick(level, player, ABILITY_CACHE);
            }

            for (var augment : ABILITY_CACHE.getCachedAbilities(player)) {
                if (augments.stream().noneMatch(a -> augment.equals(a.getId().toString()))) {
                    ABILITY_CACHE.remove(augment, player);
                }
            }
        }
    }

    @SubscribeEvent
    public void onLivingFall(LivingFallEvent event) {
        var entity = event.getEntity();

        if (entity instanceof EntityPlayer player) {
            var level = player.world;

            for (var augment : AugmentUtils.getArmorAugments(player)) {
                augment.onPlayerFall(level, player, event);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        var player = event.player;

        for (var augment : ABILITY_CACHE.getCachedAbilities(player)) {
            ABILITY_CACHE.removeQuietly(augment, player);
        }
    }

    @SubscribeEvent
    public void onPlayerDeath(LivingDeathEvent event) {
        var entity = event.getEntity();

        if (entity instanceof EntityPlayer player) {
            for (var augment : ABILITY_CACHE.getCachedAbilities(player)) {
                ABILITY_CACHE.removeQuietly(augment, player);
            }
        }
    }
}

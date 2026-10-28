package com.blakebr0.mysticalagriculture.api.lib;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import com.blakebr0.mysticalagriculture.api.tinkering.Augment;

import net.minecraft.entity.player.EntityPlayer;

public class AbilityCache {
    private static final HashMap<String, Runnable> EMPTY_MAP = new HashMap<>();
    private final Map<String, Map<String, Runnable>> cache = new ConcurrentHashMap<>();

    /**
     * Add a user to the cache for the specified augment, meaning they have it equipped currently
     * @param augment the augment
     * @param player the player
     * @param onRemove a callback to execute when they are removed from the cache
     */
    public void add(Augment augment, EntityPlayer player, Runnable onRemove) {
        this.add(augment.getId().toString(), player, onRemove);
    }

    /**
     * Add a user to the cache for the specified augment id, meaning they have it equipped currently
     * @param augment the augment id
     * @param player the player
     * @param onRemove a callback to execute when they are removed from the cache
     */
    public void add(String augment, EntityPlayer player, Runnable onRemove) {
        var key = getPlayerKey(player);
        this.cache.computeIfAbsent(augment, s -> new ConcurrentHashMap<>()).put(key, onRemove);
    }

    /**
     * Remove a user from the cache for the specified augment, meaning they no longer have it equipped, and the
     * onRemove callback will be invoked
     * @param augment the augment
     * @param player the player
     */
    public void remove(Augment augment, EntityPlayer player) {
        this.remove(augment.getId().toString(), player);
    }

    /**
     * Remove a user from the cache for the specified augment id, meaning they no longer have it equipped, and the
     * onRemove callback will be invoked
     * @param augment the augment id
     * @param player the player
     */
    public void remove(String augment, EntityPlayer player) {
        var key = getPlayerKey(player);
        this.cache.getOrDefault(augment, EMPTY_MAP).remove(key).run();
    }

    /**
     * Remove a user from the cache for the specified augment, without invoking the onRemove callback
     * @param augment the augment
     * @param player the player
     */
    public void removeQuietly(Augment augment, EntityPlayer player) {
        this.removeQuietly(augment.getId().toString(), player);
    }

    /**
     * Remove a user from the cache for the specified augment id, without invoking the onRemove callback
     * @param augment the augment id
     * @param player the player
     */
    public void removeQuietly(String augment, EntityPlayer player) {
        String key = getPlayerKey(player);
        this.cache.getOrDefault(augment, EMPTY_MAP).remove(key);
    }

    /**
     * Check if the provided player is already cached for this augment
     * @param augment the augment
     * @param player the player
     * @return is cached
     */
    public boolean isCached(Augment augment, EntityPlayer player) {
        return this.isCached(augment.getId().toString(), player);
    }

    /**
     * Check if the provided player is already cached for this augment id
     * @param augment the augment id
     * @param player the player
     * @return is cached
     */
    public boolean isCached(String augment, EntityPlayer player) {
        var key = getPlayerKey(player);
        return this.cache.getOrDefault(augment, EMPTY_MAP).containsKey(key);
    }

    /**
     * Gets the augment ids for the cached abilities for this player
     * @param player the player
     * @return the augment ids
     */
    public Set<String> getCachedAbilities(EntityPlayer player) {
        var key = getPlayerKey(player);
        return this.cache.entrySet()
                .stream()
                .filter(e -> e.getValue().containsKey(key))
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
    }

    private static String getPlayerKey(EntityPlayer player) {
        return player.getGameProfile().getName() + ":" + player.world.isRemote;
    }
}

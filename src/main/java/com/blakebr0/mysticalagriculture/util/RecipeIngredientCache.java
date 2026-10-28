package com.blakebr0.mysticalagriculture.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import io.github.xponer2.translator.network.PacketDistributor;
import io.github.xponer2.translator.recipe.Recipe;
import io.github.xponer2.translator.recipe.RecipeInput;
import io.github.xponer2.translator.recipe.RecipeType;
import com.blakebr0.cucumber.helper.RecipeHelper;
import com.blakebr0.mysticalagriculture.MysticalAgriculture;
import com.blakebr0.mysticalagriculture.init.ModRecipeTypes;
import com.blakebr0.mysticalagriculture.network.payloads.ReloadIngredientCachePayload;
import com.google.common.base.Stopwatch;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import io.github.xponer2.translator.util.Platform;

public class RecipeIngredientCache {
    public static final RecipeIngredientCache INSTANCE = new RecipeIngredientCache();

    private final Map<RecipeType<?>, Map<Item, List<Ingredient>>> caches;
    private final Set<Item> validVesselItems;

    private RecipeIngredientCache() {
        this.caches = new HashMap<>();
        this.validVesselItems = new HashSet<>();
    }

    // 1.21 OnDatapackSyncEvent of a joining player (1.12.2 has no datapack reloads)
    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        // send the caches to the client
        if (event.player instanceof EntityPlayerMP player) {
            PacketDistributor.sendToPlayer(player, new ReloadIngredientCachePayload(this.caches, this.validVesselItems));
        }
    }

    // update caches when tags are loaded to prevent issues like #600; 1.12.2: once recipes and tags are loaded
    // (RecipeLoader.onLoaded, registered in MysticalAgriculture#preInit)
    public void onRecipesLoaded() {
        var stopwatch = Stopwatch.createStarted();

        this.caches.clear();

        cache(RecipeType.SMELTING);
        cache(ModRecipeTypes.REPROCESSOR.get());
        cache(ModRecipeTypes.SOUL_EXTRACTION.get());
        cache(ModRecipeTypes.SOULIUM_SPAWNER.get());

        this.validVesselItems.clear();

        cacheVesselItems();

        MysticalAgriculture.LOGGER.info("Recipe ingredient caching done in {} ms", stopwatch.stop().elapsed(TimeUnit.MILLISECONDS));
    }

    // called on the client by ReloadIngredientCacheMessage
    public void setCaches(Map<RecipeType<?>, Map<Item, List<Ingredient>>> caches) {
        this.caches.clear();
        this.caches.putAll(caches);
    }

    // called on the client by ReloadIngredientCacheMessage
    public void setValidVesselItems(Set<Item> validVesselItems) {
        this.validVesselItems.clear();
        this.validVesselItems.addAll(validVesselItems);
    }

    public boolean isValidInput(ItemStack stack, RecipeType<?> type) {
        var cache = this.caches.getOrDefault(type, Collections.emptyMap()).get(stack.getItem());
        return cache != null && cache.stream().anyMatch(i -> i.test(stack));
    }

    // soulium spawner ingredients are count dependant, and we don't care in this case
    public boolean isValidSouliumSpawnerInput(ItemStack stack) {
        return isValidInput(Platform.copyWithCount(stack, Integer.MAX_VALUE), ModRecipeTypes.SOULIUM_SPAWNER.get());
    }

    public boolean isValidVesselItem(ItemStack stack) {
        return this.validVesselItems.contains(stack.getItem());
    }

    private static <C extends RecipeInput, T extends Recipe<C>> void cache(RecipeType<T> type) {
        INSTANCE.caches.put(type, new HashMap<>());

        for (var recipe : RecipeHelper.byType(type)) {
            for (var ingredient : recipe.value().getIngredients()) {
                var items = new HashSet<>();
                for (var stack : ingredient.getMatchingStacks()) {
                    var item = stack.getItem();
                    if (items.contains(item))
                        continue;

                    var cache = INSTANCE.caches.get(type).computeIfAbsent(item, i -> new ArrayList<>());

                    items.add(item);
                    cache.add(ingredient);
                }
            }
        }
    }

    private static void cacheVesselItems() {
        for (var recipe : RecipeHelper.byType(ModRecipeTypes.AWAKENING.get())) {
            for (var essence : recipe.value().getEssences()) {
                INSTANCE.validVesselItems.add(essence.getItem());
            }
        }
    }
}

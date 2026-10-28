package com.blakebr0.mysticalagriculture.network.payloads;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.blakebr0.mysticalagriculture.MysticalAgriculture;
import com.blakebr0.mysticalagriculture.util.RecipeIngredientCache;
import io.github.xponer2.translator.network.ByteBufCodecs;
import io.github.xponer2.translator.network.CustomPacketPayload;
import io.github.xponer2.translator.network.IPayloadContext;
import io.github.xponer2.translator.recipe.RecipeType;

import net.minecraft.item.Item;
import net.minecraft.item.crafting.Ingredient;

import io.github.xponer2.translator.network.RegistryFriendlyByteBuf;
import io.github.xponer2.translator.network.StreamCodec;
import io.github.xponer2.translator.util.BuiltInRegistries;

public final class ReloadIngredientCachePayload implements CustomPacketPayload {
    private final Map<RecipeType<?>, Map<Item, List<Ingredient>>> caches;
    private final Set<Item> validVesselItems;

    public ReloadIngredientCachePayload(Map<RecipeType<?>, Map<Item, List<Ingredient>>> caches, Set<Item> validVesselItems) {
        this.caches = caches;
        this.validVesselItems = validVesselItems;
    }

    public Map<RecipeType<?>, Map<Item, List<Ingredient>>> caches() {
        return caches;
    }

    public Set<Item> validVesselItems() {
        return validVesselItems;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReloadIngredientCachePayload)) return false;
        ReloadIngredientCachePayload that = (ReloadIngredientCachePayload) o;
        return java.util.Objects.equals(caches, that.caches)
                && java.util.Objects.equals(validVesselItems, that.validVesselItems);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(caches, validVesselItems);
    }

    @Override
    public String toString() {
        return "ReloadIngredientCachePayload[" + "caches=" + caches + ", " + "validVesselItems=" + validVesselItems + "]";
    }

    public static final Type<ReloadIngredientCachePayload> TYPE = new Type<>(MysticalAgriculture.resource("reload_ingredient_cache"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ReloadIngredientCachePayload> STREAM_CODEC = StreamCodec.of(
            ReloadIngredientCachePayload::toNetwork, ReloadIngredientCachePayload::fromNetwork
    );

    @Override
    public Type<ReloadIngredientCachePayload> type() {
        return TYPE;
    }

    private static ReloadIngredientCachePayload fromNetwork(RegistryFriendlyByteBuf buffer) {
        var caches = new HashMap<RecipeType<?>, Map<Item, List<Ingredient>>>();
        var types = buffer.readVarInt();

        for (var i = 0; i < types; i++) {
            var type = BuiltInRegistries.RECIPE_TYPE.get(buffer.readResourceLocation());
            var items = buffer.readVarInt();

            caches.put(type, new HashMap<>());

            for (var j = 0; j < items; j++) {
                var item = BuiltInRegistries.ITEM.get(buffer.readResourceLocation());
                var ingredients = buffer.readVarInt();

                for (var k = 0; k < ingredients; k++) {
                    var cache = caches.get(type).computeIfAbsent(item, l -> new ArrayList<>());
                    var ingredient = ByteBufCodecs.INGREDIENT.decode(buffer);

                    cache.add(ingredient);
                }
            }
        }

        var validVesselItems = new HashSet<Item>();
        var items = buffer.readVarInt();

        for (var i = 0; i < items; i++) {
            var item = BuiltInRegistries.ITEM.get(buffer.readResourceLocation());

            validVesselItems.add(item);
        }

        return new ReloadIngredientCachePayload(caches, validVesselItems);
    }

    private static void toNetwork(RegistryFriendlyByteBuf buffer, ReloadIngredientCachePayload payload) {
        buffer.writeVarInt(payload.caches.size());

        for (var entry : payload.caches.entrySet()) {
            var type = BuiltInRegistries.RECIPE_TYPE.getKey(entry.getKey());
            var caches = entry.getValue();

            assert type != null;

            buffer.writeResourceLocation(type);
            buffer.writeVarInt(caches.size());

            for (var cache : caches.entrySet()) {
                var item = BuiltInRegistries.ITEM.getKey(cache.getKey());
                var ingredients = cache.getValue();

                buffer.writeResourceLocation(item);
                buffer.writeVarInt(ingredients.size());

                for (var ingredient : ingredients) {
                    ByteBufCodecs.INGREDIENT.encode(buffer, ingredient);
                }
            }
        }

        buffer.writeVarInt(payload.validVesselItems.size());

        for (var item : payload.validVesselItems) {
            var id = BuiltInRegistries.ITEM.getKey(item);

            buffer.writeResourceLocation(id);
        }
    }

    public static void handleClient(ReloadIngredientCachePayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            RecipeIngredientCache.INSTANCE.setCaches(payload.caches);
            RecipeIngredientCache.INSTANCE.setValidVesselItems(payload.validVesselItems);
        });
    }
}

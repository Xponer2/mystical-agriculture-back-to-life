package com.blakebr0.mysticalagriculture.crafting;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import io.github.xponer2.translator.network.PacketDistributor;
import com.blakebr0.cucumber.helper.ParsingHelper;
import com.blakebr0.mysticalagriculture.MysticalAgriculture;
import com.blakebr0.mysticalagriculture.network.payloads.SyncEssenceVesselColorsPayload;
import com.google.common.base.Stopwatch;
import com.google.gson.JsonParser;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import io.github.xponer2.translator.util.BuiltInRegistries;

// 1.12.2 has no datapacks: the colors are read once from the jars of all mods
// (data/<namespace>/mysticalagriculture/essence_vessel_colors.json) and sent to players when they join
public class EssenceVesselColorManager {
    public static final EssenceVesselColorManager INSTANCE = new EssenceVesselColorManager();
    private static final Pattern COLORS_FILE = Pattern.compile("[^/]+/mysticalagriculture/essence_vessel_colors\\.json");

    private final Map<String, Integer> colors = new HashMap<>();

    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        // send the caches to the client (1.21 OnDatapackSyncEvent of a joining player)
        if (event.player instanceof EntityPlayerMP player) {
            PacketDistributor.sendToPlayer(player, new SyncEssenceVesselColorsPayload(this.colors));
        }
    }

    public int getColor(ItemStack stack) {
        var id = BuiltInRegistries.ITEM.getKey(stack.getItem());
        // resource pack colors exist on the client only
        if (FMLCommonHandler.instance().getSide().isClient()) {
            var color = com.blakebr0.mysticalagriculture.client.EssenceVesselColorManager.INSTANCE.getColor(stack);
            if (color != 0xFFFFFF)
                return color;
        }

        return this.colors.getOrDefault(id.toString(), 0xFFFFFF);
    }

    public void addColor(ItemStack stack, int color) {
        var id = BuiltInRegistries.ITEM.getKey(stack.getItem());
        this.colors.put(id.toString(), color);
    }

    public void setColors(Map<String, Integer> colors) {
        this.colors.clear();
        this.colors.putAll(colors);
    }

    public void load() {
        var stopwatch = Stopwatch.createStarted();

        this.colors.clear();

        for (var mod : Loader.instance().getActiveModList()) {
            CraftingHelper.findFiles(mod, "data", null, (root, file) -> {
                var relative = root.relativize(file).toString().replace('\\', '/');
                if (!COLORS_FILE.matcher(relative).matches())
                    return true;

                try {
                    var text = new String(Files.readAllBytes(file), StandardCharsets.UTF_8);
                    var json = new JsonParser().parse(text).getAsJsonObject();

                    for (var entry : json.entrySet()) {
                        var item = entry.getKey();
                        var color = ParsingHelper.parseHex(entry.getValue().getAsString(), item);

                        this.colors.put(item, color);
                    }
                } catch (IOException | RuntimeException e) {
                    MysticalAgriculture.LOGGER.error("Failed to load {} of {}", relative, mod.getModId(), e);
                }

                return true;
            }, true, true);
        }

        MysticalAgriculture.LOGGER.info("Loaded {} essence vessel colors in {} ms", this.colors.size(), stopwatch.stop().elapsed(TimeUnit.MILLISECONDS));
    }
}

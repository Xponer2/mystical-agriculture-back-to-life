package com.blakebr0.mysticalagriculture.client;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import com.blakebr0.cucumber.helper.ParsingHelper;
import com.blakebr0.mysticalagriculture.MysticalAgriculture;
import com.google.common.base.Stopwatch;
import com.google.gson.JsonParser;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import io.github.xponer2.translator.util.BuiltInRegistries;

// colors of resource packs: assets/<namespace>/essence_vessel_colors.json
@Deprecated
@SideOnly(Side.CLIENT)
public class EssenceVesselColorManager implements IResourceManagerReloadListener {
    public static final EssenceVesselColorManager INSTANCE = new EssenceVesselColorManager();

    private final HashMap<String, Integer> colors = new HashMap<>();

    /**
     * 1.21 RegisterClientReloadListenersEvent; also loads the colors right away.
     */
    public static void register() {
        ((IReloadableResourceManager) Minecraft.getMinecraft().getResourceManager()).registerReloadListener(INSTANCE);
    }

    @Override
    public void onResourceManagerReload(IResourceManager manager) {
        this.load(manager);
    }

    public int getColor(ItemStack stack) {
        var id = BuiltInRegistries.ITEM.getKey(stack.getItem());
        return this.colors.getOrDefault(id.toString(), 0xFFFFFF);
    }

    private void load(IResourceManager manager) {
        var stopwatch = Stopwatch.createStarted();

        this.colors.clear();

        for (var namespace : manager.getResourceDomains()) {
            var location = new ResourceLocation(namespace, "essence_vessel_colors.json");
            try {
                for (var resource : manager.getAllResources(location)) {
                    try (var reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)) {
                        var json = new JsonParser().parse(reader).getAsJsonObject();

                        for (var entry : json.entrySet()) {
                            var item = entry.getKey();
                            var color = ParsingHelper.parseHex(entry.getValue().getAsString(), item);

                            this.colors.put(item, color);
                        }
                    } catch (IOException | RuntimeException e) {
                        MysticalAgriculture.LOGGER.error("Failed to load {}", location, e);
                    }
                }
            } catch (FileNotFoundException e) {
                // no colors in this namespace
            } catch (IOException e) {
                MysticalAgriculture.LOGGER.error("Failed to load {}", location, e);
            }
        }

        MysticalAgriculture.LOGGER.info("Loaded {} essence vessel colors in {} ms", this.colors.size(), stopwatch.stop().elapsed(TimeUnit.MILLISECONDS));
    }
}

package com.blakebr0.mysticalagriculture.world;

import com.blakebr0.mysticalagriculture.MysticalAgriculture;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * Reads the 1.21 world generation data of the mod jar (data/mysticalagriculture/...). 1.12.2 has no data packs, so
 * these files can only be changed in the jar.
 */
final class WorldGenData {
    private WorldGenData() { }

    static JsonObject read(String path) {
        var file = "/data/" + MysticalAgriculture.MOD_ID + "/" + path + ".json";
        var stream = MysticalAgriculture.class.getResourceAsStream(file);
        if (stream == null) {
            throw new IllegalStateException(file + " is missing");
        }

        try (var reader = new InputStreamReader(stream, StandardCharsets.UTF_8)) {
            return new JsonParser().parse(reader).getAsJsonObject();
        } catch (IOException e) {
            throw new IllegalStateException("can't read " + file, e);
        }
    }

    /**
     * The path of an id of this mod ("mysticalagriculture:inferium_ore" -> "inferium_ore").
     */
    static String path(String id) {
        var prefix = MysticalAgriculture.MOD_ID + ":";
        if (!id.startsWith(prefix)) {
            throw new IllegalStateException("not an id of " + MysticalAgriculture.MOD_ID + ": " + id);
        }
        return id.substring(prefix.length());
    }
}

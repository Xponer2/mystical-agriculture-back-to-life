package com.blakebr0.mysticalagriculture.item;

import java.util.function.Function;

import com.blakebr0.cucumber.item.BaseWateringCanItem;
import com.blakebr0.mysticalagriculture.config.ModConfigs;
import io.github.xponer2.translator.item.ItemProperties;

public class WateringCanItem extends BaseWateringCanItem {
    public WateringCanItem(int range, double chance) {
        super(range, chance);
    }

    public WateringCanItem(int range, double chance, Function<ItemProperties, ItemProperties> properties) {
        super(range, chance, properties);
    }

    @Override
    protected boolean allowFakePlayerWatering() {
        return ModConfigs.FAKE_PLAYER_WATERING.get();
    }
}

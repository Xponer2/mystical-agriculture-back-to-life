package com.blakebr0.mysticalagriculture.augment;

import java.util.EnumSet;

import com.blakebr0.cucumber.helper.ColorHelper;
import com.blakebr0.mysticalagriculture.MysticalAgriculture;
import com.blakebr0.mysticalagriculture.api.lib.AbilityCache;
import com.blakebr0.mysticalagriculture.api.tinkering.Augment;
import com.blakebr0.mysticalagriculture.api.tinkering.AugmentType;
import com.blakebr0.mysticalagriculture.registry.AugmentRegistry;
import io.github.xponer2.translator.entity.AttributeModifier;
import io.github.xponer2.translator.entity.Attributes;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import io.github.xponer2.translator.entity.AttributeInstance;
import io.github.xponer2.translator.util.ResourceLocations;

public class HealthBoostAugment extends Augment {
    private static final ResourceLocation ATTRIBUTE_ID = MysticalAgriculture.resource("health_boost_augment");
    private final int amplifier;

    public HealthBoostAugment(ResourceLocation id, int tier, int amplifier) {
        super(id, tier, EnumSet.of(AugmentType.ARMOR), getColor(0xC6223B, tier), getColor(0x3B0402, tier));
        this.amplifier = amplifier;
    }

    @Override
    public void onPlayerTick(World level, EntityPlayer player, AbilityCache cache) {
        if (!cache.isCached(this, player)) {
            var health = AttributeInstance.of(player, Attributes.MAX_HEALTH);
            if (health == null)
                return;

            int boost = 4 * this.amplifier;

            var modifier = health.getModifier(ATTRIBUTE_ID);
            if (modifier != null) {
                if (boost < modifier.amount())
                    return;

                health.removeModifier(modifier);

                cache.getCachedAbilities(player).forEach(c -> {
                    var augment = AugmentRegistry.getInstance().getAugmentById(ResourceLocations.parse(c));

                    if (augment instanceof HealthBoostAugment && cache.isCached(augment, player)) {
                        cache.removeQuietly(c, player);
                    }
                });
            }

            health.addPermanentModifier(new AttributeModifier(ATTRIBUTE_ID, boost, AttributeModifier.Operation.ADD_VALUE));

            cache.add(this, player, () -> {
                float max = player.getMaxHealth() - boost;

                if (player.getHealth() > max) {
                    player.setHealth(max);
                }

                health.removeModifier(ATTRIBUTE_ID);
            });
        }
    }

    private static int getColor(int color, int tier) {
        return ColorHelper.saturate(color, Math.min((float) tier / 5, 1));
    }
}

package com.blakebr0.mysticalagriculture.augment;

import java.util.EnumSet;

import com.blakebr0.cucumber.helper.ColorHelper;
import com.blakebr0.mysticalagriculture.api.lib.AbilityCache;
import com.blakebr0.mysticalagriculture.api.tinkering.Augment;
import com.blakebr0.mysticalagriculture.api.tinkering.AugmentType;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class SpeedAugment extends Augment {
    private final int amplifier;

    public SpeedAugment(ResourceLocation id, int tier, int amplifier) {
        super(id, tier, EnumSet.of(AugmentType.LEGGINGS), getColor(0xAD524D, tier), getColor(0x240805, tier));
        this.amplifier = amplifier;
    }

    @Override
    public void onPlayerTick(World level, EntityPlayer player, AbilityCache cache) {
        var flying = player.capabilities.isFlying;
        var swimming = player.isInWater();
        var inWater = player.isInWater();

        if (player.onGround || flying || swimming || inWater) {
            var sneaking = player.isSneaking();
            var sprinting = player.isSprinting();

            float speed = 0.1F
                    * (flying ? 0.6F : 1.0F)
                    * (sneaking ? 0.1F : 1.0F)
                    * (!sprinting ? 0.6F : 1.2F)
                    * (inWater ? 0.5F : 1.0F)
                    * (swimming ? 0.8F : 1.0F)
                    * this.amplifier;

            if (player.moveForward > 0F) {
                player.moveRelative(0F, 0F, speed, 1F);
            } else if (player.moveForward < 0F) {
                player.moveRelative(0F, 0F, -speed * 0.3F, 1F);
            }

            if (player.moveStrafing != 0F) {
                player.moveRelative(speed * 0.5F * Math.signum(player.moveStrafing), 0F, 0F, 1F);
            }
        }
    }

    private static int getColor(int color, int tier) {
        return ColorHelper.saturate(color, Math.min((float) tier / 4, 1));
    }
}

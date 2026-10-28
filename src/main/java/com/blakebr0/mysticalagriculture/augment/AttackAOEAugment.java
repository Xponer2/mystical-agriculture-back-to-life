package com.blakebr0.mysticalagriculture.augment;

import java.util.EnumSet;

import com.blakebr0.cucumber.helper.ColorHelper;
import com.blakebr0.mysticalagriculture.api.tinkering.Augment;
import com.blakebr0.mysticalagriculture.api.tinkering.AugmentType;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.DamageSource;

public class AttackAOEAugment extends Augment {
    private final int amplifier;

    public AttackAOEAugment(ResourceLocation id, int tier, int amplifier) {
        super(id, tier, EnumSet.of(AugmentType.SWORD), getColor(0xFF0000, tier), getColor(0x700000, tier));
        this.amplifier = amplifier;
    }

    @Override
    public boolean onHitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        if (attacker instanceof EntityPlayer player) {
            if (!player.getCooldownTracker().hasCooldown(stack.getItem())) {
                var level = player.world;
                var entities = level.getEntitiesWithinAABB(EntityLivingBase.class, target.getEntityBoundingBox().grow(1.5D * this.amplifier, 0.25D * this.amplifier, 1.5D * this.amplifier));

                for (var aoeEntity : entities) {
                    if (aoeEntity != player && aoeEntity != target && !player.isOnSameTeam(target)) {
                        aoeEntity.knockBack(null,0.4F, MathHelper.sin(player.rotationYaw * 0.017453292F), -MathHelper.cos(player.rotationYaw * 0.017453292F));
                        aoeEntity.attackEntityFrom(DamageSource.causePlayerDamage(player), 5.0F + (5.0F * this.amplifier));
                    }
                }

                player.getEntityWorld().playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, player.getSoundCategory(), 1.0F, 1.0F);
                player.spawnSweepParticles();
            }

            return true;
        }

        return false;
    }

    private static int getColor(int color, int tier) {
        return ColorHelper.saturate(color, Math.min((float) tier / 5, 1));
    }
}

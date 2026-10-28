package com.blakebr0.mysticalagriculture.handler;

import java.util.List;
import java.util.stream.Collectors;

import com.blakebr0.mysticalagriculture.api.soul.ISoulSiphoningItem;
import com.blakebr0.mysticalagriculture.api.soul.MobSoulType;
import com.blakebr0.mysticalagriculture.api.util.MobSoulUtils;
import com.blakebr0.mysticalagriculture.item.SoulJarItem;
import com.blakebr0.mysticalagriculture.lib.ModEnchantments;
import com.blakebr0.mysticalagriculture.registry.MobSoulTypeRegistry;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public final class MobSoulHandler {
    @SubscribeEvent
    public void onLivingDeath(LivingDeathEvent event) {
        var source = event.getSource().getTrueSource();

        if (source instanceof EntityPlayer player) {
            var held = player.getHeldItem(EnumHand.MAIN_HAND);

            if (held.getItem() instanceof ISoulSiphoningItem siphoner) {
                var entity = event.getEntityLiving();
                var type = MobSoulTypeRegistry.getInstance().getMobSoulTypeByEntity(entity);

                if (type == null || !type.isEnabled())
                    return;

                var jars = getValidSoulJars(player, type);

                if (!jars.isEmpty()) {
                    double remaining = getSoulSiphonerTotal(siphoner, held, entity);

                    for (var jar : jars) {
                        remaining = MobSoulUtils.addSoulsToJar(jar, type, remaining);
                        if (remaining <= 0)
                            break;
                    }
                }
            }
        }
    }

    private static List<ItemStack> getValidSoulJars(EntityPlayer player, MobSoulType type) {
        return player.inventory.mainInventory.stream()
                .filter(s -> s.getItem() instanceof SoulJarItem)
                .filter(s -> MobSoulUtils.canAddTypeToJar(s, type))
                .sorted((a, b) -> MobSoulUtils.getType(a) != null ? -1 : MobSoulUtils.getType(b) != null ? 0 : 1)
                .collect(Collectors.toList());
    }

    private static double getSoulSiphonerTotal(ISoulSiphoningItem siphoner, ItemStack stack, EntityLivingBase entity) {
        double amount = siphoner.getSiphonAmount(stack, entity);

        var enchantmentLevel = EnchantmentHelper.getEnchantmentLevel(ModEnchantments.SOUL_SIPHONER.value(), stack);
        if (enchantmentLevel > 0) {
            amount *= (1.0D + (0.1D * enchantmentLevel));
        }

        return amount;
    }
}

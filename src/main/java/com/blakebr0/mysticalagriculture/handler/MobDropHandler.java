package com.blakebr0.mysticalagriculture.handler;

import com.blakebr0.cucumber.util.Utils;
import com.blakebr0.mysticalagriculture.api.tinkering.ITinkerable;
import com.blakebr0.mysticalagriculture.config.ModConfigs;
import com.blakebr0.mysticalagriculture.init.ModItems;
import com.blakebr0.mysticalagriculture.lib.ModEnchantments;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public final class MobDropHandler {
    @SubscribeEvent
    public void onLivingDrops(LivingDropsEvent event) {
        var entity = event.getEntity();
        var level = entity.world;

        if (!level.getGameRules().getBoolean("doMobLoot"))
            return;

        var drops = event.getDrops();
        var attacker = event.getSource().getTrueSource();
        double inferiumDropChance = ModConfigs.INFERIUM_DROP_CHANCE.get();

        if (entity instanceof EntityCreature && Math.random() < inferiumDropChance) {
            drops.add(new EntityItem(level, entity.posX, entity.posY, entity.posZ, new ItemStack(ModItems.INFERIUM_ESSENCE.get())));
        }

        if (attacker instanceof EntityPlayer player) {
            var held = player.getHeldItemMainhand();
            var item = held.getItem();

            if (item instanceof ITinkerable tinkerable) {
                boolean witherDropsEssence = ModConfigs.WITHER_DROPS_ESSENCE.get();

                if (witherDropsEssence && entity instanceof EntityWither) {
                    var stack = getEssenceForTinkerable(tinkerable, 1, 3);

                    if (!stack.isEmpty()) {
                        drops.add(new EntityItem(level, entity.posX, entity.posY, entity.posZ, stack));
                    }
                }

                boolean dragonDropsEssence = ModConfigs.DRAGON_DROPS_ESSENCE.get();

                if (dragonDropsEssence && entity instanceof EntityDragon) {
                    var stack = getEssenceForTinkerable(tinkerable, 2, 4);

                    if (!stack.isEmpty()) {
                        drops.add(new EntityItem(level, entity.posX, entity.posY, entity.posZ, stack));
                    }
                }

                var enlightenmentLevel = EnchantmentHelper.getEnchantmentLevel(ModEnchantments.MYSTICAL_ENLIGHTENMENT.value(), held);

                if (enlightenmentLevel > 0) {
                    boolean witherDropsCognizant = ModConfigs.WITHER_DROPS_COGNIZANT.get();

                    if (witherDropsCognizant && entity instanceof EntityWither) {
                        var stack = new ItemStack(ModItems.COGNIZANT_DUST.get(), 4 + (enlightenmentLevel - 1));

                        drops.add(new EntityItem(level, entity.posX, entity.posY, entity.posZ, stack));
                    }

                    boolean dragonDropsCognizant = ModConfigs.DRAGON_DROPS_COGNIZANT.get();

                    if (dragonDropsCognizant && entity instanceof EntityDragon) {
                        var stack = new ItemStack(ModItems.COGNIZANT_DUST.get(), 4 + (enlightenmentLevel * 2));

                        drops.add(new EntityItem(level, entity.posX, entity.posY, entity.posZ, stack));
                    }
                }
            }
        }
    }

    private static ItemStack getEssenceForTinkerable(ITinkerable tinkerable, int min, int max) {
        return switch (tinkerable.getTinkerableTier()) {
            case 1 -> new ItemStack(ModItems.INFERIUM_ESSENCE.get(), Utils.randInt(min, max));
            case 2 -> new ItemStack(ModItems.PRUDENTIUM_ESSENCE.get(), Utils.randInt(min, max));
            case 3 -> new ItemStack(ModItems.TERTIUM_ESSENCE.get(), Utils.randInt(min, max));
            case 4 -> new ItemStack(ModItems.IMPERIUM_ESSENCE.get(), Utils.randInt(min, max));
            case 5 -> new ItemStack(ModItems.SUPREMIUM_ESSENCE.get(), Utils.randInt(min, max));
            default -> ItemStack.EMPTY;
        };
    }
}

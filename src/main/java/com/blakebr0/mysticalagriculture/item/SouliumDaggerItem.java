package com.blakebr0.mysticalagriculture.item;

import java.util.List;
import java.util.function.BiFunction;

import com.blakebr0.cucumber.item.tool.BaseSwordItem;
import com.blakebr0.mysticalagriculture.api.soul.ISoulSiphoningItem;
import com.blakebr0.mysticalagriculture.lib.ModItemTier;
import com.blakebr0.mysticalagriculture.lib.ModTooltips;
import io.github.xponer2.translator.item.Tier;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import io.github.xponer2.translator.item.TooltipContext;

public class SouliumDaggerItem extends BaseSwordItem implements ISoulSiphoningItem, io.github.xponer2.translator.item.TooltipContext.HoverText {
    private final DaggerType type;

    public SouliumDaggerItem(Tier tier, DaggerType type) {
        super(tier, type.getDamage(), -2.4F, p -> p.durability(type.getDurability()));
        this.type = type;
    }

    @Override
    public String getTranslationKey(ItemStack stack) {
        return "item.mysticalagriculture.soulium_dagger";
    }

    public void appendHoverText(ItemStack stack, TooltipContext context, List<ITextComponent> tooltip, ITooltipFlag flag) {
        switch (this.type) {
            case PASSIVE -> {
                tooltip.add(ModTooltips.PASSIVE_ATTUNED.color(TextFormatting.GREEN).build());
                tooltip.add(ModTooltips.PASSIVE_SOULIUM_DAGGER.build());
            }
            case HOSTILE -> {
                tooltip.add(ModTooltips.HOSTILE_ATTUNED.color(TextFormatting.RED).build());
                tooltip.add(ModTooltips.HOSTILE_SOULIUM_DAGGER.build());
            }
            case CREATIVE -> {
                tooltip.add(ModTooltips.CREATIVE_ATTUNED.color(TextFormatting.LIGHT_PURPLE).build());
                tooltip.add(ModTooltips.CREATIVE_SOULIUM_DAGGER.build());
            }
        }
    }

    @Override
    public double getSiphonAmount(ItemStack stack, EntityLivingBase entity) {
        return this.type.getSiphonAmount(stack, entity);
    }

    public enum DaggerType {
        BASIC(3, ModItemTier.SOULIUM.getUses(), (stack, entity) -> 1.0D),
        PASSIVE(6, ModItemTier.SOULIUM.getUses() * 2, (stack, entity) -> isPassive(entity) ? 1.5D : 1.0D),
        HOSTILE(6, ModItemTier.SOULIUM.getUses() * 2, (stack, entity) -> !isPassive(entity) ? 1.5D : 1.0D),
        CREATIVE(65, -1, (stack, entity) -> Double.MAX_VALUE);

        private final int damage;
        private final int durability;
        private final BiFunction<ItemStack, EntityLivingBase, Double> siphonAmountFunc;

        DaggerType(int damage, int durability, BiFunction<ItemStack, EntityLivingBase, Double> siphonAmountFunc) {
            this.damage = damage;
            this.durability = durability;
            this.siphonAmountFunc = siphonAmountFunc;
        }

        public double getSiphonAmount(ItemStack stack, EntityLivingBase entity) {
            return this.siphonAmountFunc.apply(stack, entity);
        }

        public int getDamage() {
            return this.damage;
        }

        public int getDurability() {
            return this.durability;
        }

        private static boolean isPassive(EntityLivingBase entity) {
            return !(entity instanceof net.minecraft.entity.monster.IMob);
        }
    }

    @Override
    @net.minecraftforge.fml.relauncher.SideOnly(net.minecraftforge.fml.relauncher.Side.CLIENT)
    public void addInformation(ItemStack stack, @javax.annotation.Nullable net.minecraft.world.World world, java.util.List<String> tooltip, ITooltipFlag flag) {
        io.github.xponer2.translator.item.TooltipContext.addInformation(this, stack, world, tooltip, flag);
    }
}

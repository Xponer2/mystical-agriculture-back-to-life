package com.blakebr0.mysticalagriculture.item.armor;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import com.blakebr0.cucumber.item.BaseArmorItem;
import com.blakebr0.mysticalagriculture.api.tinkering.AugmentType;
import com.blakebr0.mysticalagriculture.api.tinkering.ITinkerable;
import com.blakebr0.mysticalagriculture.api.util.AugmentUtils;
import com.blakebr0.mysticalagriculture.config.ModConfigs;
import com.blakebr0.mysticalagriculture.init.ModDataComponentTypes;
import com.blakebr0.mysticalagriculture.init.ModItems;
import com.blakebr0.mysticalagriculture.lib.ModTooltips;
import io.github.xponer2.translator.component.DataComponents;
import io.github.xponer2.translator.component.Unbreakable;
import io.github.xponer2.translator.item.ArmorMaterialData;
import io.github.xponer2.translator.registry.Holder;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import io.github.xponer2.translator.util.ItemStacks;
import io.github.xponer2.translator.item.ArmorType;
import io.github.xponer2.translator.item.TooltipContext;

public class EssenceHelmetItem extends BaseArmorItem implements ITinkerable, io.github.xponer2.translator.item.TooltipContext.HoverText {
    private static final EnumSet<AugmentType> TYPES = EnumSet.of(AugmentType.ARMOR, AugmentType.HELMET);
    private final int tinkerableTier;
    private final int slots;

    public EssenceHelmetItem(Holder<ArmorMaterialData> material, int maxDamageFactor, int tinkerableTier, int slots) {
        super(material, ArmorType.HELMET, maxDamageFactor, p -> {
            p.component(ModDataComponentTypes.EQUIPPED_AUGMENTS, new ArrayList<>(slots));

            // Supremium+ armor can be unbreakable if enabled
            if (tinkerableTier >= 5 && ModConfigs.UNBREAKABLE_SUPREMIUM_ARMOR.get()) {
                p.component(DataComponents.UNBREAKABLE, new Unbreakable(true));
            }

            return p;
        });
        this.tinkerableTier = tinkerableTier;
        this.slots = slots;
    }

    @Override
    public void onUpdate(ItemStack stack, World level, Entity entity, int slot, boolean isSelected) {
        if (slot == 39 && entity instanceof EntityPlayer player) {
            for (var augment : AugmentUtils.getAugments(stack)) {
                augment.onArmorTick(stack, level, player);
            }
        }

        for (var augment : AugmentUtils.getAugments(stack)) {
            augment.onInventoryTick(stack, level, entity, slot, isSelected);
        }
    }

    public void appendHoverText(ItemStack stack, TooltipContext context, List<ITextComponent> tooltip, ITooltipFlag flag) {
        tooltip.add(ModTooltips.getTooltipForTier(this.tinkerableTier));

        if (ModConfigs.AWAKENED_SUPREMIUM_SET_BONUS.get() && ItemStacks.is(stack, ModItems.AWAKENED_SUPREMIUM_HELMET.get())) {
            tooltip.add(ModTooltips.SET_BONUS.args(ModTooltips.AWAKENED_SUPREMIUM_SET_BONUS.build()).build());
        }

        ModTooltips.addAugmentListToTooltip(tooltip, stack, this.slots);
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return ModConfigs.ENCHANTABLE_SUPREMIUM_ARMOR.get();
    }

    @Override
    public int getAugmentSlots() {
        return this.slots;
    }

    @Override
    public EnumSet<AugmentType> getAugmentTypes() {
        return TYPES;
    }

    @Override
    public int getTinkerableTier() {
        return this.tinkerableTier;
    }

    @Override
    @net.minecraftforge.fml.relauncher.SideOnly(net.minecraftforge.fml.relauncher.Side.CLIENT)
    public void addInformation(ItemStack stack, @javax.annotation.Nullable net.minecraft.world.World world, java.util.List<String> tooltip, ITooltipFlag flag) {
        io.github.xponer2.translator.item.TooltipContext.addInformation(this, stack, world, tooltip, flag);
    }
}

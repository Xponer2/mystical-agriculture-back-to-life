package com.blakebr0.mysticalagriculture.item.armor;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import com.blakebr0.cucumber.item.BaseArmorItem;
import com.blakebr0.cucumber.util.Utils;
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
import io.github.xponer2.translator.util.particle.ParticleTypes;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import io.github.xponer2.translator.tags.BlockTags;
import io.github.xponer2.translator.tags.TagManager;
import io.github.xponer2.translator.util.ItemStacks;
import io.github.xponer2.translator.util.particle.Particles;
import io.github.xponer2.translator.item.ArmorType;
import io.github.xponer2.translator.item.TooltipContext;

public class EssenceChestplateItem extends BaseArmorItem implements ITinkerable, io.github.xponer2.translator.item.TooltipContext.HoverText {
    private static final EnumSet<AugmentType> TYPES = EnumSet.of(AugmentType.ARMOR, AugmentType.CHESTPLATE);
    private final int tinkerableTier;
    private final int slots;

    public EssenceChestplateItem(Holder<ArmorMaterialData> material, int maxDamageFactor, int tinkerableTier, int slots) {
        super(material, ArmorType.CHESTPLATE, maxDamageFactor, p -> {
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
        if (slot == 38 && entity instanceof EntityPlayer player) {
            for (var augment : AugmentUtils.getAugments(stack)) {
                augment.onArmorTick(stack, level, player);
            }

            if (ModConfigs.AWAKENED_SUPREMIUM_SET_BONUS.get() && !level.isRemote && level.getTotalWorldTime() % 20L == 0 && hasAwakenedSupremiumSet(player)) {
                handleGrowthTicks(level, player);
            }
        }

        for (var augment : AugmentUtils.getAugments(stack)) {
            augment.onInventoryTick(stack, level, entity, slot, isSelected);
        }
    }

    public void appendHoverText(ItemStack stack, TooltipContext context, List<ITextComponent> tooltip, ITooltipFlag flag) {
        tooltip.add(ModTooltips.getTooltipForTier(this.tinkerableTier));

        if (ModConfigs.AWAKENED_SUPREMIUM_SET_BONUS.get() && ItemStacks.is(stack, ModItems.AWAKENED_SUPREMIUM_CHESTPLATE.get())) {
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

    private static boolean hasAwakenedSupremiumSet(EntityPlayer player) {
        var helmet = ItemStacks.is(player.getItemStackFromSlot(EntityEquipmentSlot.HEAD), ModItems.AWAKENED_SUPREMIUM_HELMET.get());
        var chestplate = ItemStacks.is(player.getItemStackFromSlot(EntityEquipmentSlot.CHEST), ModItems.AWAKENED_SUPREMIUM_CHESTPLATE.get());
        var leggings = ItemStacks.is(player.getItemStackFromSlot(EntityEquipmentSlot.LEGS), ModItems.AWAKENED_SUPREMIUM_LEGGINGS.get());
        var boots = ItemStacks.is(player.getItemStackFromSlot(EntityEquipmentSlot.FEET), ModItems.AWAKENED_SUPREMIUM_BOOTS.get());

        return helmet && chestplate && leggings && boots;
    }

    private static void handleGrowthTicks(World level, EntityPlayer player) {
        var pos = new BlockPos(player.posX, player.posY - 0.2D, player.posZ);
        int range = 5;

        java.util.stream.StreamSupport.stream(BlockPos.getAllInBox(pos.add(-range, -range, -range), pos.add(range, range, range)).spliterator(), false).forEach(aoePos -> {
            if (Math.random() < 0.5)
                return;

            var state = level.getBlockState(aoePos);

            if (TagManager.is(state, BlockTags.CROPS)) {
                state.getBlock().randomTick((WorldServer) level, aoePos, state, Utils.RANDOM);

                double d0 = aoePos.getX() + level.rand.nextFloat();
                double d1 = aoePos.getY();
                double d2 = aoePos.getZ() + level.rand.nextFloat();

                Particles.sendParticles(level, ParticleTypes.HAPPY_VILLAGER, d0, d1, d2, 1, 0, 0, 0, 0.1D);
            }
        });
    }

    @Override
    @net.minecraftforge.fml.relauncher.SideOnly(net.minecraftforge.fml.relauncher.Side.CLIENT)
    public void addInformation(ItemStack stack, @javax.annotation.Nullable net.minecraft.world.World world, java.util.List<String> tooltip, ITooltipFlag flag) {
        io.github.xponer2.translator.item.TooltipContext.addInformation(this, stack, world, tooltip, flag);
    }
}

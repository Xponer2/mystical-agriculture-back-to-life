package com.blakebr0.mysticalagriculture.api.tinkering;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

import com.blakebr0.mysticalagriculture.api.MysticalAgricultureAPI;
import com.blakebr0.mysticalagriculture.api.lib.AbilityCache;
import io.github.xponer2.translator.entity.AttributeModifier;
import io.github.xponer2.translator.registry.DeferredHolder;
import io.github.xponer2.translator.text.MutableComponent;
import io.github.xponer2.translator.text.TranslatableComponent;
import io.github.xponer2.translator.util.FastColor;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingFallEvent;

import io.github.xponer2.translator.util.Registries;
import io.github.xponer2.translator.util.UseOnContext;

/**
 * The default implementation of an Augment
 * <p>
 * Extend this class for your augments
 */
public class Augment {
    private final ResourceLocation id;
    private final DeferredHolder<Item, Item> item;
    private int tier;
    private EnumSet<AugmentType> types;
    private int primaryColor;
    private int secondaryColor;
    private boolean enabled;

    public Augment(ResourceLocation id, int tier, EnumSet<AugmentType> types, int primaryColor, int secondaryColor) {
        this.id = id;
        this.item = DeferredHolder.create(Registries.ITEM, new ResourceLocation(MysticalAgricultureAPI.MOD_ID, id.getPath() + "_augment"));
        this.tier = tier;
        this.types = types;
        this.primaryColor = FastColor.ARGB32.color(255, primaryColor);
        this.secondaryColor = FastColor.ARGB32.color(255, secondaryColor);
        this.enabled = true;
    }

    /**
     * The id of this augment, the modid is taken from the namespace for {@link Augment#getModId()},
     * and the path is used for {@link Augment#getName()}
     * @return the id of this augment
     */
    public ResourceLocation getId() {
        return this.id;
    }

    /**
     * The internal name of this augment.
     * This is used for registration, so it MUST be all lowercase with underscores for spaces
     * @return the internal name of this augment
     */
    public String getName() {
        return this.getId().getPath();
    }

    /**
     * The modid of the mod that registered this augment
     * @return the modid of this augment
     */
    public String getModId() {
        return this.getId().getNamespace();
    }

    /**
     * Used to get the internal name of this augment with an _suffix
     * @param suffix the suffix to append (without the initial underscore)
     * @return the name with _suffix
     */
    public String getNameWithSuffix(String suffix) {
        return String.format("%s_%s", this.getName(), suffix);
    }

    /**
     * Get the localized name of this augment using the key augment.{@link Augment#getModId()}.{@link Augment#getName()}
     * @return the localized name of this augment
     */
    public MutableComponent getDisplayName() {
        return new TranslatableComponent(String.format("augment.%s.%s", this.getModId(), this.getName()));
    }

    /**
     * Get the localized description of this augment using the key augment.{@link Augment#getModId()}.{@link Augment#getName()}.description
     * @return the localized description of this augment
     */
    public MutableComponent getDescriptionDisplayText() {
        return new TranslatableComponent(String.format("augment.%s.%s.description", this.getModId(), this.getName())).withStyle(TextFormatting.GRAY);
    }

    /**
     * The augment types that this augment represents
     * @return applicable augment types
     */
    public EnumSet<AugmentType> getAugmentTypes() {
        return this.types;
    }

    /**
     * The tier of this augment, used to define the minimum tier tinkerable required
     * @return the numerical tier
     */
    public int getTier() {
        return this.tier;
    }

    /**
     * Get the augment item for this augment
     * @return the augment item
     */
    public Item getItem() {
        return this.item.get();
    }

    /**
     * The primary color of this augment (for the lighter middle areas of the augment)
     * @return the primary color
     */
    public int getPrimaryColor() {
        return this.primaryColor;
    }

    /**
     * Set the primary color of this augment
     * @param color the primary color
     * @return this augment
     */
    public Augment setPrimaryColor(int color) {
        this.primaryColor = color;
        return this;
    }

    /**
     * The secondary color of this augment (for the darker areas of the augment)
     * @return the secondary color
     */
    public int getSecondaryColor() {
        return this.secondaryColor;
    }

    /**
     * Set the secondary color of this augment
     * @param color the secondary color
     * @return this augment
     */
    public Augment setSecondaryColor(int color) {
        this.secondaryColor = color;
        return this;
    }

    /**
     * Should this augment have the enchantment glint?
     * @return has glint effect
     */
    public boolean hasEffect() {
        return this.getTier() >= 5;
    }

    /**
     * Whether this augment has recipes and shows up in the creative menu
     * @return is this augment enabled
     */
    public boolean isEnabled() {
        return this.enabled;
    }

    /**
     * Set whether this augment should be hidden from the game
     * @param enabled the enabled state
     */
    public Augment setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    /**
     * Called when the item is used while targeting a block, {@link Item#useOn(UseOnContext)}
     * @param context the item use context
     * @return was the action successful
     */
    public boolean onItemUse(UseOnContext context) {
        return false;
    }

    /**
     * Called when the item is right-clicked while not targeting a block, {@link Item#use(World, EntityPlayer, EnumHand)}
     * @param stack the item
     * @param level the level
     * @param player the player
     * @param hand the hand
     * @return was the action successful
     */
    public boolean onRightClick(ItemStack stack, World level, EntityPlayer player, EnumHand hand) {
        return false;
    }

    /**
     * Called when this item is right-clicked on an entity, {@link Item#interactLivingEntity(ItemStack, EntityPlayer, EntityLivingBase, EnumHand)}
     * @param stack the item
     * @param player the player
     * @param target the clicked entity
     * @param hand the hand
     * @return was the action successful
     */
    public boolean onRightClickEntity(ItemStack stack, EntityPlayer player, EntityLivingBase target, EnumHand hand) {
        return false;
    }

    /**
     * Called when this item is used to attack an entity, {@link Item#hurtEnemy(ItemStack, EntityLivingBase, EntityLivingBase)}
     * @param stack the item
     * @param target the attacked entity
     * @param attacker the attacking entity
     * @return was the action successful
     */
    public boolean onHitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        return false;
    }

    /**
     * Called when a block is destroyed using this item, {@link Item#mineBlock(ItemStack, World, IBlockState, BlockPos, EntityLivingBase)}
     * @param stack the item
     * @param level the level
     * @param state the block destroyed
     * @param pos the pos of the block destroyed
     * @param entity the entity that destroyed the block
     * @return was the action successful
     */
    public boolean onBlockDestroyed(ItemStack stack, World level, IBlockState state, BlockPos pos, EntityLivingBase entity) {
        return false;
    }

    /**
     * Called when the item is ticked in the player's inventory, {@link Item#inventoryTick(ItemStack, World, Entity, int, boolean)}
     * @param stack the item
     * @param level the level
     * @param entity the player
     * @param slot the slot
     * @param isSelected is currently being held
     */
    public void onInventoryTick(ItemStack stack, World level, Entity entity, int slot, boolean isSelected) { }

    /**
     * Called every tick for equipped armor, {@link Item#inventoryTick(ItemStack, World, Entity, int, boolean)}
     * @param stack the item
     * @param level the level
     * @param player the player
     */
    public void onArmorTick(ItemStack stack, World level, EntityPlayer player) { }

    /**
     * Called every tick for equipped armor, meant for player ability changes, {@link PlayerTickEvent.Pre}
     * @param level the level
     * @param player the player
     * @param cache the ability cache
     */
    public void onPlayerTick(World level, EntityPlayer player, AbilityCache cache) { }

    /**
     * Called when the player hits the ground, {@link LivingFallEvent}
     * @param level the level
     * @param player the player
     * @param event the fall event
     */
    public void onPlayerFall(World level, EntityPlayer player, LivingFallEvent event) { }

    /**
     * The list of {@link AttributeModifier}s that will be automatically applied when equipping this augment
     * @return the attribute modifiers that this augment will apply to the item
     */
    public List<AugmentAttributeModifier> getAttributeModifiers() {
        return Collections.emptyList();
    }

    /**
     * Does this augment have a set bonus
     * @return has set bonus
     */
    public boolean hasSetBonus() {
        return false;
    }

    /**
     * Get the localized text of the set bonus using the key augment.{@link Augment#getModId()}.{@link Augment#getName()}.set_bonus
     * @return the localized text of the set bonus
     */
    public MutableComponent getSetBonusDisplayText() {
        return new TranslatableComponent(String.format("augment.%s.%s.set_bonus", this.getModId(), this.getName())).withStyle(TextFormatting.GRAY);
    }
}

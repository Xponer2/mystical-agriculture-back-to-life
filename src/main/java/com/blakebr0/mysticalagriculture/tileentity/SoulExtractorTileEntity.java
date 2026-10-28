package com.blakebr0.mysticalagriculture.tileentity;

import java.util.List;

import com.blakebr0.cucumber.energy.DynamicEnergyStorage;
import com.blakebr0.cucumber.helper.StackHelper;
import com.blakebr0.cucumber.inventory.BaseItemStackHandler;
import com.blakebr0.cucumber.inventory.CachedRecipe;
import com.blakebr0.cucumber.inventory.OnContentsChangedFunction;
import com.blakebr0.cucumber.inventory.SidedInventoryWrapper;
import com.blakebr0.cucumber.tileentity.BaseInventoryTileEntity;
import com.blakebr0.cucumber.util.Localizable;
import com.blakebr0.mysticalagriculture.api.crafting.ISoulExtractionRecipe;
import com.blakebr0.mysticalagriculture.api.machine.IUpgradeableMachine;
import com.blakebr0.mysticalagriculture.api.machine.MachineUpgradeItemStackHandler;
import com.blakebr0.mysticalagriculture.api.machine.MachineUpgradeTier;
import com.blakebr0.mysticalagriculture.api.util.MobSoulUtils;
import com.blakebr0.mysticalagriculture.block.SoulExtractorBlock;
import com.blakebr0.mysticalagriculture.container.SoulExtractorContainer;
import com.blakebr0.mysticalagriculture.init.ModRecipeTypes;
import com.blakebr0.mysticalagriculture.init.ModTileEntities;
import com.blakebr0.mysticalagriculture.item.SoulJarItem;
import com.blakebr0.mysticalagriculture.util.RecipeIngredientCache;
import com.google.common.collect.ImmutableList;
import io.github.xponer2.translator.menu.AbstractContainerMenu;
import io.github.xponer2.translator.menu.MenuProvider;
import io.github.xponer2.translator.recipe.CraftingInput;
import io.github.xponer2.translator.registry.HolderLookup;
import io.github.xponer2.translator.registry.RegistryAccess;
import org.jetbrains.annotations.Nullable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;

public class SoulExtractorTileEntity extends BaseInventoryTileEntity implements MenuProvider, IUpgradeableMachine {
    public SoulExtractorTileEntity() {
        this(BlockPos.ORIGIN, null);
    }

    private static final int FUEL_TICK_MULTIPLIER = 20;
    public static final int OPERATION_TIME = 100;
    public static final int FUEL_USAGE = 40;
    public static final int FUEL_CAPACITY = 80000;

    private final BaseItemStackHandler inventory;
    private final MachineUpgradeItemStackHandler upgradeInventory;
    private final DynamicEnergyStorage energy;
    private final SidedInventoryWrapper[] sidedInventoryWrappers;
    private final CachedRecipe<CraftingInput, ISoulExtractionRecipe> recipe;
    private MachineUpgradeTier tier;
    private int progress;
    private int fuelLeft;
    private int fuelItemValue;
    private boolean isRunning;

    public SoulExtractorTileEntity(BlockPos pos, IBlockState state) {
        super(ModTileEntities.SOUL_EXTRACTOR.get(), pos, state);
        this.inventory = createInventoryHandler((slot) -> this.markDirty());
        this.upgradeInventory = new MachineUpgradeItemStackHandler();
        this.energy = new DynamicEnergyStorage(FUEL_CAPACITY, this::setChangedFast);
        this.sidedInventoryWrappers = SidedInventoryWrapper.create(this.inventory, ImmutableList.of(EnumFacing.UP, EnumFacing.DOWN, EnumFacing.NORTH), this::canInsertStackSided, null);
        this.recipe = new CachedRecipe<>(ModRecipeTypes.SOUL_EXTRACTION.get());
    }

    @Override
    public BaseItemStackHandler getInventory() {
        return this.inventory;
    }

    @Override
    public void loadAdditional(NBTTagCompound tag, HolderLookup.Provider lookup) {
        super.loadAdditional(tag, lookup);

        this.progress = tag.getInteger("Progress");
        this.fuelLeft = tag.getInteger("FuelLeft");
        this.fuelItemValue = tag.getInteger("FuelItemValue");
        this.energy.deserializeNBT(tag.getTag("Energy"));
        this.upgradeInventory.deserializeNBT(tag.getCompoundTag("UpgradeInventory"));
    }

    @Override
    public void saveAdditional(NBTTagCompound tag, HolderLookup.Provider lookup) {
        super.saveAdditional(tag, lookup);

        tag.setInteger("Progress", this.progress);
        tag.setInteger("FuelLeft", this.fuelLeft);
        tag.setInteger("FuelItemValue", this.fuelItemValue);
        tag.setInteger("Energy", this.energy.getEnergyStored());
        tag.setTag("UpgradeInventory", this.upgradeInventory.serializeNBT());
    }

    @Override
    public ITextComponent getDisplayName() {
        return Localizable.of("container.mysticalagriculture.soul_extractor").build();
    }

    @Override
    public AbstractContainerMenu createMenu(int id, InventoryPlayer playerInventory, EntityPlayer player) {
        return SoulExtractorContainer.create(id, playerInventory, this.inventory, this.upgradeInventory, this.getPos());
    }

    @Override
    public MachineUpgradeItemStackHandler getUpgradeInventory() {
        return this.upgradeInventory;
    }

    @Override
    protected void clearAdditional() {
        this.upgradeInventory.getStacks().clear();
    }

    public IItemHandler getSidedInventory(@Nullable EnumFacing direction) {
        if (direction == null) direction = EnumFacing.NORTH;

        return switch (direction) {
            case UP -> this.sidedInventoryWrappers[0];
            case DOWN -> this.sidedInventoryWrappers[1];
            default -> this.sidedInventoryWrappers[2];
        };
    }

    public static void tick(World world, BlockPos pos, IBlockState state, SoulExtractorTileEntity tile) {
        if (tile.energy.getEnergyStored() < tile.energy.getMaxEnergyStored()) {
            var fuel = tile.inventory.getStackInSlot(1);

            if (tile.fuelLeft <= 0 && !fuel.isEmpty()) {
                tile.fuelItemValue = TileEntityFurnace.getItemBurnTime(fuel);

                if (tile.fuelItemValue > 0) {
                    tile.fuelLeft = tile.fuelItemValue *= FUEL_TICK_MULTIPLIER;
                    tile.inventory.setStackInSlot(1, StackHelper.shrink(fuel, 1, true));

                    tile.setChangedFast();
                }
            }

            if (tile.fuelLeft > 0) {
                var fuelPerTick = Math.min(Math.min(tile.fuelLeft, tile.getFuelUsage() * 2), tile.energy.getMaxEnergyStored() - tile.energy.getEnergyStored());

                tile.fuelLeft -= tile.energy.receiveEnergy(fuelPerTick, false);

                if (tile.fuelLeft <= 0)
                    tile.fuelItemValue = 0;

                tile.setChangedFast();
            }
        }

        var tier = tile.getMachineTier();

        if (tier != tile.tier) {
            tile.tier = tier;

            if (tier == null) {
                tile.energy.resetMaxEnergyStorage();
            } else {
                tile.energy.setMaxEnergyStorage((int) (FUEL_CAPACITY * tier.getFuelCapacityMultiplier()));
            }

            tile.setChangedFast();
        }

        var wasRunning = tile.isRunning;
        var recipe = tile.getActiveRecipe();

        tile.isRunning = false;

        if (recipe != null) {
            if (tile.energy.getEnergyStored() >= tile.getFuelUsage()) {
                tile.isRunning = true;
                tile.progress++;
                tile.energy.extractEnergy(tile.getFuelUsage(), false);

                if (tile.progress >= tile.getOperationTime()) {
                    tile.inventory.setStackInSlot(0, StackHelper.shrink(tile.inventory.getStackInSlot(0), 1, false));
                    tile.inventory.setStackInSlot(2, recipe.assemble(tile.toCraftingInput(), RegistryAccess.INSTANCE));

                    tile.progress = 0;
                }

                tile.setChangedFast();
            }
        } else {
            if (tile.progress > 0) {
                tile.progress = 0;

                tile.setChangedFast();
            }
        }

        if (wasRunning != tile.isRunning) {
            world.setBlockState(pos, state.withProperty(SoulExtractorBlock.RUNNING, tile.isRunning), 3);

            tile.setChangedFast();
        }

        tile.dispatchIfChanged();
    }

    public static BaseItemStackHandler createInventoryHandler() {
        return createInventoryHandler(null);
    }

    public static BaseItemStackHandler createInventoryHandler(OnContentsChangedFunction onContentsChanged) {
        return BaseItemStackHandler.create(3, onContentsChanged, builder -> {
            builder.setCanInsert((slot, stack) -> switch (slot) {
                case 1 -> TileEntityFurnace.isItemFuel(stack);
                case 2 -> stack.getItem() instanceof SoulJarItem;
                default -> true;
            });
            builder.setCanExtract(slot -> switch (slot) {
                case 1 -> !TileEntityFurnace.isItemFuel(builder.getStackInSlot(1));
                case 2 -> {
                    var stack = builder.getStackInSlot(2);
                    yield stack.getItem() instanceof SoulJarItem && MobSoulUtils.isJarFull(stack);
                }
                default -> false;
            });
        });
    }

    public ISoulExtractionRecipe getActiveRecipe() {
        if (this.world == null)
            return null;

        return this.recipe.checkAndGet(this.toCraftingInput(), this.world);
    }

    public DynamicEnergyStorage getEnergy() {
        return this.energy;
    }

    public int getProgress() {
        return this.progress;
    }

    public int getOperationTime() {
        if (this.tier == null)
            return OPERATION_TIME;

        return (int) (OPERATION_TIME * this.tier.getOperationTimeMultiplier());
    }

    public int getFuelLeft() {
        return this.fuelLeft;
    }

    public int getFuelItemValue() {
        return this.fuelItemValue;
    }

    public int getFuelUsage() {
        if (this.tier == null)
            return FUEL_USAGE;

        return (int) (FUEL_USAGE * this.tier.getFuelUsageMultiplier());
    }

    private CraftingInput toCraftingInput() {
        return this.inventory.toShapelessCraftingInput();
    }

    private boolean canInsertStackSided(int slot, ItemStack stack, EnumFacing direction) {
        if (direction == null)
            return true;
        if (slot == 0 && direction == EnumFacing.UP)
            return RecipeIngredientCache.INSTANCE.isValidInput(stack, ModRecipeTypes.SOUL_EXTRACTION.get());
        if (slot == 1 && direction == EnumFacing.NORTH)
            return TileEntityFurnace.isItemFuel(stack);
        if (slot == 2 && direction == EnumFacing.NORTH)
            return stack.getItem() instanceof SoulJarItem;

        return false;
    }
}

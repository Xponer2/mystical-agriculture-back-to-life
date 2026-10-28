package com.blakebr0.mysticalagriculture.tileentity;

import com.blakebr0.cucumber.energy.DynamicEnergyStorage;
import com.blakebr0.cucumber.helper.CropHelper;
import com.blakebr0.cucumber.helper.StackHelper;
import com.blakebr0.cucumber.inventory.BaseItemStackHandler;
import com.blakebr0.cucumber.inventory.OnContentsChangedFunction;
import com.blakebr0.cucumber.tileentity.BaseInventoryTileEntity;
import com.blakebr0.cucumber.util.Localizable;
import com.blakebr0.mysticalagriculture.api.machine.IUpgradeableMachine;
import com.blakebr0.mysticalagriculture.api.machine.MachineUpgradeItemStackHandler;
import com.blakebr0.mysticalagriculture.api.machine.MachineUpgradeTier;
import com.blakebr0.mysticalagriculture.block.HarvesterBlock;
import com.blakebr0.mysticalagriculture.container.HarvesterContainer;
import com.blakebr0.mysticalagriculture.init.ModTileEntities;
import io.github.xponer2.translator.menu.AbstractContainerMenu;
import io.github.xponer2.translator.menu.MenuProvider;
import io.github.xponer2.translator.registry.HolderLookup;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
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
import net.minecraft.world.WorldServer;
import io.github.xponer2.translator.util.Platform;

public class HarvesterTileEntity extends BaseInventoryTileEntity implements MenuProvider, IUpgradeableMachine {
    public HarvesterTileEntity() {
        this(BlockPos.ORIGIN, null);
    }

    private static final int FUEL_TICK_MULTIPLIER = 20;
    public static final int OPERATION_TIME = 100;
    public static final int FUEL_USAGE = 40;
    public static final int SCAN_FUEL_USAGE = 10;
    public static final int FUEL_CAPACITY = 80000;
    public static final int BASE_RANGE = 1;

    private final BaseItemStackHandler inventory;
    private final MachineUpgradeItemStackHandler upgradeInventory;
    private final DynamicEnergyStorage energy;
    private MachineUpgradeTier tier;
    private EnumFacing direction;
    private int progress;
    private int fuelLeft;
    private int fuelItemValue;
    private int lastScanIndex = -1;
    private boolean isRunning;

    public HarvesterTileEntity(BlockPos pos, IBlockState state) {
        super(ModTileEntities.HARVESTER.get(), pos, state);
        this.inventory = createInventoryHandler((slot) -> this.markDirty());
        this.upgradeInventory = new MachineUpgradeItemStackHandler();
        this.energy = new DynamicEnergyStorage(FUEL_CAPACITY, this::setChangedFast);
    }

    @Override
    public BaseItemStackHandler getInventory() {
        return this.inventory;
    }

    @Override
    public ITextComponent getDisplayName() {
        return Localizable.of("container.mysticalagriculture.harvester").build();
    }

    @Override
    public AbstractContainerMenu createMenu(int i, InventoryPlayer inventory, EntityPlayer player) {
        return HarvesterContainer.create(i, inventory, this.inventory, this.upgradeInventory, this.getPos());
    }

    @Override
    public MachineUpgradeItemStackHandler getUpgradeInventory() {
        return this.upgradeInventory;
    }

    @Override
    public void loadAdditional(NBTTagCompound tag, HolderLookup.Provider lookup) {
        super.loadAdditional(tag, lookup);

        this.progress = tag.getInteger("Progress");
        this.fuelLeft = tag.getInteger("FuelLeft");
        this.fuelItemValue = tag.getInteger("FuelItemValue");
        this.lastScanIndex = tag.getInteger("LastScanIndex");
        this.energy.deserializeNBT(tag.getTag("Energy"));
        this.upgradeInventory.deserializeNBT(tag.getCompoundTag("UpgradeInventory"));
    }

    @Override
    public void saveAdditional(NBTTagCompound tag, HolderLookup.Provider lookup) {
        super.saveAdditional(tag, lookup);

        tag.setInteger("Progress", this.progress);
        tag.setInteger("FuelLeft", this.fuelLeft);
        tag.setInteger("FuelItemValue", this.fuelItemValue);
        tag.setInteger("LastScanIndex", this.lastScanIndex);
        tag.setInteger("Energy", this.energy.getEnergyStored());
        tag.setTag("UpgradeInventory", this.upgradeInventory.serializeNBT());
    }

    @Override
    protected void clearAdditional() {
        this.upgradeInventory.getStacks().clear();
    }

    public static void tick(World world, BlockPos pos, IBlockState state, HarvesterTileEntity tile) {
        if (tile.energy.getEnergyStored() < tile.energy.getMaxEnergyStored()) {
            var fuel = tile.inventory.getStackInSlot(0);

            if (tile.fuelLeft <= 0 && !fuel.isEmpty()) {
                tile.fuelItemValue = TileEntityFurnace.getItemBurnTime(fuel);

                if (tile.fuelItemValue > 0) {
                    tile.fuelLeft = tile.fuelItemValue *= FUEL_TICK_MULTIPLIER;
                    tile.inventory.setStackInSlot(0, StackHelper.shrink(fuel, 1, true));

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
        var direction = state.getValue(HarvesterBlock.FACING);

        if (tier != tile.tier || direction != tile.direction) {
            tile.tier = tier;
            tile.direction = direction;

            if (tier == null) {
                tile.energy.resetMaxEnergyStorage();
            } else {
                tile.energy.setMaxEnergyStorage((int) (FUEL_CAPACITY * tier.getFuelCapacityMultiplier()));
            }

            tile.setChangedFast();
        }

        var isDisabled = world.isBlockPowered(tile.getPos());
        var operationTime = tile.getOperationTime();

        if (tile.progress > operationTime && !isDisabled) {
            var nextPos = tile.findNextPosition();
            var cropState = world.getBlockState(nextPos);
            var block = cropState.getBlock();

            if (block instanceof BlockCrops crop) {
                var seed = CropHelper.getSeedsItem(block);
                if (seed != null && crop.isMaxAge(cropState)) {
                    var drops = Platform.getDrops(cropState, world, nextPos);

                    for (var drop : drops) {
                        var item = drop.getItem();

                        if (!drop.isEmpty() && item == seed) {
                            drop.shrink(1);
                            break;
                        }
                    }

                    for (var drop : drops) {
                        if (!drop.isEmpty()) {
                            tile.addItemToInventory(drop, world, nextPos);
                        }
                    }

                    world.setBlockState(nextPos, crop.withAge(0));

                    tile.energy.extractEnergy(tile.getFuelUsage(), false);
                } else {
                    tile.energy.extractEnergy(SCAN_FUEL_USAGE, false);
                }
            }

            tile.progress = 0;

            tile.setChangedFast();
        }

        var wasRunning = tile.isRunning;

        if (!isDisabled && tile.energy.getEnergyStored() >= tile.getFuelUsage()) {
            tile.progress++;
            tile.isRunning = true;
            tile.setChangedFast();
        } else {
            tile.isRunning = false;
        }

        if (wasRunning != tile.isRunning) {
            world.setBlockState(pos, state.withProperty(HarvesterBlock.RUNNING, tile.isRunning), 3);

            tile.setChangedFast();
        }

        tile.dispatchIfChanged();
    }

    public static BaseItemStackHandler createInventoryHandler() {
        return createInventoryHandler(null);
    }

    public static BaseItemStackHandler createInventoryHandler(OnContentsChangedFunction onContentsChanged) {
        return BaseItemStackHandler.create(16, onContentsChanged, builder -> {
            builder.setCanInsert((slot, stack) -> switch (slot) {
                case 0 -> TileEntityFurnace.isItemFuel(stack);
                default -> false;
            });
            builder.setCanExtract(slot -> switch (slot) {
                case 0 -> !TileEntityFurnace.isItemFuel(builder.getStackInSlot(slot));
                default -> true;
            });
        });
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

    private BlockPos findNextPosition() {
        var range = tier != null ? BASE_RANGE + tier.getAddedRange() : BASE_RANGE;
        var size = range * 2 + 1;

        var index = this.lastScanIndex + 1;
        if (index >= (int) Math.pow(size, 2)) {
            index = 0;
        }

        this.lastScanIndex = index;

        var xOffset = (index % size) - range;
        var zOffset = (index / size) - range;

        var center = this.getPos().offset(direction, range + 1);

        return switch (direction) {
            case NORTH -> new BlockPos(center.getX() + xOffset, center.getY(), center.getZ() - zOffset);
            case SOUTH -> new BlockPos(center.getX() - xOffset, center.getY(), center.getZ() + zOffset);
            case EAST -> new BlockPos(center.getX() + zOffset, center.getY(), center.getZ() + xOffset);
            case WEST -> new BlockPos(center.getX() - zOffset, center.getY(), center.getZ() - xOffset);
            default -> center;
        };
    }

    private void addItemToInventory(ItemStack stack, World world, BlockPos pos) {
        var remaining = stack.getCount();
        for (int i = 1; i < this.inventory.getSlots(); i++) {
            var stackInSlot = this.inventory.getStackInSlot(i);

            if (stackInSlot.isEmpty()) {
                this.inventory.setStackInSlot(i, stack.copy());
                return;
            }

            if (StackHelper.areStacksEqual(stackInSlot, stack)) {
                var insertSize = Math.min(remaining, stackInSlot.getMaxStackSize() - stackInSlot.getCount());

                this.inventory.setStackInSlot(i, StackHelper.grow(stackInSlot, insertSize));

                remaining -= insertSize;
            }

            if (remaining == 0)
                return;
        }

        Block.spawnAsEntity(world, pos, StackHelper.withSize(stack, remaining, false));
    }
}

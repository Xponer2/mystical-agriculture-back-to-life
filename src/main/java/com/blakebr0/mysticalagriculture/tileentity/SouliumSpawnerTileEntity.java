package com.blakebr0.mysticalagriculture.tileentity;

import java.util.List;
import java.util.UUID;

import com.blakebr0.cucumber.energy.DynamicEnergyStorage;
import com.blakebr0.cucumber.helper.StackHelper;
import com.blakebr0.cucumber.inventory.BaseItemStackHandler;
import com.blakebr0.cucumber.inventory.CachedRecipe;
import com.blakebr0.cucumber.inventory.OnContentsChangedFunction;
import com.blakebr0.cucumber.inventory.SidedInventoryWrapper;
import com.blakebr0.cucumber.tileentity.BaseInventoryTileEntity;
import com.blakebr0.cucumber.util.Localizable;
import com.blakebr0.cucumber.util.Utils;
import com.blakebr0.mysticalagriculture.api.crafting.ISouliumSpawnerRecipe;
import com.blakebr0.mysticalagriculture.api.machine.IUpgradeableMachine;
import com.blakebr0.mysticalagriculture.api.machine.MachineUpgradeItemStackHandler;
import com.blakebr0.mysticalagriculture.api.machine.MachineUpgradeTier;
import com.blakebr0.mysticalagriculture.block.SouliumSpawnerBlock;
import com.blakebr0.mysticalagriculture.container.SouliumSpawnerContainer;
import com.blakebr0.mysticalagriculture.init.ModRecipeTypes;
import com.blakebr0.mysticalagriculture.init.ModTileEntities;
import com.blakebr0.mysticalagriculture.util.RecipeIngredientCache;
import com.google.common.collect.ImmutableList;
import io.github.xponer2.translator.menu.AbstractContainerMenu;
import io.github.xponer2.translator.menu.MenuProvider;
import io.github.xponer2.translator.recipe.CraftingInput;
import io.github.xponer2.translator.registry.HolderLookup;
import io.github.xponer2.translator.util.particle.ParticleTypes;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.items.IItemHandler;
import io.github.xponer2.translator.util.particle.Particles;
import io.github.xponer2.translator.util.Platform;

public class SouliumSpawnerTileEntity extends BaseInventoryTileEntity implements MenuProvider, IUpgradeableMachine {
    public SouliumSpawnerTileEntity() {
        this(BlockPos.ORIGIN, null);
    }

    private static final int FUEL_TICK_MULTIPLIER = 20;
    public static final int OPERATION_TIME = 200;
    public static final int FUEL_USAGE = 20;
    public static final int FUEL_CAPACITY = 80000;
    public static final int SPAWN_RADIUS = 3;

    private final BaseItemStackHandler inventory;
    private final MachineUpgradeItemStackHandler upgradeInventory;
    private final DynamicEnergyStorage energy;
    private final SidedInventoryWrapper[] sidedInventoryWrappers;
    private final CachedRecipe<CraftingInput, ISouliumSpawnerRecipe> recipe;
    private MachineUpgradeTier tier;
    private int progress;
    private int fuelLeft;
    private int fuelItemValue;
    private boolean isRunning;
    private double spin, oSpin;
    private DisplayEntity[] displayEntities;

    public SouliumSpawnerTileEntity(BlockPos pos, IBlockState state) {
        super(ModTileEntities.SOULIUM_SPAWNER.get(), pos, state);
        this.inventory = createInventoryHandler((slot) -> this.onInventoryChanged());
        this.upgradeInventory = new MachineUpgradeItemStackHandler();
        this.energy = new DynamicEnergyStorage(FUEL_CAPACITY, this::setChangedFast);
        this.sidedInventoryWrappers = SidedInventoryWrapper.create(this.inventory, ImmutableList.of(EnumFacing.UP, EnumFacing.DOWN, EnumFacing.NORTH), this::canInsertStackSided, null);
        this.recipe = new CachedRecipe<>(ModRecipeTypes.SOULIUM_SPAWNER.get());
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
        tag.setTag("Energy", this.energy.serializeNBT());
        tag.setTag("UpgradeInventory", this.upgradeInventory.serializeNBT());
    }

    @Override
    public void onLoad() {
        super.onLoad();

        this.reloadActiveRecipe();
    }

    @Override
    public void onDataPacket(NetworkManager connection, SPacketUpdateTileEntity packet) {
        HolderLookup.Provider lookup = io.github.xponer2.translator.registry.RegistryAccess.INSTANCE;
        super.onDataPacket(connection, packet);

        this.reloadActiveRecipe();
    }

    @Override
    public void handleUpdateTag(NBTTagCompound tag) {
        HolderLookup.Provider lookup = io.github.xponer2.translator.registry.RegistryAccess.INSTANCE;
        super.handleUpdateTag(tag);

        this.reloadActiveRecipe();
    }

    @Override
    public ITextComponent getDisplayName() {
        return Localizable.of("container.mysticalagriculture.soulium_spawner").build();
    }

    @Override
    public AbstractContainerMenu createMenu(int id, InventoryPlayer playerInventory, EntityPlayer player) {
        return SouliumSpawnerContainer.create(id, playerInventory, this.inventory, this.upgradeInventory, this.getPos());
    }

    @Override
    public MachineUpgradeItemStackHandler getUpgradeInventory() {
        return this.upgradeInventory;
    }

    @Override
    protected void clearAdditional() {
        this.upgradeInventory.getStacks().clear();
    }

    public IItemHandler getSidedInventory(EnumFacing direction) {
        if (direction == null) direction = EnumFacing.NORTH;

        return switch (direction) {
            case UP -> this.sidedInventoryWrappers[0];
            case DOWN -> this.sidedInventoryWrappers[1];
            default -> this.sidedInventoryWrappers[2];
        };
    }

    public static void tick(World world, BlockPos pos, IBlockState state, SouliumSpawnerTileEntity tile) {
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

        var isDisabled = world.isBlockPowered(tile.getPos());
        var wasRunning = tile.isRunning;

        tile.isRunning = false;

        if (tile.energy.getEnergyStored() >= tile.getFuelUsage() && !isDisabled) {
            var input = tile.inventory.getStackInSlot(0);

            if (!input.isEmpty()) {
                var recipe = tile.getActiveRecipe();

                if (recipe != null && input.getCount() >= recipe.getCount(0)) {
                    tile.isRunning = true;
                    tile.progress++;
                    tile.energy.extractEnergy(tile.getFuelUsage(), false);

                    if (tile.progress >= tile.getOperationTime()) {
                        if (tile.attemptSpawn(recipe)) {
                            tile.inventory.setStackInSlot(0, StackHelper.shrink(input, recipe.getCount(0), false));
                            tile.progress = 0;
                            tile.sendSpawnParticles();
                        } else {
                            tile.progress = 0;
                        }
                    }

                    tile.setChangedFast();
                }
            } else {
                tile.isRunning = false;

                if (tile.progress > 0) {
                    tile.progress = 0;

                    tile.setChangedFast();
                }
            }
        }

        if (wasRunning != tile.isRunning) {
            world.setBlockState(pos, state.withProperty(SouliumSpawnerBlock.RUNNING, tile.isRunning), 3);

            tile.setChangedFast();
        }

        tile.dispatchIfChanged();
    }

    public static void clientTick(World world, BlockPos pos, IBlockState state, SouliumSpawnerTileEntity tile) {
        var isRunning = state.getValue(SouliumSpawnerBlock.RUNNING);
        var spinSpeed = isRunning ? 200D : 400D;

        tile.oSpin = tile.spin;
        tile.spin = (tile.spin + (1000.0D / spinSpeed)) % 360.0D;

        if (isRunning) {
            tile.sendRunningParticles();
        }
    }

    public static BaseItemStackHandler createInventoryHandler() {
        return createInventoryHandler(null);
    }

    public static BaseItemStackHandler createInventoryHandler(OnContentsChangedFunction onContentsChanged) {
        return BaseItemStackHandler.create(2, onContentsChanged, builder -> {
            builder.addSlotLimit(0, 512);
            builder.setCanInsert((slot, stack) -> switch (slot) {
                case 1 -> TileEntityFurnace.isItemFuel(stack);
                default -> true;
            });
            builder.setCanExtract(slot -> switch (slot) {
                case 1 -> !TileEntityFurnace.isItemFuel(builder.getStackInSlot(slot));
                default -> false;
            });
        });
    }

    public ISouliumSpawnerRecipe getActiveRecipe() {
        return this.recipe.get();
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

    public double getSpin() {
        return this.spin;
    }

    public double getoSpin() {
        return this.oSpin;
    }

    public DisplayEntity getDisplayEntity() {
        if (this.displayEntities == null)
            return null;

        var index = Math.toIntExact((System.currentTimeMillis() / 2000L) % this.displayEntities.length);

        return this.displayEntities[index];
    }

    private CraftingInput toCraftingInput() {
        return this.inventory.toShapelessCraftingInput(0, 1);
    }

    private boolean attemptSpawn(ISouliumSpawnerRecipe recipe) {
        if (this.world == null)
            return false;

        var entity = recipe.getRandomEntityType(this.world.rand)
                .map(e -> e.data().create(this.world))
                .orElse(null);

        if (entity == null)
            return false;

        var entities = this.world.getEntitiesWithinAABB(entity.getClass(), Platform.boxOfSize(Platform.atCenterOf(this.getPos()), SPAWN_RADIUS * 2, SPAWN_RADIUS * 2, SPAWN_RADIUS * 2))
                .stream()
                .filter(Entity::isEntityAlive)
                .count();

        if (entities >= 32)
            return false;

        var pos = this.findSpawnPosition();

        entity.setUniqueId(UUID.randomUUID());
        entity.setLocationAndAngles(pos.getX(), pos.getY(), pos.getZ(), this.world.rand.nextFloat() * 360F, 0);

        if (entity instanceof EntityLiving mob) {
            mob.onInitialSpawn( this.world.getDifficultyForLocation(this.getPos()), null);
        }

        int attempts = 20;

        while (attempts-- > 0 && !this.canEntitySpawn(entity)) {
            pos = this.findSpawnPosition();

            entity.setLocationAndAngles(pos.getX(), pos.getY(), pos.getZ(), this.world.rand.nextFloat() * 360F, 0);
        }

        if (attempts <= 0)
            return false;

        this.world.spawnEntity(entity);

        return true;
    }

    private boolean canEntitySpawn(Entity entity) {
        return this.world != null && this.world.checkNoEntityCollision(entity.getEntityBoundingBox(), entity);
    }

    private BlockPos findSpawnPosition() {
        var size = SPAWN_RADIUS * 2 + 1;
        var index = Utils.randInt(0, (int) Math.pow(size, 2));
        var xOffset = (index % size) - SPAWN_RADIUS;
        var zOffset = (index / size) - SPAWN_RADIUS;

        return this.getPos().add(xOffset, 0, zOffset);
    }

    private void onInventoryChanged() {
        this.reloadActiveRecipe();
        this.markDirty();
    }

    private void reloadActiveRecipe() {
        if (this.world == null)
            return;

        var recipe = this.recipe.checkAndGet(this.toCraftingInput(), this.world);

        if (recipe != null) {
            var entities = recipe.getEntityTypes().unwrap();
            var totalWeight = entities.stream().map(e -> e.getWeight().asInt()).reduce(0, Integer::sum);

            this.displayEntities = entities
                    .stream()
                    .map(e -> new DisplayEntity(e.data().create(this.world), ((double) e.getWeight().asInt() / totalWeight) * 100D))
                    .toArray(DisplayEntity[]::new);
        } else {
            this.displayEntities = null;
        }
    }

    private void sendRunningParticles() {
        if (this.world == null)
            return;

        var pos = this.getPos();

        double x = pos.getX() + (Math.random() / 2) + 0.25D;
        double y = pos.getY() + (Math.random() / 2) + 0.25D;
        double z = pos.getZ() + (Math.random() / 2) + 0.25D;

        Particles.addParticle(this.world, ParticleTypes.FLAME, x, y, z, 0, 0, 0);
    }

    private void sendSpawnParticles() {
        if (this.getWorld() == null || this.getWorld().isRemote)
            return;

        var world = (WorldServer) this.getWorld();
        var pos = this.getPos();

        for (int i = 0; i < 20; i++) {
            double x = pos.getX() + Math.random();
            double y = pos.getY() + Math.random();
            double z = pos.getZ() + Math.random();

            Particles.sendParticles(world, ParticleTypes.FLAME, x, y, z, 1, 0, 0, 0, 0.1D);
        }
    }

    private boolean canInsertStackSided(int slot, ItemStack stack, EnumFacing direction) {
        if (direction == null)
            return true;
        if (slot == 0 && direction == EnumFacing.UP)
            return RecipeIngredientCache.INSTANCE.isValidSouliumSpawnerInput(stack);
        if (slot == 1 && direction == EnumFacing.NORTH)
            return TileEntityFurnace.isItemFuel(stack);

        return false;
    }

    public final static class DisplayEntity {
        private final Entity entity;
        private final double chance;

        public DisplayEntity(Entity entity, double chance) {
            this.entity = entity;
            this.chance = chance;
        }

        public Entity entity() {
            return entity;
        }

        public double chance() {
            return chance;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof DisplayEntity)) return false;
            DisplayEntity that = (DisplayEntity) o;
            return java.util.Objects.equals(entity, that.entity)
                    && Double.compare(chance, that.chance) == 0;
        }

        @Override
        public int hashCode() {
            return java.util.Objects.hash(entity, chance);
        }

        @Override
        public String toString() {
            return "DisplayEntity[" + "entity=" + entity + ", " + "chance=" + chance + "]";
        }
}
}

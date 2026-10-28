package com.blakebr0.mysticalagriculture.tileentity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.blakebr0.cucumber.inventory.BaseItemStackHandler;
import com.blakebr0.cucumber.inventory.CachedRecipe;
import com.blakebr0.cucumber.tileentity.BaseInventoryTileEntity;
import com.blakebr0.cucumber.util.MultiblockPositions;
import com.blakebr0.mysticalagriculture.api.crafting.IAwakeningRecipe;
import com.blakebr0.mysticalagriculture.crafting.recipe.AwakeningRecipe;
import com.blakebr0.mysticalagriculture.init.ModRecipeTypes;
import com.blakebr0.mysticalagriculture.init.ModTileEntities;
import com.blakebr0.mysticalagriculture.util.IActivatable;
import io.github.xponer2.translator.recipe.CraftingInput;
import io.github.xponer2.translator.registry.HolderLookup;
import io.github.xponer2.translator.registry.RegistryAccess;
import io.github.xponer2.translator.util.particle.ItemParticleOption;
import io.github.xponer2.translator.util.particle.ParticleOptions;
import io.github.xponer2.translator.util.particle.ParticleTypes;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import io.github.xponer2.translator.util.particle.Particles;

public class AwakeningAltarTileEntity extends BaseInventoryTileEntity implements IActivatable {
    public AwakeningAltarTileEntity() {
        this(BlockPos.ORIGIN, null);
    }

    // the order of these matters because it's affects the order they are rendered
    private static final MultiblockPositions PEDESTAL_LOCATIONS = MultiblockPositions.builder()
            .pos(-3, 0, 0).pos(2, 0, 2).pos(3, 0, 0).pos(-2, 0, -2)
            .pos(0, 0, -3).pos(2, 0, -2).pos(0, 0, 3).pos(-2, 0, 2).build();
    private final BaseItemStackHandler inventory;
    private final BaseItemStackHandler recipeInventory;
    private final CachedRecipe<CraftingInput, IAwakeningRecipe> recipe;
    private int progress;
    private boolean active;

    public AwakeningAltarTileEntity(BlockPos pos, IBlockState state) {
        super(ModTileEntities.AWAKENING_ALTAR.get(), pos, state);
        this.inventory = BaseItemStackHandler.create(2, (slot) -> this.markDirty(), handler -> {
            handler.setDefaultSlotLimit(1);
            handler.setCanInsert((slot, stack) -> handler.getStackInSlot(1).isEmpty());
            handler.setOutputSlots(1);
        });
        this.recipeInventory = BaseItemStackHandler.create(9);
        this.recipe = new CachedRecipe<>(ModRecipeTypes.AWAKENING.get());
    }

    @Override
    public BaseItemStackHandler getInventory() {
        return this.inventory;
    }

    @Override
    public void loadAdditional(NBTTagCompound tag, HolderLookup.Provider lookup) {
        super.loadAdditional(tag, lookup);

        this.progress = tag.getInteger("Progress");
        this.active = tag.getBoolean("Active");
    }

    @Override
    public void saveAdditional(NBTTagCompound tag, HolderLookup.Provider lookup) {
        super.saveAdditional(tag, lookup);

        tag.setInteger("Progress", this.progress);
        tag.setBoolean("Active", this.active);
    }

    @Override
    public boolean isActive() {
        if (!this.active) {
            this.active = this.world != null && this.world.isBlockPowered(this.getPos());
        }

        return this.active;
    }

    @Override
    public void activate() {
        this.active = true;
    }

    public static void tick(World world, BlockPos pos, IBlockState state, AwakeningAltarTileEntity tile) {
        var input = tile.inventory.getStackInSlot(0);

        if (input.isEmpty()) {
            tile.reset();
            tile.dispatchIfChanged();
            return;
        }

        if (tile.isActive()) {
            var recipe = tile.getActiveRecipe();

            if (recipe != null && tile.hasRequiredEssences()) {
                tile.progress++;

                var collections = tile.getPedestalCollections();

                if (tile.progress >= 100) {
                    var remaining = recipe.getRemainingItems(tile.toCraftingInput());

                    for (var i = 0; i < collections.pedestals.size(); i++) {
                        var pedestal = collections.pedestals.get(i);
                        var inventory = pedestal.getInventory();

                        inventory.setStackInSlot(0, remaining.get(i + 1));

                        tile.spawnParticles(ParticleTypes.SMOKE, pedestal.getPos(), 1.2D, 20);
                    }

                    for (var i = 0; i < collections.vessels.size(); i++) {
                        var vessel = collections.vessels.get(i);
                        var inventory = vessel.getInventory();

                        inventory.setStackInSlot(0, remaining.get(i + 5));

                        tile.spawnParticles(ParticleTypes.SMOKE, vessel.getPos(), 1.2D, 20);
                    }

                    var result = recipe.assemble(tile.toCraftingInput(), RegistryAccess.INSTANCE);

                    tile.setOutput(result, remaining.get(0));
                    tile.reset();
                    tile.setChangedFast();
                    tile.spawnParticles(ParticleTypes.HAPPY_VILLAGER, pos, 1.0D, 10);
                } else {
                    for (var pedestal : collections.all()) {
                        var pedestalPos = pedestal.getPos();
                        var stack = pedestal.getInventory().getStackInSlot(0);

                        tile.spawnItemParticles(pedestalPos, stack);
                    }
                }
            } else {
                tile.reset();
            }
        } else {
            tile.progress = 0;
        }

        tile.dispatchIfChanged();
    }

    public List<BlockPos> getPedestalPositions() {
        return PEDESTAL_LOCATIONS.get(this.getPos());
    }

    private void reset() {
        this.progress = 0;
        this.active = false;
    }

    public IAwakeningRecipe getActiveRecipe() {
        if (this.world == null)
            return null;

        this.updateRecipeInventory(this.getPedestalCollections());

        return this.recipe.checkAndGet(this.toCraftingInput(), this.world);
    }

    public NonNullList<ItemStack> getEssenceItems() {
        return this.getPedestalCollections()
                .vessels
                .stream()
                .map(v -> v.getInventory().getStackInSlot(0))
                .collect(Collectors.toCollection(NonNullList::create));
    }

    private CraftingInput toCraftingInput() {
        return this.recipeInventory.toCraftingInput(3, 3);
    }

    private void updateRecipeInventory(PedestalTileEntityCollections collections) {
        this.recipeInventory.setSize(AwakeningRecipe.RECIPE_SIZE);
        this.recipeInventory.setStackInSlot(0, this.inventory.getStackInSlot(0));

        for (int i = 0; i < collections.pedestals.size(); i++) {
            var stack = collections.pedestals.get(i).getInventory().getStackInSlot(0);

            this.recipeInventory.setStackInSlot(i + 1, stack);
        }

        for (int i = 0; i < collections.vessels.size(); i++) {
            var stack = collections.vessels.get(i).getInventory().getStackInSlot(0);

            this.recipeInventory.setStackInSlot(i + 5, stack);
        }
    }

    private PedestalTileEntityCollections getPedestalCollections() {
        if (this.world == null) {
            return PedestalTileEntityCollections.EMPTY;
        }

        var collections = new PedestalTileEntityCollections();

        for (var pos : this.getPedestalPositions()) {
            if (!this.world.isBlockLoaded(pos)) {
                return PedestalTileEntityCollections.EMPTY;
            }

            var tile = this.world.getTileEntity(pos);

            if (tile instanceof AwakeningPedestalTileEntity pedestal) {
                collections.addPedestal(pedestal);
            }

            if (tile instanceof EssenceVesselTileEntity vessel) {
                collections.addVessel(vessel);
            }
        }

        return collections;
    }

    private <T extends ParticleOptions> void spawnParticles(T particle, BlockPos pos, double yOffset, int count) {
        if (this.world == null || this.world.isRemote)
            return;

        var world = (WorldServer) this.world;

        double x = pos.getX() + 0.5D;
        double y = pos.getY() + yOffset;
        double z = pos.getZ() + 0.5D;

        Particles.sendParticles(world, particle, x, y, z, count, 0, 0, 0, 0.1D);
    }

    private void spawnItemParticles(BlockPos pedestalPos, ItemStack stack) {
        if (this.world == null || this.world.isRemote || stack.isEmpty())
            return;

        var world = (WorldServer) this.world;
        var pos = this.getPos();

        double x = pedestalPos.getX() + (world.rand.nextDouble() * 0.2D) + 0.4D;
        double y = pedestalPos.getY() + (world.rand.nextDouble() * 0.2D) + 1.2D;
        double z = pedestalPos.getZ() + (world.rand.nextDouble() * 0.2D) + 0.4D;

        double velX = pos.getX() - pedestalPos.getX();
        double velY = 0.25D;
        double velZ = pos.getZ() - pedestalPos.getZ();

        Particles.sendParticles(world, new ItemParticleOption(ParticleTypes.ITEM, stack), x, y, z, 0, velX, velY, velZ, 0.18D);
    }

    private void setOutput(ItemStack stack, ItemStack remaining) {
        var stacks = this.inventory.getStacks();

        stacks.set(0, remaining);
        stacks.set(1, stack);
    }

    private boolean hasRequiredEssences() {
        var essences = this.getEssenceItems();
        return this.recipe.get().hasRequiredEssences(essences);
    }

    private static class PedestalTileEntityCollections {
        public static final PedestalTileEntityCollections EMPTY = new PedestalTileEntityCollections();

        public final List<AwakeningPedestalTileEntity> pedestals = new ArrayList<>();
        public final List<EssenceVesselTileEntity> vessels = new ArrayList<>();

        public void addPedestal(AwakeningPedestalTileEntity pedestal) {
            if (this.pedestals.size() < 4)
                this.pedestals.add(pedestal);
        }

        public void addVessel(EssenceVesselTileEntity vessel) {
            if (this.vessels.size() < 4)
                this.vessels.add(vessel);
        }

        public List<BaseInventoryTileEntity> all() {
            var list = new ArrayList<BaseInventoryTileEntity>();
            list.addAll(this.pedestals);
            list.addAll(this.vessels);
            return list;
        }
    }
}

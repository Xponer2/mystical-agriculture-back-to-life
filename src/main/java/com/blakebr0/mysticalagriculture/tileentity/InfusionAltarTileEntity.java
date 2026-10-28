package com.blakebr0.mysticalagriculture.tileentity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.blakebr0.cucumber.inventory.BaseItemStackHandler;
import com.blakebr0.cucumber.inventory.CachedRecipe;
import com.blakebr0.cucumber.inventory.OnContentsChangedFunction;
import com.blakebr0.cucumber.tileentity.BaseInventoryTileEntity;
import com.blakebr0.cucumber.util.MultiblockPositions;
import com.blakebr0.mysticalagriculture.api.crafting.IInfusionRecipe;
import com.blakebr0.mysticalagriculture.crafting.recipe.InfusionRecipe;
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
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import io.github.xponer2.translator.util.particle.Particles;

public class InfusionAltarTileEntity extends BaseInventoryTileEntity implements IActivatable {
    public InfusionAltarTileEntity() {
        this(BlockPos.ORIGIN, null);
    }

    private final BaseItemStackHandler inventory;
    private final BaseItemStackHandler recipeInventory;
    private final MultiblockPositions pedestalLocations = new MultiblockPositions.Builder()
            .pos(3, 0, 0).pos(0, 0, 3).pos(-3, 0, 0).pos(0, 0, -3)
            .pos(2, 0, 2).pos(2, 0, -2).pos(-2, 0, 2).pos(-2, 0, -2).build();
    private final CachedRecipe<CraftingInput, IInfusionRecipe> recipe;
    private int progress;
    private boolean active;

    public InfusionAltarTileEntity(BlockPos pos, IBlockState state) {
        super(ModTileEntities.INFUSION_ALTAR.get(), pos, state);
        this.inventory = createInventoryHandler((slot) -> this.markDirty());
        this.recipeInventory = BaseItemStackHandler.create(9);
        this.recipe = new CachedRecipe<>(ModRecipeTypes.INFUSION.get());
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

    public static void tick(World world, BlockPos pos, IBlockState state, InfusionAltarTileEntity tile) {
        var input = tile.inventory.getStackInSlot(0);

        if (input.isEmpty()) {
            tile.reset();
            tile.dispatchIfChanged();
            return;
        }

        if (tile.isActive()) {
            var recipe = tile.getActiveRecipe();

            if (recipe != null) {
                tile.progress++;

                var pedestals = tile.getPedestals();

                if (tile.progress >= 100) {
                    var inventory = tile.toCraftingInput();
                    var remaining = recipe.getRemainingItems(inventory);

                    for (var i = 1; i < remaining.size(); i++) {
                        var pedestal = pedestals.get(i - 1);
                        pedestal.getInventory().setStackInSlot(0, remaining.get(i));
                        tile.spawnParticles(ParticleTypes.SMOKE, pedestal.getPos(), 1.2D, 20);
                    }

                    var result = recipe.assemble(inventory, RegistryAccess.INSTANCE);

                    tile.setOutput(result, remaining.get(0));
                    tile.reset();
                    tile.setChangedFast();
                    tile.spawnParticles(ParticleTypes.HAPPY_VILLAGER, pos, 1.0D, 10);
                } else {
                    for (var pedestal : pedestals) {
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

    public static BaseItemStackHandler createInventoryHandler(OnContentsChangedFunction onContentsChanged) {
        return BaseItemStackHandler.create(2, onContentsChanged, builder -> {
            builder.setDefaultSlotLimit(1);
            builder.setCanInsert((slot, stack) -> builder.getStackInSlot(1).isEmpty());
            builder.setOutputSlots(1);
        });
    }

    public List<BlockPos> getPedestalPositions() {
        return this.pedestalLocations.get(this.getPos());
    }

    public IInfusionRecipe getActiveRecipe() {
        if (this.world == null)
            return null;

        this.updateRecipeInventory(this.getPedestals());

        return this.recipe.checkAndGet(this.toCraftingInput(), this.world);
    }

    private CraftingInput toCraftingInput() {
        return this.recipeInventory.toCraftingInput(3, 3);
    }

    private void reset() {
        this.progress = 0;
        this.active = false;
    }

    private void updateRecipeInventory(List<InfusionPedestalTileEntity> pedestals) {
        this.recipeInventory.setSize(InfusionRecipe.RECIPE_SIZE);
        this.recipeInventory.setStackInSlot(0, this.inventory.getStackInSlot(0));

        for (int i = 0; i < pedestals.size(); i++) {
            var stack = pedestals.get(i).getInventory().getStackInSlot(0);

            this.recipeInventory.setStackInSlot(i + 1, stack);
        }
    }

    private List<InfusionPedestalTileEntity> getPedestals() {
        if (this.world == null) {
            return Collections.emptyList();
        }

        List<InfusionPedestalTileEntity> pedestals = new ArrayList<>();

        for (var pos : this.getPedestalPositions()) {
            if (!this.world.isBlockLoaded(pos)) {
                return Collections.emptyList();
            }

            var tile = this.world.getTileEntity(pos);
            if (tile instanceof InfusionPedestalTileEntity pedestal)
                pedestals.add(pedestal);
        }

        return pedestals;
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
}

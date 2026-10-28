package com.blakebr0.mysticalagriculture.tileentity;

import com.blakebr0.cucumber.inventory.BaseItemStackHandler;
import com.blakebr0.cucumber.inventory.OnContentsChangedFunction;
import com.blakebr0.cucumber.tileentity.BaseInventoryTileEntity;
import com.blakebr0.mysticalagriculture.init.ModTileEntities;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;

public class InfusionPedestalTileEntity extends BaseInventoryTileEntity {
    public InfusionPedestalTileEntity() {
        this(BlockPos.ORIGIN, null);
    }

    private final BaseItemStackHandler inventory;

    public InfusionPedestalTileEntity(BlockPos pos, IBlockState state) {
        super(ModTileEntities.INFUSION_PEDESTAL.get(), pos, state);
        this.inventory = createInventoryHandler((slot) -> this.setChangedAndDispatch());
    }

    @Override
    public BaseItemStackHandler getInventory() {
        return this.inventory;
    }

    public static BaseItemStackHandler createInventoryHandler(OnContentsChangedFunction onContentsChanged) {
        return BaseItemStackHandler.create(1, onContentsChanged, builder -> {
            builder.setDefaultSlotLimit(1);
        });
    }
}

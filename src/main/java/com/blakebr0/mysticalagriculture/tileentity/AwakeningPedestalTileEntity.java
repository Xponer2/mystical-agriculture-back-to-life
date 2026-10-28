package com.blakebr0.mysticalagriculture.tileentity;

import com.blakebr0.cucumber.inventory.BaseItemStackHandler;
import com.blakebr0.cucumber.tileentity.BaseInventoryTileEntity;
import com.blakebr0.mysticalagriculture.init.ModTileEntities;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;

public class AwakeningPedestalTileEntity extends BaseInventoryTileEntity {
    public AwakeningPedestalTileEntity() {
        this(BlockPos.ORIGIN, null);
    }

    private final BaseItemStackHandler inventory;

    public AwakeningPedestalTileEntity(BlockPos pos, IBlockState state) {
        super(ModTileEntities.AWAKENING_PEDESTAL.get(), pos, state);
        this.inventory = BaseItemStackHandler.create(1, (slot) -> this.setChangedAndDispatch(), handler -> {
            handler.setDefaultSlotLimit(1);
        });
    }

    @Override
    public BaseItemStackHandler getInventory() {
        return this.inventory;
    }
}

package com.blakebr0.mysticalagriculture.tileentity;

import com.blakebr0.cucumber.inventory.BaseItemStackHandler;
import com.blakebr0.cucumber.inventory.OnContentsChangedFunction;
import com.blakebr0.cucumber.tileentity.BaseInventoryTileEntity;
import com.blakebr0.cucumber.util.Localizable;
import com.blakebr0.mysticalagriculture.api.tinkering.IAugmentProvider;
import com.blakebr0.mysticalagriculture.api.tinkering.ITinkerable;
import com.blakebr0.mysticalagriculture.container.TinkeringTableContainer;
import com.blakebr0.mysticalagriculture.init.ModTileEntities;
import com.blakebr0.mysticalagriculture.lib.ModCrops;
import io.github.xponer2.translator.menu.AbstractContainerMenu;
import io.github.xponer2.translator.menu.MenuProvider;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;

public class TinkeringTableTileEntity extends BaseInventoryTileEntity implements MenuProvider {
    public TinkeringTableTileEntity() {
        this(BlockPos.ORIGIN, null);
    }

    private final BaseItemStackHandler inventory;

    public TinkeringTableTileEntity(BlockPos pos, IBlockState state) {
        super(ModTileEntities.TINKERING_TABLE.get(), pos, state);
        this.inventory = createInventoryHandler((slot) -> this.setChangedAndDispatch());
    }

    @Override
    public BaseItemStackHandler getInventory() {
        return this.inventory;
    }

    @Override
    public ITextComponent getDisplayName() {
        return Localizable.of("container.mysticalagriculture.tinkering_table").build();
    }

    @Override
    public AbstractContainerMenu createMenu(int windowId, InventoryPlayer playerInventory, EntityPlayer player) {
        return TinkeringTableContainer.create(windowId, playerInventory, this.inventory, this.getPos());
    }

    public static BaseItemStackHandler createInventoryHandler() {
        return createInventoryHandler(null);
    }

    public static BaseItemStackHandler createInventoryHandler(OnContentsChangedFunction onContentsChanged) {
        return BaseItemStackHandler.create(7, onContentsChanged, builder -> {
            builder.setDefaultSlotLimit(1);
            builder.setCanInsert((slot, stack) -> {
                var item = stack.getItem();
                return switch (slot) {
                    case 0 -> item instanceof ITinkerable;
                    case 1, 2 -> item instanceof IAugmentProvider;
                    case 3 -> item == ModCrops.AIR.getEssenceItem();
                    case 4 -> item == ModCrops.EARTH.getEssenceItem();
                    case 5 -> item == ModCrops.WATER.getEssenceItem();
                    case 6 -> item == ModCrops.FIRE.getEssenceItem();
                    default -> true;
                };
            });
        });
    }
}

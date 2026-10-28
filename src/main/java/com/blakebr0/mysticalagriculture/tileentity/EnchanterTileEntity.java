package com.blakebr0.mysticalagriculture.tileentity;

import com.blakebr0.cucumber.inventory.BaseItemStackHandler;
import com.blakebr0.cucumber.inventory.OnContentsChangedFunction;
import com.blakebr0.cucumber.tileentity.BaseInventoryTileEntity;
import com.blakebr0.mysticalagriculture.container.EnchanterContainer;
import com.blakebr0.mysticalagriculture.init.ModTileEntities;
import io.github.xponer2.translator.menu.AbstractContainerMenu;
import io.github.xponer2.translator.menu.MenuProvider;
import io.github.xponer2.translator.text.TranslatableComponent;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

public class EnchanterTileEntity extends BaseInventoryTileEntity implements MenuProvider {
    public EnchanterTileEntity() {
        this(BlockPos.ORIGIN, null);
    }

    private final BaseItemStackHandler inventory;

    public EnchanterTileEntity(BlockPos pos, IBlockState state) {
        super(ModTileEntities.ENCHANTER.get(), pos, state);
        this.inventory = createInventoryHandler((slot) -> this.setChangedAndDispatch());
    }

    @Override
    public BaseItemStackHandler getInventory() {
        return this.inventory;
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslatableComponent("container.mysticalagriculture.enchanter");
    }

    @Override
    public AbstractContainerMenu createMenu(int id, InventoryPlayer playerInventory, EntityPlayer player) {
        return EnchanterContainer.create(id, playerInventory, this.inventory, this.getPos());
    }

    public static BaseItemStackHandler createInventoryHandler() {
        return createInventoryHandler(null);
    }

    public static BaseItemStackHandler createInventoryHandler(OnContentsChangedFunction onContentsChanged) {
        return BaseItemStackHandler.create(3, onContentsChanged, handler -> {
            handler.addSlotLimit(0, 512);
            handler.addSlotLimit(1, 512);
        });
    }
}

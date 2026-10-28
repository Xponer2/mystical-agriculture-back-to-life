package com.blakebr0.mysticalagriculture.container;

import com.blakebr0.cucumber.container.BaseContainerMenu;
import com.blakebr0.cucumber.inventory.BaseItemStackHandler;
import com.blakebr0.cucumber.inventory.slot.BaseItemStackHandlerSlot;
import com.blakebr0.mysticalagriculture.api.machine.MachineUpgradeItemStackHandler;
import com.blakebr0.mysticalagriculture.init.ModMenuTypes;
import com.blakebr0.mysticalagriculture.item.MachineUpgradeItem;
import com.blakebr0.mysticalagriculture.tileentity.EssenceFurnaceTileEntity;
import com.blakebr0.mysticalagriculture.util.RecipeIngredientCache;
import io.github.xponer2.translator.menu.MenuType;
import io.github.xponer2.translator.recipe.RecipeType;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.items.SlotItemHandler;

import io.github.xponer2.translator.network.RegistryFriendlyByteBuf;
import net.minecraft.tileentity.TileEntityFurnace;

public class EssenceFurnaceContainer extends BaseContainerMenu {
    private EssenceFurnaceContainer(MenuType<?> type, int id, InventoryPlayer playerInventory, BlockPos pos) {
        this(type, id, playerInventory, EssenceFurnaceTileEntity.createInventoryHandler(), new MachineUpgradeItemStackHandler(), pos);
    }

    private EssenceFurnaceContainer(MenuType<?> type, int id, InventoryPlayer playerInventory, BaseItemStackHandler inventory, MachineUpgradeItemStackHandler upgradeInventory, BlockPos pos) {
        super(type, id, pos);

        this.addSlot(new SlotItemHandler(upgradeInventory, 0, 152, 9));

        this.addSlot(new BaseItemStackHandlerSlot(inventory, 0, 74, 52));
        this.addSlot(new BaseItemStackHandlerSlot(inventory, 1, 30, 56));
        this.addSlot(new BaseItemStackHandlerSlot(inventory, 2, 134, 52));

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 112 + i * 18));
            }
        }

        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 170));
        }
    }

    @Override
    public ItemStack quickMoveStack(EntityPlayer player, int index) {
        var itemstack = ItemStack.EMPTY;
        var slot = this.slots.get(index);

        if (slot.getHasStack()) {
            var itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index > 3) {
                if (itemstack1.getItem() instanceof MachineUpgradeItem) {
                    if (!this.moveItemStackTo(itemstack1, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (RecipeIngredientCache.INSTANCE.isValidInput(itemstack1, RecipeType.SMELTING)) {
                    if (!this.moveItemStackTo(itemstack1, 1, 2, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (TileEntityFurnace.getItemBurnTime(itemstack1) > 0) {
                    if (!this.moveItemStackTo(itemstack1, 2, 3, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index < 31) {
                    if (!this.moveItemStackTo(itemstack1, 31, 40, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index < 40 && !this.moveItemStackTo(itemstack1, 4, 30, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 4, 40, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, itemstack1);
        }

        return itemstack;
    }

    public static EssenceFurnaceContainer create(int windowId, InventoryPlayer playerInventory, RegistryFriendlyByteBuf buffer) {
        return new EssenceFurnaceContainer(ModMenuTypes.FURNACE.get(), windowId, playerInventory, buffer.readBlockPos());
    }

    public static EssenceFurnaceContainer create(int windowId, InventoryPlayer playerInventory, BaseItemStackHandler inventory, MachineUpgradeItemStackHandler upgradeInventory, BlockPos pos) {
        return new EssenceFurnaceContainer(ModMenuTypes.FURNACE.get(), windowId, playerInventory, inventory, upgradeInventory, pos);
    }
}

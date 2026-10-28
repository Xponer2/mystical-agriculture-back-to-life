package com.blakebr0.mysticalagriculture.container;

import com.blakebr0.cucumber.container.BaseContainerMenu;
import com.blakebr0.cucumber.inventory.BaseItemStackHandler;
import com.blakebr0.cucumber.inventory.slot.BaseItemStackHandlerSlot;
import com.blakebr0.mysticalagriculture.api.machine.MachineUpgradeItemStackHandler;
import com.blakebr0.mysticalagriculture.init.ModMenuTypes;
import com.blakebr0.mysticalagriculture.item.MachineUpgradeItem;
import com.blakebr0.mysticalagriculture.tileentity.HarvesterTileEntity;
import io.github.xponer2.translator.menu.MenuType;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.items.SlotItemHandler;

import io.github.xponer2.translator.network.RegistryFriendlyByteBuf;
import net.minecraft.tileentity.TileEntityFurnace;

public class HarvesterContainer extends BaseContainerMenu {
    private HarvesterContainer(MenuType<?> type, int id, InventoryPlayer playerInventory, RegistryFriendlyByteBuf buffer) {
        this(type, id, playerInventory, HarvesterTileEntity.createInventoryHandler(), new MachineUpgradeItemStackHandler(), buffer.readBlockPos());
    }

    private HarvesterContainer(MenuType<?> type, int id, InventoryPlayer playerInventory, BaseItemStackHandler inventory, MachineUpgradeItemStackHandler upgradeInventory, BlockPos pos) {
        super(type, id, pos);

        this.addSlot(new SlotItemHandler(upgradeInventory, 0, 152, 9));
        this.addSlot(new BaseItemStackHandlerSlot(inventory, 0, 30, 56));

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 5; j++) {
                this.addSlot(new BaseItemStackHandlerSlot(inventory, 1 + j + i * 5, 80 + j * 18, 42 + i * 18));
            }
        }

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

            if (index > 1) {
                if (itemstack1.getItem() instanceof MachineUpgradeItem) {
                    if (!this.moveItemStackTo(itemstack1, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (TileEntityFurnace.getItemBurnTime(itemstack1) > 0) {
                    if (!this.moveItemStackTo(itemstack1, 1, 2, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index < 44) {
                    // these are the Harvester output slots
                    if (index < 17) {
                        if (!this.moveItemStackTo(itemstack1, 17, 53, false)) {
                            return ItemStack.EMPTY;
                        }
                    } else if (!this.moveItemStackTo(itemstack1, 44, 53, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index < 53 && !this.moveItemStackTo(itemstack1, 17, 44, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 17, 53, false)) {
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

    public static HarvesterContainer create(int windowId, InventoryPlayer playerInventory, RegistryFriendlyByteBuf buffer) {
        return new HarvesterContainer(ModMenuTypes.HARVESTER.get(), windowId, playerInventory, buffer);
    }

    public static HarvesterContainer create(int windowId, InventoryPlayer playerInventory, BaseItemStackHandler inventory, MachineUpgradeItemStackHandler upgradeInventory, BlockPos pos) {
        return new HarvesterContainer(ModMenuTypes.HARVESTER.get(), windowId, playerInventory, inventory, upgradeInventory, pos);
    }
}

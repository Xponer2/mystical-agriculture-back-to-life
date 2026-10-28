package com.blakebr0.mysticalagriculture.container;

import com.blakebr0.cucumber.container.ExtendedContainerMenu;
import com.blakebr0.cucumber.inventory.BaseItemStackHandler;
import com.blakebr0.cucumber.inventory.RecipeInventory;
import com.blakebr0.mysticalagriculture.container.slot.EnchanterOutputSlot;
import com.blakebr0.mysticalagriculture.container.slot.EnchanterSlot;
import com.blakebr0.mysticalagriculture.init.ModMenuTypes;
import com.blakebr0.mysticalagriculture.init.ModRecipeTypes;
import com.blakebr0.mysticalagriculture.tileentity.EnchanterTileEntity;
import io.github.xponer2.translator.menu.MenuType;
import io.github.xponer2.translator.recipe.RecipeManager;
import io.github.xponer2.translator.registry.RegistryAccess;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import io.github.xponer2.translator.network.RegistryFriendlyByteBuf;

public class EnchanterContainer extends ExtendedContainerMenu {
    private final World level;
    private final BaseItemStackHandler inventory;
    private final IInventory result;

    private EnchanterContainer(MenuType<?> type, int id, InventoryPlayer playerInventory, BlockPos pos) {
        this(type, id, playerInventory, EnchanterTileEntity.createInventoryHandler(), pos);
    }

    private EnchanterContainer(MenuType<?> type, int id, InventoryPlayer playerInventory, BaseItemStackHandler inventory, BlockPos pos) {
        super(type, id, pos);
        this.level = playerInventory.player.world;
        this.inventory = inventory;
        this.result = new InventoryCraftResult();

        this.addSlot(new EnchanterSlot(this, inventory, 0, 17, 41));
        this.addSlot(new EnchanterSlot(this, inventory, 1, 39, 41));
        this.addSlot(new EnchanterSlot(this, inventory, 2, 79, 41));

        this.addSlot(new EnchanterOutputSlot(this, inventory, this.result, 3, 139, 41));

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 95 + i * 18));
            }
        }

        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 153));
        }

        this.onCraftMatrixChanged(new RecipeInventory(inventory));
    }

    @Override
    public void onCraftMatrixChanged(IInventory matrix) {
        var inventory = this.inventory.toShapelessCraftingInput(0, 3);
        var recipe = RecipeManager.get().getRecipeFor(ModRecipeTypes.ENCHANTER.get(), inventory, this.level);

        if (recipe.isPresent()) {
            this.result.setInventorySlotContents(0, recipe.get().value().assemble(inventory, RegistryAccess.INSTANCE));
        } else {
            this.result.setInventorySlotContents(0, ItemStack.EMPTY);
        }

        super.onCraftMatrixChanged(matrix);
    }

    @Override
    public ItemStack quickMoveStack(EntityPlayer player, int index) {
        var itemstack = ItemStack.EMPTY;
        var slot = this.slots.get(index);

        if (slot.getHasStack()) {
            var itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index == 3) {
                if (!this.moveItemStackTo(itemstack1, 4, 40, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onSlotChange(itemstack1, itemstack);
            } else if (index >= 4 && index < 40) {
                if (!this.moveItemStackTo(itemstack1, 0, 3, false)) {
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

    public static EnchanterContainer create(int windowId, InventoryPlayer playerInventory, RegistryFriendlyByteBuf buffer) {
        return new EnchanterContainer(ModMenuTypes.ENCHANTER.get(), windowId, playerInventory, buffer.readBlockPos());
    }

    public static EnchanterContainer create(int windowId, InventoryPlayer playerInventory, BaseItemStackHandler inventory, BlockPos pos) {
        return new EnchanterContainer(ModMenuTypes.ENCHANTER.get(), windowId, playerInventory, inventory, pos);
    }
}

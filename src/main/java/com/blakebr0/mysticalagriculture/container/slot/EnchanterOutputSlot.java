package com.blakebr0.mysticalagriculture.container.slot;

import com.blakebr0.cucumber.inventory.BaseItemStackHandler;
import com.blakebr0.mysticalagriculture.init.ModRecipeTypes;
import io.github.xponer2.translator.menu.AbstractContainerMenu;
import io.github.xponer2.translator.recipe.RecipeManager;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class EnchanterOutputSlot extends Slot {
    private final AbstractContainerMenu container;
    private final BaseItemStackHandler matrix;

    public EnchanterOutputSlot(AbstractContainerMenu container, BaseItemStackHandler matrix, IInventory inventory, int slotNumber, int xPosition, int yPosition) {
        super(inventory, slotNumber, xPosition, yPosition);
        this.container = container;
        this.matrix = matrix;
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return false;
    }

    @Override
    public ItemStack onTake(EntityPlayer player, ItemStack stack) {
        var remaining = RecipeManager.get().getRemainingItemsFor(ModRecipeTypes.ENCHANTER.get(), this.matrix.toCraftingInput(2, 1), player.world);

        for (int i = 0; i < remaining.size(); i++) {
            this.matrix.setStackInSlot(i, remaining.get(i));
        }

        this.container.onCraftMatrixChanged(this.matrix.toRecipeInventory());
        return stack;
    }
}

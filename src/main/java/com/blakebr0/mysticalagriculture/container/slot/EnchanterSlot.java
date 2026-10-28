package com.blakebr0.mysticalagriculture.container.slot;

import com.blakebr0.cucumber.inventory.BaseItemStackHandler;
import com.blakebr0.cucumber.inventory.RecipeInventory;
import com.blakebr0.cucumber.inventory.slot.BaseItemStackHandlerSlot;
import io.github.xponer2.translator.menu.AbstractContainerMenu;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class EnchanterSlot extends BaseItemStackHandlerSlot {
    private final AbstractContainerMenu container;
    private final IInventory inventory;

    public EnchanterSlot(AbstractContainerMenu container, BaseItemStackHandler inventory, int index, int xPosition, int yPosition) {
        super(inventory, index, xPosition, yPosition);
        this.container = container;
        this.inventory = new RecipeInventory(inventory);
    }

    @Override
    public ItemStack onTake(EntityPlayer player, ItemStack stack) {
        super.onTake(player, stack);
        this.container.onCraftMatrixChanged(this.inventory);
        return stack;
    }

    @Override
    public void putStack(ItemStack stack) {
        super.putStack(stack);
        this.container.onCraftMatrixChanged(this.inventory);
    }
}

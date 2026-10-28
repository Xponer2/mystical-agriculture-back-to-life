package com.blakebr0.mysticalagriculture.container.slot;

import com.blakebr0.cucumber.iface.IToggleableSlot;
import com.blakebr0.mysticalagriculture.api.tinkering.IElementalItem;
import io.github.xponer2.translator.menu.AbstractContainerMenu;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ElementSlot extends SlotItemHandler implements IToggleableSlot {
    private final AbstractContainerMenu container;

    public ElementSlot(AbstractContainerMenu container, IItemHandler inventory, int slotNumber, int xPosition, int yPosition) {
        super(inventory, slotNumber, xPosition, yPosition);
        this.container = container;
    }

    @Override
    public ItemStack onTake(EntityPlayer player, ItemStack stack) {
        super.onTake(player, stack);
        this.container.onCraftMatrixChanged(null);
        return stack;
    }

    @Override
    public void putStack(ItemStack stack) {
        super.putStack(stack);
        this.container.onCraftMatrixChanged(null);
    }

    @Override
    public boolean isEnabled() {
        var stack = this.getItemHandler().getStackInSlot(0);
        var item = stack.getItem();

        return item instanceof IElementalItem;
    }
}

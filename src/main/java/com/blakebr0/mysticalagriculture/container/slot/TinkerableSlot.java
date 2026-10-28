package com.blakebr0.mysticalagriculture.container.slot;

import com.blakebr0.mysticalagriculture.api.util.AugmentUtils;
import io.github.xponer2.translator.menu.AbstractContainerMenu;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class TinkerableSlot extends SlotItemHandler {
    private final AbstractContainerMenu container;

    public TinkerableSlot(AbstractContainerMenu container, IItemHandler inventory, int slotNumber, int xPosition, int yPosition) {
        super(inventory, slotNumber, xPosition, yPosition);
        this.container = container;
    }

    @Override
    public ItemStack onTake(EntityPlayer player, ItemStack stack) {
        for (int i = 0; i < 2; i++) {
            this.getItemHandler().extractItem(i + 1, 1, false);
        }
        return stack;
    }

    @Override
    public void putStack(ItemStack stack) {
        for (int i = 0; i < 2; i++) {
            var augmentStack = this.getItemHandler().getStackInSlot(i + 1);
            if (!augmentStack.isEmpty()) {
                this.getItemHandler().extractItem(i + 1, augmentStack.getMaxStackSize(), false);
            }

            var augment = AugmentUtils.getAugment(stack, i);
            if (augment != null) {
                this.getItemHandler().insertItem(i + 1, new ItemStack(augment.getItem()), false);
            }
        }

        super.putStack(stack);
        this.container.onCraftMatrixChanged(null);
    }
}

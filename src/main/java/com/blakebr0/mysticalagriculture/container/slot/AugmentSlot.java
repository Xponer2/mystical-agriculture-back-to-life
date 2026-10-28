package com.blakebr0.mysticalagriculture.container.slot;

import com.blakebr0.cucumber.iface.IToggleableSlot;
import com.blakebr0.mysticalagriculture.api.tinkering.IAugmentProvider;
import com.blakebr0.mysticalagriculture.api.tinkering.ITinkerable;
import io.github.xponer2.translator.menu.AbstractContainerMenu;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class AugmentSlot extends SlotItemHandler implements IToggleableSlot {
    private final AbstractContainerMenu container;
    private final int augmentSlot;

    public AugmentSlot(AbstractContainerMenu container, IItemHandler inventory, int slotNumber, int xPosition, int yPosition, int augmentSlot) {
        super(inventory, slotNumber, xPosition, yPosition);
        this.container = container;
        this.augmentSlot = augmentSlot;
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
    public int getItemStackLimit(ItemStack stack) {
        return 1;
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        if (!super.isItemValid(stack) || !this.isEnabled())
            return false;

        var stackInSlot = this.getItemHandler().getStackInSlot(0);
        var tinkerableItem = stackInSlot.getItem();
        var augmentItem = stack.getItem();

        if (tinkerableItem instanceof ITinkerable tinkerable && augmentItem instanceof IAugmentProvider augmentProvider) {
            var augment = augmentProvider.getAugment();

            return tinkerable.canApplyAugment(augment);
        }

        return false;
    }

    @Override
    public boolean isEnabled() {
        var stack = this.getItemHandler().getStackInSlot(0);
        var item = stack.getItem();

        if (item instanceof ITinkerable tinkerable) {
            return this.augmentSlot < tinkerable.getAugmentSlots();
        }

        return false;
    }
}

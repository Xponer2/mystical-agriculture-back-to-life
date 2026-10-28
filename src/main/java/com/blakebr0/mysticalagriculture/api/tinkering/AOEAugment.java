package com.blakebr0.mysticalagriculture.api.tinkering;

import java.util.EnumSet;
import java.util.stream.Stream;

import com.blakebr0.mysticalagriculture.api.MysticalAgricultureDataComponentTypes;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import io.github.xponer2.translator.component.StackComponents;

/**
 * This class adds range variable to Augment object.
 */
public abstract class AOEAugment extends Augment {
    protected final int range;

    public AOEAugment(ResourceLocation id, int tier, EnumSet<AugmentType> types, int primaryColor, int secondaryColor, int range) {
        super(id, tier, types, primaryColor, secondaryColor);
        this.range = range;
    }

    public int getRange() {
        return this.range;
    }

    /**
     * This method returns a stream of blocks that should be affected by the AOE augment.
     * @return the stream of block positions that are affected by AOE.
     */
    public static Stream<BlockPos> getAOEBlocks(ItemStack stack, int radius, BlockPos pos, EnumFacing side, EntityPlayer player) {
        int xRange = radius;
        int yRange = radius;
        int zRange = 0;

        int horizontalOffset = 0;
        int verticalOffset = 0;

        var offset = StackComponents.get(stack, MysticalAgricultureDataComponentTypes.AOE_AUGMENT_OFFSET);
        if (offset != null) {
            horizontalOffset = MathHelper.clamp(offset.horizontalOffset(), -radius, radius);
            verticalOffset = MathHelper.clamp(offset.verticalOffset(), -radius, radius);
        }

        var offsetPos = pos;

        if (side == EnumFacing.DOWN || side == EnumFacing.UP) {
            zRange = radius;
            yRange = 0;

            offsetPos = offsetPos.offset(player.getHorizontalFacing().rotateY(), horizontalOffset);
            offsetPos = offsetPos.offset(player.getHorizontalFacing(), verticalOffset);
        } else {
            offsetPos = offsetPos.offset(side.rotateYCCW(), horizontalOffset);
            offsetPos = offsetPos.offset(EnumFacing.UP, verticalOffset);
        }

        if (side == EnumFacing.WEST || side == EnumFacing.EAST) {
            xRange = 0;
            zRange = radius;
        }

        return java.util.stream.StreamSupport.stream(BlockPos.getAllInBox(
            offsetPos.add(-xRange, -yRange, -zRange),
            offsetPos.add(xRange, yRange, zRange)).spliterator(), false);
    }
}

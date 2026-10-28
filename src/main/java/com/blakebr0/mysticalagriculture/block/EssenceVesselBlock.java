package com.blakebr0.mysticalagriculture.block;

import com.blakebr0.cucumber.block.BaseTileEntityBlock;
import com.blakebr0.cucumber.helper.BlockHelper;
import com.blakebr0.cucumber.util.VoxelShapeBuilder;
import com.blakebr0.mysticalagriculture.tileentity.EssenceVesselTileEntity;
import io.github.xponer2.translator.util.Containers;

import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import io.github.xponer2.translator.util.ItemInteractionResult;
import io.github.xponer2.translator.shape.CollisionContext;
import io.github.xponer2.translator.shape.Shapes;
import io.github.xponer2.translator.shape.VoxelShape;

public class EssenceVesselBlock extends BaseTileEntityBlock {
    public static final VoxelShape VESSEL_SHAPE = VoxelShapeBuilder.fromShapes(
            Shapes.box(0.690625, 0, 0.1875, 0.815625, 0.125, 0.3125),
            Shapes.box(0.1875, 0, 0.1875, 0.3125, 0.125, 0.3125),
            Shapes.box(0.1875, 0, 0.6875, 0.3125, 0.125, 0.8125),
            Shapes.box(0.6875, 0, 0.6875, 0.8125, 0.125, 0.8125),
            Shapes.box(0.1875, 0.71875, 0.1875, 0.8125, 1.15625, 0.8125),
            Shapes.box(0.4375, 0.6875, 0.15625, 0.5625, 1.1875, 0.21875),
            Shapes.box(0.15625, 0.6875, 0.4375, 0.21875, 1.1875, 0.5625),
            Shapes.box(0.78125, 0.6875, 0.4375, 0.84375, 1.1875, 0.5625),
            Shapes.box(0.4375, 0.6875, 0.78125, 0.5625, 1.1875, 0.84375),
            Shapes.box(0.21875, 0.6875, 0.4375, 0.40625, 0.71875, 0.5625),
            Shapes.box(0.59375, 0.6875, 0.4375, 0.78125, 0.71875, 0.5625),
            Shapes.box(0.4375, 0.6875, 0.59375, 0.5625, 0.71875, 0.78125),
            Shapes.box(0.4375, 0.6875, 0.21875, 0.5625, 0.71875, 0.40625),
            Shapes.box(0.3125, 0.625, 0.3125, 0.6875, 0.6875, 0.6875),
            Shapes.box(0.375, 0.15625, 0.375, 0.625, 0.625, 0.625),
            Shapes.box(0.25, 0.125, 0.25, 0.75, 0.1875, 0.75)
    ).build();

    public EssenceVesselBlock() {
        super(SoundType.STONE, 10.0F, 12.0F, true);
    }

    @Override
    public TileEntity newBlockEntity(BlockPos pos, IBlockState state) {
        return new EssenceVesselTileEntity(pos, state);
    }

    protected ItemInteractionResult useItemOn(ItemStack stack, IBlockState state, World level, BlockPos pos, EntityPlayer player, EnumHand hand, RayTraceResult hitResult) {
        var tile = level.getTileEntity(pos);

        if (tile instanceof EssenceVesselTileEntity vessel) {
            var inventory = vessel.getInventory();
            var input = inventory.getStackInSlot(0);
            var held = player.getHeldItem(hand);

            var remaining = inventory.insertItem(0, held, false);
            if (held != remaining) {
                player.setHeldItem(hand, remaining);
                level.playSound(null, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 1.0F, 1.0F);
            } else if (!input.isEmpty()) {
                inventory.setStackInSlot(0, ItemStack.EMPTY);

                var item = new EntityItem(level, player.posX, player.posY, player.posZ, input);
                item.setNoPickupDelay();

                level.spawnEntity(item);
            }
        }

        return ItemInteractionResult.SUCCESS;
    }

    public void onRemove(IBlockState state, World level, BlockPos pos, IBlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            var tile = level.getTileEntity(pos);

            if (tile instanceof EssenceVesselTileEntity vessel) {
                Containers.dropContents(level, pos, vessel.getInventory().getStacks());
            }
        }

    }

    public VoxelShape getShape(IBlockState state, IBlockAccess level, BlockPos pos, CollisionContext context) {
        return VESSEL_SHAPE;
    }

    @Override
    public boolean hasComparatorInputOverride(IBlockState state) {
        return true;
    }

    @Override
    public int getComparatorInputOverride(IBlockState state, World level, BlockPos pos) {
        return BlockHelper.getRedstoneSignalFromInventory(level.getTileEntity(pos));
    }

    @Override
    @SuppressWarnings("deprecation")
    public net.minecraft.util.math.AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
        return io.github.xponer2.translator.shape.Shapes.selectionBox(this.getShape(state, world, pos, io.github.xponer2.translator.shape.CollisionContext.empty()));
    }

    @Override
    @SuppressWarnings("deprecation")
    public void addCollisionBoxToList(IBlockState state, net.minecraft.world.World world, BlockPos pos, net.minecraft.util.math.AxisAlignedBB entityBox,
            java.util.List<net.minecraft.util.math.AxisAlignedBB> list, @javax.annotation.Nullable net.minecraft.entity.Entity entity, boolean isActualState) {
        io.github.xponer2.translator.shape.Shapes.addCollisionBoxes(this.getShape(state, world, pos, io.github.xponer2.translator.shape.CollisionContext.of(entity)), pos, entityBox, list);
    }

    @Override
    public boolean onBlockActivated(net.minecraft.world.World world, BlockPos pos, IBlockState state, net.minecraft.entity.player.EntityPlayer player,
            net.minecraft.util.EnumHand hand, net.minecraft.util.EnumFacing facing, float hitX, float hitY, float hitZ) {
        var hit = io.github.xponer2.translator.util.Interactions.blockHit(pos, facing, hitX, hitY, hitZ);
        var itemResult = this.useItemOn(player.getHeldItem(hand), state, world, pos, player, hand, hit);
        var blockResult = itemResult == io.github.xponer2.translator.util.ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION && hand == net.minecraft.util.EnumHand.MAIN_HAND
                ? net.minecraft.util.EnumActionResult.PASS : net.minecraft.util.EnumActionResult.PASS;
        return io.github.xponer2.translator.util.Interactions.activated(itemResult, blockResult);
    }

    @Override
    public void breakBlock(net.minecraft.world.World world, BlockPos pos, IBlockState state) {
        this.onRemove(state, world, pos, net.minecraft.init.Blocks.AIR.getDefaultState(), false);
        super.breakBlock(world, pos, state);
    }
}

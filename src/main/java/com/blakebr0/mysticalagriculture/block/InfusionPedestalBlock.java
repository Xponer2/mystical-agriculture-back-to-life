package com.blakebr0.mysticalagriculture.block;

import com.blakebr0.cucumber.block.BaseTileEntityBlock;
import com.blakebr0.cucumber.helper.BlockHelper;
import com.blakebr0.cucumber.helper.StackHelper;
import com.blakebr0.cucumber.util.VoxelShapeBuilder;
import com.blakebr0.mysticalagriculture.tileentity.InfusionPedestalTileEntity;
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
import io.github.xponer2.translator.shape.VoxelShape;
import io.github.xponer2.translator.util.Platform;

public class InfusionPedestalBlock extends BaseTileEntityBlock {
    public static final VoxelShape PEDESTAL_SHAPE = VoxelShapeBuilder.builder()
            .cuboid(2.0, 0.0, 2.0, 5.0, 2.0, 5.0).cuboid(11.0, 0.0, 2.0, 14.0, 2.0, 5.0)
            .cuboid(2.0, 0.0, 11.0, 5.0, 2.0, 14.0).cuboid(11.0, 0.0, 11.0, 14.0, 2.0, 14.0)
            .cuboid(3.0, 2.0, 3.0, 13.0, 4.0, 13.0).cuboid(4.0, 4.0, 4.0, 12.0, 14.0, 12.0)
            .cuboid(3.0, 14.0, 3.0, 13.0, 16.0, 5.0).cuboid(3.0, 14.0, 11.0, 13.0, 16.0, 13.0)
            .cuboid(3.0, 14.0, 5.0, 5.0, 16.0, 11.0).cuboid(11.0, 14.0, 5.0, 13.0, 16.0, 11.0).build();

    public InfusionPedestalBlock() {
        super(SoundType.STONE, 10.0F, 12.0F, true);
    }

    @Override
    public TileEntity newBlockEntity(BlockPos pos, IBlockState state) {
        return new InfusionPedestalTileEntity(pos, state);
    }

    protected ItemInteractionResult useItemOn(ItemStack stack, IBlockState state, World level, BlockPos pos, EntityPlayer player, EnumHand hand, RayTraceResult hitResult) {
        var tile = level.getTileEntity(pos);

        if (tile instanceof InfusionPedestalTileEntity pedestal) {
            var inventory = pedestal.getInventory();
            var input = inventory.getStackInSlot(0);
            var held = player.getHeldItem(hand);

            if (input.isEmpty() && !held.isEmpty()) {
                inventory.setStackInSlot(0, StackHelper.withSize(held, 1, false));
                player.setHeldItem(hand, StackHelper.shrink(held, 1, false));
                level.playSound(null, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 1.0F, 1.0F);
            } else if (!input.isEmpty()) {
                inventory.setStackInSlot(0, ItemStack.EMPTY);

                var item = new EntityItem(level, player.posX, player.posY, player.posZ, input);

                item.setNoPickupDelay();
                Platform.addFreshEntity(level, item);
            }
        }

        return ItemInteractionResult.SUCCESS;
    }

    public void onRemove(IBlockState state, World level, BlockPos pos, IBlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            var tile = level.getTileEntity(pos);

            if (tile instanceof InfusionPedestalTileEntity altar) {
                Containers.dropContents(level, pos, altar.getInventory().getStacks());
            }
        }

    }

    public VoxelShape getShape(IBlockState state, IBlockAccess world, BlockPos pos, CollisionContext context) {
        return PEDESTAL_SHAPE;
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
    @SuppressWarnings("deprecation")
    public boolean isOpaqueCube(IBlockState state) {
        return io.github.xponer2.translator.shape.Shapes.isFullBlock(() -> this.getShape(state, null, null, io.github.xponer2.translator.shape.CollisionContext.empty()));
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isFullCube(IBlockState state) {
        return this.isOpaqueCube(state);
    }

    @Override
    @SuppressWarnings("deprecation")
    public net.minecraft.block.state.BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, net.minecraft.util.EnumFacing face) {
        return this.isOpaqueCube(state) ? net.minecraft.block.state.BlockFaceShape.SOLID : net.minecraft.block.state.BlockFaceShape.UNDEFINED;
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

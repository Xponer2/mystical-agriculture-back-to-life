package com.blakebr0.mysticalagriculture.block;

import com.blakebr0.cucumber.block.BaseTileEntityBlock;
import com.blakebr0.cucumber.helper.BlockHelper;
import com.blakebr0.cucumber.util.VoxelShapeBuilder;
import com.blakebr0.mysticalagriculture.tileentity.EnchanterTileEntity;
import io.github.xponer2.translator.menu.Menus;
import io.github.xponer2.translator.util.Containers;

import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import io.github.xponer2.translator.util.BlockPlaceContext;
import io.github.xponer2.translator.util.ItemInteractionResult;
import io.github.xponer2.translator.shape.CollisionContext;
import io.github.xponer2.translator.shape.Shapes;
import io.github.xponer2.translator.shape.VoxelShape;

public class EnchanterBlock extends BaseTileEntityBlock {
    public static final PropertyDirection FACING = BlockHorizontal.FACING;
    public static final VoxelShape ENCHANTER_SHAPE = VoxelShapeBuilder.fromShapes(
            Shapes.box16(1.0D, 8.0D, 1.0D, 15.0D, 16.0D, 15.0D),
            Shapes.box16(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D),
            Shapes.box16(4.0D, 2.0D, 4.0D, 12.0D, 12.0D, 12.0D)
    ).build();

    public EnchanterBlock() {
        super(SoundType.STONE, 3.5F, 3.5F, true);
        this.setDefaultState(this.getBlockState().getBaseState().withProperty(FACING, EnumFacing.NORTH));
    }

    @Override
    public TileEntity newBlockEntity(BlockPos pos, IBlockState state) {
        return new EnchanterTileEntity(pos, state);
    }

    protected ItemInteractionResult useItemOn(ItemStack stack, IBlockState state, World level, BlockPos pos, EntityPlayer player, EnumHand hand, RayTraceResult hitResult) {
        if (!level.isRemote) {
            var tile = level.getTileEntity(pos);

            if (tile instanceof EnchanterTileEntity enchanter) {
                Menus.openMenu(player, enchanter, pos);
            }
        }

        return ItemInteractionResult.SUCCESS;
    }

    public void onRemove(IBlockState state, World level, BlockPos pos, IBlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            var tile = level.getTileEntity(pos);

            if (tile instanceof EnchanterTileEntity enchanter) {
                Containers.dropContents(level, pos, enchanter.getInventory().getStacks());
            }
        }

    }

    public IBlockState getStateForPlacement(BlockPlaceContext context) {
        return this.getDefaultState().withProperty(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public IBlockState withRotation(IBlockState state, Rotation rot) {
        return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    public IBlockState withMirror(IBlockState state, Mirror mirror) {
        return state.withRotation(mirror.toRotation(state.getValue(FACING)));
    }

    public VoxelShape getShape(IBlockState state, IBlockAccess level, BlockPos pos, CollisionContext context) {
        return ENCHANTER_SHAPE;
    }

    @Override
    public boolean hasComparatorInputOverride(IBlockState state) {
        return true;
    }

    @Override
    public int getComparatorInputOverride(IBlockState state, World level, BlockPos pos) {
        return BlockHelper.getRedstoneSignalFromInventory(level.getTileEntity(pos));
    }

    protected void createBlockStateDefinition(BlockStateContainer.Builder builder) {
        builder.add(FACING);
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
    public IBlockState getStateForPlacement(net.minecraft.world.World world, BlockPos pos, net.minecraft.util.EnumFacing facing, float hitX, float hitY,
            float hitZ, int meta, net.minecraft.entity.EntityLivingBase placer, net.minecraft.util.EnumHand hand) {
        var state = this.getStateForPlacement(io.github.xponer2.translator.util.BlockPlaceContext.of(world, pos, facing, hitX, hitY, hitZ, placer, hand));
        return state == null ? this.getDefaultState() : state;
    }

    @Override
    public void breakBlock(net.minecraft.world.World world, BlockPos pos, IBlockState state) {
        this.onRemove(state, world, pos, net.minecraft.init.Blocks.AIR.getDefaultState(), false);
        super.breakBlock(world, pos, state);
    }

    @Override
    protected net.minecraft.block.state.BlockStateContainer createBlockState() {
        var builder = new net.minecraft.block.state.BlockStateContainer.Builder(this);
        this.createBlockStateDefinition(builder);
        return builder.build();
    }

    // 1.21 saves the whole state, 1.12.2 4 bits of metadata
    @Override
    public int getMetaFromState(net.minecraft.block.state.IBlockState state) {
        return io.github.xponer2.translator.block.BlockStates.toMeta(state);
    }

    @Override
    public net.minecraft.block.state.IBlockState getStateFromMeta(int meta) {
        return io.github.xponer2.translator.block.BlockStates.fromMeta(this, meta);
    }
}

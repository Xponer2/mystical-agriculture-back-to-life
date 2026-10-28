package com.blakebr0.mysticalagriculture.block;

import java.util.List;

import com.blakebr0.cucumber.block.BaseTileEntityBlock;
import com.blakebr0.cucumber.helper.BlockHelper;
import com.blakebr0.cucumber.helper.StackHelper;
import com.blakebr0.cucumber.util.VoxelShapeBuilder;
import com.blakebr0.mysticalagriculture.init.ModTileEntities;
import com.blakebr0.mysticalagriculture.item.WandItem;
import com.blakebr0.mysticalagriculture.lib.ModTooltips;
import com.blakebr0.mysticalagriculture.tileentity.InfusionAltarTileEntity;
import io.github.xponer2.translator.block.BlockEntityTicker;
import io.github.xponer2.translator.item.TooltipContext;
import io.github.xponer2.translator.util.Containers;

import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import io.github.xponer2.translator.registry.BlockEntityType;
import io.github.xponer2.translator.util.ItemInteractionResult;
import io.github.xponer2.translator.shape.CollisionContext;
import io.github.xponer2.translator.shape.VoxelShape;
import io.github.xponer2.translator.util.Platform;

public class InfusionAltarBlock extends BaseTileEntityBlock implements io.github.xponer2.translator.item.TooltipContext.HoverText {
    public static final VoxelShape ALTAR_SHAPE = VoxelShapeBuilder.builder()
            .cuboid(0, 0, 0, 16, 8, 16).cuboid(3, 13, 3, 13, 14, 13)
            .cuboid(6, 10, 6, 10, 11, 10).cuboid(5, 11, 5, 11, 13, 11)
            .cuboid(2, 13.5, 4, 3, 14.5, 12).cuboid(13, 13.5, 4, 14, 14.5, 12)
            .cuboid(4, 13.5, 2, 12, 14.5, 3).cuboid(4, 13.5, 13, 12, 14.5, 14)
            .cuboid(0.5, 8, 4, 4, 10, 12).cuboid(2, 8, 0.5, 14, 10, 4)
            .cuboid(12, 8, 4, 15.5, 10, 12).cuboid(2, 8, 12, 14, 10, 15.5)
            .cuboid(14, 8, 2, 15.5, 10, 4).cuboid(14, 8, 12, 15.5, 10, 14)
            .cuboid(0.5, 8, 12, 2, 10, 14).cuboid(0.5, 8, 2, 2, 10, 4)
            .cuboid(0.25, 8, 14, 2, 9, 15.75).cuboid(14, 8, 14, 15.75, 9, 15.75)
            .cuboid(14, 8, 0.25, 15.75, 9, 2).cuboid(0.25, 8, 0.25, 2, 9, 2)
            .cuboid(5, 8, 5, 11, 10, 11).build();

    public InfusionAltarBlock() {
        super(SoundType.STONE, 10.0F, 12.0F, true);
    }

    @Override
    public TileEntity newBlockEntity(BlockPos pos, IBlockState state) {
        return new InfusionAltarTileEntity(pos, state);
    }

    protected ItemInteractionResult useItemOn(ItemStack stack, IBlockState state, World level, BlockPos pos, EntityPlayer player, EnumHand hand, RayTraceResult hitResult) {
        var tile = level.getTileEntity(pos);

        if (tile instanceof InfusionAltarTileEntity altar) {
            var inventory = altar.getInventory();
            var input = inventory.getStackInSlot(0);
            var output = inventory.getStackInSlot(1);

            if (!output.isEmpty()) {
                var item = new EntityItem(level, player.posX, player.posY, player.posZ, output);

                item.setNoPickupDelay();
                Platform.addFreshEntity(level, item);
                inventory.setStackInSlot(1, ItemStack.EMPTY);
            } else {
                var held = player.getHeldItem(hand);
                if (input.isEmpty() && !held.isEmpty()) {
                    inventory.setStackInSlot(0, StackHelper.withSize(held, 1, false));
                    player.setHeldItem(hand, StackHelper.shrink(held, 1, false));
                    level.playSound(null, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 1.0F, 1.0F);
                } else if (!input.isEmpty()) {
                    if (held.getItem() instanceof WandItem)
                        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;

                    var item = new EntityItem(level, player.posX, player.posY, player.posZ, input);

                    item.setNoPickupDelay();
                    Platform.addFreshEntity(level, item);
                    inventory.setStackInSlot(0, ItemStack.EMPTY);
                }
            }
        }

        return ItemInteractionResult.SUCCESS;
    }

    public void onRemove(IBlockState state, World level, BlockPos pos, IBlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            var tile = level.getTileEntity(pos);

            if (tile instanceof InfusionAltarTileEntity altar) {
                Containers.dropContents(level, pos, altar.getInventory().getStacks());
            }
        }

    }

    public VoxelShape getShape(IBlockState state, IBlockAccess world, BlockPos post, CollisionContext context) {
        return ALTAR_SHAPE;
    }

    public void appendHoverText(ItemStack stack, TooltipContext context, List<ITextComponent> tooltip, ITooltipFlag flag) {
        tooltip.add(ModTooltips.ACTIVATE_WITH_REDSTONE.build());
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
    protected <T extends TileEntity> BlockEntityTicker<T> getServerTicker(World level, IBlockState state, BlockEntityType<T> type) {
        return createTicker(type, ModTileEntities.INFUSION_ALTAR.get(), InfusionAltarTileEntity::tick);
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

    @Override
    @net.minecraftforge.fml.relauncher.SideOnly(net.minecraftforge.fml.relauncher.Side.CLIENT)
    public void addInformation(ItemStack stack, @javax.annotation.Nullable net.minecraft.world.World world, java.util.List<String> tooltip, ITooltipFlag flag) {
        io.github.xponer2.translator.item.TooltipContext.addInformation(this, stack, world, tooltip, flag);
    }
}

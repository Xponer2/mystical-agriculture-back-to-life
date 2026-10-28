package com.blakebr0.mysticalagriculture.block;

import java.util.List;

import com.blakebr0.cucumber.block.BaseTileEntityBlock;
import com.blakebr0.cucumber.helper.BlockHelper;
import com.blakebr0.cucumber.lib.Tooltips;
import com.blakebr0.cucumber.util.Formatting;
import com.blakebr0.mysticalagriculture.init.ModTileEntities;
import com.blakebr0.mysticalagriculture.lib.ModTooltips;
import com.blakebr0.mysticalagriculture.tileentity.EssenceFurnaceTileEntity;
import io.github.xponer2.translator.block.BlockEntityTicker;
import io.github.xponer2.translator.item.TooltipContext;
import io.github.xponer2.translator.menu.Menus;
import io.github.xponer2.translator.util.Containers;

import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import io.github.xponer2.translator.registry.BlockEntityType;
import io.github.xponer2.translator.util.BlockPlaceContext;
import io.github.xponer2.translator.util.ItemInteractionResult;

public class EssenceFurnaceBlock extends BaseTileEntityBlock implements io.github.xponer2.translator.item.TooltipContext.HoverText {
    public static final PropertyDirection FACING = BlockHorizontal.FACING;
    public static final PropertyBool RUNNING = PropertyBool.create("running");

    public EssenceFurnaceBlock() {
        super(SoundType.METAL, 3.5F, 3.5F, true);
        this.setDefaultState(this.getBlockState().getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(RUNNING, false));
    }

    @Override
    public TileEntity newBlockEntity(BlockPos pos, IBlockState state) {
        return new EssenceFurnaceTileEntity(pos, state);
    }

    protected ItemInteractionResult useItemOn(ItemStack stack, IBlockState state, World level, BlockPos pos, EntityPlayer player, EnumHand hand, RayTraceResult hitResult) {
        if (!level.isRemote) {
            var tile = level.getTileEntity(pos);

            if (tile instanceof EssenceFurnaceTileEntity furnace) {
                Menus.openMenu(player, furnace, pos);
            }
        }

        return ItemInteractionResult.SUCCESS;
    }

    @Override
    public void onBlockPlacedBy(World level, BlockPos pos, IBlockState state, EntityLivingBase entity, ItemStack stack) { }

    public void onRemove(IBlockState state, World level, BlockPos pos, IBlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            var tile = level.getTileEntity(pos);

            if (tile instanceof EssenceFurnaceTileEntity furnace) {
                Containers.dropContents(level, pos, furnace.getInventory().getStacks());
                Containers.dropContents(level, pos, furnace.getUpgradeInventory().getStacks());
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

    public void appendHoverText(ItemStack stack, TooltipContext context, List<ITextComponent> tooltip, ITooltipFlag flag) {
        if (GuiScreen.isShiftKeyDown()) {
            var speed = Formatting.number(EssenceFurnaceTileEntity.OPERATION_TIME).withStyle(TextFormatting.WHITE);
            var fuelRate = Formatting.number(EssenceFurnaceTileEntity.FUEL_USAGE).withStyle(TextFormatting.WHITE);
            var fuelCapacity = Formatting.number(EssenceFurnaceTileEntity.FUEL_CAPACITY).withStyle(TextFormatting.WHITE);

            tooltip.add(ModTooltips.MACHINE_SPEED.args(speed).build());
            tooltip.add(ModTooltips.MACHINE_FUEL_RATE.args(fuelRate).build());
            tooltip.add(ModTooltips.MACHINE_FUEL_CAPACITY.args(fuelCapacity).build());
        } else {
            tooltip.add(Tooltips.HOLD_SHIFT_FOR_INFO.build());
        }
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
        builder.add(FACING, RUNNING);
    }

    @Override
    protected <T extends TileEntity> BlockEntityTicker<T> getServerTicker(World level, IBlockState state, BlockEntityType<T> type) {
        return createTicker(type, ModTileEntities.FURNACE.get(), EssenceFurnaceTileEntity::tick);
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

    @Override
    @net.minecraftforge.fml.relauncher.SideOnly(net.minecraftforge.fml.relauncher.Side.CLIENT)
    public void addInformation(ItemStack stack, @javax.annotation.Nullable net.minecraft.world.World world, java.util.List<String> tooltip, ITooltipFlag flag) {
        io.github.xponer2.translator.item.TooltipContext.addInformation(this, stack, world, tooltip, flag);
    }
}

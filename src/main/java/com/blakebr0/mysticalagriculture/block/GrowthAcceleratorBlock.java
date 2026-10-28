package com.blakebr0.mysticalagriculture.block;

import java.util.List;
import java.util.Random;

import com.blakebr0.cucumber.block.BaseBlock;
import com.blakebr0.mysticalagriculture.config.ModConfigs;
import com.blakebr0.mysticalagriculture.lib.ModTooltips;
import io.github.xponer2.translator.item.TooltipContext;
import io.github.xponer2.translator.text.LiteralComponent;

import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import io.github.xponer2.translator.text.MutableComponent;

public class GrowthAcceleratorBlock extends BaseBlock implements io.github.xponer2.translator.item.TooltipContext.HoverText {
    private final int range;
    private final TextFormatting textColor;

    public GrowthAcceleratorBlock(int range, TextFormatting textColor) {
        super(SoundType.STONE, 5.0F, 8.0F, true);
        this.range = range;
        this.textColor = textColor;
    }

    @Override
    public void onBlockAdded(World level, BlockPos pos, IBlockState state) {
        IBlockState oldState = net.minecraft.init.Blocks.AIR.getDefaultState();
        boolean isMoving = false;
        level.scheduleUpdate(pos, this, getTickRate());
    }

    @Override
    public void updateTick(World levelWorld, BlockPos pos, IBlockState state, Random random) {
        WorldServer level = (WorldServer) levelWorld;
        java.util.stream.StreamSupport.stream(BlockPos.getAllInBox(pos.up(2), pos.add(0, this.range + 2, 0)).spliterator(), false)
                .filter(aoePos -> level.getBlockState(aoePos).getBlock() instanceof IGrowable)
                .findFirst()
                .ifPresent(aoePos -> level.getBlockState(aoePos).getBlock().randomTick(level, aoePos, level.getBlockState(aoePos), random));

        level.scheduleUpdate(pos, this, getTickRate());
    }

    public void appendHoverText(ItemStack stack, TooltipContext context, List<ITextComponent> tooltip, ITooltipFlag flag) {
        tooltip.add(ModTooltips.GROWTH_ACCELERATOR.build());

        MutableComponent rangeNumber = new LiteralComponent(String.valueOf(this.range)).withStyle(this.textColor);

        tooltip.add(ModTooltips.GROWTH_ACCELERATOR_RANGE.args(rangeNumber).build());
    }

    private static int getTickRate() {
        double variance = Math.random() * (1.1 - 0.9) + 0.9;
        return (int) (ModConfigs.GROWTH_ACCELERATOR_COOLDOWN.get() * variance) * 20;
    }

    @Override
    @net.minecraftforge.fml.relauncher.SideOnly(net.minecraftforge.fml.relauncher.Side.CLIENT)
    public void addInformation(ItemStack stack, @javax.annotation.Nullable net.minecraft.world.World world, java.util.List<String> tooltip, ITooltipFlag flag) {
        io.github.xponer2.translator.item.TooltipContext.addInformation(this, stack, world, tooltip, flag);
    }
}

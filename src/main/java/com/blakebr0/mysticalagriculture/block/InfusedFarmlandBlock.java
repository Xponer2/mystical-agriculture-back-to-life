package com.blakebr0.mysticalagriculture.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.github.xponer2.translator.block.LootContextParams;
import io.github.xponer2.translator.block.LootParams;
import io.github.xponer2.translator.item.TooltipContext;
import io.github.xponer2.translator.registry.RegistryAccess;
import com.blakebr0.cucumber.iface.IColored;
import com.blakebr0.mysticalagriculture.api.crop.CropTier;
import com.blakebr0.mysticalagriculture.api.farmland.IEssenceFarmland;
import com.blakebr0.mysticalagriculture.lib.ModTooltips;

import net.minecraft.block.BlockFarmland;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import io.github.xponer2.translator.util.Registries;
import net.minecraft.util.DamageSource;
import io.github.xponer2.translator.registry.Holder;

public class InfusedFarmlandBlock extends BlockFarmland implements IColored, IEssenceFarmland, TooltipContext.HoverText {
    public static final List<InfusedFarmlandBlock> FARMLANDS = new ArrayList<>();
    private final CropTier tier;

    public InfusedFarmlandBlock(CropTier tier) {
        super();
        this.tier = tier;
        this.setHardness(0.6F);
        this.setSoundType(net.minecraft.block.SoundType.GROUND);

        FARMLANDS.add(this);
    }

    @Override
    public void onFallenUpon(World level, BlockPos pos, Entity entity, float fallDistance) {
        IBlockState state = level.getBlockState(pos);
        entity.fall(fallDistance, 1.0F);
    }

    @Override
    public boolean isFertile(World levelWorld, BlockPos pos) {
        IBlockAccess level = levelWorld;
        IBlockState state = levelWorld.getBlockState(pos);
        return state.getValue(MOISTURE) > 0;
    }

    @Override
    public void randomTick(World levelWorld, BlockPos pos, IBlockState state, Random random) {
        WorldServer level = (WorldServer) levelWorld;
        int moisture = state.getValue(MOISTURE);

        if (!hasWater(level, pos) && !level.isRainingAt(pos.up())) {
            if (moisture > 0) {
                level.setBlockState(pos, state.withProperty(MOISTURE, moisture - 1), 2);
            }
        } else if (moisture < 7) {
            level.setBlockState(pos, state.withProperty(MOISTURE, 7), 2);
        }
    }

    // BlockFarmland#hasWater is private in 1.12.2
    private static boolean hasWater(World level, BlockPos pos) {
        for (var check : BlockPos.getAllInBoxMutable(pos.add(-4, 0, -4), pos.add(4, 1, 4))) {
            if (level.getBlockState(check).getMaterial() == net.minecraft.block.material.Material.WATER) {
                return true;
            }
        }

        return false;
    }

    public List<ItemStack> getDrops(IBlockState state, LootParams.Builder builder) {
        List<ItemStack> drops = new ArrayList<>();
        var stack = builder.getOptionalParameter(LootContextParams.TOOL);
        var silkTouch = Holder.direct(Enchantments.SILK_TOUCH);

        if (stack != null && EnchantmentHelper.getEnchantmentLevel(silkTouch.value(), stack) > 0) {
            drops.add(new ItemStack(this));
        } else {
            drops.add(new ItemStack(Blocks.DIRT));

            var random = builder.getLevel().rand;
            if (random.nextInt(100) < 25)
                drops.add(new ItemStack(this.tier.getEssence(), 1));
        }

        return drops;
    }

    public void appendHoverText(ItemStack stack, TooltipContext context, List<ITextComponent> tooltip, ITooltipFlag flag) {
        tooltip.add(ModTooltips.TIER.args(this.tier.getDisplayName()).build());
    }

    @Override
    @net.minecraftforge.fml.relauncher.SideOnly(net.minecraftforge.fml.relauncher.Side.CLIENT)
    public void addInformation(ItemStack stack, @javax.annotation.Nullable World world, List<String> tooltip, ITooltipFlag flag) {
        TooltipContext.addInformation(this, stack, world, tooltip, flag);
    }

    @Override
    public int getColor(int index) {
        return this.tier.getColor();
    }

    @Override
    public CropTier getTier() {
        return this.tier;
    }

    @Override
    public void getDrops(net.minecraft.util.NonNullList<ItemStack> drops, net.minecraft.world.IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        drops.addAll(this.getDrops(state, new io.github.xponer2.translator.block.LootParams.Builder(world, pos, this.harvesters.get(), state)));
    }
}

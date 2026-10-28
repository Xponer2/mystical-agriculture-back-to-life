package com.blakebr0.mysticalagriculture.compat;

import java.util.function.Function;

import io.github.xponer2.translator.text.LiteralComponent;
import io.github.xponer2.translator.text.TranslatableComponent;
import com.blakebr0.mysticalagriculture.MysticalAgriculture;
import com.blakebr0.mysticalagriculture.api.crop.ICropProvider;
import com.blakebr0.mysticalagriculture.api.farmland.IEssenceFarmland;
import com.blakebr0.mysticalagriculture.block.InferiumCropBlock;
import com.blakebr0.mysticalagriculture.config.ModConfigs;
import com.blakebr0.mysticalagriculture.lib.ModCrops;
import com.blakebr0.mysticalagriculture.lib.ModTooltips;
import com.blakebr0.mysticalagriculture.tileentity.EssenceVesselTileEntity;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.ITheOneProbe;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraftforge.fml.common.event.FMLInterModComms;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import io.github.xponer2.translator.text.MutableComponent;

public class TOPCompat implements Function<ITheOneProbe, Void> {
    @Override
    public Void apply(ITheOneProbe probe) {
        probe.registerProvider(new IProbeInfoProvider() {
            @Override
            public String getID() {
                return MysticalAgriculture.resource("crops").toString();
            }

            @Override
            public void addProbeInfo(ProbeMode mode, IProbeInfo info, EntityPlayer player, World level, IBlockState state, IProbeHitData data) {
                var block = state.getBlock();
                var pos = data.getPos();

                if (block instanceof ICropProvider provider) {
                    var crop = provider.getMysticalCrop();
                    var belowBlock = level.getBlockState(pos.down()).getBlock();

                    info.text(ModTooltips.TIER.args(crop.getTier().getDisplayName()).build().getFormattedText());

                    if (ModConfigs.REQUIRES_EFFECTIVE_FARMLAND.get() && crop != ModCrops.INFERIUM) {
                        var farmland = crop.getTier().getFarmland();
                        if (farmland != null) {
                            info.text(ModTooltips.REQUIRES_EFFECTIVE_FARMLAND.args(new TranslatableComponent(farmland.getTranslationKey() + ".name")).build().getFormattedText());
                        }
                    }

                    if (ModConfigs.SECONDARY_SEED_DROPS.get()) {
                        double secondaryChance = crop.getSecondaryChance(belowBlock);
                        if (secondaryChance > 0) {
                            MutableComponent chanceText = new LiteralComponent(String.valueOf((int) (secondaryChance * 100)))
                                    .append("%")
                                    .withStyle(crop.getTier().getTextColor());

                            info.text(ModTooltips.SECONDARY_CHANCE.args(chanceText).build().getFormattedText());
                        }
                    }

                    var crux = crop.getCruxBlock();
                    if (crux != null) {
                        var stack = new ItemStack(crux);

                        info.text(ModTooltips.REQUIRES_CRUX.args(stack.getDisplayName()).build().getFormattedText());
                    }

                    var biomes = crop.getRequiredBiomes();
                    if (!biomes.isEmpty()) {
                        var biome = level.getBiome(pos).getRegistryName();

                        if (biome != null && !biomes.contains(biome)) {
                            info.text(ModTooltips.INVALID_BIOME.color(TextFormatting.RED).build().getFormattedText());
                        }
                    }

                    if (block instanceof InferiumCropBlock) {
                        int output = 100;
                        if (belowBlock instanceof IEssenceFarmland farmland) {
                            int tier = farmland.getTier().getValue();
                            output = (tier * 50) + 50;
                        }

                        MutableComponent inferiumOutputText = new LiteralComponent(String.valueOf(output))
                                .append("%")
                                .withStyle(crop.getTier().getTextColor());

                        info.text(ModTooltips.INFERIUM_OUTPUT.args(inferiumOutputText).build().getFormattedText());
                    }
                }

                if (block instanceof IEssenceFarmland farmland) {
                    info.text(ModTooltips.TIER.args(farmland.getTier().getDisplayName()).build().getFormattedText());
                }

                var tile = level.getTileEntity(pos);

                if (tile instanceof EssenceVesselTileEntity vessel) {
                    var stack = vessel.getInventory().getStackInSlot(0);

                    if (!stack.isEmpty()) {
                        info.text(String.format("%sx %s", stack.getCount(), stack.getDisplayName()));
                    }
                }
            }
        });

        return null;
    }

    public static void onInterModEnqueue() {
        FMLInterModComms.sendFunctionMessage("theoneprobe", "getTheOneProbe", TOPCompat.class.getName());
    }
}

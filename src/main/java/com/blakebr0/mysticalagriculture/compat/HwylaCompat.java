package com.blakebr0.mysticalagriculture.compat;

import io.github.xponer2.translator.text.LiteralComponent;
import io.github.xponer2.translator.text.MutableComponent;
import io.github.xponer2.translator.text.TranslatableComponent;
import com.blakebr0.mysticalagriculture.api.crop.ICropProvider;
import com.blakebr0.mysticalagriculture.api.farmland.IEssenceFarmland;
import com.blakebr0.mysticalagriculture.block.InferiumCropBlock;
import com.blakebr0.mysticalagriculture.block.InfusedFarmlandBlock;
import com.blakebr0.mysticalagriculture.block.MysticalCropBlock;
import com.blakebr0.mysticalagriculture.config.ModConfigs;
import com.blakebr0.mysticalagriculture.lib.ModCrops;
import com.blakebr0.mysticalagriculture.lib.ModTooltips;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.IWailaRegistrar;
import mcp.mobius.waila.api.WailaPlugin;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;

import java.util.List;

// Hwyla 1.8 takes the place of Jade: body providers per block class, lines are strings formatted on the client
@WailaPlugin
public class HwylaCompat implements IWailaPlugin {
    @Override
    public void register(IWailaRegistrar registrar) {
        registrar.registerBodyProvider(new IWailaDataProvider() {
            @Override
            public List<String> getWailaBody(ItemStack itemStack, List<String> tooltip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
                var block = accessor.getBlock();
                var crop = ((ICropProvider) block).getMysticalCrop();

                tooltip.add(ModTooltips.TIER.args(crop.getTier().getDisplayName()).build().getFormattedText());

                var pos = accessor.getPosition();
                var downPos = pos.down();
                var level = accessor.getWorld();
                var belowBlock = level.getBlockState(downPos).getBlock();

                if (ModConfigs.REQUIRES_EFFECTIVE_FARMLAND.get() && crop != ModCrops.INFERIUM) {
                    var farmland = crop.getTier().getFarmland();
                    if (farmland != null) {
                        var name = new TranslatableComponent(farmland.getTranslationKey() + ".name").withStyle(crop.getTier().getTextColor());
                        tooltip.add(ModTooltips.REQUIRES_EFFECTIVE_FARMLAND.args(name).build().getFormattedText());
                    }
                }

                if (ModConfigs.SECONDARY_SEED_DROPS.get()) {
                    var secondaryChance = crop.getSecondaryChance(belowBlock);
                    if (secondaryChance > 0) {
                        MutableComponent chanceText = new LiteralComponent(String.valueOf((int) (secondaryChance * 100)))
                                .append("%")
                                .withStyle(crop.getTier().getTextColor());

                        tooltip.add(ModTooltips.SECONDARY_CHANCE.args(chanceText).build().getFormattedText());
                    }
                }

                var crux = crop.getCruxBlock();
                if (crux != null) {
                    var stack = new ItemStack(crux);
                    tooltip.add(ModTooltips.REQUIRES_CRUX.args(stack.getDisplayName()).build().getFormattedText());
                }

                var biomes = crop.getRequiredBiomes();
                if (!biomes.isEmpty()) {
                    var biome = level.getBiome(pos).getRegistryName();
                    if (biome != null && !biomes.contains(biome)) {
                        tooltip.add(ModTooltips.INVALID_BIOME.color(TextFormatting.RED).build().getFormattedText());
                    }
                }

                return tooltip;
            }
        }, MysticalCropBlock.class);

        registrar.registerBodyProvider(new IWailaDataProvider() {
            @Override
            public List<String> getWailaBody(ItemStack itemStack, List<String> tooltip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
                var block = accessor.getBlock();
                var crop = ((ICropProvider) block).getMysticalCrop();
                var downPos = accessor.getPosition().down();
                var belowBlock = accessor.getWorld().getBlockState(downPos).getBlock();

                int output = 100;
                if (belowBlock instanceof IEssenceFarmland farmland) {
                    int tier = farmland.getTier().getValue();
                    output = (tier * 50) + 50;
                }

                MutableComponent inferiumOutputText = new LiteralComponent(String.valueOf(output)).append("%").withStyle(crop.getTier().getTextColor());

                tooltip.add(ModTooltips.INFERIUM_OUTPUT.args(inferiumOutputText).build().getFormattedText());

                return tooltip;
            }
        }, InferiumCropBlock.class);

        registrar.registerBodyProvider(new IWailaDataProvider() {
            @Override
            public List<String> getWailaBody(ItemStack itemStack, List<String> tooltip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
                var farmland = (IEssenceFarmland) accessor.getBlock();

                tooltip.add(ModTooltips.TIER.args(farmland.getTier().getDisplayName()).build().getFormattedText());

                return tooltip;
            }
        }, InfusedFarmlandBlock.class);
    }
}

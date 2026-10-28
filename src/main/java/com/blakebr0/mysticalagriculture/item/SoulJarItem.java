package com.blakebr0.mysticalagriculture.item;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;

import com.blakebr0.cucumber.item.BaseItem;
import com.blakebr0.mysticalagriculture.api.util.MobSoulUtils;
import com.blakebr0.mysticalagriculture.lib.ModTooltips;
import io.github.xponer2.translator.item.ItemPropertyFunction;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import io.github.xponer2.translator.item.TooltipContext;

public class SoulJarItem extends BaseItem implements io.github.xponer2.translator.item.TooltipContext.HoverText {
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.00");

    static {
        DECIMAL_FORMAT.setRoundingMode(RoundingMode.DOWN);
    }

    public SoulJarItem() {
        super(p -> p.stacksTo(1));
    }

    public void appendHoverText(ItemStack stack, TooltipContext context, List<ITextComponent> tooltip, ITooltipFlag flag) {
        var type = MobSoulUtils.getType(stack);

        if (type != null) {
            var entityName = type.getEntityDisplayName();
            var souls = DECIMAL_FORMAT.format(MobSoulUtils.getSouls(stack));
            var requirement = DECIMAL_FORMAT.format(type.getSoulRequirement());

            tooltip.add(ModTooltips.SOUL_JAR.args(entityName, souls, requirement).build());

            if (flag.isAdvanced()) {
                tooltip.add(ModTooltips.MST_ID.args(type.getId().toString()).color(TextFormatting.DARK_GRAY).build());
            }
        }
    }

    public static ItemPropertyFunction getFillPropertyGetter() {
        return (stack, world, entity, _unused) -> {
            var type = MobSoulUtils.getType(stack);

            if (type != null) {
                double souls = MobSoulUtils.getSouls(stack);

                if (souls > 0) {
                    return (int) ((souls / type.getSoulRequirement()) * 9);
                }
            }

            return 0;
        };
    }

    @Override
    @net.minecraftforge.fml.relauncher.SideOnly(net.minecraftforge.fml.relauncher.Side.CLIENT)
    public void addInformation(ItemStack stack, @javax.annotation.Nullable net.minecraft.world.World world, java.util.List<String> tooltip, ITooltipFlag flag) {
        io.github.xponer2.translator.item.TooltipContext.addInformation(this, stack, world, tooltip, flag);
    }
}

package com.enderjhess.arcanelegacy.spell;

import java.util.List;

import com.enderjhess.arcanelegacy.ArcaneLegacy;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

/** Item de uma runa. Colocado na Mesa de Inscrição para montar um feitiço. */
public class RuneItem extends Item {
    private final Rune rune;

    public RuneItem(Rune rune, Properties properties) {
        super(properties);
        this.rune = rune;
    }

    public Rune rune() {
        return rune;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        String typeKey = "rune_type." + ArcaneLegacy.MODID + "." + rune.type().name().toLowerCase(java.util.Locale.ROOT);
        tooltip.add(Component.translatable(typeKey).withStyle(ChatFormatting.DARK_PURPLE));
        if (rune.type() == RuneType.MODIFIER) {
            tooltip.add(Component.translatable("tooltip." + ArcaneLegacy.MODID + ".rune_multiplier",
                    String.valueOf(rune.costMultiplier())).withStyle(ChatFormatting.GRAY));
        } else {
            tooltip.add(Component.translatable("tooltip." + ArcaneLegacy.MODID + ".rune_cost", rune.baseCost())
                    .withStyle(ChatFormatting.GRAY));
        }
    }
}

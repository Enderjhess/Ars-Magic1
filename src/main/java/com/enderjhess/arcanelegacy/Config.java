package com.enderjhess.arcanelegacy;

import net.neoforged.neoforge.common.ModConfigSpec;

public final class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.DoubleValue MAX_MANA = BUILDER
            .comment("Mana máxima de cada jogador / Maximum mana per player")
            .defineInRange("maxMana", 100.0, 1.0, 100000.0);

    public static final ModConfigSpec.DoubleValue MANA_REGEN_PER_SECOND = BUILDER
            .comment("Mana recuperada por segundo / Mana regenerated per second")
            .defineInRange("manaRegenPerSecond", 1.0, 0.0, 10000.0);

    static final ModConfigSpec SPEC = BUILDER.build();

    private Config() {
    }
}

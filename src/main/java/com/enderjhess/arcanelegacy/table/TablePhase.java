package com.enderjhess.arcanelegacy.table;

import java.util.Locale;

import net.minecraft.util.StringRepresentable;

/** Os três estados visuais da Mesa de Inscrição. */
public enum TablePhase implements StringRepresentable {
    /** Sem runas. */
    IDLE(0),
    /** Com runas, mas o feitiço ainda não é válido: partículas. */
    CRAFTING(7),
    /** Feitiço válido, pronto para gravar: feixe de luz. */
    READY(12);

    private final int lightLevel;

    TablePhase(int lightLevel) {
        this.lightLevel = lightLevel;
    }

    public int lightLevel() {
        return lightLevel;
    }

    @Override
    public String getSerializedName() {
        return name().toLowerCase(Locale.ROOT);
    }
}

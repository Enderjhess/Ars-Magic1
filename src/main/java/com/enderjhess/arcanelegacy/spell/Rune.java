package com.enderjhess.arcanelegacy.spell;

/**
 * Runas da primeira versão. Nomes e valores são provisórios (ver o documento de design).
 * Formas e efeitos somam {@code baseCost}; modificadores multiplicam o custo por {@code costMultiplier}.
 * TODO: trocar por um registro próprio (NewRegistryEvent) quando os efeitos forem implementados.
 */
public enum Rune {
    PROJECTILE("projectile", RuneType.SHAPE, 2, 1.0f),
    TOUCH("touch", RuneType.SHAPE, 0, 1.0f),
    SELF("self", RuneType.SHAPE, 0, 1.0f),
    DAMAGE("damage", RuneType.EFFECT, 8, 1.0f),
    HEAL("heal", RuneType.EFFECT, 10, 1.0f),
    FIRE("fire", RuneType.EFFECT, 6, 1.0f),
    DIG("dig", RuneType.EFFECT, 5, 1.0f),
    POWER("power", RuneType.MODIFIER, 0, 1.5f),
    RANGE("range", RuneType.MODIFIER, 0, 1.25f);

    private final String id;
    private final RuneType type;
    private final int baseCost;
    private final float costMultiplier;

    Rune(String id, RuneType type, int baseCost, float costMultiplier) {
        this.id = id;
        this.type = type;
        this.baseCost = baseCost;
        this.costMultiplier = costMultiplier;
    }

    public String id() {
        return id;
    }

    public RuneType type() {
        return type;
    }

    public int baseCost() {
        return baseCost;
    }

    public float costMultiplier() {
        return costMultiplier;
    }
}

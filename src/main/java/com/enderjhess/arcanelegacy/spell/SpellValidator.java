package com.enderjhess.arcanelegacy.spell;

import java.util.List;

/**
 * Confere a ordem das runas (forma → efeitos → modificadores) e calcula o custo de mana.
 * Não depende do Minecraft, para poder ser testado sem abrir o jogo.
 */
public final class SpellValidator {
    public static final int MAX_RUNES = 6;

    private SpellValidator() {
    }

    public enum Error {
        EMPTY("empty"),
        SHAPE_FIRST("shape_first"),
        ONE_SHAPE("one_shape"),
        NEEDS_EFFECT("needs_effect"),
        EFFECT_AFTER_MODIFIER("effect_after_modifier"),
        TOO_MANY("too_many");

        private final String key;

        Error(String key) {
            this.key = key;
        }

        /** Sufixo usado na chave de tradução {@code gui.<modid>.inscription_table.error.<key>}. */
        public String key() {
            return key;
        }
    }

    /** Resultado: {@code error} é nulo quando o feitiço é válido. */
    public record Result(Error error, int manaCost) {
        public boolean valid() {
            return error == null;
        }

        static Result fail(Error error) {
            return new Result(error, 0);
        }
    }

    /** @param runes runas na ordem dos espaços da mesa, já sem os espaços vazios */
    public static Result validate(List<Rune> runes) {
        if (runes.isEmpty()) {
            return Result.fail(Error.EMPTY);
        }
        if (runes.size() > MAX_RUNES) {
            return Result.fail(Error.TOO_MANY);
        }
        if (runes.get(0).type() != RuneType.SHAPE) {
            return Result.fail(Error.SHAPE_FIRST);
        }

        int effects = 0;
        boolean seenModifier = false;
        float cost = runes.get(0).baseCost();
        float multiplier = 1.0f;

        for (int i = 1; i < runes.size(); i++) {
            Rune rune = runes.get(i);
            switch (rune.type()) {
                case SHAPE -> {
                    return Result.fail(Error.ONE_SHAPE);
                }
                case EFFECT -> {
                    if (seenModifier) {
                        return Result.fail(Error.EFFECT_AFTER_MODIFIER);
                    }
                    effects++;
                    cost += rune.baseCost();
                }
                case MODIFIER -> {
                    seenModifier = true;
                    multiplier *= rune.costMultiplier();
                }
            }
        }

        if (effects == 0) {
            return Result.fail(Error.NEEDS_EFFECT);
        }
        return new Result(null, Math.round(cost * multiplier));
    }
}

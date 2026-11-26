package com.enderjhess.arcanelegacy.spell;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

class SpellValidatorTest {
    private static void assertError(SpellValidator.Error expected, Rune... runes) {
        assertEquals(expected, SpellValidator.validate(List.of(runes)).error());
    }

    @Test
    void projectileDamageCostsTen() {
        SpellValidator.Result result = SpellValidator.validate(List.of(Rune.PROJECTILE, Rune.DAMAGE));
        assertTrue(result.valid());
        assertEquals(10, result.manaCost());
    }

    @Test
    void powerMultipliesCost() {
        assertEquals(15, SpellValidator.validate(List.of(Rune.PROJECTILE, Rune.DAMAGE, Rune.POWER)).manaCost());
    }

    @Test
    void modifiersStack() {
        // (0 + 10 + 6) * 1.5 * 1.25 = 30
        assertEquals(30, SpellValidator.validate(List.of(Rune.SELF, Rune.HEAL, Rune.FIRE, Rune.POWER, Rune.RANGE)).manaCost());
    }

    @Test
    void rejectsBadOrders() {
        assertError(SpellValidator.Error.EMPTY);
        assertError(SpellValidator.Error.SHAPE_FIRST, Rune.DAMAGE, Rune.PROJECTILE);
        assertError(SpellValidator.Error.ONE_SHAPE, Rune.PROJECTILE, Rune.TOUCH, Rune.DAMAGE);
        assertError(SpellValidator.Error.NEEDS_EFFECT, Rune.TOUCH);
        assertError(SpellValidator.Error.NEEDS_EFFECT, Rune.TOUCH, Rune.POWER);
        assertError(SpellValidator.Error.EFFECT_AFTER_MODIFIER, Rune.TOUCH, Rune.DIG, Rune.POWER, Rune.FIRE);
        assertError(SpellValidator.Error.TOO_MANY, Rune.TOUCH, Rune.DIG, Rune.DIG, Rune.DIG, Rune.DIG, Rune.DIG, Rune.DIG);
    }
}

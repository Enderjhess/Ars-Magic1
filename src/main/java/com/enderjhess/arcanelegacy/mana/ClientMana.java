package com.enderjhess.arcanelegacy.mana;

/** Cópia da mana do jogador local, recebida do servidor. Sem classes do cliente, para ser segura nos dois lados. */
public final class ClientMana {
    private static float mana;
    private static float maxMana;

    private ClientMana() {
    }

    static void set(float mana, float maxMana) {
        ClientMana.mana = mana;
        ClientMana.maxMana = maxMana;
    }

    public static float mana() {
        return mana;
    }

    public static float maxMana() {
        return maxMana;
    }
}

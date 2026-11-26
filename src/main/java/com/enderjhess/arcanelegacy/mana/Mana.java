package com.enderjhess.arcanelegacy.mana;

import com.enderjhess.arcanelegacy.Config;
import com.enderjhess.arcanelegacy.registry.ModRegistries;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;

/** Leitura e escrita da mana de um jogador (lado do servidor). */
public final class Mana {
    private Mana() {
    }

    public static float getMax(Player player) {
        // Fixa na primeira versão; depois vai crescer com nível ou equipamento.
        return Config.MAX_MANA.get().floatValue();
    }

    public static float get(Player player) {
        return Math.min(player.getData(ModRegistries.MANA), getMax(player));
    }

    public static void set(Player player, float value) {
        player.setData(ModRegistries.MANA, Mth.clamp(value, 0f, getMax(player)));
    }

    /** Gasta mana se houver o bastante. Devolve false (sem gastar nada) se faltar. */
    public static boolean consume(ServerPlayer player, float amount) {
        float current = get(player);
        if (current < amount) {
            return false;
        }
        set(player, current - amount);
        sync(player);
        return true;
    }

    public static void sync(ServerPlayer player) {
        PacketDistributor.sendToPlayer(player, new ManaSyncPayload(get(player), getMax(player)));
    }
}

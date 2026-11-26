package com.enderjhess.arcanelegacy.mana;

import com.enderjhess.arcanelegacy.Config;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

/** Regeneração da mana e sincronização com o cliente. */
public final class ManaEvents {
    /** De quantos em quantos ticks a mana é enviada ao cliente enquanto regenera. */
    private static final int SYNC_INTERVAL = 10;

    private ManaEvents() {
    }

    public static void onPlayerTick(PlayerTickEvent.Post event) {
        if (!(event.getEntity() instanceof ServerPlayer player) || !player.isAlive()) {
            return;
        }
        float current = Mana.get(player);
        float max = Mana.getMax(player);
        if (current >= max) {
            return;
        }
        float regenPerTick = Config.MANA_REGEN_PER_SECOND.get().floatValue() / 20f;
        Mana.set(player, current + regenPerTick);
        if (player.tickCount % SYNC_INTERVAL == 0 || Mana.get(player) >= max) {
            Mana.sync(player);
        }
    }

    public static void onLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            Mana.sync(player);
        }
    }

    public static void onRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            Mana.sync(player);
        }
    }

    public static void onChangeDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            Mana.sync(player);
        }
    }
}

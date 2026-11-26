package com.enderjhess.arcanelegacy.client;

import com.enderjhess.arcanelegacy.mana.ClientMana;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;

/**
 * Barra de mana azul, do lado direito acima da fome (e do ar, quando aparece).
 * Fica escondida quando a mana está cheia, para não poluir a tela.
 */
public class ManaHudLayer implements LayeredDraw.Layer {
    private static final int WIDTH = 81;
    private static final int HEIGHT = 5;

    @Override
    public void render(GuiGraphics graphics, DeltaTracker deltaTracker) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null || minecraft.options.hideGui || minecraft.gameMode == null || !minecraft.gameMode.canHurtPlayer()) {
            return;
        }
        float max = ClientMana.maxMana();
        if (max <= 0) {
            return;
        }
        float mana = ClientMana.mana();
        // TODO: mostrar também quando o jogador segurar um item de lançamento (cajado).
        if (mana >= max) {
            return;
        }

        int x = graphics.guiWidth() / 2 + 10;
        int y = graphics.guiHeight() - minecraft.gui.rightHeight + 4;
        minecraft.gui.rightHeight += HEIGHT + 3;

        int filled = Math.round(WIDTH * Math.min(1f, mana / max));
        graphics.fill(x - 1, y - 1, x + WIDTH + 1, y + HEIGHT + 1, 0xFF0B0B1E);
        graphics.fill(x, y, x + WIDTH, y + HEIGHT, 0xFF1E2350);
        if (filled > 0) {
            graphics.fill(x, y, x + filled, y + HEIGHT, 0xFF3C78FF);
            graphics.fill(x, y, x + filled, y + 1, 0xFF8FB4FF);
        }
    }
}

package com.enderjhess.arcanelegacy.client;

import com.enderjhess.arcanelegacy.ArcaneLegacy;
import com.enderjhess.arcanelegacy.spell.Rune;
import com.enderjhess.arcanelegacy.spell.RuneType;
import com.enderjhess.arcanelegacy.spell.SpellValidator;
import com.enderjhess.arcanelegacy.table.InscriptionTableMenu;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

/**
 * Tela da Mesa de Inscrição. Desenhada só com retângulos por enquanto (sem textura de fundo).
 * Mostra se a ordem das runas é válida e quanto o feitiço custaria de mana.
 */
public class InscriptionTableScreen extends AbstractContainerScreen<InscriptionTableMenu> {
    private static final int PANEL = 0xFF1C1326;
    private static final int PANEL_LIGHT = 0xFF2C1F3D;
    private static final int GOLD = 0xFFC9A24A;
    private static final int SLOT_BG = 0xFF0E0A14;
    private static final int TEXT = 0xFFE6D9FF;
    private static final int OK = 0xFF7CFFB2;
    private static final int ERROR = 0xFFFF7C7C;

    public InscriptionTableScreen(InscriptionTableMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        imageWidth = 176;
        imageHeight = 166;
        inventoryLabelY = InscriptionTableMenu.INVENTORY_Y - 11;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);
        renderTooltip(graphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        int x = leftPos;
        int y = topPos;
        graphics.fill(x, y, x + imageWidth, y + imageHeight, GOLD);
        graphics.fill(x + 2, y + 2, x + imageWidth - 2, y + imageHeight - 2, PANEL);
        graphics.fill(x + 6, y + 16, x + imageWidth - 6, y + InscriptionTableMenu.INVENTORY_Y - 14, PANEL_LIGHT);

        for (Slot slot : menu.slots) {
            graphics.fill(x + slot.x - 1, y + slot.y - 1, x + slot.x + 17, y + slot.y + 17, SLOT_BG);
        }

        // Marca colorida embaixo de cada runa: forma, efeito ou modificador.
        for (int i = 0; i < InscriptionTableMenu.RUNE_SLOTS; i++) {
            ItemStack stack = menu.getSlot(i).getItem();
            if (stack.getItem() instanceof com.enderjhess.arcanelegacy.spell.RuneItem runeItem) {
                Slot slot = menu.getSlot(i);
                graphics.fill(x + slot.x, y + slot.y + 18, x + slot.x + 16, y + slot.y + 20, colorFor(runeItem.rune()));
            }
        }
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        graphics.drawString(font, title, titleLabelX, titleLabelY, GOLD, false);
        graphics.drawString(font, playerInventoryTitle, inventoryLabelX, inventoryLabelY, TEXT, false);

        SpellValidator.Result result = menu.validate();
        Component status;
        int color;
        if (result.valid()) {
            status = Component.translatable("gui." + ArcaneLegacy.MODID + ".inscription_table.cost", result.manaCost());
            color = OK;
        } else {
            status = Component.translatable("gui." + ArcaneLegacy.MODID + ".inscription_table.error." + result.error().key());
            color = result.error() == SpellValidator.Error.EMPTY ? TEXT : ERROR;
        }
        int width = font.width(status);
        graphics.drawString(font, status, (imageWidth - width) / 2, InscriptionTableMenu.RUNE_Y + 26, color, false);
    }

    private static int colorFor(Rune rune) {
        return rune.type() == RuneType.SHAPE ? 0xFF5FA8FF : rune.type() == RuneType.EFFECT ? 0xFFFF8A5F : 0xFFC77DFF;
    }
}

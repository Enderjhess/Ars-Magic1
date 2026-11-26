package com.enderjhess.arcanelegacy.table;

import java.util.List;

import com.enderjhess.arcanelegacy.registry.ModRegistries;
import com.enderjhess.arcanelegacy.spell.Rune;
import com.enderjhess.arcanelegacy.spell.RuneItem;
import com.enderjhess.arcanelegacy.spell.SpellValidator;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

/** Menu da Mesa de Inscrição: 6 espaços de runa em linha + inventário do jogador. */
public class InscriptionTableMenu extends AbstractContainerMenu {
    public static final int RUNE_SLOTS = InscriptionTableBlockEntity.RUNE_SLOTS;
    /** Posição dos espaços na tela (usada também pela tela para desenhar). */
    public static final int RUNE_X = 35;
    public static final int RUNE_Y = 30;
    public static final int INVENTORY_Y = 84;

    private final Container container;

    /** Construtor do cliente: o conteúdo chega pela sincronização normal de espaços. */
    public InscriptionTableMenu(int containerId, Inventory inventory) {
        this(containerId, inventory, new SimpleContainer(RUNE_SLOTS));
    }

    public InscriptionTableMenu(int containerId, Inventory inventory, Container container) {
        super(ModRegistries.INSCRIPTION_TABLE_MENU.get(), containerId);
        checkContainerSize(container, RUNE_SLOTS);
        this.container = container;
        container.startOpen(inventory.player);

        for (int i = 0; i < RUNE_SLOTS; i++) {
            addSlot(new RuneSlot(container, i, RUNE_X + i * 18, RUNE_Y));
        }
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                addSlot(new Slot(inventory, col + row * 9 + 9, 8 + col * 18, INVENTORY_Y + row * 18));
            }
        }
        for (int col = 0; col < 9; col++) {
            addSlot(new Slot(inventory, col, 8 + col * 18, INVENTORY_Y + 58));
        }
    }

    public List<Rune> runes() {
        return InscriptionTableBlockEntity.runesIn(container, RUNE_SLOTS);
    }

    public SpellValidator.Result validate() {
        return SpellValidator.validate(runes());
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot slot = slots.get(index);
        if (!slot.hasItem()) {
            return ItemStack.EMPTY;
        }
        ItemStack stack = slot.getItem();
        ItemStack original = stack.copy();
        if (index < RUNE_SLOTS) {
            if (!moveItemStackTo(stack, RUNE_SLOTS, slots.size(), true)) {
                return ItemStack.EMPTY;
            }
        } else if (stack.getItem() instanceof RuneItem) {
            if (!moveItemStackTo(stack, 0, RUNE_SLOTS, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            return ItemStack.EMPTY;
        }
        if (stack.isEmpty()) {
            slot.setByPlayer(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }
        if (stack.getCount() == original.getCount()) {
            return ItemStack.EMPTY;
        }
        slot.onTake(player, stack);
        return original;
    }

    @Override
    public boolean stillValid(Player player) {
        return container.stillValid(player);
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        container.stopOpen(player);
    }

    /** Espaço que só aceita uma runa. */
    private static class RuneSlot extends Slot {
        RuneSlot(Container container, int slot, int x, int y) {
            super(container, slot, x, y);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return stack.getItem() instanceof RuneItem;
        }

        @Override
        public int getMaxStackSize() {
            return 1;
        }
    }
}

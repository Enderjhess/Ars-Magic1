package com.enderjhess.arcanelegacy.table;

import java.util.ArrayList;
import java.util.List;

import com.enderjhess.arcanelegacy.ArcaneLegacy;
import com.enderjhess.arcanelegacy.registry.ModRegistries;
import com.enderjhess.arcanelegacy.spell.Rune;
import com.enderjhess.arcanelegacy.spell.RuneItem;
import com.enderjhess.arcanelegacy.spell.SpellValidator;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/** Guarda as runas colocadas na mesa e atualiza o estado visual do bloco. */
public class InscriptionTableBlockEntity extends BaseContainerBlockEntity {
    public static final int RUNE_SLOTS = SpellValidator.MAX_RUNES;

    private NonNullList<ItemStack> items = NonNullList.withSize(RUNE_SLOTS, ItemStack.EMPTY);

    public InscriptionTableBlockEntity(BlockPos pos, BlockState state) {
        super(ModRegistries.INSCRIPTION_TABLE_BE.get(), pos, state);
    }

    /** Lê as runas na ordem dos espaços, pulando os vazios. Usado no servidor e na tela do cliente. */
    public static List<Rune> runesIn(Container container, int slots) {
        List<Rune> runes = new ArrayList<>();
        for (int i = 0; i < slots; i++) {
            if (container.getItem(i).getItem() instanceof RuneItem runeItem) {
                runes.add(runeItem.rune());
            }
        }
        return runes;
    }

    public static TablePhase phaseFor(List<Rune> runes) {
        if (runes.isEmpty()) {
            return TablePhase.IDLE;
        }
        return SpellValidator.validate(runes).valid() ? TablePhase.READY : TablePhase.CRAFTING;
    }

    @Override
    public void setChanged() {
        super.setChanged();
        if (level != null && !level.isClientSide) {
            BlockState state = getBlockState();
            TablePhase phase = phaseFor(runesIn(this, RUNE_SLOTS));
            if (state.hasProperty(InscriptionTableBlock.PHASE) && state.getValue(InscriptionTableBlock.PHASE) != phase) {
                level.setBlock(worldPosition, state.setValue(InscriptionTableBlock.PHASE, phase), 3);
            }
        }
    }

    @Override
    public int getContainerSize() {
        return RUNE_SLOTS;
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        return stack.getItem() instanceof RuneItem;
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return items;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> items) {
        this.items = items;
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container." + ArcaneLegacy.MODID + ".inscription_table");
    }

    @Override
    protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
        return new InscriptionTableMenu(containerId, inventory, this);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        items = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(tag, items, registries);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        ContainerHelper.saveAllItems(tag, items, registries);
    }
}

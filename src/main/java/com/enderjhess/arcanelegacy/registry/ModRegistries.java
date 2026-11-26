package com.enderjhess.arcanelegacy.registry;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Supplier;

import com.enderjhess.arcanelegacy.ArcaneLegacy;
import com.enderjhess.arcanelegacy.Config;
import com.enderjhess.arcanelegacy.spell.Rune;
import com.enderjhess.arcanelegacy.spell.RuneItem;
import com.enderjhess.arcanelegacy.table.InscriptionTableBlock;
import com.enderjhess.arcanelegacy.table.InscriptionTableBlockEntity;
import com.enderjhess.arcanelegacy.table.InscriptionTableMenu;
import com.mojang.serialization.Codec;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

/** Todos os registros do mod num lugar só. */
public final class ModRegistries {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(ArcaneLegacy.MODID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(ArcaneLegacy.MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, ArcaneLegacy.MODID);
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registries.MENU, ArcaneLegacy.MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ArcaneLegacy.MODID);
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENTS =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, ArcaneLegacy.MODID);

    // --- Mana ---
    /** Mana atual do jogador. Salva com o mundo e copiada ao morrer (a mana fica como estava). */
    public static final Supplier<AttachmentType<Float>> MANA = ATTACHMENTS.register("mana",
            () -> AttachmentType.builder(() -> Config.MAX_MANA.get().floatValue())
                    .serialize(Codec.FLOAT)
                    .copyOnDeath()
                    .build());

    // --- Mesa de Inscrição ---
    public static final DeferredBlock<InscriptionTableBlock> INSCRIPTION_TABLE = BLOCKS.register("inscription_table",
            () -> new InscriptionTableBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.WOOD)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(2.5f)
                    .sound(SoundType.WOOD)));
    public static final DeferredItem<BlockItem> INSCRIPTION_TABLE_ITEM = ITEMS.registerSimpleBlockItem("inscription_table", INSCRIPTION_TABLE);

    public static final Supplier<BlockEntityType<InscriptionTableBlockEntity>> INSCRIPTION_TABLE_BE =
            BLOCK_ENTITIES.register("inscription_table",
                    () -> BlockEntityType.Builder.of(InscriptionTableBlockEntity::new, INSCRIPTION_TABLE.get()).build(null));

    public static final Supplier<MenuType<InscriptionTableMenu>> INSCRIPTION_TABLE_MENU = MENUS.register("inscription_table",
            () -> new MenuType<>(InscriptionTableMenu::new, FeatureFlags.DEFAULT_FLAGS));

    // --- Runas ---
    public static final Map<Rune, DeferredItem<RuneItem>> RUNES = new EnumMap<>(Rune.class);

    static {
        for (Rune rune : Rune.values()) {
            RUNES.put(rune, ITEMS.register(rune.id() + "_rune", () -> new RuneItem(rune, new Item.Properties().stacksTo(16))));
        }
    }

    // --- Aba do modo criativo ---
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> TAB = CREATIVE_TABS.register("main",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup." + ArcaneLegacy.MODID))
                    .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
                    .icon(() -> INSCRIPTION_TABLE_ITEM.get().getDefaultInstance())
                    .displayItems((parameters, output) -> {
                        output.accept(INSCRIPTION_TABLE_ITEM.get());
                        RUNES.values().forEach(rune -> output.accept(rune.get()));
                    })
                    .build());

    private ModRegistries() {
    }

    public static void register(IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        BLOCK_ENTITIES.register(modEventBus);
        MENUS.register(modEventBus);
        CREATIVE_TABS.register(modEventBus);
        ATTACHMENTS.register(modEventBus);
    }
}

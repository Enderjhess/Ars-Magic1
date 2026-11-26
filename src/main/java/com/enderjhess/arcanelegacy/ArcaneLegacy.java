package com.enderjhess.arcanelegacy;

import org.slf4j.Logger;

import com.enderjhess.arcanelegacy.mana.ManaCommand;
import com.enderjhess.arcanelegacy.mana.ManaEvents;
import com.enderjhess.arcanelegacy.mana.ManaSyncPayload;
import com.enderjhess.arcanelegacy.registry.ModRegistries;
import com.mojang.logging.LogUtils;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.NeoForge;

/** Classe principal do Arcane Legacy. */
@Mod(ArcaneLegacy.MODID)
public class ArcaneLegacy {
    public static final String MODID = "arcanelegacy";
    public static final Logger LOGGER = LogUtils.getLogger();

    public ArcaneLegacy(IEventBus modEventBus, ModContainer modContainer) {
        ModRegistries.register(modEventBus);
        modEventBus.addListener(ManaSyncPayload::register);

        NeoForge.EVENT_BUS.addListener(ManaEvents::onPlayerTick);
        NeoForge.EVENT_BUS.addListener(ManaEvents::onLogin);
        NeoForge.EVENT_BUS.addListener(ManaEvents::onRespawn);
        NeoForge.EVENT_BUS.addListener(ManaEvents::onChangeDimension);
        NeoForge.EVENT_BUS.addListener(ManaCommand::register);

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }
}

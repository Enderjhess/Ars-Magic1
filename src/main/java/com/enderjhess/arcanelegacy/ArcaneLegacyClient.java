package com.enderjhess.arcanelegacy;

import com.enderjhess.arcanelegacy.client.InscriptionTableScreen;
import com.enderjhess.arcanelegacy.client.ManaHudLayer;
import com.enderjhess.arcanelegacy.registry.ModRegistries;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

/** Só carrega no cliente: telas e a barra de mana. */
@Mod(value = ArcaneLegacy.MODID, dist = Dist.CLIENT)
public class ArcaneLegacyClient {
    public ArcaneLegacyClient(IEventBus modEventBus, ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
        modEventBus.addListener(ArcaneLegacyClient::registerScreens);
        modEventBus.addListener(ArcaneLegacyClient::registerGuiLayers);
    }

    private static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(ModRegistries.INSCRIPTION_TABLE_MENU.get(), InscriptionTableScreen::new);
    }

    private static void registerGuiLayers(RegisterGuiLayersEvent event) {
        event.registerAbove(VanillaGuiLayers.AIR_LEVEL, ArcaneLegacy.id("mana_bar"), new ManaHudLayer());
    }
}

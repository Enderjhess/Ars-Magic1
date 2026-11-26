package com.enderjhess.arcanelegacy.mana;

import java.util.Collection;

import com.enderjhess.arcanelegacy.ArcaneLegacy;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

/** Comando de teste: /arcanelegacy mana get|set|fill [jogadores]. Só para operadores. */
public final class ManaCommand {
    private ManaCommand() {
    }

    public static void register(RegisterCommandsEvent event) {
        event.getDispatcher().register(Commands.literal(ArcaneLegacy.MODID)
                .requires(source -> source.hasPermission(2))
                .then(Commands.literal("mana")
                        .then(Commands.literal("get")
                                .executes(ctx -> get(ctx, ctx.getSource().getPlayerOrException()))
                                .then(Commands.argument("target", EntityArgument.player())
                                        .executes(ctx -> get(ctx, EntityArgument.getPlayer(ctx, "target")))))
                        .then(Commands.literal("set")
                                .then(Commands.argument("value", FloatArgumentType.floatArg(0))
                                        .executes(ctx -> set(ctx, java.util.List.of(ctx.getSource().getPlayerOrException()),
                                                FloatArgumentType.getFloat(ctx, "value")))
                                        .then(Commands.argument("targets", EntityArgument.players())
                                                .executes(ctx -> set(ctx, EntityArgument.getPlayers(ctx, "targets"),
                                                        FloatArgumentType.getFloat(ctx, "value"))))))
                        .then(Commands.literal("fill")
                                .executes(ctx -> set(ctx, java.util.List.of(ctx.getSource().getPlayerOrException()),
                                        Float.MAX_VALUE)))));
    }

    private static int get(CommandContext<CommandSourceStack> ctx, ServerPlayer player) {
        int mana = Math.round(Mana.get(player));
        int max = Math.round(Mana.getMax(player));
        ctx.getSource().sendSuccess(() -> Component.translatable("command." + ArcaneLegacy.MODID + ".mana.get",
                player.getDisplayName(), mana, max), false);
        return mana;
    }

    private static int set(CommandContext<CommandSourceStack> ctx, Collection<ServerPlayer> players, float value)
            throws CommandSyntaxException {
        for (ServerPlayer player : players) {
            Mana.set(player, value);
            Mana.sync(player);
            int mana = Math.round(Mana.get(player));
            ctx.getSource().sendSuccess(() -> Component.translatable("command." + ArcaneLegacy.MODID + ".mana.set",
                    player.getDisplayName(), mana), true);
        }
        return players.size();
    }
}

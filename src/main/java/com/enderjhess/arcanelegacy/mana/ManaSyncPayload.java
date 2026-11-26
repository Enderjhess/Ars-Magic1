package com.enderjhess.arcanelegacy.mana;

import com.enderjhess.arcanelegacy.ArcaneLegacy;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/** Servidor → cliente: mana atual e máxima, para a barra na tela. */
public record ManaSyncPayload(float mana, float maxMana) implements CustomPacketPayload {
    public static final Type<ManaSyncPayload> TYPE = new Type<>(ArcaneLegacy.id("mana_sync"));

    public static final StreamCodec<ByteBuf, ManaSyncPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT, ManaSyncPayload::mana,
            ByteBufCodecs.FLOAT, ManaSyncPayload::maxMana,
            ManaSyncPayload::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void register(RegisterPayloadHandlersEvent event) {
        event.registrar("1").playToClient(TYPE, STREAM_CODEC, ManaSyncPayload::handle);
    }

    private static void handle(ManaSyncPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> ClientMana.set(payload.mana(), payload.maxMana()));
    }
}

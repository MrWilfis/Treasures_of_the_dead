package net.mrwilfis.treasures_of_the_dead.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.mrwilfis.treasures_of_the_dead.Treasures_of_the_dead;
import net.mrwilfis.treasures_of_the_dead.common.PlayerReputationData;

public record S2CReputationSyncPacket(String company, int exp, int level) implements CustomPacketPayload {
    public static final Type<S2CReputationSyncPacket> TYPE =
            new Type<>(Treasures_of_the_dead.resource("reputation_sync"));

    public static final StreamCodec<FriendlyByteBuf, S2CReputationSyncPacket> STREAM_CODEC =
            StreamCodec.ofMember(S2CReputationSyncPacket::write, S2CReputationSyncPacket::new);

    public S2CReputationSyncPacket(FriendlyByteBuf buf) {
        this(buf.readUtf(), buf.readInt(), buf.readInt());
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeUtf(company);
        buf.writeInt(exp);
        buf.writeInt(level);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    // Обработчик на клиенте
    public static void handle(S2CReputationSyncPacket packet, net.neoforged.neoforge.network.handling.IPayloadContext context) {
        context.enqueueWork(() -> {
            // Обновляем данные на клиенте
            var player = context.player();
            if (player != null) {

                // Используем статический метод для обновления
                PlayerReputationData.setExperience(player, packet.company(), packet.exp());
                PlayerReputationData.setLevel(player, packet.company(), packet.level());
            }
        });
    }
}

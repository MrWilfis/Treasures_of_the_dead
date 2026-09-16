package net.mrwilfis.treasures_of_the_dead.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.mrwilfis.treasures_of_the_dead.Treasures_of_the_dead;
import net.mrwilfis.treasures_of_the_dead.common.PlayerReputationData;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record C2SRequestReputationSyncPacket(String company) implements CustomPacketPayload {
    public static final Type<C2SRequestReputationSyncPacket> TYPE =
            new Type<>(Treasures_of_the_dead.resource("request_reputation_sync"));

    public static final StreamCodec<FriendlyByteBuf, C2SRequestReputationSyncPacket> STREAM_CODEC =
            StreamCodec.ofMember(C2SRequestReputationSyncPacket::write, C2SRequestReputationSyncPacket::new);

    public C2SRequestReputationSyncPacket(FriendlyByteBuf buf) {
        this(buf.readUtf());
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeUtf(company != null ? company : PlayerReputationData.ORDER_OF_SOULS);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    /**
     * Обработчик на сервере - отправляет актуальные данные клиенту
     */
    public static void handle(C2SRequestReputationSyncPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            var player = context.player();
            if (player instanceof ServerPlayer serverPlayer) {
                String company = packet.company() != null ? packet.company() : PlayerReputationData.ORDER_OF_SOULS;

                int exp = PlayerReputationData.getExperience(serverPlayer, company);
                int level = PlayerReputationData.getLevel(serverPlayer, company);

                // Отправляем актуальные данные клиенту
                PacketDistributor.sendToPlayer(serverPlayer,
                        new S2CReputationSyncPacket(company, exp, level)
                );

                //Treasures_of_the_dead.LOGGER.info("Sent reputation sync for {}: level={}, exp={}", company, level, exp);
            }
        });
    }
}

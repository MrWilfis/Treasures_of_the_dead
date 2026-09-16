package net.mrwilfis.treasures_of_the_dead.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.mrwilfis.treasures_of_the_dead.Treasures_of_the_dead;
import net.mrwilfis.treasures_of_the_dead.screen.custom.AbstractSellScreen;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record S2CSellResultPacket(int price, int experience, int playerLevel, int requiredLevel) implements CustomPacketPayload {
    public static final Type<S2CSellResultPacket> TYPE =
            new Type<>(Treasures_of_the_dead.resource("sell_result"));

    public static final StreamCodec<FriendlyByteBuf, S2CSellResultPacket> STREAM_CODEC =
            StreamCodec.ofMember(S2CSellResultPacket::write, S2CSellResultPacket::new);

    public S2CSellResultPacket(FriendlyByteBuf buf) {
        this(buf.readInt(), buf.readInt(), buf.readInt(), buf.readInt());
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeInt(price);
        buf.writeInt(experience);
        buf.writeInt(playerLevel);
        buf.writeInt(requiredLevel);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    /**
     * Обработчик на клиенте - показывает результат продажи в GUI
     */
    public static void handle(S2CSellResultPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            var screen = Minecraft.getInstance().screen;
            if (screen instanceof AbstractSellScreen skullScreen) {
                if (packet.playerLevel() >= packet.requiredLevel()) {
                    skullScreen.showSellResult(packet.price(), packet.experience());
                } else {
                    skullScreen.showBadSellResult(packet.requiredLevel());
                }
            }
        });
    }
}

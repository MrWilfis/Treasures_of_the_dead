package net.mrwilfis.treasures_of_the_dead.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.mrwilfis.treasures_of_the_dead.Treasures_of_the_dead;
import net.mrwilfis.treasures_of_the_dead.screen.custom.AbstractSellMenu;
import net.mrwilfis.treasures_of_the_dead.villager.BuyMenuProvider;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record C2SOpenBuyMenuPacket(String companyId) implements CustomPacketPayload {
    public static final Type<C2SOpenBuyMenuPacket> TYPE =
            new Type<>(Treasures_of_the_dead.resource("open_buy_menu"));

    public static final StreamCodec<FriendlyByteBuf, C2SOpenBuyMenuPacket> STREAM_CODEC =
            StreamCodec.ofMember(C2SOpenBuyMenuPacket::write, C2SOpenBuyMenuPacket::new);

    public C2SOpenBuyMenuPacket(FriendlyByteBuf buf) {
        this(buf.readUtf());
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeUtf(companyId);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    /**
     * Обработчик на сервере
     */
    public static void handle(C2SOpenBuyMenuPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            var player = context.player();
            if (player instanceof ServerPlayer serverPlayer) {

                if (serverPlayer.containerMenu instanceof AbstractSellMenu menu) {
                    serverPlayer.openMenu(new BuyMenuProvider(menu.getVillager(), packet.companyId()));

                    //Treasures_of_the_dead.LOGGER.info("Opened buy menu for company: {} for player: {}", packet.companyId(), serverPlayer.getName().getString());
                } else {
                    Treasures_of_the_dead.LOGGER.warn(
                            "Villager not found for player: {}",
                            serverPlayer.getName().getString()
                    );
                }
            }
        });
    }
}

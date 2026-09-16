package net.mrwilfis.treasures_of_the_dead.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.Villager;
import net.mrwilfis.treasures_of_the_dead.Treasures_of_the_dead;
import net.mrwilfis.treasures_of_the_dead.screen.custom.AbstractBuyMenu;
import net.mrwilfis.treasures_of_the_dead.villager.SellMenuProvider;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record C2SOpenSellMenuPacket(String companyId) implements CustomPacketPayload {

    public static final Type<C2SOpenSellMenuPacket> TYPE =
            new Type<>(Treasures_of_the_dead.resource("open_sell_menu"));

    public static final StreamCodec<FriendlyByteBuf, C2SOpenSellMenuPacket> STREAM_CODEC =
            StreamCodec.ofMember(C2SOpenSellMenuPacket::write, C2SOpenSellMenuPacket::new);

    public C2SOpenSellMenuPacket(FriendlyByteBuf buf) {
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
    public static void handle(C2SOpenSellMenuPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            var player = context.player();
            if (player instanceof ServerPlayer serverPlayer) {

                if (serverPlayer.containerMenu instanceof AbstractBuyMenu menu) {
                    serverPlayer.openMenu(new SellMenuProvider(menu.getVillager(), packet.companyId()));

                    //Treasures_of_the_dead.LOGGER.info("Opened sell menu for company: {} for player: {}", packet.companyId(), serverPlayer.getName().getString());
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

package net.mrwilfis.treasures_of_the_dead.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.mrwilfis.treasures_of_the_dead.Treasures_of_the_dead;
import net.mrwilfis.treasures_of_the_dead.common.PlayerReputationData;
import net.mrwilfis.treasures_of_the_dead.config.SellConfigManager;
import net.mrwilfis.treasures_of_the_dead.screen.custom.AbstractSellMenu;
import net.mrwilfis.treasures_of_the_dead.util.DoubloonUtils;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/**
 * Пакет для обработки продажи предметов.
 * Поддерживает разные торговые компании.
 */
public record C2SSellItemPacket(String company) implements CustomPacketPayload {
    public static final Type<C2SSellItemPacket> TYPE =
            new Type<>(Treasures_of_the_dead.resource("sell_item"));

    public static final StreamCodec<FriendlyByteBuf, C2SSellItemPacket> STREAM_CODEC =
            StreamCodec.ofMember(C2SSellItemPacket::write, C2SSellItemPacket::new);

    public C2SSellItemPacket(FriendlyByteBuf buf) {
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
     * Обработчик на сервере
     */
    public static void handle(C2SSellItemPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();
            String company = packet.company() != null ? packet.company() : PlayerReputationData.ORDER_OF_SOULS;

            if (player instanceof ServerPlayer serverPlayer) {
                if (serverPlayer.containerMenu instanceof AbstractSellMenu menu) {

                    ItemStack stack = menu.getSellItem();
                    if (stack.isEmpty()) {
                        serverPlayer.sendSystemMessage(
                                net.minecraft.network.chat.Component.literal("§cPlace an item to sell!")
                        );
                        return;
                    }

                    var entry = SellConfigManager.getInstance().getEntry(stack);
                    if (entry == null) {
                        serverPlayer.sendSystemMessage(
                                net.minecraft.network.chat.Component.literal("§cThis item cannot be sold!")
                        );
                        return;
                    }

                    // Проверяем уровень для конкретной компании
                    int playerLevel = PlayerReputationData.getLevel(serverPlayer, company);
                    if (playerLevel < entry.minLevel) {
                        PacketDistributor.sendToPlayer(serverPlayer,
                                new S2CSellResultPacket(0, 0, playerLevel, entry.minLevel)
                        );
                        return;
                    }

                    int totalPrice = entry.price * stack.getCount();
                    int totalExperience = entry.experienceGain * stack.getCount();

                    // Выдаем дублоны (с учетом мешочка)
                    DoubloonUtils.giveDoubloons(serverPlayer, totalPrice);

                    // Добавляем опыт для конкретной компании
                    PlayerReputationData.addExperience(serverPlayer, company, totalExperience);

                    // Очищаем слот
                    menu.clearSellSlot();

                    // Получаем обновленные данные
                    int newExp = PlayerReputationData.getExperience(serverPlayer, company);
                    int newLevel = PlayerReputationData.getLevel(serverPlayer, company);

                    // 1. Отправляем синхронизацию репутации
                    PacketDistributor.sendToPlayer(serverPlayer,
                            new S2CReputationSyncPacket(company, newExp, newLevel)
                    );

                    // 2. Отправляем результат продажи в GUI
                    PacketDistributor.sendToPlayer(serverPlayer,
                            new S2CSellResultPacket(totalPrice, totalExperience, playerLevel, entry.minLevel)
                    );
                }
            }
        });
    }
}
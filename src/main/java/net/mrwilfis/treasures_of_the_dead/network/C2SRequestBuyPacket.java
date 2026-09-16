package net.mrwilfis.treasures_of_the_dead.network;

import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.mrwilfis.treasures_of_the_dead.TOTDUtils;
import net.mrwilfis.treasures_of_the_dead.Treasures_of_the_dead;
import net.mrwilfis.treasures_of_the_dead.common.ModDataComponents;
import net.mrwilfis.treasures_of_the_dead.screen.custom.AbstractBuyMenu;
import net.mrwilfis.treasures_of_the_dead.util.DoubloonUtils;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public record C2SRequestBuyPacket(String companyId, int slotIndex) implements CustomPacketPayload {

    public static final Type<C2SRequestBuyPacket> TYPE =
            new Type<>(Treasures_of_the_dead.resource("request_buy"));

    public static final StreamCodec<FriendlyByteBuf, C2SRequestBuyPacket> STREAM_CODEC =
            StreamCodec.ofMember(C2SRequestBuyPacket::write, C2SRequestBuyPacket::new);

    public C2SRequestBuyPacket(FriendlyByteBuf buf) {
        this(buf.readUtf(), buf.readInt());
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeUtf(companyId);
        buf.writeInt(slotIndex);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    /**
     * Обработчик на сервере - выполняет покупку
     * Все проверки уже пройдены на клиенте
     */
    public static void handle(C2SRequestBuyPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            var player = context.player();
            if (player instanceof ServerPlayer serverPlayer) {

                if (!(serverPlayer.containerMenu instanceof AbstractBuyMenu menu)) {
                    return;
                }

                if (!menu.getCompanyId().equals(packet.companyId())) {
                    return;
                }

                var offers = menu.getCurrentOffers();
                if (packet.slotIndex() < 0 || packet.slotIndex() >= offers.size()) {
                    return;
                }

                TOTDUtils.ShopOfferEntry offer = offers.get(packet.slotIndex());
                if (offer == null) {
                    return;
                }

                // Забираем дублоны (на клиенте уже проверили, что они есть)
                if (!DoubloonUtils.removeDoubloons(serverPlayer, offer.price)) {
                    // Если не удалось забрать - возвращаемся
                    Treasures_of_the_dead.LOGGER.warn("Failed to remove doubloons from player {}",
                            serverPlayer.getName().getString());
                    return;
                }

                // Создаем предмет
                ItemStack resultStack = createResultItem(serverPlayer, offer);
                if (resultStack.isEmpty()) {
                    // Если ошибка - возвращаем дублоны
                    DoubloonUtils.giveDoubloons(serverPlayer, offer.price);
                    return;
                }

                // Выдаем предмет
                if (!serverPlayer.addItem(resultStack)) {
                    serverPlayer.drop(resultStack, false);
                }

                // Звук успеха
                PacketDistributor.sendToPlayer(serverPlayer,
                        new S2CPlaySoundPacket("minecraft:entity.villager.yes", 1.0F, 1.0F));
            }
        });
    }

    private static ItemStack createResultItem(ServerPlayer player, TOTDUtils.ShopOfferEntry offer) {
        ItemStack stack = TOTDUtils.getItemStackFromString(offer.itemId, offer.count);
        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        }

        if (offer.hasEnchantments()) {
            applyEnchantments(player, stack, offer.enchantments);
        }

        if (offer.hasComponents()) {
            applyComponents(player, stack, offer.components);
        }

        return stack;
    }

    private static void applyEnchantments(ServerPlayer player, ItemStack stack, Map<String, Integer> enchantments) {
        if (stack.isEmpty() || enchantments.isEmpty()) return;

        try {
            var level = player.level();
            var enchantmentRegistry = level.registryAccess()
                    .registry(Registries.ENCHANTMENT)
                    .orElse(null);

            if (enchantmentRegistry == null) return;

            ItemEnchantments.Mutable builder = new ItemEnchantments.Mutable(ItemEnchantments.EMPTY);

            for (Map.Entry<String, Integer> entry : enchantments.entrySet()) {
                String enchantmentId = entry.getKey();
                int levelValue = entry.getValue();

                ResourceLocation location = ResourceLocation.parse(enchantmentId);
                var enchantmentHolder = enchantmentRegistry.getHolder(location);

                if (enchantmentHolder.isPresent()) {
                    builder.set(enchantmentHolder.get(), levelValue);
                } else {
                    Treasures_of_the_dead.LOGGER.warn("Enchantment not found: {}", enchantmentId);
                }
            }

            stack.set(DataComponents.ENCHANTMENTS, builder.toImmutable());

        } catch (Exception e) {
            Treasures_of_the_dead.LOGGER.error("Error applying enchantments: {}", e.getMessage(), e);
        }
    }

    @SuppressWarnings("unchecked")
    private static void applyComponents(ServerPlayer player, ItemStack stack, Map<String, Object> components) {
        if (stack.isEmpty() || components.isEmpty()) return;

        try {
            for (Map.Entry<String, Object> entry : components.entrySet()) {
                String componentId = entry.getKey();
                Object value = entry.getValue();

                switch (componentId) {
                    case "minecraft:custom_name" -> {
                        String name = value.toString().replaceAll("^\"|\"$", "");
                        stack.set(DataComponents.CUSTOM_NAME,
                                net.minecraft.network.chat.Component.literal(name));
                    }

                    case "minecraft:lore" -> {
                        List<net.minecraft.network.chat.Component> loreComponents = new ArrayList<>();

                        if (value instanceof List<?> list) {
                            for (Object item : list) {
                                String line = item.toString().replaceAll("^\"|\"$", "");
                                loreComponents.add(net.minecraft.network.chat.Component.literal(line));
                            }
                        } else if (value instanceof String stringValue) {
                            String line = stringValue.replaceAll("^\"|\"$", "");
                            loreComponents.add(net.minecraft.network.chat.Component.literal(line));
                        }

                        if (!loreComponents.isEmpty()) {
                            stack.set(DataComponents.LORE,
                                    new net.minecraft.world.item.component.ItemLore(loreComponents));
                        }
                    }

                    case "treasures_of_the_dead:difficulty" -> {
                        if (value instanceof Number number) {
                            stack.set(ModDataComponents.DIFFICULTY, number.intValue());
                        }
                    }

                    default -> {
                        Treasures_of_the_dead.LOGGER.debug("Unknown component: {}", componentId);
                    }
                }
            }
        } catch (Exception e) {
            Treasures_of_the_dead.LOGGER.error("Error applying components: {}", e.getMessage(), e);
        }
    }
}
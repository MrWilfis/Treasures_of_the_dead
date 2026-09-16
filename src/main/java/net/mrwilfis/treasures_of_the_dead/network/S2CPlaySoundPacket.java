package net.mrwilfis.treasures_of_the_dead.network;

import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.mrwilfis.treasures_of_the_dead.Treasures_of_the_dead;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record S2CPlaySoundPacket(String soundId, float volume, float pitch) implements CustomPacketPayload {

    public static final Type<S2CPlaySoundPacket> TYPE =
            new Type<>(Treasures_of_the_dead.resource("play_sound"));

    public static final StreamCodec<FriendlyByteBuf, S2CPlaySoundPacket> STREAM_CODEC =
            StreamCodec.ofMember(S2CPlaySoundPacket::write, S2CPlaySoundPacket::new);

    public S2CPlaySoundPacket(FriendlyByteBuf buf) {
        this(buf.readUtf(), buf.readFloat(), buf.readFloat());
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeUtf(soundId);
        buf.writeFloat(volume);
        buf.writeFloat(pitch);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    /**
     * Обработчик на клиенте - воспроизводит звук
     */
    public static void handle(S2CPlaySoundPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            var player = Minecraft.getInstance().player;
            if (player != null) {
                ResourceLocation location = ResourceLocation.parse(packet.soundId());
                SoundEvent soundEvent = BuiltInRegistries.SOUND_EVENT.get(location);
                if (soundEvent != null) {
                    player.playSound(soundEvent, packet.volume(), packet.pitch());
                } else {
                    Treasures_of_the_dead.LOGGER.warn("Sound not found: {}", packet.soundId());
                }
            }
        });
    }
}
package net.mrwilfis.treasures_of_the_dead.sound;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.mrwilfis.treasures_of_the_dead.Treasures_of_the_dead;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, Treasures_of_the_dead.MOD_ID);

    public static final Supplier<SoundEvent> BLUNDER_BOMB_EXPLODE = registerSoundEvent("blunder_bomb_explode");

    private static Supplier<SoundEvent> registerSoundEvent(String name) {
        ResourceLocation id = Treasures_of_the_dead.resource(name);
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(id));
    }


    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}

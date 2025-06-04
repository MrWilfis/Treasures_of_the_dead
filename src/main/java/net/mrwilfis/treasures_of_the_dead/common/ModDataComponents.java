package net.mrwilfis.treasures_of_the_dead.common;

import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.mrwilfis.treasures_of_the_dead.Treasures_of_the_dead;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.UnaryOperator;

public class ModDataComponents {
    private static final DeferredRegister.DataComponents DATA_COMPONENTS = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, Treasures_of_the_dead.MOD_ID);

    public static final DataComponentType<Float> CAPTAIN_SKELETON_DEATH_X = register(
            "death_x",
            builder -> builder.persistent(Codec.FLOAT).networkSynchronized(ByteBufCodecs.FLOAT)
    );
    public static final DataComponentType<Float> CAPTAIN_SKELETON_DEATH_Z = register(
            "death_z",
            builder -> builder.persistent(Codec.FLOAT).networkSynchronized(ByteBufCodecs.FLOAT)
    );
    public static final DataComponentType<Boolean> TREASURE_CHEST_IS_ROBBED = register(
            "treasure_chest_is_robbed",
            builder -> builder.persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL)
    );
    public static final DataComponentType<Boolean> TREASURE_CHEST_IS_OPEN = register(
            "treasure_chest_is_open",
            builder -> builder.persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL)
    );
    public static final DataComponentType<Boolean> KEG_IS_GOING_TO_BLOW_UP = register(
            "keg_is_going_to_blow_up",
            builder -> builder.persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL)
    );
    public static final DataComponentType<Integer> KEG_PREPARE_TO_BLOW_UP = register(
            "keg_prepare_to_blow_up",
            builder -> builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT)
    );
    public static final DataComponentType<Boolean> GUN_IS_CHARGED = register(
            "is_charged",
            builder -> builder.persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL)
    );


    private static <T> DataComponentType<T> register(String name, UnaryOperator<DataComponentType.Builder<T>> builder) {
        DataComponentType<T> type = builder.apply(DataComponentType.builder()).build();
        DATA_COMPONENTS.register(name, () -> type);
        return type;
    }

    public static void register(IEventBus event) {
        DATA_COMPONENTS.register(event);
    }
}

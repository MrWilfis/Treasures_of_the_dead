package net.mrwilfis.treasures_of_the_dead.villager;

import com.google.common.collect.ImmutableSet;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.level.block.Blocks;
import net.mrwilfis.treasures_of_the_dead.Treasures_of_the_dead;
import net.mrwilfis.treasures_of_the_dead.block.ModBlocks;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModVillagers {
    public static final DeferredRegister<PoiType> POI_TYPES =
            DeferredRegister.create(BuiltInRegistries.POINT_OF_INTEREST_TYPE, Treasures_of_the_dead.MOD_ID);
    public static final DeferredRegister<VillagerProfession> VILLAGER_PROFESSIONS =
            DeferredRegister.create(BuiltInRegistries.VILLAGER_PROFESSION, Treasures_of_the_dead.MOD_ID);

    public static final Holder<PoiType> SKULL_MERCHANT_POI = POI_TYPES.register("skull_merchant_poi",
            () -> new PoiType(ImmutableSet.copyOf(ModBlocks.SKULL_MERCHANT_TABLE.get().getStateDefinition().getPossibleStates()), 1, 1));

    public static final Holder<VillagerProfession> SKULL_MERCHANT = VILLAGER_PROFESSIONS.register("skull_merchant",
            () -> new VillagerProfession("skull_merchant", holder -> holder.value() == SKULL_MERCHANT_POI.value(),
                    poiTypeHolder -> poiTypeHolder.value() == SKULL_MERCHANT_POI.value(), ImmutableSet.of(), ImmutableSet.of(),
                    SoundEvents.ENCHANTMENT_TABLE_USE));

    public static void register(IEventBus event) {
        POI_TYPES.register(event);
        VILLAGER_PROFESSIONS.register(event);
    }
}

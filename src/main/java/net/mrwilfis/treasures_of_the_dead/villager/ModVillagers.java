package net.mrwilfis.treasures_of_the_dead.villager;

import com.google.common.collect.ImmutableSet;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.mrwilfis.treasures_of_the_dead.Treasures_of_the_dead;
import net.mrwilfis.treasures_of_the_dead.block.ModBlocks;

public class ModVillagers {
    public static final DeferredRegister<PoiType> POI_TYPES =
            DeferredRegister.create(ForgeRegistries.POI_TYPES, Treasures_of_the_dead.MOD_ID);
    public static final DeferredRegister<VillagerProfession> VILLAGER_PROFESSIONS =
            DeferredRegister.create(ForgeRegistries.VILLAGER_PROFESSIONS, Treasures_of_the_dead.MOD_ID);

    public static final RegistryObject<PoiType> SKULL_MERCHANT_POI = POI_TYPES.register("skull_merchant_poi",
            () -> new PoiType(ImmutableSet.copyOf(ModBlocks.SKULL_MERCHANT_TABLE.get().getStateDefinition().getPossibleStates()),
                    1, 1));

    public static final RegistryObject<VillagerProfession> SKULL_MERCHANT = VILLAGER_PROFESSIONS.register("skull_merchant",
            () -> new VillagerProfession("skull_merchant", holder -> holder.get() == SKULL_MERCHANT_POI.get(),
                    holder -> holder.get() == SKULL_MERCHANT_POI.get(), ImmutableSet.of(), ImmutableSet.of(), SoundEvents.ENCHANTMENT_TABLE_USE));



    public static void register(IEventBus eventBus) {
        POI_TYPES.register(eventBus);
        VILLAGER_PROFESSIONS.register(eventBus);
    }
}

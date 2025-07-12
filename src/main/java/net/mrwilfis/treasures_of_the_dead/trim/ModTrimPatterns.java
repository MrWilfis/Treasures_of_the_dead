package net.mrwilfis.treasures_of_the_dead.trim;

import net.minecraft.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.armortrim.TrimPattern;
import net.minecraftforge.registries.ForgeRegistries;
import net.mrwilfis.treasures_of_the_dead.Treasures_of_the_dead;
import net.mrwilfis.treasures_of_the_dead.item.ModItems;

public class ModTrimPatterns {
    public static final ResourceKey<TrimPattern> ORDER_OF_SOULS = ResourceKey.create(Registries.TRIM_PATTERN,
            new ResourceLocation(Treasures_of_the_dead.MOD_ID, "order_of_souls"));

    public static void bootstrap(BootstapContext<TrimPattern> pContext) {
        register(pContext, ModItems.ORDER_OF_SOULS_SMITHING_TEMPLATE.get(), ORDER_OF_SOULS);
    }

    private static void register(BootstapContext<TrimPattern> context, Item item, ResourceKey<TrimPattern> key) {
        TrimPattern trimPattern = new TrimPattern(key.location(), ForgeRegistries.ITEMS.getHolder(item).get(),
                Component.translatable(Util.makeDescriptionId("trim_pattern", key.location())));
        context.register(key, trimPattern);
    }

}

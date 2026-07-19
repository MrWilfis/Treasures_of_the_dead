package net.mrwilfis.treasures_of_the_dead.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WeatheringCopper;

import java.util.Optional;
import java.util.function.Supplier;

public record WeatheringChain(
        Supplier<Block> unaffected,
        Supplier<Block> exposed,
        Supplier<Block> weathered,
        Supplier<Block> oxidized,
        Supplier<Block> waxedUnaffected,
        Supplier<Block> waxedExposed,
        Supplier<Block> waxedWeathered,
        Supplier<Block> waxedOxidized
) {
    public Optional<Block> getNext(WeatheringCopper.WeatherState state) {
        return switch (state) {
            case UNAFFECTED -> Optional.of(exposed.get());
            case EXPOSED -> Optional.of(weathered.get());
            case WEATHERED -> Optional.of(oxidized.get());
            case OXIDIZED -> Optional.empty();
        };
    }

    public Optional<Block> getPrevious(WeatheringCopper.WeatherState state) {
        return switch (state) {
            case UNAFFECTED -> Optional.empty();
            case EXPOSED -> Optional.of(unaffected.get());
            case WEATHERED -> Optional.of(exposed.get());
            case OXIDIZED -> Optional.of(weathered.get());
        };
    }

    public Optional<Block> getWaxed(WeatheringCopper.WeatherState state) {
        return switch (state) {
            case UNAFFECTED -> Optional.of(waxedUnaffected.get());
            case EXPOSED -> Optional.of(waxedExposed.get());
            case WEATHERED -> Optional.of(waxedWeathered.get());
            case OXIDIZED -> Optional.of(waxedOxidized.get());
        };
    }

    public Optional<Block> getUnwaxed(WeatheringCopper.WeatherState state) {
        return switch (state) {
            case UNAFFECTED -> Optional.of(unaffected.get());
            case EXPOSED -> Optional.of(exposed.get());
            case WEATHERED -> Optional.of(weathered.get());
            case OXIDIZED -> Optional.of(oxidized.get());
        };
    }
}

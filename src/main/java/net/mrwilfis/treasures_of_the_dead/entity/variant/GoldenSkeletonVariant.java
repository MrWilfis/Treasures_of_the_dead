package net.mrwilfis.treasures_of_the_dead.entity.variant;

import java.util.Arrays;
import java.util.Comparator;

public enum GoldenSkeletonVariant {
    DEFAULT(0),
    VAR1(1),
    VAR2(2);

    private static final GoldenSkeletonVariant[] BY_ID = Arrays.stream(values()).sorted(Comparator.
            comparingInt(GoldenSkeletonVariant::getId)).toArray(GoldenSkeletonVariant[]::new);
    private final int id;

    GoldenSkeletonVariant(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static GoldenSkeletonVariant byId(int id) {
        return BY_ID[id % BY_ID.length];
    }
}

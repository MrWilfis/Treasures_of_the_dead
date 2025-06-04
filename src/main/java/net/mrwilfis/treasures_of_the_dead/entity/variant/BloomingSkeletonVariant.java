package net.mrwilfis.treasures_of_the_dead.entity.variant;

import java.util.Arrays;
import java.util.Comparator;

public enum BloomingSkeletonVariant {
    DEFAULT(0),
    VAR1(1),
    VAR2(2),
    VAR3(3),
    VAR4(4),
    VAR5(5);

    private static final BloomingSkeletonVariant[] BY_ID = Arrays.stream(values()).sorted(Comparator.
            comparingInt(BloomingSkeletonVariant::getId)).toArray(BloomingSkeletonVariant[]::new);
    private final int id;

    BloomingSkeletonVariant(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static BloomingSkeletonVariant byId(int id) {
        return BY_ID[id % BY_ID.length];
    }
}

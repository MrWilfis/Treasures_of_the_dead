package net.mrwilfis.treasures_of_the_dead.entity.variant;

import java.util.Arrays;
import java.util.Comparator;

public enum CaptainBloomingSkeletonVariant {
    DEFAULT(0),
    VAR1(1),
    VAR2(2);

    private static final CaptainBloomingSkeletonVariant[] BY_ID = Arrays.stream(values()).sorted(Comparator.
            comparingInt(CaptainBloomingSkeletonVariant::getId)).toArray(CaptainBloomingSkeletonVariant[]::new);
    private final int id;

    CaptainBloomingSkeletonVariant(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static CaptainBloomingSkeletonVariant byId(int id) {
        return BY_ID[id % BY_ID.length];
    }
}

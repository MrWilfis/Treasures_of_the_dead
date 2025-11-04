package net.mrwilfis.treasures_of_the_dead.entity.variant;

import java.util.Arrays;
import java.util.Comparator;

public enum CaptainGoldenSkeletonVariant {
    DEFAULT(0),
    VAR1(1),
    VAR2(2);

    private static final CaptainGoldenSkeletonVariant[] BY_ID = Arrays.stream(values()).sorted(Comparator.
            comparingInt(CaptainGoldenSkeletonVariant::getId)).toArray(CaptainGoldenSkeletonVariant[]::new);
    private final int id;

    CaptainGoldenSkeletonVariant(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static CaptainGoldenSkeletonVariant byId(int id) {
        return BY_ID[id % BY_ID.length];
    }
}

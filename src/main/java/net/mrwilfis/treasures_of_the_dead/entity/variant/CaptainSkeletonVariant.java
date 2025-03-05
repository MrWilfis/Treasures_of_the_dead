package net.mrwilfis.treasures_of_the_dead.entity.variant;

import java.util.Arrays;
import java.util.Comparator;

public enum CaptainSkeletonVariant {
    DEFAULT(0),
    VAR1(1),
    VAR2(2),
    VAR3(3),
    VAR4(4),
    VAR5(5),
    VAR6(6),
    VAR7(7);

    private static final CaptainSkeletonVariant[] BY_ID = Arrays.stream(values()).sorted(Comparator.
            comparingInt(CaptainSkeletonVariant::getId)).toArray(CaptainSkeletonVariant[]::new);
    private final int id;

    CaptainSkeletonVariant(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static CaptainSkeletonVariant byId(int id) {
        return BY_ID[id % BY_ID.length];
    }
}

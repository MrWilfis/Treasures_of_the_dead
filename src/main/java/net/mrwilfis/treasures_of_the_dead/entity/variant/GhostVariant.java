package net.mrwilfis.treasures_of_the_dead.entity.variant;

import java.util.Arrays;
import java.util.Comparator;

public enum GhostVariant {
    DEFAULT(0),
    VAR1(1),
    VAR2(2),
    VAR3(3);

    private static final GhostVariant[] BY_ID = Arrays.stream(values()).sorted(Comparator.
            comparingInt(GhostVariant::getId)).toArray(GhostVariant[]::new);
    private final int id;

    GhostVariant(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static GhostVariant byId(int id) {
        return BY_ID[id % BY_ID.length];
    }
}

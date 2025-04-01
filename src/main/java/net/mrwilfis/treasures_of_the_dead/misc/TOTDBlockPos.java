package net.mrwilfis.treasures_of_the_dead.misc;

import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;

public class TOTDBlockPos {

    public static final BlockPos fromCoords(double x, double y, double z){
        return new BlockPos((int) x, (int) y, (int) z);
    }

    public static final BlockPos fromVec3(Vec3 vec3){
        return fromCoords(vec3.x, vec3.y, vec3.z);
    }
}

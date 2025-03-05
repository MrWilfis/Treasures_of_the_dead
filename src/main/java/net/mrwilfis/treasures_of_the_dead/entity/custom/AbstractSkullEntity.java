package net.mrwilfis.treasures_of_the_dead.entity.custom;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class AbstractSkullEntity extends AnyTreasureClass {


    public AbstractSkullEntity(EntityType pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.noCulling = true;
    }


    public static AttributeSupplier setAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 10.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.0f).build();
    }


    @Override
    public @NotNull InteractionResult mobInteract(Player pPlayer, @NotNull InteractionHand pHand) {
        if (pPlayer.isShiftKeyDown()) {
            this.setYRot(pPlayer.getYRot() - 180.0f);
            this.addTag("TOTD_Rotate");

        //    this.teleportTo(this.getX(), this.getY(), this.getZ());


        }
        return super.mobInteract(pPlayer, pHand);
    }



}

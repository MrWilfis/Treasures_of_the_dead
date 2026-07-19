package net.mrwilfis.treasures_of_the_dead.entity.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.mrwilfis.treasures_of_the_dead.common.ModDataComponents;
import net.mrwilfis.treasures_of_the_dead.entity.ModEntities;
import net.mrwilfis.treasures_of_the_dead.entity.custom.chestVariants.TreasureChestEntity;
import net.mrwilfis.treasures_of_the_dead.item.ModItems;

import java.util.List;
import java.util.Random;

public class AbstractChestEntity extends AnyTreasureClass {

    private static final EntityDataAccessor<Boolean> IS_OPEN = SynchedEntityData.defineId(TreasureChestEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_ROBBED = SynchedEntityData.defineId(TreasureChestEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_TRAP = SynchedEntityData.defineId(TreasureChestEntity.class, EntityDataSerializers.BOOLEAN);


    public AbstractChestEntity(EntityType pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.noCulling = true;
    }


    public static AttributeSupplier setAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 10.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.0f).build();
    }

    @Override
    public void tick() {

        if (!this.level().isClientSide) {
            if (this.getIsTrap() && this.isInWater()) {
                this.setIsTrap(false);
            }
            if (this.getIsTrap() && this.tickCount % 20 == 0) {
                AABB detectionBox = new AABB(this.getOnPos()).inflate(2);
                List<Player> nearbyPlayers = this.level().getEntitiesOfClass(Player.class, detectionBox);
                if (!nearbyPlayers.isEmpty()) {
                    Random rand = new Random();
                    for (int i = 0; i < 3; i++) {
                        int x = (int)this.getX() + random.nextInt(-3, 3);
                        int z = (int)this.getZ() + random.nextInt(-3, 3);

                        int y = this.level().getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z);

                        TOTDSkeletonEntity pirate = new TOTDSkeletonEntity(ModEntities.TOTD_SKELETON.get(), this.level());
                        this.level().addFreshEntity(pirate);
                        pirate.setPos(x+0.5, y, z+0.5);
                        pirate.yHeadRot = rand.nextFloat(0.0f, 360.0f);
                        pirate.setIsSpawning(true);
                        pirate.specialProcedures();
                    }

                    this.setIsTrap(false);
                }
            }

        }

        super.tick();
    }

    @Override
    public ItemStack getTreasureItem() {
        ItemStack stack = new ItemStack(ModItems.TREASURE_CHEST_ITEM.get());

        stack.set(ModDataComponents.TREASURE_CHEST_IS_ROBBED, this.getIsRobbed());
        stack.set(ModDataComponents.TREASURE_CHEST_IS_OPEN, this.getIsOpen());
        stack.set(ModDataComponents.TREASURE_CHEST_IS_TRAP, this.getIsTrap());

//        stack.setTag(new CompoundTag());
//        stack.getTag().putBoolean("IsOpen", this.getIsOpen());
//        stack.getTag().putBoolean("IsRobbed", this.getIsRobbed());

        return stack;
    }

    public boolean getIsOpen() {
        return this.getEntityData().get(IS_OPEN).booleanValue();
    }

    public void setIsOpen(boolean var) {
        this.getEntityData().set(IS_OPEN, var);
    }

    public boolean getIsRobbed() {
        return this.getEntityData().get(IS_ROBBED).booleanValue();
    }

    public void setIsRobbed(boolean var) {
        this.getEntityData().set(IS_ROBBED, var);
    }

    public boolean getIsTrap() {
        return this.getEntityData().get(IS_TRAP).booleanValue();
    }

    public void setIsTrap(boolean var) {
        this.getEntityData().set(IS_TRAP, var);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(IS_OPEN, Boolean.FALSE);
        builder.define(IS_ROBBED, Boolean.FALSE);
        builder.define(IS_TRAP, Boolean.FALSE);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        this.setIsOpen(pCompound.getBoolean("IsOpen"));
        this.setIsRobbed(pCompound.getBoolean("IsRobbed"));
        this.setIsTrap(pCompound.getBoolean("IsTrap"));
        super.readAdditionalSaveData(pCompound);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        pCompound.putBoolean("IsOpen", this.getIsOpen());
        pCompound.putBoolean("IsRobbed", this.getIsRobbed());
        pCompound.putBoolean("IsTrap", this.getIsTrap());
        super.addAdditionalSaveData(pCompound);
    }



}

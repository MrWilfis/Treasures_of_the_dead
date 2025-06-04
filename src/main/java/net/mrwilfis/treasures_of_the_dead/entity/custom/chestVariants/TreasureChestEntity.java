package net.mrwilfis.treasures_of_the_dead.entity.custom.chestVariants;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.mrwilfis.treasures_of_the_dead.common.ModDataComponents;
import net.mrwilfis.treasures_of_the_dead.common.ModLootTables;
import net.mrwilfis.treasures_of_the_dead.entity.custom.AbstractChestEntity;
import net.mrwilfis.treasures_of_the_dead.item.ModItems;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TreasureChestEntity extends AbstractChestEntity implements GeoAnimatable, GeoEntity {
    private static final EntityDataAccessor<Boolean> IS_OPEN = SynchedEntityData.defineId(TreasureChestEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_ROBBED = SynchedEntityData.defineId(TreasureChestEntity.class, EntityDataSerializers.BOOLEAN);

    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    private static final RawAnimation IDLE = RawAnimation.begin().then("animation.model.idle", Animation.LoopType.LOOP);
    private static final RawAnimation IDLE_OPENED = RawAnimation.begin().then("animation.model.idle_opened", Animation.LoopType.LOOP);
    private static final RawAnimation OPENING = RawAnimation.begin().then("animation.model.opening", Animation.LoopType.HOLD_ON_LAST_FRAME);


    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller", 0, this::predicate));

    }

    private PlayState predicate(AnimationState<TreasureChestEntity> state) {
        if (!this.getIsOpen()) {
            state.getController().setAnimation(IDLE);
        } else {
            state.getController().setAnimationSpeed(1.3D);
            state.getController().setAnimation(OPENING);
        }
        return PlayState.CONTINUE;
    }

    @Override
    public double getTick(Object o) {
        return (double)((Entity)o).tickCount;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    public TreasureChestEntity(EntityType pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public @NotNull InteractionResult mobInteract(Player pPlayer, @NotNull InteractionHand pHand) {
        ItemStack key = pPlayer.getItemInHand(pHand);

        if (pPlayer.isShiftKeyDown()) {
            this.setYRot(pPlayer.getYRot() - 180.0f);
            this.addTag("TOTD_Rotate");
            //    this.teleportTo(this.getX(), this.getY(), this.getZ());

        } else if (!this.getIsRobbed() && key.getItem().equals(ModItems.TREASURE_KEY.get())) {
            this.setIsRobbed(true);
            this.setIsOpen(true);
            ejectItems(pPlayer.level());

            if (!pPlayer.isCreative()) {
                key.shrink(1);
            }
            this.playSound(SoundEvents.FENCE_GATE_OPEN, 0.85f, 0.5f);
            return InteractionResult.SUCCESS;
        } else if (this.getIsRobbed() && this.getIsOpen()) {
            this.setIsOpen(false);
            this.playSound(SoundEvents.FENCE_GATE_CLOSE, 0.85f, 0.5f);
            return InteractionResult.SUCCESS;
        } else if (this.getIsRobbed() && !this.getIsOpen()) {
            this.setIsOpen(true);
            CreateUnderwaterParticlesOnOpening(pPlayer.level(), (int) this.getX(), (int) this.getY(), (int) this.getZ());
            this.playSound(SoundEvents.FENCE_GATE_OPEN, 0.85f, 0.5f);
            return InteractionResult.SUCCESS;
        }
        return super.mobInteract(pPlayer, pHand);
    }

    private void ejectItems(Level level) {
        double x = this.getX();
        double y = this.getY();
        double z = this.getZ();

        if (!this.level().isClientSide) {
        //    LootTable loottable = level.getServer().getLootData().getLootTable(ModLootTables.DEFAULT_TREASURE_CHEST_LOOT);

            LootTable loottable = level.getServer().reloadableRegistries().getLootTable(ModLootTables.DEFAULT_TREASURE_CHEST_LOOT);

            LootParams lootParams = (new LootParams.Builder(level.getServer().overworld())).withParameter(LootContextParams.ORIGIN, Vec3.ZERO).withParameter(LootContextParams.THIS_ENTITY, this).create(LootContextParamSets.GIFT);
            List<ItemStack> list = loottable.getRandomItems(lootParams);

            Iterator<ItemStack> var6 = list.iterator();

            while (var6.hasNext()) {
                ItemStack itemstack = var6.next();
                ItemEntity itementity = new ItemEntity(level, x, y, z, itemstack);
                itementity.setDefaultPickUpDelay();
                level.addFreshEntity(itementity);
            }
        }
    }

    private void CreateUnderwaterParticlesOnOpening(Level level, int x, int y, int z) {
        BlockPos pos1 = new BlockPos(x, y, z);
        boolean isInWater = (level.getBlockState(pos1).getBlock() == Blocks.WATER);
        if (isInWater) {
            Vec3 position = this.position();

            for (int i = 0; i < 25; i++) {
                double xOffset = this.random.nextDouble() * 0.5 - 0.25; // Случайный смещение по X
                double yOffset = this.random.nextDouble() * 0.5 - 0.25; // Случайный смещение по Y
                double zOffset = this.random.nextDouble() * 0.5 - 0.25; // Случайный смещение по Z

                double xSpeed = (this.random.nextDouble() * 0.5) - 0.25; // Случайная скорость по X от -0.2 до 0.2
                double ySpeed = (this.random.nextDouble() * 0.5); // Случайная скорость по Y от -0.2 до 0.2
                double zSpeed = (this.random.nextDouble() * 0.5) - 0.25; // Случайная скорость по Z от -0.2 до 0.2
                // Добавляем частицы
                level.addParticle(ParticleTypes.BUBBLE_COLUMN_UP, // Тип частиц
                        position.x + xOffset, // Позиция X
                        position.y + yOffset + 0.5, // Позиция Y
                        position.z + zOffset, // Позиция Z
                        xSpeed, ySpeed + 0.1, zSpeed); // Скорость (0, 0, 0)
            }
        }
    }

    @Override
    public ItemStack getTreasureItem() {
        ItemStack stack = new ItemStack(ModItems.TREASURE_CHEST_ITEM.get());

        stack.set(ModDataComponents.TREASURE_CHEST_IS_ROBBED, this.getIsRobbed());
        stack.set(ModDataComponents.TREASURE_CHEST_IS_OPEN, this.getIsOpen());

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

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(IS_OPEN, Boolean.FALSE);
        builder.define(IS_ROBBED, Boolean.FALSE);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        this.setIsOpen(pCompound.getBoolean("IsOpen"));
        this.setIsRobbed(pCompound.getBoolean("IsRobbed"));
        super.readAdditionalSaveData(pCompound);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        pCompound.putBoolean("IsOpen", this.getIsOpen());
        pCompound.putBoolean("IsRobbed", this.getIsRobbed());
        super.addAdditionalSaveData(pCompound);
    }
}

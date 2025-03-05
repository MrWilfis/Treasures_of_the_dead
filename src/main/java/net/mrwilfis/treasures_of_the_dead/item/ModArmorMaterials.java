package net.mrwilfis.treasures_of_the_dead.item;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.mrwilfis.treasures_of_the_dead.Treasures_of_the_dead;

import java.util.function.Supplier;

public enum ModArmorMaterials implements ArmorMaterial {
    BICORN("bicorn", 22, new int[]{3, 5, 4, 5}, 15,
            SoundEvents.ARMOR_EQUIP_LEATHER, 0f, 0f, () -> Ingredient.of(Items.LEATHER)),
    PIRATE_CLOTHES1("pirate_clothes1", 6, new int[]{1, 2, 2, 1}, 20,
            SoundEvents.ARMOR_EQUIP_LEATHER, 0f, 0f, () -> Ingredient.of(Items.LEATHER)),
    PIRATE_CLOTHES2("pirate_clothes2", 6, new int[]{1, 2, 2, 1}, 22,
            SoundEvents.ARMOR_EQUIP_LEATHER, 0f, 0f, () -> Ingredient.of(Items.LEATHER)),
    PIRATE_CLOTHES3("pirate_clothes3", 6, new int[]{1, 2, 2, 1}, 18,
            SoundEvents.ARMOR_EQUIP_LEATHER, 0f, 0f, () -> Ingredient.of(Items.LEATHER)),
    CAPTAIN_CLOTHES1("captain_clothes1", 20, new int[]{3, 5, 4, 3}, 15,
            SoundEvents.ARMOR_EQUIP_LEATHER, 0f, 0f, () -> Ingredient.of(Items.LEATHER)),
    CAPTAIN_CLOTHES2("captain_clothes2", 20, new int[]{3, 5, 4, 3}, 15,
            SoundEvents.ARMOR_EQUIP_LEATHER, 0f, 0f, () -> Ingredient.of(Items.LEATHER));

    private final String name;
    private final int durabilityMultiplier;
    private final int[] protectionAmounts;
    private final int enchantmentValue;
    private final SoundEvent equipSound;
    private final float toughness;
    private final float knockbackResistance;
    private final Supplier<Ingredient> repairIngredient;

    private static final int[] BASE_DURABILITY = { 11, 16, 15, 13 };

    ModArmorMaterials(String name, int durabilityMultiplier, int[] protectionAmounts, int enchantmentValue, SoundEvent equipSound, float toughness, float knockbackResistance, Supplier<Ingredient> repairIngredient) {
        this.name = name;
        this.durabilityMultiplier = durabilityMultiplier;
        this.protectionAmounts = protectionAmounts;
        this.enchantmentValue = enchantmentValue;
        this.equipSound = equipSound;
        this.toughness = toughness;
        this.knockbackResistance = knockbackResistance;
        this.repairIngredient = repairIngredient;
    }

    @Override
    public int getDurabilityForType(ArmorItem.Type type) {
        return BASE_DURABILITY[type.ordinal()] * this.durabilityMultiplier;
    }

    @Override
    public int getDefenseForType(ArmorItem.Type type) {
        return this.protectionAmounts[type.ordinal()];
    }

    @Override
    public int getEnchantmentValue() {
        return enchantmentValue;
    }

    @Override
    public SoundEvent getEquipSound() {
        return this.equipSound;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return this.repairIngredient.get();
    }

    @Override
    public String getName() {
        return Treasures_of_the_dead.MOD_ID + ":" + this.name;
    }

    @Override
    public float getToughness() {
        return this.toughness;
    }

    @Override
    public float getKnockbackResistance() {
        return this.knockbackResistance;
    }
}

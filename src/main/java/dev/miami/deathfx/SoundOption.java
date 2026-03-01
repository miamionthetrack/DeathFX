package dev.miami.deathfx;

import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;

@SuppressWarnings("unused")
public enum SoundOption {
    Ding(SoundEvents.BLOCK_NOTE_BLOCK_BELL.value()),
    Sizzle(SoundEvents.BLOCK_FIRE_EXTINGUISH),
    Bell(SoundEvents.BLOCK_BELL_USE),
    Anvil(SoundEvents.BLOCK_ANVIL_LAND),
    Ghast(SoundEvents.ENTITY_GHAST_SCREAM),
    Bones(SoundEvents.ENTITY_SKELETON_AMBIENT),
    Beacon(SoundEvents.BLOCK_BEACON_ACTIVATE),
    Enderman(SoundEvents.ENTITY_ENDERMAN_TELEPORT),
    Experience(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP),
    Trident(SoundEvents.ITEM_TRIDENT_HIT);

    private final SoundEvent sound;

    SoundOption(SoundEvent event) {
        this.sound = event;
    }

    public SoundEvent getSound() {
        return this.sound;
    }
}
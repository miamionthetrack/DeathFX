package dev.miami.deathfx;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import net.minecraft.component.type.FireworkExplosionComponent;

@Config(name = "deathfx")
public class DeathFXConfigScreen implements ConfigData {
    public boolean summonLightning = true;
    public boolean playSound = false;
    public SoundOption soundOption = SoundOption.Ding;
    public boolean fireworksEnabled = false;
    public FireworksSettings fireworksSettings = new FireworksSettings();

    public static class FireworksSettings {
        public int color = 0xFF0000;
        public boolean twinkle = false;
        public FireworkExplosionComponent.Type fireworkType = FireworkExplosionComponent.Type.SMALL_BALL;
        public int particleMultiplier = 1;
    }
}
package dev.miami.deathfx;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.component.type.FireworkExplosionComponent;
import net.minecraft.text.Text;


@Environment(EnvType.CLIENT)
public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            DeathFXConfigScreen config = AutoConfig.getConfigHolder(DeathFXConfigScreen.class).getConfig();

            ConfigBuilder builder = ConfigBuilder.create()
                    .setParentScreen(parent)
                    .setTitle(Text.literal("DeathFX Configuration"))
                    .setTransparentBackground(true);

            ConfigEntryBuilder entryBuilder = builder.entryBuilder();

            ConfigCategory general = builder.getOrCreateCategory(Text.literal("General Effects"));

            general.addEntry(entryBuilder.startBooleanToggle(Text.literal("Summon Lightning"), config.summonLightning)
                    .setDefaultValue(true)
                    .setTooltip(Text.literal("Whether to summon lightning when a player dies"))
                    .setSaveConsumer(value -> config.summonLightning = value)
                    .build());

            general.addEntry(entryBuilder.startBooleanToggle(Text.literal("Play Sound"), config.playSound)
                    .setDefaultValue(false)
                    .setTooltip(Text.literal("Whether to play a sound when a player dies"))
                    .setSaveConsumer(value -> config.playSound = value)
                    .build());

            general.addEntry(entryBuilder.startEnumSelector(Text.literal("Death Sound"), SoundOption.class, config.soundOption)
                    .setDefaultValue(SoundOption.Ding)
                    .setTooltip(Text.literal("Select which sound to play on death"))
                    .setSaveConsumer(value -> config.soundOption = value)
                    .build());

            general.addEntry(entryBuilder.startBooleanToggle(Text.literal("Enable Fireworks"), config.fireworksEnabled)
                    .setDefaultValue(false)
                    .setTooltip(Text.literal("Whether to spawn fireworks when a player dies"))
                    .setSaveConsumer(value -> config.fireworksEnabled = value)
                    .build());

            ConfigCategory fireworks = builder.getOrCreateCategory(Text.literal("Fireworks Settings"));

            fireworks.addEntry(entryBuilder.startColorField(Text.literal("Firework Color"), config.fireworksSettings.color)
                    .setDefaultValue(0xFF0000)
                    .setTooltip(Text.literal("Color of the fireworks"))
                    .setSaveConsumer(value -> config.fireworksSettings.color = value)
                    .build());

            fireworks.addEntry(entryBuilder.startBooleanToggle(Text.literal("Twinkle Effect"), config.fireworksSettings.twinkle)
                    .setDefaultValue(false)
                    .setTooltip(Text.literal("Whether the fireworks should twinkle"))
                    .setSaveConsumer(value -> config.fireworksSettings.twinkle = value)
                    .build());

            fireworks.addEntry(entryBuilder.startEnumSelector(Text.literal("Firework Type"), FireworkExplosionComponent.Type.class, config.fireworksSettings.fireworkType)
                    .setDefaultValue(FireworkExplosionComponent.Type.SMALL_BALL)
                    .setTooltip(Text.literal("Shape of the fireworks"))
                    .setSaveConsumer(value -> config.fireworksSettings.fireworkType = value)
                    .build());

            fireworks.addEntry(entryBuilder.startIntSlider(Text.literal("Particle Multiplier"), config.fireworksSettings.particleMultiplier, 1, 5)
                    .setDefaultValue(1)
                    .setTooltip(Text.literal("Number of firework particles to spawn"))
                    .setSaveConsumer(value -> config.fireworksSettings.particleMultiplier = value)
                    .build());

            builder.setSavingRunnable(() -> AutoConfig.getConfigHolder(DeathFXConfigScreen.class).save());

            return builder.build();
        };
    }
}
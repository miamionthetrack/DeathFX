package dev.miami.deathfx.client;

import dev.miami.deathfx.DeathFXConfigScreen;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.component.type.FireworkExplosionComponent;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;

import java.util.Collections;

@Environment(EnvType.CLIENT)
public class DeathFXClient implements ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("DeathFX");
    public static final Identifier CUSTOM = Identifier.of("deathfx:custom");
    public static SoundEvent CUSTOM_SOUNDEVENT;

    @Override
    public void onInitializeClient() {
        LOGGER.info("Initializing DeathFX");
        AutoConfig.register(DeathFXConfigScreen.class, GsonConfigSerializer::new);
        Registry.register(Registries.SOUND_EVENT, CUSTOM, CUSTOM_SOUNDEVENT);
    }

    public static void runEffects(Vec3d pos) {
        DeathFXConfigScreen config = AutoConfig.getConfigHolder(DeathFXConfigScreen.class).getConfig();
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.world == null) return;

        if (config.summonLightning) {
            LightningEntity lightning = new LightningEntity(EntityType.LIGHTNING_BOLT, client.world);
            lightning.setPosition(pos);
            lightning.setCosmetic(true);
            client.world.addEntity(lightning);
        }

        if (config.playSound) {
            client.getSoundManager().play(new PositionedSoundInstance(
                    config.soundOption.getSound(),
                    SoundCategory.MASTER,
                    1.0F,
                    1.0F,
                    Random.create(),
                    BlockPos.ofFloored(pos)
            ));
        }

        if (config.fireworksEnabled) {
            spawnFireworks(pos, config);
        }
    }

    private static void spawnFireworks(Vec3d pos, DeathFXConfigScreen config) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.world == null) return;

        FireworkExplosionComponent explosionData = createFireworkExplosionData(config);

        for (int i = 0; i < config.fireworksSettings.particleMultiplier; ++i) {

            double offsetX = (Math.random() - 0.5) * 1.5;
            double offsetY = Math.random();
            double offsetZ = (Math.random() - 0.5) * 1.5;


            client.world.addFireworkParticle(
                    pos.x + offsetX,
                    pos.y + 0.5 + offsetY,
                    pos.z + offsetZ,
                    0, 0, 0,
                    Collections.singletonList(explosionData)
            );
        }
    }

    private static FireworkExplosionComponent createFireworkExplosionData(DeathFXConfigScreen config) {
        IntList colors = new IntArrayList();
        colors.add(config.fireworksSettings.color);
        IntList fadeColors = IntList.of();

        return new FireworkExplosionComponent(
                config.fireworksSettings.fireworkType,
                colors,
                fadeColors,
                config.fireworksSettings.twinkle,
                false
        );
    }

    static {
        CUSTOM_SOUNDEVENT = SoundEvent.of(CUSTOM);
    }
}
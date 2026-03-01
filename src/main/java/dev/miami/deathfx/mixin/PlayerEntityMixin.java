package dev.miami.deathfx.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.miami.deathfx.client.DeathFXClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerRemoveS2CPacket;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;
import java.util.UUID;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class PlayerEntityMixin {
    @Inject(
            at = @At("HEAD"),
            method = "onEntityStatus"
    )
    private void onDeath(EntityStatusS2CPacket packet, CallbackInfo ci) {
        if (packet.getStatus() == 3) {
            if (RenderSystem.isOnRenderThread()) {
                MinecraftClient client = MinecraftClient.getInstance();
                if (client.world != null) {
                    try {
                        Entity entity = packet.getEntity(client.world);
                        if (!(entity instanceof PlayerEntity)) {
                            return;
                        }

                        if (entity == client.player) {
                            return;
                        }

                        Vec3d pos = entity.getPos();
                        DeathFXClient.runEffects(pos);
                    } catch (Exception ignored) {
                    }
                }
            }
        }
    }

    @Inject(
            at = @At("HEAD"),
            method = "onPlayerRemove"
    )
    private void onPlayerRemove(PlayerRemoveS2CPacket packet, CallbackInfo ci) {
        try {
            if (!RenderSystem.isOnRenderThread()) {return;}

            MinecraftClient client = MinecraftClient.getInstance();

            if (client.getCurrentServerEntry() == null) {return;}

            if (client.getCurrentServerEntry().address == null) {return;}

            if (client.getCurrentServerEntry().address.equals("play.pvplegacy.net")) {
                for (UUID uuid : packet.profileIds() ) {
                    PlayerEntity entity = Objects.requireNonNull(client.world).getPlayerByUuid(uuid);
                    if (entity != null && entity != client.player) {
                        Vec3d pos = entity.getPos();
                        DeathFXClient.runEffects(pos);
                    }
                }
            }
        } catch (Exception ignored) {
        }
    }
}
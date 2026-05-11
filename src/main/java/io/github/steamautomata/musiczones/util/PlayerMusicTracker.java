package io.github.steamautomata.musiczones.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;

import java.util.HashSet;
import java.util.Set;

/**
 * Representation of a player and the last music the server has suggested to play next
 */
public class PlayerMusicTracker {
    public ServerPlayer player;
    public Set<ResourceLocation> lastMusics = new HashSet<>();
    public Vec3 lastPosition = new Vec3(0,0,0);

    public PlayerMusicTracker(ServerPlayer player) {
        this.player = player;
    }

    public boolean hasMoved() {
        if (lastPosition.distanceTo(player.position()) > 0.25d) {
            lastPosition = player.position();
            return true;
        }
        return false;
    }
}

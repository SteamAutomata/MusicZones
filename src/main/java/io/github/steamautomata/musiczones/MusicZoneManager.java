package io.github.steamautomata.musiczones;

import io.github.steamautomata.musiczones.remotes.SetZoneMusicPacket;
import io.github.steamautomata.musiczones.util.PlayerMusicTracker;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.*;

@EventBusSubscriber(modid = MusicZonesMod.MODID)
public class MusicZoneManager {
    public static void setDirty(Level level) {
        getSavedData(level).setDirty();
    }

    /** Creates an instance of a MusicZone */
    public static MusicZone addMusicZone(Level level, String label, Vec3 from, Vec3 to, ResourceLocation event, int priority) {
        var musicZone = new MusicZone(from, to, priority);
        musicZone.setSound(event);
        musicZone.label = label;
        return addMusicZone(level, label, musicZone);
    }

    public static MusicZone addMusicZone(Level level, String label, MusicZone musicZone) {
        var savedData = getSavedData(level);
        if (savedData.musicZones.containsKey(label)) {
            throw new IllegalStateException(String.format("Zone %s already exists!", label));
        }
        if (savedData.musicZones.containsKey(musicZone.label)) {
            throw new IllegalStateException(String.format("Trying to rename zone %s to %s using MusicZoneHandler.addMusicZone", musicZone.label, label));
        }
        musicZone.label = label;
        savedData.musicZones.put(label, musicZone);
        savedData.setDirty();
        return musicZone;
    }

    public static void deleteMusicZone(Level level, String label) {
        var savedData = getSavedData(level);
        if (!savedData.musicZones.containsKey(label)) {
            throw new IllegalStateException("Zone doesn't exists");
        }
        savedData.musicZones.remove(label);
        savedData.setDirty();
    }

    public static Optional<MusicZone> getMusicZone(Level level, String label) {
        return Optional.ofNullable(getSavedData(level).musicZones.get(label));
    }

    public static HashMap<String, MusicZone> getMusicZones(Level level) {
        var savedData = getSavedData(level);
        return savedData.musicZones;
    }

    public static Set<String> getMusicZoneLabels(Level level) {
        return getSavedData(level).musicZones.keySet();
    }

    public static MusicZonesSavedData getSavedData(Level w) {
        if (w instanceof ServerLevel level) {
            var dataStorage = level.getChunkSource().getDataStorage();
            return dataStorage.computeIfAbsent(
                    new SavedData.Factory<>(MusicZonesSavedData::create, MusicZonesSavedData::load),
                    MusicZonesSavedData.IDENTIFIER
            );
        }
        throw new IllegalStateException("GetSavedData can only be ran on Server");
    }

    private static final HashMap<ServerPlayer, PlayerMusicTracker> trackedPlayers = new HashMap<>();

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            var tracker = new PlayerMusicTracker(player);
            tracker.lastPosition = player.position();
            trackedPlayers.put(player,tracker);
        }
    }

    @SubscribeEvent
    public static void onPlayerLeft(PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            trackedPlayers.remove(player);
        }
    }

    private static int tickCounter = 0;
    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {
        if (++tickCounter % 10 != 0) return;

        for (var tracker : trackedPlayers.values()) {
            if (!tracker.hasMoved()) {
                continue;
            }

            var highestPriority = Integer.MIN_VALUE;
            var resourceLocations = new HashSet<ResourceLocation>();

            for (var zone : MusicZoneManager.getMusicZones(tracker.player.serverLevel()).values()) {
                if (!zone.isEnabled()) continue;
                if (!zone.overlaps(tracker.player.blockPosition())) continue;
                if (zone.priority < highestPriority) continue;

                if (zone.priority > highestPriority) {
                    highestPriority = zone.priority;
                    resourceLocations.clear();
                }

                resourceLocations.addAll(zone.getSounds());
            }

            // Apparently it checks if the contents are equals, not if the two objects are equal
            if (!resourceLocations.equals(tracker.lastMusics)) {
                tracker.lastMusics = resourceLocations;
                PacketDistributor.sendToPlayer(tracker.player, new SetZoneMusicPacket(new ArrayList<>(resourceLocations)));
            }
        }
    }
}

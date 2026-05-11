package io.github.steamautomata.musiczones;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

import java.util.*;

public class MusicZone {
    public Vec3 position1;
    public Vec3 position2;
    private final HashSet<ResourceLocation> associated_musics = new HashSet<>();
    public int priority;
    public String label;
    private boolean enabled = true;

    public MusicZone(Vec3 position1, Vec3 position2, int priority) {
        this.position1 = position1;
        this.position2 = position2;
        this.priority = priority;
    }

    public boolean overlaps(Vec3 p) {
        if (p.x <= position1.x-1 && p.x <= position2.x-1) return false;
        if (p.x >= position1.x+1 && p.x >= position2.x+1) return false;

        if (p.y <= position1.y-1 && p.y <= position2.y-1) return false;
        if (p.y >= position1.y+1 && p.y >= position2.y+1) return false;

        if (p.z <= position1.z-1 && p.z <= position2.z-1) return false;
        if (p.z >= position1.z+1 && p.z >= position2.z+1) return false;

        return true;
    }
    public boolean overlaps(BlockPos p) {
        return overlaps(p.getCenter());
    }


    public void setSound(ResourceLocation sound) {
        associated_musics.clear();
        associated_musics.add(sound);
    }
    public void addSound(ResourceLocation sound) {
        associated_musics.add(sound);
    }
    public void removeSound(ResourceLocation sound) {
        associated_musics.remove(sound);
    }
    public boolean hasSound(ResourceLocation sound) {
        return associated_musics.contains(sound);
    }

    public void setSounds(String sounds) {
        associated_musics.clear();
        String[] parts = sounds.split(",");

        for (String p : parts) {
            associated_musics.add(ResourceLocation.parse(p));
        }
    }

    public List<ResourceLocation> getSounds() {
        return new ArrayList<>(associated_musics);
    }

    public String getSoundsAsString() {
        return String.join(",", associated_musics.stream().map(ResourceLocation::toString).toArray(String[]::new));
    }

    // Might need to keep toString for later
    public String toStringCommandOutput() {
        return String.format("%s -> %s", label, getSoundsAsString());
    }

    public boolean isEnabled() {
        return enabled;
    }
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}

package io.github.steamautomata.musiczones;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

public class MusicZone {
    public Vec3 position1;
    public Vec3 position2;
    public ResourceLocation[] associated_musics;
    public int priority;
    public String label;

    public MusicZone(Vec3 position1, Vec3 position2, ResourceLocation[] associated_musics, int priority) {
        this.position1 = position1;
        this.position2 = position2;
        this.associated_musics = associated_musics;
        this.priority = priority;
    }

    public boolean overlaps(Vec3 p) {
        if (p.x <= position1.x && p.x <= position2.x) return false;
        if (p.x >= position1.x && p.x >= position2.x) return false;

        if (p.y <= position1.y && p.y <= position2.y) return false;
        if (p.y >= position1.y && p.y >= position2.y) return false;

        if (p.z <= position1.z && p.z <= position2.z) return false;
        if (p.z >= position1.z && p.z >= position2.z) return false;

        return true;
    }

    // Might need to keep toString for later
    public String toStringCommandOutput() {
        return String.format("%s -> %s", label, MusicZonesMod.resourceLocationsToString(associated_musics));
    }
}

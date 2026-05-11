package io.github.steamautomata.musiczones;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class MusicZonesSavedData extends SavedData {
    public static final String IDENTIFIER = "music_zones";
    public HashMap<String, MusicZone> musicZones = new HashMap<>();

    public static MusicZonesSavedData create() {
        return new MusicZonesSavedData();
    }

    public static MusicZonesSavedData load(CompoundTag levelNbt, HolderLookup.Provider lookupProvider) {
        MusicZonesSavedData data = new MusicZonesSavedData();
        data.musicZones.clear();

        ListTag zoneTags = levelNbt.getList("MusicZones", ListTag.TAG_COMPOUND);
        for (int i = 0; i < zoneTags.size(); i++) {
            var zt = zoneTags.getCompound(i);

            var zone = new MusicZone(
                    new Vec3(zt.getDouble("X1"), zt.getDouble("Y1"), zt.getDouble("Z1")),
                    new Vec3(zt.getDouble("X2"), zt.getDouble("Y2"), zt.getDouble("Z2")),
                    zt.getInt("Priority")
            );
            zone.setSounds(zt.getString("Sounds"));
            zone.label = zt.getString("Label");
            data.musicZones.put(zt.getString("Label"), zone);
        }

        return data;
    }

    @Override
    public @NotNull CompoundTag save(CompoundTag levelNbt, HolderLookup.Provider registries) {
        var musicZonesList = new ListTag();
        for (var entry : musicZones.entrySet()) {
            var zoneTag = new CompoundTag();
            var musicZone = entry.getValue();

            zoneTag.putDouble("X1", musicZone.position1.x());
            zoneTag.putDouble("Y1", musicZone.position1.y());
            zoneTag.putDouble("Z1", musicZone.position1.z());

            zoneTag.putDouble("X2", musicZone.position2.x());
            zoneTag.putDouble("Y2", musicZone.position2.y());
            zoneTag.putDouble("Z2", musicZone.position2.z());

            zoneTag.putInt("Priority", musicZone.priority);
            zoneTag.putString("Label", entry.getKey());
            zoneTag.putString("Sounds", musicZone.getSoundsAsString());

            musicZonesList.add(zoneTag);
        }
        levelNbt.put("MusicZones", musicZonesList);
        return levelNbt;
    }
}
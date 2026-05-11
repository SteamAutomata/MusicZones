package io.github.steamautomata.musiczones;

import io.github.steamautomata.musiczones.util.ZoneMusic;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.SelectMusicEvent;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber(modid = MusicZonesMod.MODID, value = Dist.CLIENT)
public class MusicZoneModClient {
    private static List<ResourceLocation> musicsToPlay = new ArrayList<>();
    private final static RandomSource random = RandomSource.create();
    @Nullable private static ResourceLocation lastMusic;
    private static int index = 0;

    // Fires when the player exits a zone or enters a zone
    public static void onZoneMusicPacketReceived(List<ResourceLocation> list) {
        musicsToPlay = list;

        // OnMusicSelect fires each tick, better to assign this value when the current music has finished playing or when the zone state has changed
        index = musicsToPlay.isEmpty() ? 0 : random.nextInt(musicsToPlay.size());
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onMusicSelect(SelectMusicEvent event) {
        var player = Minecraft.getInstance().player;
        if (player != null) {
            // Player is not inside any zone
            if (musicsToPlay.isEmpty()) {
                // A music is still playing
                if (lastMusic != null) {
                    event.setMusic(null);
                    lastMusic = null;
                }
                return;
            }

            var selected = musicsToPlay.get(index);
            var holder = Holder.direct(SoundEvent.createFixedRangeEvent(selected, 16F));
            var zoneMusic = new ZoneMusic(holder, 0, 0, true);
            event.setMusic(zoneMusic);
            lastMusic = selected;
        }
    }
}
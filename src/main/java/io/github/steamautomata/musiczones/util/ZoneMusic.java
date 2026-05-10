package io.github.steamautomata.musiczones.util;

import net.minecraft.core.Holder;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.SoundEvent;

public class ZoneMusic extends Music {
    public ZoneMusic(Holder<SoundEvent> event, int minDelay, int maxDelay, boolean replaceCurrentMusic) {
        super(event, minDelay, maxDelay, replaceCurrentMusic);
    }
}

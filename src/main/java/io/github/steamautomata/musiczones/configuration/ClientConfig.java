package io.github.steamautomata.musiczones.configuration;

import io.github.steamautomata.musiczones.MusicZonesMod;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

@EventBusSubscriber(modid = MusicZonesMod.MODID, value = Dist.CLIENT)
public class ClientConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.BooleanValue STOP_MUSIC_AREA_ON_LEAVE = BUILDER.comment("Stops the area music when you leave the area if true. Otherwise, the music will still play even if you leave.").define("stopMusicOnAreaExit", true);

    //public static final ModConfigSpec.ConfigValue<String> MAGIC_NUMBER_INTRODUCTION = BUILDER.comment("What you want the introduction message to be for the magic number").define("magicNumberIntroduction", "The magic number is... ");

    // a list of strings that are treated as resource locations for items
    //private static final ModConfigSpec.ConfigValue<List<? extends String>> ITEM_STRINGS = BUILDER.comment("A list of items to log on common setup.").defineListAllowEmpty("items", List.of("minecraft:iron_ingot"), Config::validateItemName);

    public static final ModConfigSpec CLIENT_SPEC = BUILDER.build();

    public static boolean stopMusicOnLeavingArea;

    private static boolean validateItemName(final Object obj) {
        return obj instanceof String itemName && BuiltInRegistries.ITEM.containsKey(ResourceLocation.parse(itemName));
    }

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        stopMusicOnLeavingArea = STOP_MUSIC_AREA_ON_LEAVE.get();
    }
}

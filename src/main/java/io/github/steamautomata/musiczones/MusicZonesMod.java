package io.github.steamautomata.musiczones;

import com.mojang.logging.LogUtils;
import io.github.steamautomata.musiczones.commands.arguments.ZoneLabelArgument;
import io.github.steamautomata.musiczones.configuration.ClientConfig;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.slf4j.Logger;

import java.util.Arrays;

@net.neoforged.fml.common.Mod(MusicZonesMod.MODID)
public class MusicZonesMod {
    public static final String MODID = "musiczones";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister<ArgumentTypeInfo<?,?>> ARGUMENT_TYPES = DeferredRegister.create(BuiltInRegistries.COMMAND_ARGUMENT_TYPE, MODID);

    public static final DeferredHolder<ArgumentTypeInfo<?, ?>, SingletonArgumentInfo<ZoneLabelArgument>> MUSIC_ZONES_COMMAND =
        ARGUMENT_TYPES.register("musiczones", () -> ArgumentTypeInfos.registerByClass(ZoneLabelArgument.class, SingletonArgumentInfo.contextFree(ZoneLabelArgument::new)));


    public MusicZonesMod(IEventBus modEventBus, ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.CLIENT, ClientConfig.CLIENT_SPEC);
        ARGUMENT_TYPES.register(modEventBus);
    }

    public static String resourceLocationsToString(ResourceLocation[] locations) {
        return String.join(",", Arrays.stream(locations).map(ResourceLocation::toString).toList());
    }

    public static ResourceLocation[] stringToResourceLocation(String locations) {
        return Arrays.stream(locations.split(",")).map(ResourceLocation::parse).toArray(ResourceLocation[]::new);
    }
}

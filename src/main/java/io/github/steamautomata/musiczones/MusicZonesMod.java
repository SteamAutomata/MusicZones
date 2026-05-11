package io.github.steamautomata.musiczones;

import com.mojang.logging.LogUtils;
import io.github.steamautomata.musiczones.configuration.ClientConfig;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.slf4j.Logger;


@net.neoforged.fml.common.Mod(MusicZonesMod.MODID)
public class MusicZonesMod {
    public static final String MODID = "musiczones";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister<ArgumentTypeInfo<?,?>> ARGUMENT_TYPES = DeferredRegister.create(BuiltInRegistries.COMMAND_ARGUMENT_TYPE, MODID);

    public MusicZonesMod(IEventBus modEventBus, ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.CLIENT, ClientConfig.CLIENT_SPEC);
        ARGUMENT_TYPES.register(modEventBus);
    }
}

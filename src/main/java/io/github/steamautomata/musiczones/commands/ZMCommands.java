package io.github.steamautomata.musiczones.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.steamautomata.musiczones.MusicZonesMod;
import net.minecraft.commands.CommandSourceStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

@EventBusSubscriber(modid = MusicZonesMod.MODID)
public class ZMCommands {
    @SubscribeEvent()
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        event.getDispatcher().register(
            LiteralArgumentBuilder.<CommandSourceStack>literal("musiczones")
                .requires(cs -> cs.hasPermission(2))
                    .then(AddCommand.register())
                    .then(RemoveCommand.register())
                    .then(GetOverlappingCommand.register())
                    .then(GetAllCommand.register())
        );
    }
}

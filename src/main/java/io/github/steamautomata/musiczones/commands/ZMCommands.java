package io.github.steamautomata.musiczones.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.github.steamautomata.musiczones.MusicZoneManager;
import io.github.steamautomata.musiczones.MusicZonesMod;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

import java.util.concurrent.CompletableFuture;

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
                    .then(EditCommand.register())
        );
    }

    public static CompletableFuture<Suggestions> getSuggestions(CommandContext<CommandSourceStack> ctx, SuggestionsBuilder builder) {
        var src = ctx.getSource();
        var labels = MusicZoneManager.getMusicZoneLabels(src.getLevel());
        return SharedSuggestionProvider.suggest(labels, builder);
    }
}

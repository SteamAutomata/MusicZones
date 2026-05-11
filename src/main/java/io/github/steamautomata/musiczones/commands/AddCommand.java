package io.github.steamautomata.musiczones.commands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.steamautomata.musiczones.MusicZoneManager;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.commands.synchronization.SuggestionProviders;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;

public class AddCommand {
    static ArgumentBuilder<CommandSourceStack, ?> register() {
        // Horrible
        return Commands.literal("new")
            .then(Commands.argument("from", BlockPosArgument.blockPos())
                .then(Commands.argument("to", BlockPosArgument.blockPos())
                    .then(Commands.argument("sound", ResourceLocationArgument.id())
                        .suggests(SuggestionProviders.AVAILABLE_SOUNDS)
                        .then(Commands.argument("label", StringArgumentType.word())
                            .then(Commands.argument("priority", IntegerArgumentType.integer())
                                    .executes(AddCommand::addMusicZone)
                            )
                        )
                    )
                )
            );
    }

    private static int addMusicZone(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        Level level = ctx.getSource().getLevel();

        var pos1 = BlockPosArgument.getBlockPos(ctx, "from");
        var pos2 = BlockPosArgument.getBlockPos(ctx, "to");
        var label = StringArgumentType.getString(ctx, "label");
        var sound = ResourceLocationArgument.getId(ctx, "sound");
        var priority = IntegerArgumentType.getInteger(ctx, "priority");

        if (MusicZoneManager.getMusicZone(level, label).isPresent()) {
            throw CommandExceptions.ERROR_ALREADY_EXISTS.create(label);
        }

        MusicZoneManager.addMusicZone(level, label, pos1.getCenter(), pos2.getCenter(), sound, priority);
        ctx.getSource().sendSuccess(() -> Component.literal("Created a new zone"), true); // TODO: Translation
        return 1;
    }
}

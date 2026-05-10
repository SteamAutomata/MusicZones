package io.github.steamautomata.musiczones.commands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.github.steamautomata.musiczones.MusicZoneManager;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;

public class AddCommand {
    static ArgumentBuilder<CommandSourceStack, ?> register() {
        // Horrible
        return Commands.literal("new")
            .then(Commands.argument("from", BlockPosArgument.blockPos())
                .then(Commands.argument("to", BlockPosArgument.blockPos())
                    .then(Commands.argument("resourceLocations", StringArgumentType.string())
                        .then(Commands.argument("label", StringArgumentType.word())
                            .then(Commands.argument("priority", IntegerArgumentType.integer())
                                    .executes(AddCommand::addMusicZone)
                            )
                        )
                    )
                )
            );
    }

    private static int addMusicZone(CommandContext<CommandSourceStack> ctx) {
        Level level = ctx.getSource().getLevel();

        var pos1 = BlockPosArgument.getBlockPos(ctx, "from");
        var pos2 = BlockPosArgument.getBlockPos(ctx, "to");
        var label = StringArgumentType.getString(ctx, "label");
        var resourceLocations = StringArgumentType.getString(ctx, "resourceLocations"); // TODO: Create ResourceLocationListArgument
        var priority = IntegerArgumentType.getInteger(ctx, "priority");

        if (MusicZoneManager.getMusicZone(level, label).isPresent()) {
            ctx.getSource().sendFailure(Component.literal(String.format("Zone %s already exists!", label)));
            return 0;
        }

        MusicZoneManager.addMusicZone(level, label, pos1.getCenter(), pos2.getCenter(), resourceLocations, priority);
        ctx.getSource().sendSuccess(() -> Component.literal("Created a new zone"), true); // TODO: Translation
        return 1;
    }
}

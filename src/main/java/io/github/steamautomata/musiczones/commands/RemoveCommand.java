package io.github.steamautomata.musiczones.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.github.steamautomata.musiczones.MusicZoneManager;
import io.github.steamautomata.musiczones.commands.arguments.ZoneLabelArgument;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;

public class RemoveCommand {
    static ArgumentBuilder<CommandSourceStack, ?> register() {
        // Horrible
        return Commands.literal("remove")
            .then(Commands.argument("label", new ZoneLabelArgument())
                .executes(RemoveCommand::removeMusicZone)
            );
    }

    private static int removeMusicZone(CommandContext<CommandSourceStack> ctx) {
        Level level = ctx.getSource().getLevel();
        var label = StringArgumentType.getString(ctx, "label");

        if (MusicZoneManager.getMusicZone(level, label).isPresent()) {
            MusicZoneManager.deleteMusicZone(level, label);
            ctx.getSource().sendSuccess(() -> Component.literal(String.format("Zone %s has been deleted.", label)), true);
            return 0;
        }

        ctx.getSource().sendFailure(Component.literal(String.format("Zone %s don't exist!", label)));
        return 1;
    }
}

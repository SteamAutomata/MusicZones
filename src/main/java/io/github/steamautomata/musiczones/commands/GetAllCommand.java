package io.github.steamautomata.musiczones.commands;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import io.github.steamautomata.musiczones.MusicZone;
import io.github.steamautomata.musiczones.MusicZoneManager;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;

import java.util.ArrayList;

public class GetAllCommand {
    private static final SimpleCommandExceptionType ERROR_FAILED = new SimpleCommandExceptionType(Component.literal("No zones created"));

    static ArgumentBuilder<CommandSourceStack, ?> register() {
        return Commands.literal("get_all").executes(ctx -> GetAllCommand.getAll(ctx, 0));
    }

    private static int getAll(CommandContext<CommandSourceStack> ctx, int page) throws CommandSyntaxException {
        Level level = ctx.getSource().getLevel();

        var zones = new ArrayList<MusicZone>();
        for (var entry : MusicZoneManager.getMusicZones(level).entrySet()) {
            zones.add(entry.getValue());
        }

        if (zones.isEmpty()) {
            throw ERROR_FAILED.create();
        }

        ctx.getSource().sendSuccess(() -> Component.literal(
                String.format("%s created zones:", zones.size())
        ), true);

        for (int i = 0; i < zones.size(); i++) {
            var zone = zones.get(i);
            final int finalI = i;
            ctx.getSource().sendSuccess(() -> Component.literal(
                    String.format(" %s. %s", finalI, zone.toStringCommandOutput())
            ), true);
        }
        return 1;
    }
}

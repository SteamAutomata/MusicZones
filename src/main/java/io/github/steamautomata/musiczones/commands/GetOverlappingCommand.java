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
import java.util.Comparator;

public class GetOverlappingCommand {
    private static final SimpleCommandExceptionType ERROR_FAILED = new SimpleCommandExceptionType(Component.literal("No zones found"));

    static ArgumentBuilder<CommandSourceStack, ?> register() {
        return Commands.literal("get_overlapping").executes(GetOverlappingCommand::getOverlapping);
    }

    private static int getOverlapping(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        Level level = ctx.getSource().getLevel();

        var overlappingZones = new ArrayList<MusicZone>();
        for (var entry : MusicZoneManager.getMusicZones(level).entrySet()) {
            if (entry.getValue().overlaps(ctx.getSource().getPosition())) {
                overlappingZones.add(entry.getValue());
            }
        }

        if (overlappingZones.isEmpty()) {
            throw ERROR_FAILED.create();
        }

        overlappingZones.sort(Comparator.comparing(z -> z.priority));

        ctx.getSource().sendSuccess(() -> Component.literal(
                String.format("Found %s zones:", overlappingZones.size())
        ), true);

        for (int i = 0; i < overlappingZones.size(); i++) {
            var zone = overlappingZones.get(i);
            final int finalI = i;
            ctx.getSource().sendSuccess(() -> Component.literal(
                    String.format(" %s. %s", finalI, zone.toStringCommandOutput())
            ), true);
        }
        return 1;
    }
}

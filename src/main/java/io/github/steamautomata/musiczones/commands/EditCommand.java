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
import net.minecraft.commands.synchronization.SuggestionProviders;
import net.minecraft.network.chat.Component;

public class EditCommand {
    static ArgumentBuilder<CommandSourceStack, ?> register() {
        // Horrible
        return Commands.literal("edit")
            .then(Commands.argument("label", StringArgumentType.word())
                    .suggests(ZMCommands::getSuggestions)
                    .then(Commands.literal("sound")
                            .then(Commands.literal("set")
                                    .then(Commands.argument("sound", ResourceLocationArgument.id())
                                            .suggests(SuggestionProviders.AVAILABLE_SOUNDS)
                                            .executes(ctx -> changeSound(ctx, OPERATION.SET))))
                            .then(Commands.literal("add")
                                    .then(Commands.argument("sound", ResourceLocationArgument.id())
                                            .suggests(SuggestionProviders.AVAILABLE_SOUNDS)
                                            .executes(ctx -> changeSound(ctx, OPERATION.ADD))))
                            .then(Commands.literal("remove")
                                    .then(Commands.argument("sound", ResourceLocationArgument.id())
                                            .suggests(SuggestionProviders.AVAILABLE_SOUNDS)
                                            .executes(ctx -> changeSound(ctx, OPERATION.REMOVE))))
                    )

                    .then(Commands.literal("set_priority")
                            .then(Commands.argument("priority", IntegerArgumentType.integer())
                                    .executes(EditCommand::setPriority)))

                    .then(Commands.literal("rename")
                            .then(Commands.argument("new_label", StringArgumentType.word())
                                    .executes(EditCommand::rename)))

                    .then(Commands.literal("enable")
                            .executes(ctx -> setEnabled(ctx, true)))

                    .then(Commands.literal("disable")
                            .executes(ctx -> setEnabled(ctx, false)))
            );
    }

    private enum OPERATION {
        ADD,
        SET,
        REMOVE
    }

    private static int setEnabled(CommandContext<CommandSourceStack> ctx, boolean enabled) throws CommandSyntaxException {
        var level = ctx.getSource().getLevel();
        var label = StringArgumentType.getString(ctx, "label");
        var zone = MusicZoneManager.getMusicZone(level, label).orElseThrow(() -> CommandExceptions.ERROR_NOT_FOUND.create(label));

        if (zone.isEnabled() == enabled) {
            throw enabled
                ? CommandExceptions.ERROR_ALREADY_ENABLED.create(label)
                : CommandExceptions.ERROR_ALREADY_DISABLED.create(label);
        }

        zone.setEnabled(enabled);
        MusicZoneManager.setDirty(level);

        ctx.getSource().sendSuccess(() -> enabled
                ? Component.literal(String.format("Zone %s has enabled.", label))
                : Component.literal(String.format("Zone %s has been disabled.", label))
        , true);

        return 1;
    }


    private static int rename(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        var level = ctx.getSource().getLevel();
        var label = StringArgumentType.getString(ctx, "label");
        var zone = MusicZoneManager.getMusicZone(level, label).orElseThrow(() -> CommandExceptions.ERROR_NOT_FOUND.create(label));

        var newLabel = StringArgumentType.getString(ctx, "new_label");

        MusicZoneManager.deleteMusicZone(level, label);
        MusicZoneManager.addMusicZone(level, newLabel, zone);

        ctx.getSource().sendSuccess(() -> Component.literal(String.format("Zone %s has been renamed to %s.", label, newLabel)), true);
        return 1;
    }

    private static int changeSound(CommandContext<CommandSourceStack> ctx, OPERATION operation) throws CommandSyntaxException {
        var level = ctx.getSource().getLevel();
        var label = StringArgumentType.getString(ctx, "label");
        var zone = MusicZoneManager.getMusicZone(level, label).orElseThrow(() -> CommandExceptions.ERROR_NOT_FOUND.create(label));

        var newSound = ResourceLocationArgument.getId(ctx, "sound");

        var pattern = switch (operation) {
            case ADD -> {
                zone.addSound(newSound);
                yield "Added %s to %s";
            }
            case SET -> {
                zone.setSound(newSound);
                yield "Set %s to %s";
            }
            case REMOVE -> {
                if (!zone.hasSound(newSound)) {
                    throw CommandExceptions.ERROR_SOUND_NOT_FOUND.create(label, newSound);
                }
                zone.removeSound(newSound);
                yield "Removed %s from %s";
            }
        };
        MusicZoneManager.setDirty(level);

        ctx.getSource().sendSuccess(() -> Component.literal(String.format(pattern, newSound, label)), true);
        return 1;
    }

    private static int setPriority(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        var level = ctx.getSource().getLevel();
        var label = StringArgumentType.getString(ctx, "label");
        var zone = MusicZoneManager.getMusicZone(level, label).orElseThrow(() -> CommandExceptions.ERROR_NOT_FOUND.create(label));

        var priority = IntegerArgumentType.getInteger(ctx, "priority");
        var oldPriority = zone.priority;

        zone.priority = priority;
        MusicZoneManager.setDirty(level);

        ctx.getSource().sendSuccess(() -> Component.literal(String.format("Zone %s priority has been set from %s to %s.", label, oldPriority, priority)), true);
        return 1;
    }
}

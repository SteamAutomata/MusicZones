package io.github.steamautomata.musiczones.commands;

import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import net.minecraft.network.chat.Component;

public class CommandExceptions {
    public static final DynamicCommandExceptionType ERROR_NOT_FOUND = new DynamicCommandExceptionType(
            o -> Component.literal(String.format("Can't find zone %s!", o)));
    public static final DynamicCommandExceptionType ERROR_ALREADY_EXISTS = new DynamicCommandExceptionType(
            o -> Component.literal(String.format("Zone %s already exists!", o)));
    public static final DynamicCommandExceptionType ERROR_ALREADY_ENABLED = new DynamicCommandExceptionType(
            o -> Component.literal(String.format("Zone %s already enabled!", o)));
    public static final DynamicCommandExceptionType ERROR_ALREADY_DISABLED = new DynamicCommandExceptionType(
            o -> Component.literal(String.format("Zone %s already disabled!", o)));
    public static final Dynamic2CommandExceptionType ERROR_SOUND_NOT_FOUND = new Dynamic2CommandExceptionType(
            (o1, o2) -> Component.literal(String.format("Zone %s doesn't have %s!", o1, o2)));
}

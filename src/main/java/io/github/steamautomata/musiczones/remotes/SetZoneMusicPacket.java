package io.github.steamautomata.musiczones.remotes;

import io.github.steamautomata.musiczones.MusicZonesMod;
import io.github.steamautomata.musiczones.MusicZoneModClient;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.ArrayList;
import java.util.List;

public record SetZoneMusicPacket(List<ResourceLocation> musics) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<SetZoneMusicPacket> ID =
        new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(MusicZonesMod.MODID, "zone_musics_to_play"));

    public static final StreamCodec<ByteBuf, SetZoneMusicPacket> CODEC =
        StreamCodec.composite(
            ByteBufCodecs.collection(ArrayList::new, ResourceLocation.STREAM_CODEC),
            SetZoneMusicPacket::musics,
            SetZoneMusicPacket::new
        );

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return ID;
    }

    public static void handle(SetZoneMusicPacket message, IPayloadContext ctx) {
        ctx.enqueueWork(() -> MusicZoneModClient.onZoneMusicPacketReceived(message.musics()));
    }
}

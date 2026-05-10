package io.github.steamautomata.musiczones.remotes;

import io.github.steamautomata.musiczones.MusicZonesMod;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = MusicZonesMod.MODID)
public class ZMNetworking {
    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");
        
        registrar.playToClient(
                SetZoneMusicPacket.ID,
                SetZoneMusicPacket.CODEC,
                SetZoneMusicPacket::handle
        );
    }
}

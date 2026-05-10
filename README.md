# Music Zones

An experimental Minecraft NeoForge mod that adds music zones. Pretty much like custom biome musics, but instead of biomes, you define specific coordinates using commands. Perfect for mapmaking or survival multiplayer worlds that needs to be more lively.

Music Zones is licensed GNU GPLv3

# Why?

I used mods like Etched, WaterMedia and other music mods to have musics related to specific areas. Like shops, dungeons or even a whole city. Like in a RPG!

But most of these mods have their own issues. So I came up with a a fair trade:
- Players with operator permissions are able to create Music Zones using the `/musiczones` command.
- The zones can play multiple sound events, it should work with resource packs that adds sounds events too.
- Using priorities, you can put zones inside zones, and the zone with the highest priority will be played first.
- Zones are identified by a unique label.
- Command blocks can create and remove zones
- Integrates poorly with vanilla minecraft music

There is valid concerns about performance as the mod needs to loop over every Music Zones created, the tickrate might suffer from iterating over hundred of zones.

# Any issues?

Create an Issue or a Pull Request. No matter how important it is, even if it's for fixing a typo in the README.
But please make sure the issue is caused by Music Zones.

I won't support Fabric nor other Minecraft versions, feel free to port. 

# TODO
For transparency reasons, those planned features needs to be added and are not implemented yet:
- [] Don't run `MusicZoneManager::onServerTick` every tick
- [] Add enable/disable to music zones for better management with command blocks
- [] Refactor the whole source code and hope it won't break existing worlds
- [] ZoneLabelArgument is not working properly yet
- [] Create a ResourceLocationListArgument, or find a way to accept multiple resource locations, or even add resource locations to a zone later.
- [] Actually, also be able to completely edit zones after they have been created 

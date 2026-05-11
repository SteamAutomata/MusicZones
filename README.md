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

# Performance impact
My approach is not meant for thousands of zones in the server. It goes over every players, and loops for every zones inside their respective dimension. The more zones, the bigger the performance impact is. With Spark, it should be easy to determine if Music Zones is responsible for the tick rate impact since there is no mixins (it only use NeoForge events).

Zones are stored per dimensions, for example, a player in the End won't need to check for zones inside the Overworld.

The check also only runs for players who have moved at least one block. 

I've been told to store zones in chunks, but it's too complex to implement for now, and this mod is just a proof of context to see if anyone will find a use to it.

# Any issues?
Create an Issue or a Pull Request. No matter how important it is, even if it's for fixing a typo in the README.
But please make sure the issue is caused by Music Zones.


I won't support Fabric nor other Minecraft versions, feel free to port.
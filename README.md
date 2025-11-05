# RtpQueuePlus Plugin Description

## Overview
RtpQueuePlus is a Minecraft plugin for Paper/Spigot 1.20+ that allows players to queue for 1v1 duels in randomly teleported arenas. Players can join different arena types and will be matched with another player when enough people are in the queue.

## Commands
The plugin provides two identical commands that can be used interchangeably:

- **`/1v1`** - Opens the queue GUI
- **`/rtpqueue`** - Opens the queue GUI (alternative command)
- **`/1v1 leave`** or **`/rtpqueue leave`** - Leaves the current queue
- **`/1v1 reload`** or **`/rtpqueue reload`** - Reloads the plugin configuration (requires `rtpqueueplus.admin` permission)

## Permissions
- **`rtpqueueplus.use`** - Allows players to use the queue commands (default: true for all players)
- **`rtpqueueplus.admin`** - Allows reloading the plugin configuration (default: op)

## How It Works
1. Players use `/1v1` or `/rtpqueue` to open the GUI
2. Players select an arena type from the GUI (Grass, Desert, Mushroom, or Mesa)
3. Players are added to a queue for that arena type
4. When two players are in the same queue, they are teleported to random locations in the specified world
5. Players face each other and are frozen for 3 seconds before the duel begins
6. Players can leave the queue at any time using `/1v1 leave` or `/rtpqueue leave`

## Configuration
The plugin uses a configurable system for arena types and teleportation settings in the [config.yml](file://d:\goatPlugins\RtpQueuePlus\target\classes\config.yml) file:

- **Arena Types**: Grass, Desert, Mushroom, and Mesa with configurable materials, names, and worlds
- **Teleport Settings**: Configurable distance ranges, Y-level limits, and safe teleport options
- **GUI Settings**: Customizable title and layout

## Arena Types
- **Grass Arena**: Teleports to the overworld with grass blocks
- **Desert Arena**: Teleports to the nether with sand
- **Mushroom Arena**: Teleports to the end with mushroom blocks
- **Mesa Arena**: Teleports to the overworld with terracotta

Players will be randomly teleported within the configured distance ranges when a duel starts.

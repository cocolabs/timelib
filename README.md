# Tickrate Changer Mod

This is a Forge mod for Mincraft version 1.15 that lets you slow down time making everything in your environment move slower. This includes all game animations (including the player), but does not affect the camera.

## Where do I download it?

- Check the [releases](https://github.com/yooksi/trcm/releases) section in project repository page to get the latest release.

## How do I install it?

- Make sure you have the appropriate Forge [version](https://github.com/yooksi/trcm/blob/master/gradle.properties#L9)-[build](https://github.com/yooksi/trcm/blob/master/gradle.properties#L10) installed.
- Place the mod `jar` in game `mods` folder as per standard mod installation.
- Download the appropriate MixinBootstrap [version](https://github.com/yooksi/trcm/blob/master/gradle.properties#L13) from the [releases](https://github.com/LXGaming/MixinBootstrap/releases) section of the project repository page and place it in game `mods` folder alongside the mod `jar` as instructed by the previous step.

### Why do I need an external mod?

Due to the nature of how Forge works Mixin needs a bit of help to work in production environment with Forge. MixinBootstrap does just that, it's only function is to enable Mixin to work with Forge, that's it. If you are interested in learning more about Mixin environment read [Introduction to Mixins: The Mixin Environment](https://github.com/SpongePowered/Mixin/wiki/Introduction-to-Mixins---The-Mixin-Environment).

## How do I use it?

Tick rate is changed through the use of the following game commands:

- `\t <rate>` - change tick rate to a desired value (min 0.1, max 20).
- `\t` - reset tick rate to game default value (20).

## Technical details

The process used to accomplish this is complex and involves bytecode manipulation using [Mixin](https://github.com/SpongePowered/Mixin). The technical side of things can be difficult to understand if you are not familiar with [ASM](https://asm.ow2.io/), however this is the simple version of what happens inside the mod that allows us to slow down time:

- During runtime Mixin will redirect method flow at a particular point in `MinecraftServer.run()` method and inject a callback to itself. The point of injection is the `while` loop that handles the server time calculation and determines when each tick should happen.
- We then do a series of calculations and determine how much milliseconds passed this tick based on the `mspt` (milliseconds per tick) rate calculated from tick rate set by the player via game command.
- From there it's only a matter of incrementing `serverTime` and setting when tasks will be completed (`runTasksUntil`) with the calculated value. The rest of the replaced loop is pure vanilla code.

The steps above slow down server tick rate but unfortunately result in frame skipping when rendering. This is caused by the client timer not calculating elapsed ticks properly, and here is how we deal with that: 

- During runtime Mixin will redirect field access of `tickLength` in `net.minecraft.util.Timer` class to return `mspt` (milliseconds per tick) calculated from tick rate set by player via game command.
- Special care needs to be taken when dealing with pistons (see issue #3).

## For developers

- Clone the repository with Git to your local disk.
- Import the project using your preferred IDE (IntelliJ IDEA is recommended).
- Workspace should automatically be setup by gradle (dependencies, decompilation etc.)
- Run gradle task `getIntellijRuns` if you are using IntelliJ or `genEclipseRuns` if you are using Eclipse to generate your project run configurations. Here are examples of how to run the command:
  - `gradlew getIntellijRuns` on Windows.
  - `./gradlew genEclipseRuns` on Linux.
- Run `gradlew build` to compile and obfuscate your mod as a `jar` in `build/libs/`.

## Credits

- [Unregkiller](https://github.com/Unregkiller) - for commissioning the creation of this mod.
- [gnembon](https://github.com/gnembon) - for creating [Carpet](https://github.com/gnembon/fabric-carpet/blob/master/src/main/java/carpet/mixins/MinecraftServer_tickspeedMixin.java) on which this mod is based of.
- [MDC](https://www.moddevcafe.com/) and [MMD](https://discordapp.com/invite/EDbExcX) communities - for helping resolve technical issues. 
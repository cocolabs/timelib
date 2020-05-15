# TimeLib

TimeLib is a Minecraft Forge modding library that lets you control game time.

## Motivation

Time is a very powerful game aspect that is often neglected. Changing how time behaves has the potential to dramatically alter game experience. For example, longer days and night could give players a greater sense of adventure by promoting immersion over fast-paced action, slow-motion effects applied in critical moments of battle could create a great feeling of excitement. 

Through the use of Mixin and a simple to use API to configure various time related settings TimeLib gives developers the tools they need to create game changing mods.

## Features

- Allows you to set game tick rate resulting in slower or faster movements and animations
 including all mobs and players, but does not affect the camera.

- Allows you to set time cycle speed and control how fast days and nights last.

## Where to get it?

Each repository production and maven artifacts release contains three `jar` types that you can download:

- `-dev.jar` is a non-obfuscated version of the jar used by developers.
- `-sources.jar` contains project source files used by developers.
- `-.jar` is an obfuscated production-ready jar mostly used by players. 

**Developers** will want either the dev or production jar (optionally) accompanied by sources jar to make reading and understanding the library code easier when working with their mods.

**Players** will want only the production jar found in the repository release section on [Github](#github) which they should treat as a standard game mod (see [installation](#how-to-install-it) section for more information).

### Maven

TimeLib is hosted on [JitPack](https://jitpack.io/#yooksi/TimeLib) so head over there and get the latest release.

Here is the **recommended** way of getting the library in your project:

```groovy
// Definines where Gradle should look for declared dependencies
// Declare this AFTER the buildscript block (first script block)
// and BEFORE MinecraftForge Gradle plugin configuration
repositories {
	...
	maven { url 'https://jitpack.io' }
}

minecraft {
	...
}

dependencies {
    // Specify the version of Minecraft to use
    minecraft "net.minecraftforge:forge:${minecraftVersion}-${forgeVersion}"
    
    // We need to compile with the api but don't need it during runtime
    compileOnly "com.github.yooksi:TimeLib:${timeLibVersion}:api"
     // We need the main jar during runtime but not when compiling
    runtimeOnly "com.github.yooksi:TimeLib:${timeLibVersion}:dev"
}
```

*Note that the `timeLibVersion` property in this example was defined in `gradle.properties` to make accessing and reading version numbers easier. You should update the property (or just replace the variable) to a fully qualified version of the library you want to use.*

The example above would attempt to resolve the following artifacts from Jitpack:

- API library module for other mods to interact with. We need this jar when we are writing and compiling  our mod so we use the `compileOnly` strategy. The API is also needed during runtime but in our case it is already included in the dev jar that is included on runtime classpath.

- *Deobfuscated* version of our mod (indicated by the `dev` classifier). The dependency would be exposed only during runtime because we added it to `runtimeOnly` configuration.

Another way to get the library would be to use `fg.deobf` right after declaring the configuration type to indicate that the production jar should be deobfuscated after being resolved. This is not necessary and just adds extra work during build phase, this is why the project provides the `dev` jar. Besides, this way you need to manually attach source files since the deobfuscated jar ends up in Forge cache folder.  

### Github

Check the [releases](https://github.com/yooksi/TimeLib/releases) section in project repository page to get the latest release. This is the recommended way to obtain the production jar for players, developers should use this way only if JitPack is not working and they are not afraid to do things manually.

## How to install it?

- Make sure you have a backward compatible Forge [version](https://github.com/yooksi/trcm/blob/master/gradle.properties#L11) installed.
- Place the mod `jar` in game `mods` folder as per standard mod installation.
- Download a backward compatible [version](https://github.com/yooksi/trcm/blob/master/gradle.properties#L15) of MixinBootstrap from the [releases](https://github.com/LXGaming/MixinBootstrap/releases) section of the project repository page and place it in game `mods` folder alongside the mod `jar` as previously instructed.

### Why do I need an external mod?

Forge and [Mixin](https://github.com/SpongePowered/Mixin) are not compatible straight out of the box so they need a bit of help to work in production environment. MixinBootstrap does just that, it's only function is to enable Mixin to work with Forge, that's it. If you are interested in learning more about Mixin environment read [Introduction to Mixins: The Mixin Environment](https://github.com/SpongePowered/Mixin/wiki/Introduction-to-Mixins---The-Mixin-Environment).

## How to use it?

Tick rate is changed through the use of the following game commands:

- `\t <rate>` - change tick rate to a desired value (min 0.1, max 20).
- `\t` - reset tick rate to game default value (20).

## Technical details

The following section is a brief technical explanation of how TimeLib is able to slow down game time. It is written in hopes that some developers might find it useful when learning to work with Mixin. Note that the process used to accomplish this involves bytecode manipulation and as such is not recommended as a starting point for beginners to learn about Java or Forge.

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
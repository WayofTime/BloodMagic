# Blood Magic: Alchemical Wizardry [![](https://cf.way2muchnoise.eu/full_blood-magic_downloads.svg)](https://minecraft.curseforge.com/projects/blood-magic) [![Discord](https://img.shields.io/discord/259683256348311552.svg?colorB=7289DA&logo=data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAHYAAABWAgMAAABnZYq0AAAACVBMVEUAAB38%2FPz%2F%2F%2F%2Bm8P%2F9AAAAAXRSTlMAQObYZgAAAAFiS0dEAIgFHUgAAAAJcEhZcwAACxMAAAsTAQCanBgAAAAHdElNRQfhBxwQJhxy2iqrAAABoElEQVRIx7WWzdGEIAyGgcMeKMESrMJ6rILZCiiBg4eYKr%2Fd1ZAfgXFm98sJfAyGNwno3G9sLucgYGpQ4OGVRxQTREMDZjF7ILSWjoiHo1n%2BE03Aw8p7CNY5IhkYd%2F%2F6MtO3f8BNhR1QWnarCH4tr6myl0cWgUVNcfMcXACP1hKrGMt8wcAyxide7Ymcgqale7hN6846uJCkQxw6GG7h2MH4Czz3cLqD1zHu0VOXMfZjHLoYvsdd0Q7ZvsOkafJ1P4QXxrWFd14wMc60h8JKCbyQvImzlFjyGoZTKzohwWR2UzSONHhYXBQOaKKsySsahwGGDnb%2FiYPJw22sCqzirSULYy1qtHhXGbtgrM0oagBV4XiTJok3GoLoDNH8ooTmBm7ZMsbpFzi2bgPGoXWXME6XT%2BRJ4GLddxJ4PpQy7tmfoU2HPN6cKg%2BledKHBKlF8oNSt5w5g5o8eXhu1IOlpl5kGerDxIVT%2BztzKepulD8utXqpChamkzzuo7xYGk%2FkpSYuviLXun5bzdRf0Krejzqyz7Z3p0I1v2d6HmA07dofmS48njAiuMgAAAAASUVORK5CYII%3D)](https://discord.gg/VtNrGrs)

Gruesome? Probably. Worth it? Definitely!

Have you ever picked up a magic mod for Minecraft, and thought that it was too tame? Was there not enough danger involved when creating your next high-tech gadget? Bored with all of those peaceful animals just staring at you without a care in the world? Well then, I am glad you came here!

Blood Magic is an arcane art that is practiced by mages who attempt to gather a vast amount of power through utilizing a forbidden material: blood. Even though it does grant a huge amount of power, every single action that is performed with this volatile magic can prove deadly. You have been warned.

## Links

* [Minecraft Forum Thread](https://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/1290532-bm)

### WayofTime: 

* Twitter: [@WayofTime](https://twitter.com/WayofTime)
* [Donate](https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=J7SNY7L82PQ82)
* [Patreon](https://www.patreon.com/BloodMagic)

### TehNut

* Twitter: [@OfficialTehNut](https://twitter.com/OfficialTehNut)
* [Website](https://tehnut.info)

## Building

1. Clone this repository to your local file system.
2. Open a terminal in the directory you cloned to.
3. Run `gradlew build` (or `./gradlew build` if that doesn't work).
4. Navigate to `./build/libs`, you will find the compiled jars there.

## License

Blood Magic: AlchemicalWizardry by WayofTime is licensed under the [GNU Lesser General Public License 2.1](https://tldrlegal.com/license/gnu-lesser-general-public-license-v2.1-(lgpl-2.1)).

## Installation Instructions

This mod requires "Minecraft Forge" in order to operate. It is incredibly easy to download and set up, so might as well get to it!

1. Download [Forge](https://files.minecraftforge.net/). Usually "Recommended" is best - you want the "universal", not the source. Forge also has an "install" option now, so all you need to do is launch that program and it will make a lovely Forge profile. If you haven't, look up how to use the installer and the new 1.6.x launcher if you are confused!

2. Download the latest version of BloodMagic from [Curseforge](https://minecraft.curseforge.com/projects/blood-magic).

3. Place the mod in the **mods** folder of your .minecraft. If you are unsure of where that is located, it is here: `../Users/you/AppData/roaming/.minecraft`.

## Development Setup

First, clone this repository to your local file system, then follow the steps outlined on the [Minecraft Forge documentation site](https://mcforge.readthedocs.io/en/latest/gettingstarted/#from-zero-to-modding).

## Adding Compatibility

Add to your build.gradle:

```groovy
repositories {
    maven { url "https://tehnut.info/maven/" }
}
```

```groovy
dependencies {
    deobfCompile "com.wayoftime.bloodmagic:BloodMagic:${BLOODMAGIC_VERSION}:api"
}
```

`${BLOODMAGIC_VERSION}` can be found on CurseForge (or via the Maven itself), check the file name of the version you want. If making a full addon, remove the `:api` modifier to get the full project.
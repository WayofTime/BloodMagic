# Blood Magic: Alchemical Wizardry
[![CurseForge Downloads](https://img.shields.io/curseforge/dt/224791?label=CurseForge%20Downloads)](https://www.curseforge.com/minecraft/mc-mods/blood-magic)
[![Modrinth Downloads](https://img.shields.io/modrinth/dt/PbNc6qBY?label=Modrinth%20Downloads)](https://modrinth.com/mod/blood-magic)
[![Discord](https://img.shields.io/discord/259683256348311552.svg?colorB=7289DA&logo=data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAHYAAABWAgMAAABnZYq0AAAACVBMVEUAAB38%2FPz%2F%2F%2F%2Bm8P%2F9AAAAAXRSTlMAQObYZgAAAAFiS0dEAIgFHUgAAAAJcEhZcwAACxMAAAsTAQCanBgAAAAHdElNRQfhBxwQJhxy2iqrAAABoElEQVRIx7WWzdGEIAyGgcMeKMESrMJ6rILZCiiBg4eYKr%2Fd1ZAfgXFm98sJfAyGNwno3G9sLucgYGpQ4OGVRxQTREMDZjF7ILSWjoiHo1n%2BE03Aw8p7CNY5IhkYd%2F%2F6MtO3f8BNhR1QWnarCH4tr6myl0cWgUVNcfMcXACP1hKrGMt8wcAyxide7Ymcgqale7hN6846uJCkQxw6GG7h2MH4Czz3cLqD1zHu0VOXMfZjHLoYvsdd0Q7ZvsOkafJ1P4QXxrWFd14wMc60h8JKCbyQvImzlFjyGoZTKzohwWR2UzSONHhYXBQOaKKsySsahwGGDnb%2FiYPJw22sCqzirSULYy1qtHhXGbtgrM0oagBV4XiTJok3GoLoDNH8ooTmBm7ZMsbpFzi2bgPGoXWXME6XT%2BRJ4GLddxJ4PpQy7tmfoU2HPN6cKg%2BledKHBKlF8oNSt5w5g5o8eXhu1IOlpl5kGerDxIVT%2BztzKepulD8utXqpChamkzzuo7xYGk%2FkpSYuviLXun5bzdRf0Krejzqyz7Z3p0I1v2d6HmA07dofmS48njAiuMgAAAAASUVORK5CYII%3D)](https://discord.gg/VtNrGrs)

Gruesome? Probably. Worth it? Definitely!

Have you ever picked up a magic mod for Minecraft, and thought that it was too tame? Was there not enough danger involved when creating your next high-tech gadget? Bored with all of those peaceful animals just staring at you without a care in the world? Well then, I am glad you came here!

Blood Magic is an arcane art that is practiced by mages who attempt to gather a vast amount of power through utilizing a forbidden material: blood. Even though it does grant a huge amount of power, every single action that is performed with this volatile magic can prove deadly. You have been warned.

### WayofTime:

* Twitter: [@WayofTime](https://twitter.com/WayofTime)
* [Donate](https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=J7SNY7L82PQ82)
* [Patreon](https://www.patreon.com/BloodMagic)

### TehNut

* Twitter: [@OfficialTehNut](https://twitter.com/OfficialTehNut)

## License

Blood Magic: AlchemicalWizardry by WayofTime is licensed under the [GNU Lesser General Public License 2.1](https://tldrlegal.com/license/gnu-lesser-general-public-license-v2.1-(lgpl-2.1)).

## Installation Instructions

This mod requires "Neoforge" in order to operate. It is incredibly easy to download and set up, so might as well get to it!

1. Follow the steps [here](https://docs.neoforged.net/user/docs/client/) to set up a modded minecraft client (or use your favourite launcher)
2. Download the latest version of BloodMagic from [Curseforge](https://www.curseforge.com/minecraft/mc-mods/blood-magic) or [Modrinth](https://modrinth.com/mod/blood-magic).
3. Place the mod in the **mods** folder of your .minecraft. If you are unsure of where that is located, it is here: `../Users/you/AppData/roaming/.minecraft`.

## Contributing
Refer to the [contributing page](CONTRIBUTING.MD)

## Custom Builds

1. Fork this repository and clone your fork to your local file system.
2. Make the changes you like
3. Open a terminal in the directory you cloned to.
4. Run `gradlew build` (or `./gradlew build` if that doesn't work).
5. Navigate to `./build/libs`, you will find the compiled jars there.

Note that custom builds are meant for either yourself or your friend groups. If you intend to release it publicly you should consider making it an add-on instead or ask us if we'd like to include the changes in the actual Blood Magic
Custom Builds will receive very little support from us, if any at all. If you find an issue in a custom build, try if you can replicate it in the official build first.

## Add-ons

1. Follow the steps [here](https://docs.neoforged.net/docs/1.21.1/gettingstarted/) to set up your add-on mods workspace
2. Add the Blood Magic API as a dependency, via either Maven as outlined below. Dont forget to sync gradle again so it can go fetch it!
3. Make your add-on work through coding

If you need help finding where in the API something is (or it isnt there at all) you are welcome to join our discord and ask us about it there! (Or open an issue here on github for something missing)

### Via Modrinth (recommended)
Add to your build.gradle:

```groovy
repositories {
    exclusiveContent {
        forRepository {
            maven {
                name = "Modrinth"
                url = "https://api.modrinth.com/maven"
            }
        }
        filter {
            includeGroup "maven.modrinth"
        }
    }
}
```
```groovy
dependencies {
    implementation "maven.modrinth:blood-magic:${MC_VERSION}-${BM_VERSION}:api"
}
```
`${MC_VERSION}` is the version of Minecraft you're looking to work with (eg, `1.21.1`) and `${BM_VERSION}` the Blood Magic version (eg, `3.4.0-49`)

### Via CurseForge
Add to your build.gradle:

```groovy
repositories {
    exclusiveContent {
        forRepository {
            maven {
                url "https://cursemaven.com"
            }
        }
        filter {
            includeGroup "curse.maven"
        }
    }
}
```

```groovy
dependencies {
    implementation "curse.maven:blood-magic-224791:${FILE_ID}"
}
```

`${FILE_ID}` can be found on the CurseForge page where the file is downloaded, make sure to look for the additional file marked with `-api`. The ID you need is the number at the end of the URL.
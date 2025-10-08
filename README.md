# Blood Magic: Alchemical Wizardry 
[![Curseforge](https://img.shields.io/curseforge/dt/224791.svg?style=for-the-badge&label=Curseforge&logo=curseforge&color=f16436)](https://minecraft.curseforge.com/projects/blood-magic)
[![Modrinth](https://img.shields.io/modrinth/dt/PbNc6qBY.svg?style=for-the-badge&label=Modrinth&logo=modrinth&color=1bd96a)](https://modrinth.com/mod/blood-magic)
[![Discord](https://img.shields.io/discord/259683256348311552.svg?style=for-the-badge&label=Discord&logo=discord&color=404eed)](https://discord.gg/VtNrGrs)
[![License: CC BY 4.0](https://img.shields.io/badge/License-CC_BY_4.0-lightgrey.svg?style=for-the-badge)](https://creativecommons.org/licenses/by/4.0/)
### Gruesome? Probably. Worth it? Definitely!
### [Downloads](http://minecraft.curseforge.com/projects/blood-magic/files)

## Information

Have you ever picked up a magic mod for Minecraft, and thought that it was too tame? Was there not enough danger involved when creating your next high-tech gadget? Bored with all of those peaceful animals just staring at you without a care in the world? Well then, I am glad you came here!

Blood Magic is an arcane art that is practiced by mages who attempt to gather a vast amount of power through utilizing a forbidden material: blood. Even though it does grant a huge amount of power, every single action that is performed with this volatile magic can prove deadly. You have been warned.

## Links
* Twitter: [@WayofTime](https://twitter.com/WayofTime)
* Wiki: Found at [FTBWiki](http://ftbwiki.org/Blood_Magic)
* [Minecraft Forum Thread](http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/1290532-bm)
* [Curseforge](http://minecraft.curseforge.com/projects/blood-magic)
* [Donate](https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=J7SNY7L82PQ82)
* [Patreon](https://www.patreon.com/BloodMagic)

## Development Setup

1. Fork this project to your own Github repository and clone it to your desktop.
2. Navigate to the directory you cloned to. Follow the [Forge Documentation](https://docs.minecraftforge.net/en/latest/gettingstarted/#from-zero-to-modding) (start at step 4) to setup your workspace. If you use IDEA, follow [this set](http://mcforge.readthedocs.io/en/latest/gettingstarted/#terminal-free-intellij-idea-configuration) of steps.

[Setup video](https://www.youtube.com/watch?v=8VEdtQLuLO0&feature=youtu.be) by LexManos. For more information, refer to the [Forge Forums](http://www.minecraftforge.net/forum/index.php/topic,14048.0.html).


## Developing Addons

Add to your build.gradle:

    repositories {
      maven {
        url "http://tehnut.info/maven/"
      }
    }

    dependencies {
      deobfCompile "com.wayoftime.bloodmagic:BloodMagic:<BLOODMAGIC-VERSION>"
    }
    
`<BLOODMAGIC-VERSION>` can be found on CurseForge (or via the Maven itself), check the file name of the version you want.

## Custom Builds

**Custom builds are *unsupported*. If you have an issue while using an unofficial build, it is not guaranteed that you will get support.**

#### How to make a custom build:

1. Clone directly from this repository to your desktop.
2. Navigate to the directory you cloned to. Open a command window there and run `gradlew build`
3. Once it completes, your new build will be found at `../build/libs/BloodMagic-*.jar`. You can ignore the `deobf`, `sources`, and `javadoc` jars.

## License

![CCA4.0](https://licensebuttons.net/l/by/4.0/88x31.png)

Blood Magic: Alchemical Wizardry by WayofTime is licensed under a [Creative Commons Attribution 4.0 International License](http://creativecommons.org/licenses/by/4.0/).

## Installation Instructions

This mod requires "Minecraft Forge" in order to operate. It is incredibly easy to download and set up, so might as well get to it!

1. Download [Forge](http://files.minecraftforge.net/). Usually "Recommended" is best - you want the "universal", not the source. Forge also has an "install" option now, so all you need to do is launch that program and it will make a lovely Forge profile. If you haven't, look up how to use the installer and the new 1.6.x launcher if you are confused!

2. Download the latest version of BloodMagic from [Curseforge](http://minecraft.curseforge.com/mc-mods/224791-blood-magic).

3. Place the mod in the **mods** folder of your .minecraft. If you are unsure of where that is located, it is here: `../Users/you/AppData/roaming/.minecraft`.

## FAQ

**Q**: My weak blood orb doesn't show my current LP! Fix it please.

**A**: The mechanic for viewing the player's essence has changed due to some issues with mechanics. You now need a Divination Sigil or a Seer's Sigil to view a player's essence. It does other things, too, so it is worth it!

**Q**: Why am I dying so much?

**A**: It might be a good idea to make sure that you have enough essence to do a task. If you don't have enough essence to use one of your sigils, for example, it will take it out of your health. If your health reaches 0... Well, you don't have to be a genius to see what would happen. Note that Rituals will only stop working and give you nausea, not kill you, so you don't have to worry about them quite as much.

**Q**: Waffles?

**A**: Waffles!

**Q**: Where is x? When I watched spotlight "w," it had an item called x. Don't you need x to make y, before you can create z?

**A**: A lot of 1.12 and 1.7.10 Blood Magic stuff either hasn't been implemented yet in 1.16, or it's been dropped entirely for reasons of balance or by being superseded by something else. Your best bet is probably to check out the [Patchouli](https://www.curseforge.com/minecraft/mc-mods/patchouli) Guidebook, the Sanguine Scientum. Everything that's currently in Blood Magic has an entry somewhere in there! Failing that, keep an eye on the changelogs. 

**Q**: Way, I've just had an amazing idea! Why not add an in-game book just like the Thaumonomicon?

**A**: We've beaten you to it! Blood Magic now has a fully detailed in-game guide, courtesy of [Patchouli](https://www.curseforge.com/minecraft/mc-mods/patchouli). Craft yourself a Sanguine Scientum today! 

**Q**: When I respawn, I'm not getting nearly as much LP from sacrificing! What gives? (AKA: What's this 'Soul Fray' effect I've got going on?)

**A**: This is due to my attempt to combat Zerg Rushing. When you die, you get a debuff called Soul Fray that, while active, significantly reduces the worth of your LP. Instead of dying and respawning again and again, why not look into making an Incense Altar? It's much more fun! 

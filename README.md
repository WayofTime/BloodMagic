#Blood Magic: Alchemical Wizardry

###Gruesome? Probably. Worth it? Definitely!
###[Downloads](http://minecraft.curseforge.com/mc-mods/224791-blood-magic/files)

##Information

Have you ever picked up a magic mod for Minecraft, and thought that it was too tame? Was there not enough danger involved when creating your next high-tech gadget? Bored with all of those peaceful animals just staring at you without a care in the world? Well then, I am glad you came here!

Blood Magic is an arcane art that is practiced by mages who attempt to gather a vast amount of power through utilizing a forbidden material: blood. Even though it does grant a huge amount of power, every single action that is performed with this volatile magic can prove deadly. You have been warned.

##Links
* Twitter: [@WayofTime](https://twitter.com/WayofTime)
* Wiki: Found at [FTBWiki](http://ftbwiki.org/Blood_Magic)
* [Minecraft Forum Thread](http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/1290532-bm)
* [Curseforge](http://minecraft.curseforge.com/mc-mods/224791-blood-magic)
* [Donate](https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=J7SNY7L82PQ82)
* [Patreon](https://www.patreon.com/BloodMagic)

##Development Setup

1. Fork this project to your own Github repository and clone it to your desktop.
2. Navigate to the directory you cloned to. Open a command window there and run `gradlew setupDevWorkspace` then (if you use Eclipse) `gradlew eclipse` or (if you use IDEA) `gradlew idea`. 
3. This process will setup [Forge](http://www.minecraftforge.net/forum/), your workspace, and link [MineTweaker](http://minetweaker3.powerofbytes.com/) as a dependency.
4. Open the project in your IDE of choice.
5. Set `../src/api/java` as a source directory.

####IntelliJ IDEA extra steps

1. Navigate to `File > Settings > Plugins > Browse repositories...`. Search for Lombok and install the plugin.
2. Navigate to `File > Settings > Build, Execution, Deployment > Compiler > Annotation Processors`. Check `Enable annotation processing`. 

[Setup video](https://www.youtube.com/watch?v=8VEdtQLuLO0&feature=youtu.be) by LexManos. For more information, refer to the [Forge Forums](http://www.minecraftforge.net/forum/index.php/topic,14048.0.html).


##Developing Addons

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

##Custom Builds

**Custom builds are *unsupported*. If you have an issue while using an unofficial build, it is not guaranteed that you will get support.**

####How to make a custom build:

1. Clone directly from this repository to your desktop.
2. Navigate to the directory you cloned to. Open a command window there and run `gradlew build`
3. Once it completes, your new build will be found at `../build/libs/BloodMagic-*.jar`. You can ignore the `deobf`, `sources`, and `javadoc` jars.

##License

![CCA4.0](https://licensebuttons.net/l/by/4.0/88x31.png)

Blood Magic: AlchemicalWizardry by WayofTime is licensed under a [Creative Commons Attribution 4.0 International License](http://creativecommons.org/licenses/by/4.0/).

##Installation Instructions

This mod requires "Minecraft Forge" in order to operate. It is incredibly easy to download and set up, so might as well get to it!

1. Download [Forge](http://files.minecraftforge.net/). Usually "Recommended" is best - you want the "universal", not the source. Forge also has an "install" option now, so all you need to do is launch that program and it will make a lovely Forge profile. If you haven't, look up how to use the installer and the new 1.6.x launcher if you are confused!

2. Download the latest version of BloodMagic from [Curseforge](http://minecraft.curseforge.com/mc-mods/224791-blood-magic).

3. Place the mod in the **mods** folder of your .minecraft. If you are unsure of where that is located, it is here: `../Users/you/AppData/roaming/.minecraft`.

##FAQ

**Q**: My weak blood orb doesn't show my current LP! Fix it please.

**A**: The mechanic for viewing the player's essence has changed due to some issues with mechanics. You now need a Divination Sigil to view a player's essence. It does other things, too, so it is worth it!

**Q**: Why am I dying so much?

**A**: It might be a good idea to make sure that you have enough essence to do a task. If you don't have enough essence for, say, an imperfect ritual, it will take it out of your health. If your health reaches 0... Well, you don't have to be a genius to see what would happen.

**Q**: Waffles?

**A**: Waffles!

**Q**: Where is x? When I watched spotlight "w," it had an item called x. Don't you need x to make y, before you can create z?

**A**: It might be wise to look at an updated spotlight, or check the changelogs. I'm not evil, so I do document everything that I can. There was most likely some issues with balance with the item, so it may have been removed for those reasons. Or you just grabbed the item from NEI without seeing it was a test item, in which case: shame on you!

**Q**: Isn't that armour ...

**A**: Yes, the armour texture is from the mod EE2. Pahimar and I are good friends, and I decided to use it for the armour and tools. He has given me his express permission for it, so need not worry!
[Proof](https://twitter.com/Pahimar/status/453590600689139712).

**Q**: My game is crashing with the latest TC4!

**A**: To help stop this, remove the TC4 API from my Blood Magic zip file (if it is there). It should solve things.

**Q**: But, my Thaumcraft aspects are messed up!

**A**: Once again, remove the TC4 API from the Blood Magic zip file to solve this. Azanor changed some aspect combinations.

**Q**: When will 1.7.2 be released?

**A**: It's out! Go get it!

**Q**: Way, I've just had an amazing idea! Why not add an in-game book just like the Thaumonomicon?

**A**: This has been suggested several times. It I being worked on, and is proceeding quite well. Some framework needs to be completed, but it isn't a priority - ALL of the information you need is on this post, or online.

**Q**: Why do I not have a Sacrificial Orb? It's only showing up as a knife!

**A**: This is a config option. The person you saw with an orb had a config that changed the knife to an orb. The orb and knife function exactly the same way, but you can change it in the configs by looking for the "Idontlikefun" option.

**Q**: When I respawn, I appear to only come back with 3 hearts. What's happening?

**A**: This is due to another config option in the "WhimpySettings," which is due to my attempt to combat Zerg Rushing. You can disable this if you really want to, but it shouldn't hurt unduly.

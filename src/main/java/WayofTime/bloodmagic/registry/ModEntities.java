package WayofTime.bloodmagic.registry;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.Constants;
import net.minecraft.util.ResourceLocation;
import WayofTime.bloodmagic.entity.mob.EntityCorruptedChicken;
import WayofTime.bloodmagic.entity.mob.EntityCorruptedSheep;
import WayofTime.bloodmagic.entity.mob.EntityCorruptedSpider;
import WayofTime.bloodmagic.entity.mob.EntityCorruptedZombie;
import WayofTime.bloodmagic.entity.mob.EntityMimic;
import WayofTime.bloodmagic.entity.mob.EntitySentientSpecter;
import WayofTime.bloodmagic.entity.projectile.EntityBloodLight;
import WayofTime.bloodmagic.entity.projectile.EntityMeteor;
import WayofTime.bloodmagic.entity.projectile.EntitySentientArrow;
import WayofTime.bloodmagic.entity.projectile.EntitySoulSnare;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class ModEntities
{
    public static void init()
    {
        int entities = 0;
        EntityRegistry.registerModEntity(new ResourceLocation(Constants.Mod.MODID, "BloodLight"), EntityBloodLight.class, "BloodLight", ++entities, BloodMagic.instance, 16*4, 3, true);
        EntityRegistry.registerModEntity(new ResourceLocation(Constants.Mod.MODID, "SoulSnare"), EntitySoulSnare.class, "SoulSnare", ++entities, BloodMagic.instance, 16*4, 3, true);
        EntityRegistry.registerModEntity(new ResourceLocation(Constants.Mod.MODID, "SoulArrow"), EntitySentientArrow.class, "SoulArrow", ++entities, BloodMagic.instance, 16*4, 3, true);
        EntityRegistry.registerModEntity(new ResourceLocation(Constants.Mod.MODID, "Meteor"), EntityMeteor.class, "Meteor", ++entities, BloodMagic.instance, 16*4, 3, true);
        EntityRegistry.registerModEntity(new ResourceLocation(Constants.Mod.MODID, "SentientSpecter"), EntitySentientSpecter.class, "SentientSpecter", ++entities, BloodMagic.instance, 16*4, 3, true);
        EntityRegistry.registerModEntity(new ResourceLocation(Constants.Mod.MODID, "Mimic"), EntityMimic.class, "Mimic", ++entities, BloodMagic.instance, 16*4, 3, true);
        EntityRegistry.registerModEntity(new ResourceLocation(Constants.Mod.MODID, "CorruptedZombie"), EntityCorruptedZombie.class, "CorruptedZombie", ++entities, BloodMagic.instance, 16*4, 3, true);
        EntityRegistry.registerModEntity(new ResourceLocation(Constants.Mod.MODID, "CorruptedSheep"), EntityCorruptedSheep.class, "CorruptedSheep", ++entities, BloodMagic.instance, 16*4, 3, true);
        EntityRegistry.registerModEntity(new ResourceLocation(Constants.Mod.MODID, "CorruptedChicken"), EntityCorruptedChicken.class, "CorruptedChicken", ++entities, BloodMagic.instance, 16*4, 3, true);
        EntityRegistry.registerModEntity(new ResourceLocation(Constants.Mod.MODID, "CorruptedSpider"), EntityCorruptedSpider.class, "CorruptedSpider", ++entities, BloodMagic.instance, 16*4, 3, true);
    }
}

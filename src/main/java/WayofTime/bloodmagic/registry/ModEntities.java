package WayofTime.bloodmagic.registry;

import net.minecraftforge.fml.common.registry.EntityRegistry;
import WayofTime.bloodmagic.BloodMagic;
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

public class ModEntities
{
    public static void init()
    {
        int id = 0;

        EntityRegistry.registerModEntity(EntityBloodLight.class, "BloodLight", id++, BloodMagic.instance, 64, 20, true);
        EntityRegistry.registerModEntity(EntitySoulSnare.class, "SoulSnare", id++, BloodMagic.instance, 64, 1, true);
        EntityRegistry.registerModEntity(EntitySentientArrow.class, "SoulArrow", id++, BloodMagic.instance, 64, 1, true);
        EntityRegistry.registerModEntity(EntityMeteor.class, "Meteor", id++, BloodMagic.instance, 64, 1, true);
        EntityRegistry.registerModEntity(EntitySentientSpecter.class, "SentientSpecter", id++, BloodMagic.instance, 64, 1, true);
        EntityRegistry.registerModEntity(EntityMimic.class, "Mimic", id++, BloodMagic.instance, 64, 1, true);
        EntityRegistry.registerModEntity(EntityCorruptedZombie.class, "CorruptedZombie", id++, BloodMagic.instance, 64, 1, true);
        EntityRegistry.registerModEntity(EntityCorruptedSheep.class, "CorruptedSheep", id++, BloodMagic.instance, 64, 1, true);
        EntityRegistry.registerModEntity(EntityCorruptedChicken.class, "CorruptedChicken", id++, BloodMagic.instance, 64, 1, true);
        EntityRegistry.registerModEntity(EntityCorruptedSpider.class, "CorruptedSpider", id++, BloodMagic.instance, 64, 1, true);
    }
}

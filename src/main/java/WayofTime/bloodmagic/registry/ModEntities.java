package WayofTime.bloodmagic.registry;

import net.minecraftforge.fml.common.registry.EntityEntry;
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
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModEntities
{
    public static void init()
    {
        GameRegistry.register(new EntityEntry(EntityBloodLight.class, "BloodLight").setRegistryName("BloodLight"));
        GameRegistry.register(new EntityEntry(EntitySoulSnare.class, "SoulSnare").setRegistryName("SoulSnare"));
        GameRegistry.register(new EntityEntry(EntitySentientArrow.class, "SoulArrow").setRegistryName("SoulArrow"));
        GameRegistry.register(new EntityEntry(EntityMeteor.class, "Meteor").setRegistryName("Meteor"));
        GameRegistry.register(new EntityEntry(EntitySentientSpecter.class, "SentientSpecter").setRegistryName("SentientSpecter"));
        GameRegistry.register(new EntityEntry(EntityMimic.class, "Mimic").setRegistryName("Mimic"));
        GameRegistry.register(new EntityEntry(EntityCorruptedZombie.class, "CorruptedZombie").setRegistryName("CorruptedZombie"));
        GameRegistry.register(new EntityEntry(EntityCorruptedSheep.class, "CorruptedSheep").setRegistryName("CorruptedSheep"));
        GameRegistry.register(new EntityEntry(EntityCorruptedChicken.class, "CorruptedChicken").setRegistryName("CorruptedChicken"));
        GameRegistry.register(new EntityEntry(EntityCorruptedSpider.class, "CorruptedSpider").setRegistryName("CorruptedSpider"));
    }
}

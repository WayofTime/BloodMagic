package WayofTime.bloodmagic.registry;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.entity.projectile.EntityBloodLight;
import WayofTime.bloodmagic.entity.projectile.EntitySentientArrow;
import WayofTime.bloodmagic.entity.projectile.EntitySoulSnare;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class ModEntities
{
    public static void init()
    {
        int id = 0;

        EntityRegistry.registerModEntity(EntityBloodLight.class, "BloodLight", id++, BloodMagic.instance, 64, 20, true);
        EntityRegistry.registerModEntity(EntitySoulSnare.class, "SoulSnare", id++, BloodMagic.instance, 64, 1, true);
        EntityRegistry.registerModEntity(EntitySentientArrow.class, "SoulArrow", id++, BloodMagic.instance, 64, 1, true);
    }
}

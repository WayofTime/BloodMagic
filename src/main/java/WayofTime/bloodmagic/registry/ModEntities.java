package WayofTime.bloodmagic.registry;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.entity.projectile.EntityBloodLight;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class ModEntities {

    public static void init() {
        int id = 0;

        EntityRegistry.registerModEntity(EntityBloodLight.class, "BloodLight", id++, BloodMagic.instance, 64, 20, true);
    }
}

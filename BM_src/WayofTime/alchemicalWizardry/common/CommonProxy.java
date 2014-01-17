package WayofTime.alchemicalWizardry.common;

import WayofTime.alchemicalWizardry.common.entity.projectile.*;
import WayofTime.alchemicalWizardry.common.tileEntity.TEAltar;
import WayofTime.alchemicalWizardry.common.tileEntity.TEMasterStone;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.world.World;

public class CommonProxy
{
    public static String ITEMS_PNG = "/WayofTime/alchemicalWizardry/items.png";
    public static String BLOCK_PNG = "/WayofTime/alchemicalWizardry/block.png";

    // Client stuff
    public void registerRenderers()
    {
        // Nothing here as the server doesn't render graphics!
    }

    public void registerEntities()
    {
    }

    public World getClientWorld()
    {
        return null;
    }

    public void registerActions()
    {
    }

    public void registerEvents()
    {
    }

    public void registerSoundHandler()
    {
        // Nothing here as this is a server side proxy
    }

    public void registerTileEntities()
    {
        GameRegistry.registerTileEntity(TEAltar.class, "containerAltar");
        GameRegistry.registerTileEntity(TEMasterStone.class, "containerMasterStone");
    }

    public void registerEntityTrackers()
    {
        EntityRegistry.registerModEntity(EnergyBlastProjectile.class, "energyBlastProjectile", 0, AlchemicalWizardry.instance, 128, 5, true);
        EntityRegistry.registerModEntity(FireProjectile.class, "fireProjectile", 1, AlchemicalWizardry.instance, 128, 5, true);
        EntityRegistry.registerModEntity(IceProjectile.class, "iceProjectile", 2, AlchemicalWizardry.instance, 128, 5, true);
        EntityRegistry.registerModEntity(ExplosionProjectile.class, "explosionProjectile", 3, AlchemicalWizardry.instance, 128, 5, true);
        EntityRegistry.registerModEntity(HolyProjectile.class, "holyProjectile", 4, AlchemicalWizardry.instance, 128, 5, true);
        EntityRegistry.registerModEntity(WindGustProjectile.class, "windGustProjectile", 5, AlchemicalWizardry.instance, 128, 5, true);
        EntityRegistry.registerModEntity(LightningBoltProjectile.class, "lightningBoltProjectile", 6, AlchemicalWizardry.instance, 128, 5, true);
        EntityRegistry.registerModEntity(WaterProjectile.class, "waterProjectile", 7, AlchemicalWizardry.instance, 128, 5, true);
        EntityRegistry.registerModEntity(MudProjectile.class, "mudProjectile", 8, AlchemicalWizardry.instance, 128, 5, true);
        EntityRegistry.registerModEntity(TeleportProjectile.class, "teleportProjectile", 9, AlchemicalWizardry.instance, 128, 5, true);
        EntityRegistry.registerModEntity(EntityEnergyBazookaMainProjectile.class, "energyBazookaMain", 10, AlchemicalWizardry.instance, 128, 3, true);
        EntityRegistry.registerModEntity(EntityEnergyBazookaSecondaryProjectile.class, "energyBazookaSecondary", 11, AlchemicalWizardry.instance, 128, 3, true);
        EntityRegistry.registerModEntity(EntityBloodLightProjectile.class, "bloodLightProjectile", 12, AlchemicalWizardry.instance, 128, 3, true);
        EntityRegistry.registerModEntity(EntityMeteor.class, "Meteor", 13, AlchemicalWizardry.instance, 128, 3, true);
    }

    public void registerTickHandlers()
    {
    }

    public void InitRendering()
    {
        // TODO Auto-generated method stub
    }
}

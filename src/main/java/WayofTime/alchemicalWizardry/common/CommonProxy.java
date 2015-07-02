package WayofTime.alchemicalWizardry.common;

import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.api.spell.EntitySpellProjectile;
import WayofTime.alchemicalWizardry.common.entity.mob.BookEntityItem;
import WayofTime.alchemicalWizardry.common.entity.mob.MailOrderEntityItem;
import WayofTime.alchemicalWizardry.common.entity.projectile.EnergyBlastProjectile;
import WayofTime.alchemicalWizardry.common.entity.projectile.EntityBloodLightProjectile;
import WayofTime.alchemicalWizardry.common.entity.projectile.EntityEnergyBazookaMainProjectile;
import WayofTime.alchemicalWizardry.common.entity.projectile.EntityEnergyBazookaSecondaryProjectile;
import WayofTime.alchemicalWizardry.common.entity.projectile.EntityMeteor;
import WayofTime.alchemicalWizardry.common.entity.projectile.EntityParticleBeam;
import WayofTime.alchemicalWizardry.common.entity.projectile.ExplosionProjectile;
import WayofTime.alchemicalWizardry.common.entity.projectile.FireProjectile;
import WayofTime.alchemicalWizardry.common.entity.projectile.HolyProjectile;
import WayofTime.alchemicalWizardry.common.entity.projectile.IceProjectile;
import WayofTime.alchemicalWizardry.common.entity.projectile.LightningBoltProjectile;
import WayofTime.alchemicalWizardry.common.entity.projectile.MudProjectile;
import WayofTime.alchemicalWizardry.common.entity.projectile.TeleportProjectile;
import WayofTime.alchemicalWizardry.common.entity.projectile.WaterProjectile;
import WayofTime.alchemicalWizardry.common.entity.projectile.WindGustProjectile;
import WayofTime.alchemicalWizardry.common.tileEntity.TEAltar;
import WayofTime.alchemicalWizardry.common.tileEntity.TEMasterStone;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

public class CommonProxy
{
    // Client stuff
    public void registerRenderers()
    {
        // Nothing here as the server doesn't render graphics!
    }
    
    public void registerPostSideObjects()
    {
    	
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
        EntityRegistry.registerModEntity(EntityMeteor.class, "meteor", 13, AlchemicalWizardry.instance, 120, 3, true);
        EntityRegistry.registerModEntity(EntitySpellProjectile.class, "spellProjectile", 14, AlchemicalWizardry.instance, 128, 3, true);
        EntityRegistry.registerModEntity(EntityParticleBeam.class, "particleBeam", 15, AlchemicalWizardry.instance, 120, 3, true);
        EntityRegistry.registerModEntity(MailOrderEntityItem.class, "catalogueEntityItem", 16, AlchemicalWizardry.instance, 120, 3, true);
        EntityRegistry.registerModEntity(BookEntityItem.class, "bookEntityItem", 17, AlchemicalWizardry.instance, 120, 3, true);
    }

    public void registerTickHandlers()
    {
    }

    public void InitRendering()
    {
        // TODO Auto-generated method stub
    }
}

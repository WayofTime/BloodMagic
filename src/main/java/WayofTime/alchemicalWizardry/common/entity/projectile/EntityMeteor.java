package WayofTime.alchemicalWizardry.common.entity.projectile;

import WayofTime.alchemicalWizardry.common.summoning.meteor.MeteorRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntityMeteor extends EnergyBlastProjectile
{
    private int meteorID;

    public boolean hasTerrae;
    public boolean hasOrbisTerrae;
    public boolean hasCrystallos;
    public boolean hasIncendium;
    public boolean hasTennebrae;

    public EntityMeteor(World par1World)
    {
        super(par1World);
        this.meteorID = 0;
    }

    public EntityMeteor(World par1World, double par2, double par4, double par6, int meteorID)
    {
        super(par1World, par2, par4, par6);
        this.meteorID = meteorID;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeEntityToNBT(par1NBTTagCompound);

        par1NBTTagCompound.setInteger("meteorID", meteorID);
        par1NBTTagCompound.setBoolean("hasTerrae", hasTerrae);
        par1NBTTagCompound.setBoolean("hasOrbisTerrae", hasOrbisTerrae);
        par1NBTTagCompound.setBoolean("hasCrystallos", hasCrystallos);
        par1NBTTagCompound.setBoolean("hasIncendium", hasIncendium);
        par1NBTTagCompound.setBoolean("hasTennebrae", hasTennebrae);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readEntityFromNBT(par1NBTTagCompound);

        meteorID = par1NBTTagCompound.getInteger("meteorID");
        hasTerrae = par1NBTTagCompound.getBoolean("hasTerrae");
        hasOrbisTerrae = par1NBTTagCompound.getBoolean("hasOrbisTerrae");
        hasIncendium = par1NBTTagCompound.getBoolean("hasIncendium");
        hasCrystallos = par1NBTTagCompound.getBoolean("hasCrystallos");
        hasTennebrae = par1NBTTagCompound.getBoolean("hasTennebrae");
    }

    @Override
    public DamageSource getDamageSource()
    {
        return DamageSource.fallingBlock;
    }

    @Override
    public void onImpact(MovingObjectPosition mop)
    {
        if (worldObj.isRemote)
        {
            return;
        }

        if (mop.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY && mop.entityHit != null)
        {
            this.onImpact(mop.entityHit);
        } else if (mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
        {
            MeteorRegistry.createMeteorImpact(worldObj, mop.blockX, mop.blockY, mop.blockZ, this.meteorID, new boolean[]{hasTerrae, hasOrbisTerrae, hasCrystallos, hasIncendium, hasTennebrae});
        }

        this.setDead();
    }

    @Override
    public void onImpact(Entity mop)
    {
        MeteorRegistry.createMeteorImpact(worldObj, (int) this.posX, (int) this.posY, (int) this.posZ, meteorID, new boolean[]{hasTerrae, hasOrbisTerrae, hasCrystallos, hasIncendium, hasTennebrae});

        this.setDead();
    }
}

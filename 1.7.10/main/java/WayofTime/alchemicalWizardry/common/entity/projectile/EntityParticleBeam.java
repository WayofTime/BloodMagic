package WayofTime.alchemicalWizardry.common.entity.projectile;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.registry.IThrowableEntity;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.particle.EntityCloudFX;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

//Shamelessly ripped off from x3n0ph0b3
public class EntityParticleBeam extends Entity implements IProjectile, IThrowableEntity
{
    protected int xTile = -1;
    protected int yTile = -1;
    protected int zTile = -1;
    protected int inTile = 0;
    protected int inData = 0;
    protected float colourRed = 0f;
    protected float colourGreen = 0f;
    protected float colourBlue = 0f;
    protected int xDest = 0;
    protected int yDest = 0;
    protected int zDest = 0;
    protected boolean inGround = false;
    /**
     * The owner of this arrow.
     */
    public EntityLivingBase shootingEntity;
    protected int ticksInAir = 0;
    protected int maxTicksInAir = 600;
    private int ricochetCounter = 0;
    private boolean scheduledForDeath = false;
    protected int projectileDamage;

    public EntityParticleBeam(World par1World)
    {
        super(par1World);
        this.setSize(0.5F, 0.5F);
        this.maxTicksInAir = 600;
    }

    public EntityParticleBeam(World par1World, double par2, double par4, double par6)
    {
        super(par1World);
        this.setSize(0.5F, 0.5F);
        this.setPosition(par2, par4, par6);
        yOffset = 0.0F;
        this.maxTicksInAir = 600;
    }

    public EntityParticleBeam(World par1World, EntityLivingBase par2EntityPlayer, int damage)
    {
        super(par1World);
        shootingEntity = par2EntityPlayer;
        float par3 = 0.8F;
        this.setSize(0.5F, 0.5F);
        this.setLocationAndAngles(par2EntityPlayer.posX, par2EntityPlayer.posY + par2EntityPlayer.getEyeHeight(), par2EntityPlayer.posZ, par2EntityPlayer.rotationYaw, par2EntityPlayer.rotationPitch);
        posX -= MathHelper.cos(rotationYaw / 180.0F * (float) Math.PI) * 0.16F;
        posY -= 0.2D;
        posZ -= MathHelper.sin(rotationYaw / 180.0F * (float) Math.PI) * 0.16F;
        this.setPosition(posX, posY, posZ);
        yOffset = 0.0F;
        motionX = -MathHelper.sin(rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(rotationPitch / 180.0F * (float) Math.PI);
        motionZ = MathHelper.cos(rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(rotationPitch / 180.0F * (float) Math.PI);
        motionY = -MathHelper.sin(rotationPitch / 180.0F * (float) Math.PI);
        this.setThrowableHeading(motionX, motionY, motionZ, par3 * 1.5F, 1.0F);
        this.projectileDamage = damage;
        this.maxTicksInAir = 600;
    }

    public EntityParticleBeam(World par1World, EntityLivingBase par2EntityPlayer, int damage, int maxTicksInAir, double posX, double posY, double posZ, float rotationYaw, float rotationPitch)
    {
        super(par1World);
        shootingEntity = par2EntityPlayer;
        float par3 = 0.8F;
        this.setSize(0.5F, 0.5F);
        this.setLocationAndAngles(posX, posY, posZ, rotationYaw, rotationPitch);
        posX -= MathHelper.cos(rotationYaw / 180.0F * (float) Math.PI) * 0.16F;
        posY -= 0.2D;
        posZ -= MathHelper.sin(rotationYaw / 180.0F * (float) Math.PI) * 0.16F;
        this.setPosition(posX, posY, posZ);
        yOffset = 0.0F;
        motionX = -MathHelper.sin(rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(rotationPitch / 180.0F * (float) Math.PI);
        motionZ = MathHelper.cos(rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(rotationPitch / 180.0F * (float) Math.PI);
        motionY = -MathHelper.sin(rotationPitch / 180.0F * (float) Math.PI);
        this.setThrowableHeading(motionX, motionY, motionZ, par3 * 1.5F, 1.0F);
        this.projectileDamage = damage;
        this.maxTicksInAir = maxTicksInAir;
    }

    public EntityParticleBeam(World par1World, EntityLivingBase par2EntityLivingBase, EntityLivingBase par3EntityLivingBase, float par4, float par5, int damage, int maxTicksInAir)
    {
        super(par1World);
        this.renderDistanceWeight = 10.0D;
        this.shootingEntity = par2EntityLivingBase;
        this.posY = par2EntityLivingBase.posY + (double) par2EntityLivingBase.getEyeHeight() - 0.10000000149011612D;
        double d0 = par3EntityLivingBase.posX - par2EntityLivingBase.posX;
        double d1 = par3EntityLivingBase.boundingBox.minY + (double) (par3EntityLivingBase.height / 1.5F) - this.posY;
        double d2 = par3EntityLivingBase.posZ - par2EntityLivingBase.posZ;
        double d3 = (double) MathHelper.sqrt_double(d0 * d0 + d2 * d2);

        if (d3 >= 1.0E-7D)
        {
            float f2 = (float) (Math.atan2(d2, d0) * 180.0D / Math.PI) - 90.0F;
            float f3 = (float) (-(Math.atan2(d1, d3) * 180.0D / Math.PI));
            double d4 = d0 / d3;
            double d5 = d2 / d3;
            this.setLocationAndAngles(par2EntityLivingBase.posX + d4, this.posY, par2EntityLivingBase.posZ + d5, f2, f3);
            this.yOffset = 0.0F;
            float f4 = (float) d3 * 0.2F;
            this.setThrowableHeading(d0, d1, d2, par4, par5);
        }

        this.projectileDamage = damage;
        this.maxTicksInAir = maxTicksInAir;
    }

    @Override
    protected void entityInit()
    {
        dataWatcher.addObject(16, Byte.valueOf((byte) 0));
    }

    /**
     * Similar to setArrowHeading, it's point the throwable entity to a x, y, z
     * direction.
     */
    @Override
    public void setThrowableHeading(double var1, double var3, double var5, float var7, float var8)
    {
        float var9 = MathHelper.sqrt_double(var1 * var1 + var3 * var3 + var5 * var5);
        var1 /= var9;
        var3 /= var9;
        var5 /= var9;
        var1 += rand.nextGaussian() * 0.007499999832361937D * var8;
        var3 += rand.nextGaussian() * 0.007499999832361937D * var8;
        var5 += rand.nextGaussian() * 0.007499999832361937D * var8;
        var1 *= var7;
        var3 *= var7;
        var5 *= var7;
        motionX = var1;
        motionY = var3;
        motionZ = var5;
        float var10 = MathHelper.sqrt_double(var1 * var1 + var5 * var5);
        prevRotationYaw = rotationYaw = (float) (Math.atan2(var1, var5) * 180.0D / Math.PI);
        prevRotationPitch = rotationPitch = (float) (Math.atan2(var3, var10) * 180.0D / Math.PI);
    }

    @Override
    @SideOnly(Side.CLIENT)
    /**
     * Sets the position and rotation. Only difference from the other one is no bounding on the rotation. Args: posX,
     * posY, posZ, yaw, pitch
     */
    public void setPositionAndRotation2(double par1, double par3, double par5, float par7, float par8, int par9)
    {
        this.setPosition(par1, par3, par5);
        this.setRotation(par7, par8);
    }

    @Override
    @SideOnly(Side.CLIENT)
    /**
     * Sets the velocity to the args. Args: x, y, z
     */
    public void setVelocity(double par1, double par3, double par5)
    {
        motionX = par1;
        motionY = par3;
        motionZ = par5;

        if (prevRotationPitch == 0.0F && prevRotationYaw == 0.0F)
        {
            float var7 = MathHelper.sqrt_double(par1 * par1 + par5 * par5);
            prevRotationYaw = rotationYaw = (float) (Math.atan2(par1, par5) * 180.0D / Math.PI);
            prevRotationPitch = rotationPitch = (float) (Math.atan2(par3, var7) * 180.0D / Math.PI);
            prevRotationPitch = rotationPitch;
            prevRotationYaw = rotationYaw;
            this.setLocationAndAngles(posX, posY, posZ, rotationYaw, rotationPitch);
        }
    }

    /**
     * Called to update the entity's position/logic.
     */
    @Override
    public void onUpdate()
    {
        super.onUpdate();

        if (ticksInAir > maxTicksInAir)
        {
            this.setDead();
        }

        posX += motionX;
        posY += motionY;
        posZ += motionZ;
        MathHelper.sqrt_double(motionX * motionX + motionZ * motionZ);
        this.setPosition(posX, posY, posZ);

        this.doFiringParticles();

        if (Math.pow(posX - xDest, 2) + Math.pow(posY - yDest, 2) + Math.pow(posZ - zDest, 2) <= 1)
        {
            this.scheduledForDeath = true;
        }

        if (this.scheduledForDeath)
        {
            this.setDead();
        }
    }

    public void doFiringParticles()
    {
        if (!worldObj.isRemote)
        {
            return;
        }
        EntityFX particle = new EntityCloudFX(worldObj, posX, posY, posZ, 0, 0, 0);
        particle.setRBGColorF(colourRed + 0.15f * (worldObj.rand.nextFloat() - worldObj.rand.nextFloat()), colourGreen + 0.15f * (worldObj.rand.nextFloat() - worldObj.rand.nextFloat()), colourBlue + 0.15f * (worldObj.rand.nextFloat() - worldObj.rand.nextFloat()));
        FMLClientHandler.instance().getClient().effectRenderer.addEffect(particle);
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    @Override
    public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
    {
        par1NBTTagCompound.setShort("xTile", (short) xTile);
        par1NBTTagCompound.setShort("yTile", (short) yTile);
        par1NBTTagCompound.setShort("zTile", (short) zTile);
        par1NBTTagCompound.setByte("inTile", (byte) inTile);
        par1NBTTagCompound.setByte("inData", (byte) inData);
        par1NBTTagCompound.setByte("inGround", (byte) (inGround ? 1 : 0));
        par1NBTTagCompound.setInteger("ticksInAir", ticksInAir);
        par1NBTTagCompound.setInteger("maxTicksInAir", maxTicksInAir);
        par1NBTTagCompound.setInteger("projectileDamage", this.projectileDamage);
        par1NBTTagCompound.setFloat("colourRed", colourRed);
        par1NBTTagCompound.setFloat("colourGreen", colourGreen);
        par1NBTTagCompound.setFloat("colourBlue", colourBlue);
        par1NBTTagCompound.setInteger("xDest", xDest);
        par1NBTTagCompound.setInteger("yDest", yDest);
        par1NBTTagCompound.setInteger("zDest", zDest);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
    public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        xTile = par1NBTTagCompound.getShort("xTile");
        yTile = par1NBTTagCompound.getShort("yTile");
        zTile = par1NBTTagCompound.getShort("zTile");
        inTile = par1NBTTagCompound.getByte("inTile") & 255;
        inData = par1NBTTagCompound.getByte("inData") & 255;
        inGround = par1NBTTagCompound.getByte("inGround") == 1;
        ticksInAir = par1NBTTagCompound.getInteger("ticksInAir");
        maxTicksInAir = par1NBTTagCompound.getInteger("maxTicksInAir");
        projectileDamage = par1NBTTagCompound.getInteger("projectileDamage");
        colourRed = par1NBTTagCompound.getFloat("colourRed");
        colourGreen = par1NBTTagCompound.getFloat("colourGreen");
        colourBlue = par1NBTTagCompound.getFloat("colourBlue");
        xDest = par1NBTTagCompound.getInteger("xDest");
        yDest = par1NBTTagCompound.getInteger("yDest");
        zDest = par1NBTTagCompound.getInteger("zDest");
    }

    @Override
    protected boolean canTriggerWalking()
    {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public float getShadowSize()
    {
        return 0.0F;
    }

    protected void spawnHitParticles(String string, int i)
    {
        for (int particles = 0; particles < i; particles++)
        {
            worldObj.spawnParticle(string, posX, posY - (string == "portal" ? 1 : 0), posZ, gaussian(motionX), gaussian(motionY), gaussian(motionZ));
        }
    }

    public DamageSource getDamageSource()
    {
        return DamageSource.causeMobDamage(shootingEntity);
    }

    public double smallGauss(double d)
    {
        return (worldObj.rand.nextFloat() - 0.5D) * d;
    }

    public double gaussian(double d)
    {
        return d + d * ((rand.nextFloat() - 0.5D) / 4);
    }

    private int getRicochetMax()
    {
        return 0;
    }

    @Override
    public Entity getThrower()
    {
        // TODO Auto-generated method stub
        return this.shootingEntity;
    }

    @Override
    public void setThrower(Entity entity)
    {
        if (entity instanceof EntityLivingBase)
            this.shootingEntity = (EntityLivingBase) entity;

    }

    public void setColour(float red, float green, float blue)
    {
        this.colourRed = red;
        this.colourGreen = green;
        this.colourBlue = blue;
    }

    public void setDestination(int xDest, int yDest, int zDest)
    {
        this.xDest = xDest;
        this.yDest = yDest;
        this.zDest = zDest;
    }
}

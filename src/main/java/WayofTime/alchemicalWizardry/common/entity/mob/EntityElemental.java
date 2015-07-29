package WayofTime.alchemicalWizardry.common.entity.mob;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.ModItems;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class EntityElemental extends EntityDemon
{
    private EntityAIAttackOnCollide aiAttackOnCollide = new EntityAIAttackOnCollide(this, EntityPlayer.class, 1.2D, false);

    private static float maxTamedHealth = 100.0F;
    private static float maxUntamedHealth = 100.0F;

    public EntityElemental(World par1World, String entityAirElementalID)
    {
        super(par1World, entityAirElementalID);
        this.setSize(0.5F, 1.0F);
        this.setAggro(false);
        this.setTamed(false);

        if (par1World != null && !par1World.isRemote)
        {
            this.setCombatTask();
        }
    }

    public int courseChangeCooldown;
    public double waypointX;
    public double waypointY;
    public double waypointZ;
    private Entity targetedEntity;

    /**
     * Cooldown time between target loss and new target aquirement.
     */
    private int aggroCooldown;
    public int prevAttackCounter;
    public int attackCounter;

    /**
     * The explosion radius of spawned fireballs.
     */
    protected void dropFewItems(boolean par1, int par2)
    {
        if (worldObj.rand.nextFloat() < (1 - Math.pow(0.6f, par2 + 1)))
        {
            this.entityDropItem(new ItemStack(ModItems.demonBloodShard, 1, 0), 0.0f);
        }
    }

    protected void fall(float par1)
    {
    }

    /**
     * Takes in the distance the entity has fallen this tick and whether its on the ground to update the fall distance
     * and deal fall damage if landing on the ground.  Args: distanceFallenThisTick, onGround
     */
    protected void updateFallState(double par1, boolean par3)
    {
    }

    /**
     * Moves the entity based on the specified heading.  Args: strafe, forward
     */
    public void moveEntityWithHeading(float p_70612_1_, float p_70612_2_)
    {
        if (this.isInWater())
        {
            this.moveFlying(p_70612_1_, p_70612_2_, 0.02F);
            this.moveEntity(this.motionX, this.motionY, this.motionZ);
            this.motionX *= 0.800000011920929D;
            this.motionY *= 0.800000011920929D;
            this.motionZ *= 0.800000011920929D;
        }
        else if (this.isInLava())
        {
            this.moveFlying(p_70612_1_, p_70612_2_, 0.02F);
            this.moveEntity(this.motionX, this.motionY, this.motionZ);
            this.motionX *= 0.5D;
            this.motionY *= 0.5D;
            this.motionZ *= 0.5D;
        }
        else
        {
            float f2 = 0.91F;

            if (this.onGround)
            {
                f2 = this.worldObj.getBlockState(new BlockPos(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.getEntityBoundingBox().minY) - 1, MathHelper.floor_double(this.posZ))).getBlock().slipperiness * 0.91F;
            }

            float f3 = 0.16277136F / (f2 * f2 * f2);
            this.moveFlying(p_70612_1_, p_70612_2_, this.onGround ? 0.1F * f3 : 0.02F);
            f2 = 0.91F;

            if (this.onGround)
            {
                f2 = this.worldObj.getBlockState(new BlockPos(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.getEntityBoundingBox().minY) - 1, MathHelper.floor_double(this.posZ))).getBlock().slipperiness * 0.91F;
            }

            this.moveEntity(this.motionX, this.motionY, this.motionZ);
            this.motionX *= (double)f2;
            this.motionY *= (double)f2;
            this.motionZ *= (double)f2;
        }

        this.prevLimbSwingAmount = this.limbSwingAmount;
        double d1 = this.posX - this.prevPosX;
        double d0 = this.posZ - this.prevPosZ;
        float f4 = MathHelper.sqrt_double(d1 * d1 + d0 * d0) * 4.0F;

        if (f4 > 1.0F)
        {
            f4 = 1.0F;
        }

        this.limbSwingAmount += (f4 - this.limbSwingAmount) * 0.4F;
        this.limbSwing += this.limbSwingAmount;
    }

    /**
     * returns true if this entity is by a ladder, false otherwise
     */
    public boolean isOnLadder()
    {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public boolean func_110182_bF()
    {
        return this.dataWatcher.getWatchableObjectByte(25) != 0;
    }

    /**
     * True if the ghast has an unobstructed line of travel to the waypoint.
     */
    private boolean isCourseTraversable(double par1, double par3, double par5, double par7)
    {
        double d4 = (this.waypointX - this.posX) / par7;
        double d5 = (this.waypointY - this.posY) / par7;
        double d6 = (this.waypointZ - this.posZ) / par7;
        AxisAlignedBB axisalignedbb = this.getBoundingBox();

        for (int i = 1; (double) i < par7; ++i)
        {
            axisalignedbb.offset(d4, d5, d6);

            if (!this.worldObj.getCollidingBoundingBoxes(this, axisalignedbb).isEmpty())
            {
                return false;
            }
        }

        return true;
    }

    /**
     * Will return how many at most can spawn in a chunk at once.
     */
    public int getMaxSpawnedInChunk()
    {
        return 1;
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeEntityToNBT(par1NBTTagCompound);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readEntityFromNBT(par1NBTTagCompound);
        this.setAngry(par1NBTTagCompound.getBoolean("Angry"));

        this.setCombatTask();
    }

    @Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        //This line affects the speed of the monster
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.30000001192092896D);

        //My guess is that this will alter the max health
        if (this.isTamed())
        {
            this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(maxTamedHealth);
        } else
        {
            this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(maxUntamedHealth);
        }
    }

    /**
     * Returns true if the newer Entity AI code should be run
     */
    public boolean isAIEnabled()
    {
        return false;
    }

    /**
     * Sets the active target the Task system uses for tracking
     */
    public void setAttackTarget(EntityLivingBase par1EntityLivingBase)
    {
        super.setAttackTarget(par1EntityLivingBase);

        if (par1EntityLivingBase == null)
        {
            this.setAngry(false);
        } else if (!this.isTamed())
        {
            this.setAngry(true);
        }
    }

    /**
     * main AI tick function, replaces updateEntityActionState
     */
    protected void updateAITick()
    {
        this.dataWatcher.updateObject(18, this.getHealth());
    }

    protected void entityInit()
    {
        super.entityInit();
        this.dataWatcher.addObject(18, this.getHealth());
        this.dataWatcher.addObject(19, 0);
        this.dataWatcher.addObject(25, 0);
    }

    /**
     * Plays step sound at given x, y, z for the entity
     */
    protected void playStepSound(int par1, int par2, int par3, int par4)
    {
        this.playSound("mob.zombie.step", 0.15F, 1.0F);
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    protected String getLivingSound()
    {
        //TODO change sounds
        return "none";
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    protected String getHurtSound()
    {
        return "none";
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected String getDeathSound()
    {
        return "none";
    }

    /**
     * Returns the volume for the sounds this mob makes.
     */
    protected float getSoundVolume()
    {
        return 0.4F;
    }

    /**
     * Returns the item ID for the item the mob drops on death.
     */
    protected int getDropItemId()
    {
        return -1;
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        super.onUpdate();
        
        if (this.getHealth() <= this.getMaxHealth() / 2.0f && worldObj.rand.nextInt(200) == 0)
        {
            this.addPotionEffect(new PotionEffect(AlchemicalWizardry.customPotionReciprocation.id, 100, 1));
        }
    }

    public float getEyeHeight()
    {
        return this.height * 0.8F;
    }

    /**
     * The speed it takes to move the entityliving's rotationPitch through the faceEntity method. This is only currently
     * use in wolves.
     */
    public int getVerticalFaceSpeed()
    {
        return this.isSitting() ? 20 : super.getVerticalFaceSpeed();
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource source, float par2)
    {
        if (this.func_180431_b(source))
        {
            return false;
        } else
        {
            Entity entity = source.getEntity();
            this.aiSit.setSitting(false);

            if (entity != null && !(entity instanceof EntityPlayer) && !(entity instanceof EntityArrow))
            {
                par2 = (par2 + 1.0F) / 2.0F;
            }

            return super.attackEntityFrom(source, par2);
        }
    }

    public boolean attackEntityAsMob(Entity par1Entity)
    {
        int i = this.isTamed() ? 6 : 7;
        return par1Entity.attackEntityFrom(DamageSource.causeMobDamage(this), (float) i);
    }

    public void setTamed(boolean par1)
    {
        super.setTamed(par1);

        if (par1)
        {
            this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(maxTamedHealth);
        } else
        {
            this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(maxUntamedHealth);
        }
    }

    /**
     * Called when a player interacts with a mob. e.g. gets milk from a cow, gets into the saddle on a pig.
     */

    /**
     * Checks if the parameter is an item which this animal can be fed to breed it (wheat, carrots or seeds depending on
     * the animal type)
     */
    public boolean isBreedingItem(ItemStack par1ItemStack)
    {
        return false;
    }

    /**
     * Determines whether this wolf is angry or not.
     */
    public boolean isAngry()
    {
        return (this.dataWatcher.getWatchableObjectByte(16) & 2) != 0;
    }

    /**
     * Sets whether this wolf is angry or not.
     */
    public void setAngry(boolean par1)
    {
        byte b0 = this.dataWatcher.getWatchableObjectByte(16);

        if (par1)
        {
            this.dataWatcher.updateObject(16, b0 | 2);
        } else
        {
            this.dataWatcher.updateObject(16, b0 & -3);
        }
    }

    public void func_70918_i(boolean par1)
    {
        if (par1)
        {
            this.dataWatcher.updateObject(19, 1);
        } else
        {
            this.dataWatcher.updateObject(19, 0);
        }
    }

    /**
     * Returns true if the mob is currently able to mate with the specified mob.
     */
    public boolean canMateWith(EntityAnimal par1EntityAnimal)
    {
        return false;
    }

    public boolean func_70922_bv()
    {
        return this.dataWatcher.getWatchableObjectByte(19) == 1;
    }

    /**
     * Determines if an entity can be despawned, used on idle far away entities
     */
    protected boolean canDespawn()
    {
        return false;
    }

    @Override
    public boolean func_142018_a(EntityLivingBase par1EntityLivingBase, EntityLivingBase par2EntityLivingBase)
    {
        if (!(par1EntityLivingBase instanceof EntityCreeper) && !(par1EntityLivingBase instanceof EntityGhast))
        {
            if (par1EntityLivingBase instanceof EntityBoulderFist)
            {
                EntityBoulderFist entitywolf = (EntityBoulderFist) par1EntityLivingBase;

                if (entitywolf.isTamed() && entitywolf.getOwner() == par2EntityLivingBase)
                {
                    return false;
                }
            }

            return par1EntityLivingBase instanceof EntityPlayer && par2EntityLivingBase instanceof EntityPlayer && !((EntityPlayer) par2EntityLivingBase).canAttackPlayer((EntityPlayer) par1EntityLivingBase) ? false : !(par1EntityLivingBase instanceof EntityHorse) || !((EntityHorse) par1EntityLivingBase).isTame();
        } else
        {
            return false;
        }
    }


    /**
     * sets this entity's combat AI.
     */
    public void setCombatTask()
    {
        this.tasks.removeTask(this.aiAttackOnCollide);
        this.tasks.addTask(4, this.aiAttackOnCollide);
    }

    public void inflictEffectOnEntity(Entity target)
    {
        if (target instanceof EntityLivingBase)
        {
            ((EntityLivingBase) target).addPotionEffect(new PotionEffect(AlchemicalWizardry.customPotionDrowning.id, 100, 0));
        }
    }

    public static Entity getClosestVulnerableMonsterToEntity(Entity par1Entity, double par2)
    {
        double d4 = -1.0D;
        double par1 = par1Entity.posX;
        double par3 = par1Entity.posY;
        double par5 = par1Entity.posZ;

        EntityLivingBase entityLiving = null;
        World world = par1Entity.worldObj;

        double range = Math.sqrt(par2);
        double verticalRange = Math.sqrt(par2);
        List<EntityLivingBase> entities = world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(par1 - 0.5f, par3 - 0.5f, par5 - 0.5f, par1 + 0.5f, par3 + 0.5f, par5 + 0.5f).expand(range, verticalRange, range));
        if (entities == null)
        {
            return null;
        }

        for (int i = 0; i < entities.size(); ++i)
        {
            EntityLivingBase entityLiving1 = entities.get(i);

            if (!(entityLiving1 instanceof EntityPlayer && ((EntityPlayer) entityLiving1).capabilities.disableDamage) && entityLiving1.isEntityAlive())
            {
                double d5 = entityLiving1.getDistanceSq(par1, par3, par5);
                double d6 = par2;

                if (entityLiving1.isSneaking())
                {
                    d6 = par2 * 0.800000011920929D;
                }

                if (entityLiving1.isInvisible())
                {
                    float f = entityLiving1 instanceof EntityPlayer ? ((EntityPlayer) entityLiving1).getArmorVisibility() : 1.0f;

                    if (f < 0.1F)
                    {
                        f = 0.1F;
                    }

                    d6 *= (double) (0.7F * f);
                }

                if ((par2 < 0.0D || d5 < d6 * d6) && (d4 == -1.0D || d5 < d4))
                {
                    if (par1Entity != entityLiving1)
                    {
                        d4 = d5;
                        entityLiving = entityLiving1;
                    }
                }
            }
        }

        return entityLiving;
    }

    @Override
    public int getTotalArmorValue()
    {
        return 10;
    }
}
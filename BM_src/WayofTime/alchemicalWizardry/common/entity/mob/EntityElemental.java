package WayofTime.alchemicalWizardry.common.entity.mob;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockColored;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.common.AlchemicalWizardry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class EntityElemental extends EntityDemon
{
	//private EntityAIArrowAttack aiArrowAttack = new EntityAIArrowAttack(this, 1.0D, 40, 40, 15.0F);
	private EntityAIAttackOnCollide aiAttackOnCollide = new EntityAIAttackOnCollide(this, EntityPlayer.class, 1.2D, false);

	private static float maxTamedHealth = 100.0F;
	private static float maxUntamedHealth = 100.0F;

	public EntityElemental(World par1World, int demonID)
	{
		super(par1World, demonID);
		setSize(0.5F, 1.0F);
		setAggro(false);
		//this.targetTasks.addTask(4, new EntityAITargetNonTamed(this, EntitySheep.class, 200, false));
		setTamed(false);

		if (par1World != null && !par1World.isRemote)
		{
			setCombatTask();
		}

		//this.isImmuneToFire = true;
	}

	public int courseChangeCooldown;
	public double waypointX;
	public double waypointY;
	public double waypointZ;
	private Entity targetedEntity;

	/** Cooldown time between target loss and new target aquirement. */
	private int aggroCooldown;
	public int prevAttackCounter;
	public int attackCounter;

	/** The explosion radius of spawned fireballs. */
	//private int explosionStrength = 1;
	//
	//	private int heightOffsetUpdateTime;
	//	private float heightOffset = 0.5F;
	//	private int field_70846_g;

	@Override
	protected void dropFewItems(boolean par1, int par2)
	{
		if (worldObj.rand.nextFloat() < 1 - Math.pow(0.6f, par2 + 1))
		{
			entityDropItem(new ItemStack(AlchemicalWizardry.demonBloodShard, 1, 0), 0.0f);
		}
	}

	@Override
	protected void fall(float par1) {}

	/**
	 * Takes in the distance the entity has fallen this tick and whether its on the ground to update the fall distance
	 * and deal fall damage if landing on the ground.  Args: distanceFallenThisTick, onGround
	 */
	@Override
	protected void updateFallState(double par1, boolean par3) {}

	/**
	 * Moves the entity based on the specified heading.  Args: strafe, forward
	 */
	@Override
	public void moveEntityWithHeading(float par1, float par2)
	{
		if (isInWater())
		{
			moveFlying(par1, par2, 0.02F);
			moveEntity(motionX, motionY, motionZ);
			motionX *= 0.800000011920929D;
			motionY *= 0.800000011920929D;
			motionZ *= 0.800000011920929D;
		}
		else if (handleLavaMovement())
		{
			moveFlying(par1, par2, 0.02F);
			moveEntity(motionX, motionY, motionZ);
			motionX *= 0.5D;
			motionY *= 0.5D;
			motionZ *= 0.5D;
		}
		else
		{
			float f2 = 0.91F;

			if (onGround)
			{
				f2 = 0.54600006F;
				int i = worldObj.getBlockId(MathHelper.floor_double(posX), MathHelper.floor_double(boundingBox.minY) - 1, MathHelper.floor_double(posZ));

				if (i > 0)
				{
					f2 = Block.blocksList[i].slipperiness * 0.91F;
				}
			}

			float f3 = 0.16277136F / (f2 * f2 * f2);
			moveFlying(par1, par2, onGround ? 0.1F * f3 : 0.02F);
			f2 = 0.91F;

			if (onGround)
			{
				f2 = 0.54600006F;
				int j = worldObj.getBlockId(MathHelper.floor_double(posX), MathHelper.floor_double(boundingBox.minY) - 1, MathHelper.floor_double(posZ));

				if (j > 0)
				{
					f2 = Block.blocksList[j].slipperiness * 0.91F;
				}
			}

			moveEntity(motionX, motionY, motionZ);
			motionX *= f2;
			motionY *= f2;
			motionZ *= f2;
		}

		double d0 = posX - prevPosX;
		double d1 = posZ - prevPosZ;
		float f4 = MathHelper.sqrt_double(d0 * d0 + d1 * d1) * 4.0F;

		if (f4 > 1.0F)
		{
			f4 = 1.0F;
		}
	}

	/**
	 * returns true if this entity is by a ladder, false otherwise
	 */
	@Override
	public boolean isOnLadder()
	{
		return false;
	}

	@SideOnly(Side.CLIENT)
	public boolean func_110182_bF()
	{
		return dataWatcher.getWatchableObjectByte(25) != 0;
	}

	@Override
	protected void updateEntityActionState()
	{
		//        if (!this.worldObj.isRemote && this.worldObj.difficultySetting == 0)
		//        {
		//            this.setDead();
		//        }

		//this.despawnEntity();
		if (getHealth() <= getMaxHealth() / 2.0f && worldObj.rand.nextInt(200) == 0)
		{
			addPotionEffect(new PotionEffect(AlchemicalWizardry.customPotionReciprocation.id, 100, 1));
		}

		prevAttackCounter = attackCounter;
		double d0 = waypointX - posX;
		double d1 = waypointY - posY;
		double d2 = waypointZ - posZ;
		double d3 = d0 * d0 + d1 * d1 + d2 * d2;

		if (d3 < 1.0D || d3 > 3600.0D)
		{
			waypointX = posX + (rand.nextFloat() * 2.0F - 1.0F) * 16.0F;
			waypointY = posY + (rand.nextFloat() * 2.0F - 1.0F) * 16.0F;
			waypointZ = posZ + (rand.nextFloat() * 2.0F - 1.0F) * 16.0F;
		}

		if (courseChangeCooldown-- <= 0)
		{
			courseChangeCooldown += rand.nextInt(5) + 2;
			d3 = MathHelper.sqrt_double(d3);

			if (isCourseTraversable(waypointX, waypointY, waypointZ, d3))
			{
				motionX += d0 / d3 * 0.1D;
				motionY += d1 / d3 * 0.1D;
				motionZ += d2 / d3 * 0.1D;
			}
			else
			{
				waypointX = posX + (rand.nextFloat() * 2.0F - 1.0F) * 16.0F;
				waypointY = posY + (rand.nextFloat() * 2.0F - 1.0F) * 16.0F;
				waypointZ = posZ + (rand.nextFloat() * 2.0F - 1.0F) * 16.0F;
			}
		}

		if (targetedEntity != null && targetedEntity.isDead)
		{
			targetedEntity = null;
		}

		if (targetedEntity == null || aggroCooldown-- <= 0)
		{
			targetedEntity = getClosestVulnerableMonsterToEntity(this, 100.0D);

			if (targetedEntity != null)
			{
				aggroCooldown = 20;
			}
		}

		double d4 = 64.0D;

		if (targetedEntity != null && targetedEntity.getDistanceSqToEntity(this) < d4 * d4)
		{
			double d5 = targetedEntity.posX - posX;
			double d6 = targetedEntity.boundingBox.minY + targetedEntity.height / 2.0F - (posY + height / 2.0F);
			double d7 = targetedEntity.posZ - posZ;
			renderYawOffset = rotationYaw = -((float)Math.atan2(d5, d7)) * 180.0F / (float)Math.PI;

			if (courseChangeCooldown <= 0)
			{
				if (isCourseTraversable(targetedEntity.posX, targetedEntity.posY, targetedEntity.posZ, Math.sqrt(d5 * d5 + d6 * d6 + d7 * d7)))
				{
					waypointX = targetedEntity.posX;
					waypointY = targetedEntity.posY;
					waypointZ = targetedEntity.posZ;
					motionX += d5 / d3 * 0.1D;
					motionY += d6 / d3 * 0.1D;
					motionZ += d7 / d3 * 0.1D;
				}
				else
				{
					waypointX = posX + (rand.nextFloat() * 2.0F - 1.0F) * 16.0F;
					waypointY = posY + (rand.nextFloat() * 2.0F - 1.0F) * 16.0F;
					waypointZ = posZ + (rand.nextFloat() * 2.0F - 1.0F) * 16.0F;
					motionX += d5 / d3 * 0.1D;
					motionY += d6 / d3 * 0.1D;
					motionZ += d7 / d3 * 0.1D;
				}
			}

			if (canEntityBeSeen(targetedEntity))
			{
				if (Math.sqrt(d5 * d5 + d6 * d6 + d7 * d7) < 4)
				{
					//                	if (this.attackCounter == 10)
						//                    {
						//                        this.worldObj.playAuxSFXAtEntity((EntityPlayer)null, 1007, (int)this.posX, (int)this.posY, (int)this.posZ, 0);
						//                    }
					++attackCounter;

					if (attackCounter >= 10)
					{
						worldObj.playAuxSFXAtEntity((EntityPlayer)null, 1008, (int)posX, (int)posY, (int)posZ, 0);
						inflictEffectOnEntity(targetedEntity);
						attackCounter = -40;
					}
				}
			}
			else if (attackCounter > 0)
			{
				--attackCounter;
			}
		}
		else
		{
			renderYawOffset = rotationYaw = -((float)Math.atan2(motionX, motionZ)) * 180.0F / (float)Math.PI;

			if (attackCounter > 0)
			{
				--attackCounter;
			}
		}

		if (!worldObj.isRemote)
		{
			byte b0 = dataWatcher.getWatchableObjectByte(25);
			byte b1 = (byte)(attackCounter > 10 ? 1 : 0);

			if (b0 != b1)
			{
				dataWatcher.updateObject(25, Byte.valueOf(b1));
			}
		}
	}

	/**
	 * True if the ghast has an unobstructed line of travel to the waypoint.
	 */
	private boolean isCourseTraversable(double par1, double par3, double par5, double par7)
	{
		double d4 = (waypointX - posX) / par7;
		double d5 = (waypointY - posY) / par7;
		double d6 = (waypointZ - posZ) / par7;
		AxisAlignedBB axisalignedbb = boundingBox.copy();

		for (int i = 1; i < par7; ++i)
		{
			axisalignedbb.offset(d4, d5, d6);

			if (!worldObj.getCollidingBoundingBoxes(this, axisalignedbb).isEmpty())
			{
				return false;
			}
		}

		return true;
	}

	/**
	 * Will return how many at most can spawn in a chunk at once.
	 */
	@Override
	public int getMaxSpawnedInChunk()
	{
		return 1;
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	@Override
	public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.writeEntityToNBT(par1NBTTagCompound);
		par1NBTTagCompound.setBoolean("Angry", isAngry());
		par1NBTTagCompound.setByte("CollarColor", (byte)getCollarColor());
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	@Override
	public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.readEntityFromNBT(par1NBTTagCompound);
		setAngry(par1NBTTagCompound.getBoolean("Angry"));

		if (par1NBTTagCompound.hasKey("CollarColor"))
		{
			setCollarColor(par1NBTTagCompound.getByte("CollarColor"));
		}

		setCombatTask();
	}

	@Override
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		//This line affects the speed of the monster
		getEntityAttribute(SharedMonsterAttributes.movementSpeed).setAttribute(0.30000001192092896D);

		//My guess is that this will alter the max health
		if (isTamed())
		{
			getEntityAttribute(SharedMonsterAttributes.maxHealth).setAttribute(maxTamedHealth);
		}
		else
		{
			getEntityAttribute(SharedMonsterAttributes.maxHealth).setAttribute(maxUntamedHealth);
		}

		//this.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111128_a(10.0D);
	}

	/**
	 * Returns true if the newer Entity AI code should be run
	 */
	@Override
	public boolean isAIEnabled()
	{
		return false;
	}

	/**
	 * Sets the active target the Task system uses for tracking
	 */
	@Override
	public void setAttackTarget(EntityLivingBase par1EntityLivingBase)
	{
		super.setAttackTarget(par1EntityLivingBase);

		if (par1EntityLivingBase == null)
		{
			setAngry(false);
		}
		else if (!isTamed())
		{
			setAngry(true);
		}
	}

	/**
	 * main AI tick function, replaces updateEntityActionState
	 */
	@Override
	protected void updateAITick()
	{
		dataWatcher.updateObject(18, Float.valueOf(getHealth()));
	}

	@Override
	protected void entityInit()
	{
		super.entityInit();
		dataWatcher.addObject(18, new Float(getHealth()));
		dataWatcher.addObject(19, new Byte((byte)0));
		dataWatcher.addObject(20, new Byte((byte)BlockColored.getBlockFromDye(1)));
		dataWatcher.addObject(25, Byte.valueOf((byte)0));
	}

	/**
	 * Plays step sound at given x, y, z for the entity
	 */
	@Override
	protected void playStepSound(int par1, int par2, int par3, int par4)
	{
		playSound("mob.zombie.step", 0.15F, 1.0F);
	}

	/**
	 * Returns the sound this mob makes while it's alive.
	 */
	@Override
	protected String getLivingSound()
	{
		//TODO change sounds
		return "none";
	}

	/**
	 * Returns the sound this mob makes when it is hurt.
	 */
	@Override
	protected String getHurtSound()
	{
		return "none";
	}

	/**
	 * Returns the sound this mob makes on death.
	 */
	@Override
	protected String getDeathSound()
	{
		return "none";
	}

	/**
	 * Returns the volume for the sounds this mob makes.
	 */
	@Override
	protected float getSoundVolume()
	{
		return 0.4F;
	}

	/**
	 * Returns the item ID for the item the mob drops on death.
	 */
	@Override
	protected int getDropItemId()
	{
		return -1;
	}

	/**
	 * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
	 * use this to react to sunlight and start to burn.
	 */
	@Override
	public void onLivingUpdate()
	{
		super.onLivingUpdate();
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	@Override
	public void onUpdate()
	{
		super.onUpdate();
	}

	@Override
	public float getEyeHeight()
	{
		return height * 0.8F;
	}

	/**
	 * The speed it takes to move the entityliving's rotationPitch through the faceEntity method. This is only currently
	 * use in wolves.
	 */
	@Override
	public int getVerticalFaceSpeed()
	{
		return isSitting() ? 20 : super.getVerticalFaceSpeed();
	}

	/**
	 * Called when the entity is attacked.
	 */
	@Override
	public boolean attackEntityFrom(DamageSource par1DamageSource, float par2)
	{
		if (isEntityInvulnerable())
		{
			return false;
		}
		else
		{
			Entity entity = par1DamageSource.getEntity();
			aiSit.setSitting(false);

			if (entity != null && !(entity instanceof EntityPlayer) && !(entity instanceof EntityArrow))
			{
				par2 = (par2 + 1.0F) / 2.0F;
			}

			return super.attackEntityFrom(par1DamageSource, par2);
		}
	}

	@Override
	public boolean attackEntityAsMob(Entity par1Entity)
	{
		int i = isTamed() ? 6 : 7;
		return par1Entity.attackEntityFrom(DamageSource.causeMobDamage(this), i);
	}

	@Override
	public void setTamed(boolean par1)
	{
		super.setTamed(par1);

		if (par1)
		{
			getEntityAttribute(SharedMonsterAttributes.maxHealth).setAttribute(maxTamedHealth);
		}
		else
		{
			getEntityAttribute(SharedMonsterAttributes.maxHealth).setAttribute(maxUntamedHealth);
		}
	}

	/**
	 * Called when a player interacts with a mob. e.g. gets milk from a cow, gets into the saddle on a pig.
	 */

	/**
	 * Checks if the parameter is an item which this animal can be fed to breed it (wheat, carrots or seeds depending on
	 * the animal type)
	 */
	@Override
	public boolean isBreedingItem(ItemStack par1ItemStack)
	{
		return false;
		//return par1ItemStack == null ? false : (!(Item.itemsList[par1ItemStack.itemID] instanceof ItemFood) ? false : ((ItemFood)Item.itemsList[par1ItemStack.itemID]).isWolfsFavoriteMeat());
	}

	/**
	 * Determines whether this wolf is angry or not.
	 */
	public boolean isAngry()
	{
		return (dataWatcher.getWatchableObjectByte(16) & 2) != 0;
	}

	/**
	 * Sets whether this wolf is angry or not.
	 */
	public void setAngry(boolean par1)
	{
		byte b0 = dataWatcher.getWatchableObjectByte(16);

		if (par1)
		{
			dataWatcher.updateObject(16, Byte.valueOf((byte)(b0 | 2)));
		}
		else
		{
			dataWatcher.updateObject(16, Byte.valueOf((byte)(b0 & -3)));
		}
	}

	/**
	 * Return this wolf's collar color.
	 */
	public int getCollarColor()
	{
		return dataWatcher.getWatchableObjectByte(20) & 15;
	}

	/**
	 * Set this wolf's collar color.
	 */
	public void setCollarColor(int par1)
	{
		dataWatcher.updateObject(20, Byte.valueOf((byte)(par1 & 15)));
	}

	/**
	 * This function is used when two same-species animals in 'love mode' breed to generate the new baby animal.
	 */
	public EntityWolf spawnBabyAnimal(EntityAgeable par1EntityAgeable)
	{
		return null;
	}

	public void func_70918_i(boolean par1)
	{
		if (par1)
		{
			dataWatcher.updateObject(19, Byte.valueOf((byte)1));
		}
		else
		{
			dataWatcher.updateObject(19, Byte.valueOf((byte)0));
		}
	}

	/**
	 * Returns true if the mob is currently able to mate with the specified mob.
	 */
	@Override
	public boolean canMateWith(EntityAnimal par1EntityAnimal)
	{
		return false;
	}

	public boolean func_70922_bv()
	{
		return dataWatcher.getWatchableObjectByte(19) == 1;
	}

	/**
	 * Determines if an entity can be despawned, used on idle far away entities
	 */
	@Override
	protected boolean canDespawn()
	{
		//return !this.isTamed() && this.ticksExisted > 2400;
		return false;
	}

	@Override
	public boolean func_142018_a(EntityLivingBase par1EntityLivingBase, EntityLivingBase par2EntityLivingBase)
	{
		if (!(par1EntityLivingBase instanceof EntityCreeper) && !(par1EntityLivingBase instanceof EntityGhast))
		{
			if (par1EntityLivingBase instanceof EntityBoulderFist)
			{
				EntityBoulderFist entitywolf = (EntityBoulderFist)par1EntityLivingBase;

				if (entitywolf.isTamed() && entitywolf.func_130012_q() == par2EntityLivingBase)
				{
					return false;
				}
			}

			return par1EntityLivingBase instanceof EntityPlayer && par2EntityLivingBase instanceof EntityPlayer && !((EntityPlayer)par2EntityLivingBase).canAttackPlayer((EntityPlayer)par1EntityLivingBase) ? false : !(par1EntityLivingBase instanceof EntityHorse) || !((EntityHorse)par1EntityLivingBase).isTame();
			//return par1EntityLivingBase instanceof EntityPlayer && par2EntityLivingBase instanceof EntityPlayer && !((EntityPlayer)par2EntityLivingBase).func_96122_a((EntityPlayer)par1EntityLivingBase) ? false : !(par1EntityLivingBase instanceof EntityHorse) || !((EntityHorse)par1EntityLivingBase).func_110248_bS();
		}
		else
		{
			return false;
		}
	}

	@Override
	public EntityAgeable createChild(EntityAgeable par1EntityAgeable)
	{
		return spawnBabyAnimal(par1EntityAgeable);
	}

	/**
	 * sets this entity's combat AI.
	 */
	public void setCombatTask()
	{
		tasks.removeTask(aiAttackOnCollide);
		getHeldItem();
		tasks.addTask(4, aiAttackOnCollide);
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
		List<EntityLivingBase> entities = world.getEntitiesWithinAABB(EntityLivingBase.class, AxisAlignedBB.getBoundingBox(par1 - 0.5f, par3 - 0.5f, par5 - 0.5f, par1 + 0.5f, par3 + 0.5f, par5 + 0.5f).expand(range, verticalRange, range));
		if(entities == null)
		{
			return null;
		}

		for (int i = 0; i < entities.size(); ++i)
		{
			EntityLivingBase entityLiving1 = entities.get(i);

			if (!(entityLiving1 instanceof EntityPlayer && ((EntityPlayer)entityLiving1).capabilities.disableDamage) && entityLiving1.isEntityAlive())
			{
				double d5 = entityLiving1.getDistanceSq(par1, par3, par5);
				double d6 = par2;

				if (entityLiving1.isSneaking())
				{
					d6 = par2 * 0.800000011920929D;
				}

				if (entityLiving1.isInvisible())
				{
					float f = entityLiving1 instanceof EntityPlayer ? ((EntityPlayer)entityLiving1).getArmorVisibility() : 1.0f;

					if (f < 0.1F)
					{
						f = 0.1F;
					}

					d6 *= 0.7F * f;
				}

				if ((par2 < 0.0D || d5 < d6 * d6) && (d4 == -1.0D || d5 < d4))
				{
					if(par1Entity != entityLiving1)
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
	public int getTotalArmorValue() //TODO
	{
		return 10;
	}
}
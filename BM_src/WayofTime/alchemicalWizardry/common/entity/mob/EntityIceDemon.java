package WayofTime.alchemicalWizardry.common.entity.mob;

import net.minecraft.block.Block;
import net.minecraft.block.BlockColored;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIArrowAttack;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIFollowOwner;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIOwnerHurtByTarget;
import net.minecraft.entity.ai.EntityAIOwnerHurtTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.common.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.common.EntityAITargetAggro;
import WayofTime.alchemicalWizardry.common.entity.projectile.IceProjectile;

public class EntityIceDemon extends EntityDemon implements IRangedAttackMob
{
	private EntityAIArrowAttack aiArrowAttack = new EntityAIArrowAttack(this, 1.0D, 30, 50, 15.0F);
	private EntityAIAttackOnCollide aiAttackOnCollide = new EntityAIAttackOnCollide(this, EntityPlayer.class, 1.2D, false);

	private static float maxTamedHealth = 50.0F;
	private static float maxUntamedHealth = 30.0F;

	public EntityIceDemon(World par1World)
	{
		super(par1World, AlchemicalWizardry.entityIceDemonID);
		setSize(0.5F, 2.0F);
		//this.getNavigator().setAvoidsWater(true);
		tasks.addTask(1, new EntityAISwimming(this));
		tasks.addTask(2, aiSit);
		//this.tasks.addTask(3, new EntityAILeapAtTarget(this, 0.4F));
		//this.tasks.addTask(4, new EntityAIAttackOnCollide(this, 1.0D, true));
		tasks.addTask(5, new EntityAIFollowOwner(this, 1.0D, 10.0F, 2.0F));
		//this.tasks.addTask(6, new EntityAIMate(this, 1.0D));
		tasks.addTask(7, new EntityAIWander(this, 1.0D));
		//this.tasks.addTask(8, new EntityAIBeg(this, 8.0F));
		tasks.addTask(9, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		tasks.addTask(9, new EntityAILookIdle(this));
		targetTasks.addTask(1, new EntityAIOwnerHurtByTarget(this));
		targetTasks.addTask(2, new EntityAIOwnerHurtTarget(this));
		targetTasks.addTask(3, new EntityAIHurtByTarget(this, true));
		targetTasks.addTask(4, new EntityAITargetAggro(this, EntityPlayer.class, 0, false));
		setAggro(false);
		//this.targetTasks.addTask(4, new EntityAITargetNonTamed(this, EntitySheep.class, 200, false));
		setTamed(false);

		if (par1World != null && !par1World.isRemote)
		{
			setCombatTask();
		}

		//this.isImmuneToFire = true;
	}

	@Override
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		//This line affects the speed of the monster
		getEntityAttribute(SharedMonsterAttributes.movementSpeed).setAttribute(0.40000001192092896D);

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
		return true;
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

	/**
	 * Returns the sound this mob makes while it's alive.
	 */
	@Override
	protected String getLivingSound()
	{
		return "none";
	}

	/**
	 * Returns the sound this mob makes when it is hurt.
	 */
	@Override
	protected String getHurtSound()
	{
		return "mob.irongolem.hit";
	}

	/**
	 * Returns the sound this mob makes on death.
	 */
	@Override
	protected String getDeathSound()
	{
		return "mob.irongolem.death";
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
		int range = 2;

		for (int i = -range; i <= range; i++)
		{
			for (int j = -range; j <= range; j++)
			{
				for (int k = -range; k <= range; k++)
				{
					if (worldObj.rand.nextFloat() < 0.25f)
					{
						int blockID = worldObj.getBlockId((int)posX + i, (int)posY + j, (int)posZ + k);

						if (blockID == Block.waterStill.blockID || blockID == Block.waterMoving.blockID)
						{
							worldObj.setBlock((int)posX + i, (int)posY + j, (int)posZ + k, Block.ice.blockID);
						}
					}
				}
			}
		}
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
		int i = isTamed() ? 4 : 2;
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
	@Override
	public boolean interact(EntityPlayer par1EntityPlayer)
	{
		ItemStack itemstack = par1EntityPlayer.inventory.getCurrentItem();

		if (isTamed())
		{
			if (itemstack != null)
			{
				if (Item.itemsList[itemstack.itemID] instanceof ItemFood)
				{
					ItemFood itemfood = (ItemFood)Item.itemsList[itemstack.itemID];

					if (itemfood.isWolfsFavoriteMeat() && dataWatcher.getWatchableObjectFloat(18) < maxTamedHealth)
					{
						if (!par1EntityPlayer.capabilities.isCreativeMode)
						{
							--itemstack.stackSize;
						}

						heal(itemfood.getHealAmount());

						if (itemstack.stackSize <= 0)
						{
							par1EntityPlayer.inventory.setInventorySlotContents(par1EntityPlayer.inventory.currentItem, (ItemStack)null);
						}

						return true;
					}
				}
			}

			if (par1EntityPlayer.getCommandSenderName().equalsIgnoreCase(getOwnerName()) && !isBreedingItem(itemstack))
			{
				if (!worldObj.isRemote)
				{
					aiSit.setSitting(!isSitting());
					isJumping = false;
					setPathToEntity((PathEntity)null);
					setTarget((Entity)null);
					setAttackTarget((EntityLivingBase)null);
				}

				sendSittingMessageToPlayer(par1EntityPlayer, !isSitting());
			}
		}
		else if (itemstack != null && itemstack.itemID == AlchemicalWizardry.weakBloodOrb.itemID && !isAngry() && !isAggro())
		{
			if (!par1EntityPlayer.capabilities.isCreativeMode)
			{
				--itemstack.stackSize;
			}

			if (itemstack.stackSize <= 0)
			{
				par1EntityPlayer.inventory.setInventorySlotContents(par1EntityPlayer.inventory.currentItem, (ItemStack)null);
			}

			if (!worldObj.isRemote)
			{
				if (rand.nextInt(1) == 0)
				{
					setTamed(true);
					setPathToEntity((PathEntity)null);
					setAttackTarget((EntityLivingBase)null);
					aiSit.setSitting(true);
					setHealth(maxTamedHealth);
					setOwner(par1EntityPlayer.getCommandSenderName());
					playTameEffect(true);
					worldObj.setEntityState(this, (byte)7);
				}
				else
				{
					playTameEffect(false);
					worldObj.setEntityState(this, (byte)6);
				}
			}

			return true;
		}

		return super.interact(par1EntityPlayer);
	}

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
			if (par1EntityLivingBase instanceof EntityIceDemon)
			{
				EntityIceDemon entitywolf = (EntityIceDemon)par1EntityLivingBase;

				if (entitywolf.isTamed() && entitywolf.func_130012_q() == par2EntityLivingBase)
				{
					return false;
				}
			}

			return par1EntityLivingBase instanceof EntityPlayer && par2EntityLivingBase instanceof EntityPlayer && !((EntityPlayer)par2EntityLivingBase).canAttackPlayer((EntityPlayer)par1EntityLivingBase) ? false : !(par1EntityLivingBase instanceof EntityHorse) || !((EntityHorse)par1EntityLivingBase).isTame();
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
	 * Attack the specified entity using a ranged attack.
	 */
	@Override
	public void attackEntityWithRangedAttack(EntityLivingBase par1EntityLivingBase, float par2)
	{
		IceProjectile hol = new IceProjectile(worldObj, this, par1EntityLivingBase, 1.8f, 0f, 3, 600);
		worldObj.spawnEntityInWorld(hol);
	}

	/**
	 * sets this entity's combat AI.
	 */
	public void setCombatTask()
	{
		tasks.removeTask(aiAttackOnCollide);
		tasks.removeTask(aiArrowAttack);
		getHeldItem();
		tasks.addTask(4, aiArrowAttack);
	}
}
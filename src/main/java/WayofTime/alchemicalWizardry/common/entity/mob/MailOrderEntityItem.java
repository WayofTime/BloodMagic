package WayofTime.alchemicalWizardry.common.entity.mob;

import WayofTime.alchemicalWizardry.ModItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class MailOrderEntityItem extends EntityItem
{
	public MailOrderEntityItem(World par1World, double par2, double par4, double par6) 
	{ 
		super(par1World, par2, par4, par6); 
		this.isImmuneToFire = true; 
	    this.lifespan = 72000; 
	} 

	public MailOrderEntityItem(World par1World, double par2, double par4, double par6, ItemStack par8ItemStack)
	{
		this(par1World, par2, par4, par6);
		this.setEntityItemStack(par8ItemStack);
		this.isImmuneToFire = true; 
        this.lifespan = (par8ItemStack.getItem() == null ? 6000 : par8ItemStack.getItem().getEntityLifespan(par8ItemStack, par1World));
	}
	
	public MailOrderEntityItem(World world, Entity original, ItemStack stack) 
	{ 
	    this(world, original.posX, original.posY, original.posZ); 
	    this.delayBeforeCanPickup = 20; 
	    this.motionX = original.motionX; 
	    this.motionY = original.motionY; 
	    this.motionZ = original.motionZ; 
	    this.setEntityItemStack(stack); 
		this.isImmuneToFire = true; 
	} 

	public MailOrderEntityItem(World par1world)
	{
		super(par1world);
	}
	
	public void onUpdate()
	{
		super.onUpdate();
		
		if(!worldObj.isRemote && this.ticksExisted > 100 && !this.isDead)
		{
			worldObj.addWeatherEffect(new EntityLightningBolt(worldObj, this.posX, this.posY, this.posZ));
			EntityItem entity = new BookEntityItem(worldObj, this.posX, this.posY, this.posZ, new ItemStack(ModItems.itemBloodMagicBook));
			entity.lifespan = 6000;
			entity.delayBeforeCanPickup = 20;
			entity.motionY = 1;
			worldObj.spawnEntityInWorld(entity);
			this.setDead();
		}
	}
}
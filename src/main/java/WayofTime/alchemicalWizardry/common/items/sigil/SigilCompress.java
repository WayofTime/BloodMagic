package WayofTime.alchemicalWizardry.common.items.sigil;

import java.util.List;

import WayofTime.alchemicalWizardry.api.items.interfaces.ISigil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.api.compress.CompressionRegistry;
import WayofTime.alchemicalWizardry.api.items.interfaces.ArmourUpgrade;
import WayofTime.alchemicalWizardry.api.items.interfaces.IHolding;
import WayofTime.alchemicalWizardry.common.items.BindableItems;

public class SigilCompress extends SigilToggleable implements IHolding, ArmourUpgrade, ISigil
{
    public SigilCompress()
    {
        super();
        setEnergyUsed(200);
    }

    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
    {
        par3List.add(StatCollector.translateToLocal("tooltip.packratsigil.desc"));

        if (!(par1ItemStack.getTagCompound() == null))
        {
            if (this.getActivated(par1ItemStack))
            {
                par3List.add(StatCollector.translateToLocal("tooltip.sigil.state.activated"));
            } else
            {
                par3List.add(StatCollector.translateToLocal("tooltip.sigil.state.deactivated"));
            }

            par3List.add(StatCollector.translateToLocal("tooltip.owner.currentowner") + " " + par1ItemStack.getTagCompound().getString("ownerName"));
        }
    }

    @Override
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        if (!BindableItems.checkAndSetItemOwner(par1ItemStack, par3EntityPlayer) || par3EntityPlayer.isSneaking())
        {
            return par1ItemStack;
        }

        if (par1ItemStack.getTagCompound() == null)
        {
            par1ItemStack.setTagCompound(new NBTTagCompound());
        }

        NBTTagCompound tag = par1ItemStack.getTagCompound();
        this.setActivated(par1ItemStack, !(this.getActivated(par1ItemStack)));

        if (this.getActivated(par1ItemStack))
        {
            par1ItemStack.setItemDamage(1);
            tag.setInteger("worldTimeDelay", (int) (par2World.getWorldTime() - 1) % 200);

            if (!par3EntityPlayer.capabilities.isCreativeMode)
            {
                if (!BindableItems.syphonBatteries(par1ItemStack, par3EntityPlayer, getEnergyUsed()))
                {
                	this.setActivated(par1ItemStack, false);
                }
            }
        } else
        {
            par1ItemStack.setItemDamage(par1ItemStack.getMaxDamage());
        }

        return par1ItemStack;
    }

    @Override
    public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5)
    {
        if (!(par3Entity instanceof EntityPlayer) || par2World.isRemote)
        {
            return;
        }

        EntityPlayer par3EntityPlayer = (EntityPlayer) par3Entity;

        if (par1ItemStack.getTagCompound() == null)
        {
            par1ItemStack.setTagCompound(new NBTTagCompound());
        }

        if (this.getActivated(par1ItemStack))
        {
        	ItemStack stack = CompressionRegistry.compressInventory(par3EntityPlayer.inventory.mainInventory, par2World);
        	if(stack != null)
        	{
            	EntityItem entityItem = new EntityItem(par2World, par3EntityPlayer.posX, par3EntityPlayer.posY, par3EntityPlayer.posZ, stack);
            	par2World.spawnEntityInWorld(entityItem);
        	}
        }
        if (par2World.getWorldTime() % 200 == par1ItemStack.getTagCompound().getInteger("worldTimeDelay") && this.getActivated(par1ItemStack))
        {
            if (!par3EntityPlayer.capabilities.isCreativeMode)
            {
                if(!BindableItems.syphonBatteries(par1ItemStack, par3EntityPlayer, getEnergyUsed()))
                {
                	this.setActivated(par1ItemStack, false);
                }
            }
        }
    }

	@Override
	public void onArmourUpdate(World world, EntityPlayer player, ItemStack thisItemStack) 
	{
		if(world.isRemote)
		{
			return;
		}
		ItemStack stack = CompressionRegistry.compressInventory(player.inventory.mainInventory, world);
    	if(stack != null)
    	{
        	EntityItem entityItem = new EntityItem(world, player.posX, player.posY, player.posZ, stack);
        	world.spawnEntityInWorld(entityItem);
    	}	
	}

	@Override
	public boolean isUpgrade() 
	{
		return true;
	}

	@Override
	public int getEnergyForTenSeconds() 
	{
		return 200;
	}
}
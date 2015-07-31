package WayofTime.alchemicalWizardry.common.items.sigil;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.api.items.interfaces.ArmourUpgrade;
import WayofTime.alchemicalWizardry.api.items.interfaces.ISigil;
import WayofTime.alchemicalWizardry.common.items.BindableItems;

public class SigilAir extends BindableItems implements ArmourUpgrade, ISigil
{
    public SigilAir()
    {
        super();
        setEnergyUsed(50);
    }

    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
    {
        par3List.add(StatCollector.translateToLocal("tooltip.airsigil.desc"));

        if (!(par1ItemStack.getTagCompound() == null))
        {
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

        if(par2World.isRemote && this.isItemUnusable(par1ItemStack))
        {
        	return par1ItemStack;
        }
        
        Vec3 vec = par3EntityPlayer.getLookVec();
        double wantedVelocity = 1.7;

        if (par3EntityPlayer.isPotionActive(AlchemicalWizardry.customPotionBoost))
        {
            int i = par3EntityPlayer.getActivePotionEffect(AlchemicalWizardry.customPotionBoost).getAmplifier();
            wantedVelocity += (1 + i) * (0.35);
        }

        par3EntityPlayer.motionX = vec.xCoord * wantedVelocity;
        par3EntityPlayer.motionY = vec.yCoord * wantedVelocity;
        par3EntityPlayer.motionZ = vec.zCoord * wantedVelocity;
        par2World.playSoundEffect((double) ((float) par3EntityPlayer.posX + 0.5F), (double) ((float) par3EntityPlayer.posY + 0.5F), (double) ((float) par3EntityPlayer.posZ + 0.5F), "random.fizz", 0.5F, 2.6F + (par2World.rand.nextFloat() - par2World.rand.nextFloat()) * 0.8F);
        par3EntityPlayer.fallDistance = 0;

        if (!par3EntityPlayer.capabilities.isCreativeMode)
        {
            if (!BindableItems.syphonBatteries(par1ItemStack, par3EntityPlayer, getEnergyUsed()))
            {
            	if(!par2World.isRemote)
            	{
            		this.setIsItemUnusable(par1ItemStack, true);
            	}
            }else
            {
            	if(!par2World.isRemote)
            	{
            		this.setIsItemUnusable(par1ItemStack, false);
            	}
            }
        } else
        {
            return par1ItemStack;
        }

        return par1ItemStack;
    }
    
    public boolean isItemUnusable(ItemStack stack)
    {
    	NBTTagCompound tag = stack.getTagCompound();
    	if(tag == null)
    	{
    		tag = new NBTTagCompound();
    		stack.setTagCompound(tag);
    	}
    	
    	return tag.getBoolean("unusable");
    }
    
    public void setIsItemUnusable(ItemStack stack, boolean bool)
    {
    	NBTTagCompound tag = stack.getTagCompound();
    	if(tag == null)
    	{
    		tag = new NBTTagCompound();
    		stack.setTagCompound(tag);
    	}
    	
    	tag.setBoolean("unusable", bool);
    }

    @Override
    public void onArmourUpdate(World world, EntityPlayer player, ItemStack thisItemStack)
    {
        // TODO Auto-generated method stub
        player.fallDistance = 0;
    }

    @Override
    public boolean isUpgrade()
    {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public int getEnergyForTenSeconds()
    {
        // TODO Auto-generated method stub
        return 50;
    }
}

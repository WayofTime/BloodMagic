package WayofTime.alchemicalWizardry.common.items;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.common.omega.OmegaParadigm;
import WayofTime.alchemicalWizardry.common.omega.OmegaRegistry;

public class BoundBlade extends ItemSword
{
    private int energyUsed;

    public BoundBlade()
    {
        super(AlchemicalWizardry.bloodBoundToolMaterial);
        setMaxStackSize(1);
        setEnergyUsed(50);
        setFull3D();
        setMaxDamage(100);
    }

    public void setEnergyUsed(int i)
    {
        energyUsed = i;
    }

    public int getEnergyUsed()
    {
        return this.energyUsed;
    }
    
    private OmegaParadigm getOmegaParadigmOfWeilder(EntityPlayer player)
    {
    	return OmegaRegistry.getOmegaParadigmOfWeilder(player);
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity)
    {
    	boolean isActive = getActivated(stack);
    	if(isActive && !player.worldObj.isRemote)
    	{
    		OmegaParadigm parad = this.getOmegaParadigmOfWeilder(player);
    		
    		if(parad != null && parad.isPlayerWearingFullSet(player))
    		{
    			if(!parad.onBoundSwordLeftClickEntity(stack, player, entity))
    			{
    				return true;
    			}
    		}
    	}
        return !isActive;
    }

    @Override
    public boolean hitEntity(ItemStack par1ItemStack, EntityLivingBase par2EntityLivingBase, EntityLivingBase par3EntityLivingBase)
    {
        if (par3EntityLivingBase instanceof EntityPlayer)
        {
            if (!BindableItems.checkAndSetItemOwner(par1ItemStack, (EntityPlayer) par3EntityLivingBase) || !BindableItems.syphonBatteries(par1ItemStack, (EntityPlayer) par3EntityLivingBase, this.getEnergyUsed()))
            {
            	return false;
            }
        }

        par2EntityLivingBase.addPotionEffect(new PotionEffect(Potion.weakness.id, 60, 2));
        return true;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        if (!BindableItems.checkAndSetItemOwner(par1ItemStack, par3EntityPlayer) || par3EntityPlayer.isSneaking())
        {
            this.setActivated(par1ItemStack, !getActivated(par1ItemStack));
            par1ItemStack.getTagCompound().setInteger("worldTimeDelay", (int) (par2World.getWorldTime() - 1) % 100);
            return par1ItemStack;
        }

        if (!getActivated(par1ItemStack))
        {
            return par1ItemStack;
        }

        return par1ItemStack;
    }

    @Override
    public int getItemEnchantability()
    {
        return 30;
    }

    @Override
    public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5)
    {
        if (!(par3Entity instanceof EntityPlayer))
        {
            return;
        }

        EntityPlayer par3EntityPlayer = (EntityPlayer) par3Entity;

        if (par1ItemStack.getTagCompound() == null)
        {
            par1ItemStack.setTagCompound(new NBTTagCompound());
        }

        if (par2World.getWorldTime() % 100 == par1ItemStack.getTagCompound().getInteger("worldTimeDelay") && par1ItemStack.getTagCompound().getBoolean("isActive"))
        {
            if (!par3EntityPlayer.capabilities.isCreativeMode)
            {
                if(!BindableItems.syphonBatteries(par1ItemStack, par3EntityPlayer, 50))
                {
                	this.setActivated(par1ItemStack, false);
                }
            }
        }

        par1ItemStack.setItemDamage(0);
    }

    public void setActivated(ItemStack stack, boolean newActivated)
    {
        stack.setItemDamage(newActivated ? 1 : 0);
    }

    public boolean getActivated(ItemStack stack)
    {
        return stack.getItemDamage() == 1;
    }

    public float func_82803_g()
    {
        return 4.0F;
    }

    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
    {
        par3List.add(StatCollector.translateToLocal("tooltip.caution.desc1"));
        par3List.add(StatCollector.translateToLocal("tooltip.caution.desc2"));

        if (!(par1ItemStack.getTagCompound() == null))
        {
            if (par1ItemStack.getTagCompound().getBoolean("isActive"))
            {
                par3List.add(StatCollector.translateToLocal("tooltip.sigil.state.activated"));
            } else
            {
                par3List.add(StatCollector.translateToLocal("tooltip.sigil.state.deactivated"));
            }

            if (!par1ItemStack.getTagCompound().getString("ownerName").equals(""))
            {
                par3List.add(StatCollector.translateToLocal("tooltip.owner.currentowner") + " " + par1ItemStack.getTagCompound().getString("ownerName"));
            }
        }
    }

    @Override
    public float getStrVsBlock(ItemStack par1ItemStack, Block par2Block)
    {
        if (par2Block == Blocks.web)
        {
            return 15.0F;
        } else
        {
            Material material = par2Block.getMaterial();
            return material != Material.plants && material != Material.vine && material != Material.coral && material != Material.leaves && material != Material.gourd ? 1.0F : 1.5F;
        }
    }
}

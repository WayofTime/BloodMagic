package WayofTime.alchemicalWizardry.common.items.sigil;

import WayofTime.alchemicalWizardry.api.items.interfaces.ISigil;
import WayofTime.alchemicalWizardry.common.items.BindableItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.util.List;

public class SigilOfElementalAffinity extends SigilToggleable implements ISigil
{
    public SigilOfElementalAffinity()
    {
        super();
        setEnergyUsed(200);
    }

    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
    {
        par3List.add(StatCollector.translateToLocal("tooltip.sigilofelementalaffinity.desc1"));
        par3List.add(StatCollector.translateToLocal("tooltip.sigilofelementalaffinity.desc2"));

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

        if (this.getActivated(par1ItemStack) && BindableItems.syphonBatteries(par1ItemStack, par3EntityPlayer, getEnergyUsed()))
        {
            par1ItemStack.setItemDamage(1);
            tag.setInteger("worldTimeDelay", (int) (par2World.getWorldTime() - 1) % 200);
            par3EntityPlayer.addPotionEffect(new PotionEffect(Potion.waterBreathing.id, 2, 0, true, false));
            par3EntityPlayer.addPotionEffect(new PotionEffect(Potion.fireResistance.id, 2, 0, true, false));
        } else
        {
            par1ItemStack.setItemDamage(par1ItemStack.getMaxDamage());
        }

        return par1ItemStack;
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

        if (this.getActivated(par1ItemStack))
        {
            par3EntityPlayer.fallDistance = 0;
            par3EntityPlayer.addPotionEffect(new PotionEffect(Potion.waterBreathing.id, 2, 0, true, false));
            par3EntityPlayer.addPotionEffect(new PotionEffect(Potion.fireResistance.id, 2, 0, true, false));
        }

        if (par2World.getWorldTime() % 200 == par1ItemStack.getTagCompound().getInteger("worldTimeDelay") && this.getActivated(par1ItemStack))
        {
            if (!par3EntityPlayer.capabilities.isCreativeMode)
            {
                if (!BindableItems.syphonBatteries(par1ItemStack, par3EntityPlayer, getEnergyUsed()))
                {
                	this.setActivated(par1ItemStack, false);
                }
            }
        }
    }
}
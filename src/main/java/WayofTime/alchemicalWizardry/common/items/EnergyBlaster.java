package WayofTime.alchemicalWizardry.common.items;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.common.entity.projectile.EnergyBlastProjectile;

public class EnergyBlaster extends BindableItems
{
    private int damage;

    public EnergyBlaster()
    {
        super();
        setMaxStackSize(1);
        setFull3D();
        setMaxDamage(250);
        this.setEnergyUsed(150);
        damage = 12;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        final int maxDelay = 15;

        if (!BindableItems.checkAndSetItemOwner(par1ItemStack, par3EntityPlayer) || par3EntityPlayer.isSneaking())
        {
        	System.out.println("Toggle");
            this.setActivated(par1ItemStack, !getActivated(par1ItemStack));
            par1ItemStack.getTagCompound().setInteger("worldTimeDelay", (int) (par2World.getWorldTime() - 1) % 100);
            return par1ItemStack;
        }

        if (!getActivated(par1ItemStack))
        {
            return par1ItemStack;
        }

        if (this.getDelay(par1ItemStack) > 0)
        {
            return par1ItemStack;
        }

        if (!par3EntityPlayer.capabilities.isCreativeMode)
        {
            if(!syphonBatteries(par1ItemStack, par3EntityPlayer, this.getEnergyUsed()))
            {
            	return par1ItemStack;
            }
        }

        par2World.playSoundAtEntity(par3EntityPlayer, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

        if (!par2World.isRemote)
        {
            par2World.spawnEntityInWorld(new EnergyBlastProjectile(par2World, par3EntityPlayer, damage));
            this.setDelay(par1ItemStack, maxDelay);
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
        int delay = this.getDelay(par1ItemStack);

        if (!par2World.isRemote && delay > 0)
        {
            this.setDelay(par1ItemStack, delay - 1);
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

    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
    {
        par3List.add(StatCollector.translateToLocal("tooltip.energyblast.desc1"));
        par3List.add(StatCollector.translateToLocal("tooltip.energyblast.desc2"));
        par3List.add(StatCollector.translateToLocal("tooltip.alchemy.damage") + " " + damage);

        if (!(par1ItemStack.getTagCompound() == null))
        {
            if (getActivated(par1ItemStack))
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

    public void setActivated(ItemStack stack, boolean newActivated)
    {
        stack.setItemDamage(newActivated ? 1 : 0);
    }

    public boolean getActivated(ItemStack stack)
    {
        return stack.getItemDamage() == 1;
    }

    public void setDelay(ItemStack par1ItemStack, int newDelay)
    {
        NBTTagCompound itemTag = par1ItemStack.getTagCompound();

        if (itemTag == null)
        {
            par1ItemStack.setTagCompound(new NBTTagCompound());
        }

        itemTag.setInteger("delay", newDelay);
    }

    public int getDelay(ItemStack par1ItemStack)
    {
        NBTTagCompound itemTag = par1ItemStack.getTagCompound();

        if (itemTag == null)
        {
            par1ItemStack.setTagCompound(new NBTTagCompound());
        }

        return itemTag.getInteger("delay");
    }
}

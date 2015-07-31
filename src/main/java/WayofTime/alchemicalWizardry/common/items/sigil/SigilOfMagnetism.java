package WayofTime.alchemicalWizardry.common.items.sigil;

import java.util.List;

import WayofTime.alchemicalWizardry.api.items.interfaces.ISigil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.api.items.interfaces.ArmourUpgrade;
import WayofTime.alchemicalWizardry.api.items.interfaces.IHolding;
import WayofTime.alchemicalWizardry.common.items.BindableItems;

public class SigilOfMagnetism extends SigilToggleable implements ArmourUpgrade, IHolding, ISigil
{
    private int tickDelay = 300;

    public SigilOfMagnetism()
    {
        super();
        setEnergyUsed(50);
    }

    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
    {
        par3List.add(StatCollector.translateToLocal("tooltip.sigilofmagnetism.desc"));

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
            tag.setInteger("worldTimeDelay", (int) (par2World.getWorldTime() - 1) % tickDelay);
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
            if (par2World.getWorldTime() % tickDelay == par1ItemStack.getTagCompound().getInteger("worldTimeDelay"))
            {
                if(!BindableItems.syphonBatteries(par1ItemStack, (EntityPlayer) par3Entity, getEnergyUsed()))
                {
                	this.setActivated(par1ItemStack, false);
                }
            }

            int range = 5;
            int verticalRange = 5;
            float posX = Math.round(par3Entity.posX);
            float posY = (float) (par3Entity.posY - par3Entity.getEyeHeight());
            float posZ = Math.round(par3Entity.posZ);
            List<EntityItem> entities = par3EntityPlayer.worldObj.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(posX - 0.5f, posY - 0.5f, posZ - 0.5f, posX + 0.5f, posY + 0.5f, posZ + 0.5f).expand(range, verticalRange, range));
            List<EntityXPOrb> xpOrbs = par3EntityPlayer.worldObj.getEntitiesWithinAABB(EntityXPOrb.class, new AxisAlignedBB(posX - 0.5f, posY - 0.5f, posZ - 0.5f, posX + 0.5f, posY + 0.5f, posZ + 0.5f).expand(range, verticalRange, range));

            for (EntityItem entity : entities)
            {
                if (entity != null && !par2World.isRemote)
                {
                    entity.onCollideWithPlayer(par3EntityPlayer);
                }
            }

            for (EntityXPOrb xpOrb : xpOrbs)
            {
                if (xpOrb != null && !par2World.isRemote)
                {
                    xpOrb.onCollideWithPlayer(par3EntityPlayer);
                }
            }
        }
    }

    @Override
    public void onArmourUpdate(World world, EntityPlayer player, ItemStack thisItemStack)
    {
        int range = 5;
        int verticalRange = 5;
        float posX = Math.round(player.posX);
        float posY = (float) (player.posY - player.getEyeHeight());
        float posZ = Math.round(player.posZ);
        List<EntityItem> entities = player.worldObj.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(posX - 0.5f, posY - 0.5f, posZ - 0.5f, posX + 0.5f, posY + 0.5f, posZ + 0.5f).expand(range, verticalRange, range));
        List<EntityXPOrb> xpOrbs = player.worldObj.getEntitiesWithinAABB(EntityXPOrb.class, new AxisAlignedBB(posX - 0.5f, posY - 0.5f, posZ - 0.5f, posX + 0.5f, posY + 0.5f, posZ + 0.5f).expand(range, verticalRange, range));

        for (EntityItem entity : entities)
        {
            if (entity != null && !world.isRemote)
            {
                entity.onCollideWithPlayer(player);
            }
        }

        for (EntityXPOrb xpOrb : xpOrbs)
        {
            if (xpOrb != null && !world.isRemote)
            {
                xpOrb.onCollideWithPlayer(player);
            }
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
        return 25;
    }
}

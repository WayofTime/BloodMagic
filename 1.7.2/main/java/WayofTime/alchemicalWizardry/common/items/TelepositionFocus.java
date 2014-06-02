package WayofTime.alchemicalWizardry.common.items;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TelepositionFocus extends EnergyItems
{
    private int focusLevel;

    public TelepositionFocus(int focusLevel)
    {
        super();
        this.setMaxStackSize(1);
        setCreativeTab(AlchemicalWizardry.tabBloodMagic);
        this.focusLevel = focusLevel;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister)
    {
        //TODO
        this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:TeleposerFocus");
    }

    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
    {
        par3List.add("An Enderpearl imbued with blood");

        if (!(par1ItemStack.stackTagCompound == null))
        {
            NBTTagCompound itemTag = par1ItemStack.stackTagCompound;

            if (!par1ItemStack.stackTagCompound.getString("ownerName").equals(""))
            {
                par3List.add("Current owner: " + par1ItemStack.stackTagCompound.getString("ownerName"));
            }

            par3List.add("Coords: " + itemTag.getInteger("xCoord") + ", " + itemTag.getInteger("yCoord") + ", " + itemTag.getInteger("zCoord"));
            par3List.add("Bound Dimension: " + getDimensionID(par1ItemStack));
        }
    }

    @Override
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        EnergyItems.checkAndSetItemOwner(par1ItemStack, par3EntityPlayer);

        if (par3EntityPlayer.isSneaking())
        {
            return par1ItemStack;
        }

//        if (!par2World.isRemote)
//        {
//            //par2World.spawnEntityInWorld(new EnergyBlastProjectile(par2World, par3EntityPlayer, damage));
//            par2World.spawnEntityInWorld(new FireProjectile(par2World, par3EntityPlayer, 10));
//        }
        return par1ItemStack;
    }

    public int getDimensionID(ItemStack itemStack)
    {
        if (itemStack.stackTagCompound == null)
        {
            itemStack.setTagCompound(new NBTTagCompound());
        }

        return itemStack.stackTagCompound.getInteger("dimensionId");
    }

    public World getWorld(ItemStack itemStack)
    {
        return DimensionManager.getWorld(getDimensionID(itemStack));
    }

    public int xCoord(ItemStack itemStack)
    {
        if (!(itemStack.stackTagCompound == null))
        {
            return itemStack.stackTagCompound.getInteger("xCoord");
        } else
        {
            return 0;
        }
    }

    public int yCoord(ItemStack itemStack)
    {
        if (!(itemStack.stackTagCompound == null))
        {
            return itemStack.stackTagCompound.getInteger("yCoord");
        } else
        {
            return 0;
        }
    }

    public int zCoord(ItemStack itemStack)
    {
        if (!(itemStack.stackTagCompound == null))
        {
            return itemStack.stackTagCompound.getInteger("zCoord");
        } else
        {
            return 0;
        }
    }

    public int getFocusLevel()
    {
        return this.focusLevel;
    }
}

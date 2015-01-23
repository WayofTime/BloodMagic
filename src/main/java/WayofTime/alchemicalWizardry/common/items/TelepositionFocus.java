package WayofTime.alchemicalWizardry.common.items;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

import java.util.List;

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
        this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:TeleposerFocus");
    }

    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
    {
        par3List.add(StatCollector.translateToLocal("tooltip.telepositionfocus.desc"));

        if (!(par1ItemStack.getTagCompound() == null))
        {
            NBTTagCompound itemTag = par1ItemStack.getTagCompound();

            if (!par1ItemStack.getTagCompound().getString("ownerName").equals(""))
            {
                par3List.add(StatCollector.translateToLocal("tooltip.owner.currentowner") + " " + par1ItemStack.getTagCompound().getString("ownerName"));
            }

            par3List.add(StatCollector.translateToLocal("tooltip.alchemy.coords") + " " + itemTag.getInteger("xCoord") + ", " + itemTag.getInteger("yCoord") + ", " + itemTag.getInteger("zCoord"));
            par3List.add(StatCollector.translateToLocal("tooltip.alchemy.dimension") + " " + getDimensionID(par1ItemStack));
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
        return par1ItemStack;
    }

    public int getDimensionID(ItemStack itemStack)
    {
        if (itemStack.getTagCompound() == null)
        {
            itemStack.setTagCompound(new NBTTagCompound());
        }

        return itemStack.getTagCompound().getInteger("dimensionId");
    }

    public World getWorld(ItemStack itemStack)
    {
        return DimensionManager.getWorld(getDimensionID(itemStack));
    }

    public int xCoord(ItemStack itemStack)
    {
        if (!(itemStack.getTagCompound() == null))
        {
            return itemStack.getTagCompound().getInteger("xCoord");
        } else
        {
            return 0;
        }
    }

    public int yCoord(ItemStack itemStack)
    {
        if (!(itemStack.getTagCompound() == null))
        {
            return itemStack.getTagCompound().getInteger("yCoord");
        } else
        {
            return 0;
        }
    }

    public int zCoord(ItemStack itemStack)
    {
        if (!(itemStack.getTagCompound() == null))
        {
            return itemStack.getTagCompound().getInteger("zCoord");
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

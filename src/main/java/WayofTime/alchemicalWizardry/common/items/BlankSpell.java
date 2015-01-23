package WayofTime.alchemicalWizardry.common.items;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.common.tileEntity.TEHomHeart;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

import java.util.List;

public class BlankSpell extends EnergyItems
{
    public BlankSpell()
    {
        super();
        this.setMaxStackSize(1);
        setCreativeTab(AlchemicalWizardry.tabBloodMagic);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister)
    {
        this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:BlankSpell");
    }

    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
    {
        par3List.add(StatCollector.translateToLocal("tooltip.blankspell.desc"));

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

        if (!par2World.isRemote)
        {
            World world = DimensionManager.getWorld(getDimensionID(par1ItemStack));

            if (world != null)
            {
                NBTTagCompound itemTag = par1ItemStack.getTagCompound();
                TileEntity tileEntity = world.getTileEntity(itemTag.getInteger("xCoord"), itemTag.getInteger("yCoord"), itemTag.getInteger("zCoord"));

                if (tileEntity instanceof TEHomHeart)
                {
                    TEHomHeart homHeart = (TEHomHeart) tileEntity;

                    if (homHeart.canCastSpell(par1ItemStack, par2World, par3EntityPlayer))
                    {
                    	if(EnergyItems.syphonBatteries(par1ItemStack, par3EntityPlayer, homHeart.getCostForSpell()))
                    	{
                            EnergyItems.syphonBatteries(par1ItemStack, par3EntityPlayer, homHeart.castSpell(par1ItemStack, par2World, par3EntityPlayer));
                    	}
                    } else
                    {
                        return par1ItemStack;
                    }
                } else
                {
                    return par1ItemStack;
                }
            } else
            {
                return par1ItemStack;
            }
        } else
        {
            return par1ItemStack;
        }
        par2World.playSoundAtEntity(par3EntityPlayer, "random.fizz", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
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
}

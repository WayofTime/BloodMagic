package WayofTime.alchemicalWizardry.common.items;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.common.tileEntity.TEHomHeart;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

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
        par3List.add("Crystal of infinite possibilities.");

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

        if (!par2World.isRemote)
        {
            //World world = MinecraftServer.getServer().worldServers[getDimensionID(par1ItemStack)];
            World world = DimensionManager.getWorld(getDimensionID(par1ItemStack));

            if (world != null)
            {
                NBTTagCompound itemTag = par1ItemStack.stackTagCompound;
                TileEntity tileEntity = world.getTileEntity(itemTag.getInteger("xCoord"), itemTag.getInteger("yCoord"), itemTag.getInteger("zCoord"));

                if (tileEntity instanceof TEHomHeart)
                {
                    TEHomHeart homHeart = (TEHomHeart) tileEntity;

                    if (homHeart.canCastSpell(par1ItemStack, par2World, par3EntityPlayer))
                    {
                        EnergyItems.syphonBatteries(par1ItemStack, par3EntityPlayer, homHeart.castSpell(par1ItemStack, par2World, par3EntityPlayer));
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
}

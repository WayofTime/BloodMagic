package WayofTime.alchemicalWizardry.common.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.List;

public class ReinforcedTelepositionFocus extends TelepositionFocus
{
    public ReinforcedTelepositionFocus()
    {
        super(3);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister)
    {
        this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:ReinforcedTeleposerFocus");
    }

    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
    {
        par3List.add("A stronger version of the focus,");
        par3List.add("using a weak shard");

        if (!(par1ItemStack.getTagCompound() == null))
        {
            NBTTagCompound itemTag = par1ItemStack.getTagCompound();

            if (!par1ItemStack.getTagCompound().getString("ownerName").equals(""))
            {
                par3List.add("Current owner: " + par1ItemStack.getTagCompound().getString("ownerName"));
            }

            par3List.add("Coords: " + itemTag.getInteger("xCoord") + ", " + itemTag.getInteger("yCoord") + ", " + itemTag.getInteger("zCoord"));
            par3List.add("Bound Dimension: " + getDimensionID(par1ItemStack));
        }
    }
}

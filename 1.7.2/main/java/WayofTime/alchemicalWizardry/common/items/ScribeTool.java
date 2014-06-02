package WayofTime.alchemicalWizardry.common.items;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.List;

public class ScribeTool extends EnergyItems
{
    private int meta;

    public ScribeTool(int inkType)
    {
        super();
        setMaxStackSize(1);
        setCreativeTab(AlchemicalWizardry.tabBloodMagic);
        setMaxDamage(10);
        setEnergyUsed(10);
        this.meta = inkType;
    }

    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
    {
        par3List.add("The writing is on the wall...");

        if (!(par1ItemStack.stackTagCompound == null))
        {
            par3List.add("Current owner: " + par1ItemStack.stackTagCompound.getString("ownerName"));
        }
    }

    @Override
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        EnergyItems.checkAndSetItemOwner(par1ItemStack, par3EntityPlayer);

        if (par1ItemStack.getItemDamage() > 0)
        {
            par1ItemStack.setItemDamage(par1ItemStack.getItemDamage() - 1);
            syphonBatteries(par1ItemStack, par3EntityPlayer, this.getEnergyUsed());
        }

        return par1ItemStack;
    }

    public int getType()
    {
        return this.meta;
    }
}

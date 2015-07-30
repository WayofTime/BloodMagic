package WayofTime.alchemicalWizardry.common.items;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.api.soulNetwork.SoulNetworkHandler;

public class LavaCrystal extends BindableItems
{
    public LavaCrystal()
    {
        super();
        setMaxStackSize(1);
        setEnergyUsed(25);
    }

    /*
     * Used to have the item contain itself.
     */
    @Override
    public ItemStack getContainerItem(ItemStack itemStack)
    {
        SoulNetworkHandler.syphonFromNetwork(itemStack, this.getEnergyUsed());
        ItemStack copiedStack = itemStack.copy();
        copiedStack.setItemDamage(copiedStack.getItemDamage());
        copiedStack.stackSize = 1;
        return copiedStack;
    }

    @Override
    public boolean hasContainerItem(ItemStack itemStack)
    {
        return true;
    }

    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        BindableItems.checkAndSetItemOwner(par1ItemStack, par3EntityPlayer);
        return par1ItemStack;
    }

    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
    {
        par3List.add(StatCollector.translateToLocal("tooltip.lavacrystal.desc1"));
        par3List.add(StatCollector.translateToLocal("tooltip.lavacrystal.desc2"));

        if (!(par1ItemStack.getTagCompound() == null))
        {
            par3List.add(StatCollector.translateToLocal("tooltip.owner.currentowner") + " " + par1ItemStack.getTagCompound().getString("ownerName"));
        }
    }

    public boolean hasEnoughEssence(ItemStack itemStack)
    {
        if (itemStack.getTagCompound() != null && !(itemStack.getTagCompound().getString("ownerName").equals("")))
        {
            String ownerName = itemStack.getTagCompound().getString("ownerName");

            if (MinecraftServer.getServer() == null)
            {
                return false;
            }

            if (SoulNetworkHandler.getCurrentEssence(ownerName) >= this.getEnergyUsed())
            {
                return true;
            }
        }
        return false;
    }
}

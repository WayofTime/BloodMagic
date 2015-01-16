package WayofTime.alchemicalWizardry.common.items;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.api.soulNetwork.LifeEssenceNetwork;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;

import java.util.List;

public class LavaCrystal extends EnergyItems
{
    public LavaCrystal()
    {
        super();
        setMaxStackSize(1);
        setCreativeTab(AlchemicalWizardry.tabBloodMagic);
        setUnlocalizedName("lavaCrystal");
        setEnergyUsed(25);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister)
    {
        this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:LavaCrystal");
    }

    /*
     * Used to have the item contain itself.
     */
    @Override
    public ItemStack getContainerItem(ItemStack itemStack)
    {
        {
            syphonWhileInContainer(itemStack, this.getEnergyUsed());
            ItemStack copiedStack = itemStack.copy();
            copiedStack.setItemDamage(copiedStack.getItemDamage());
            copiedStack.stackSize = 1;
            return copiedStack;
        }
    }

    @Override
    public boolean hasContainerItem()
    {
        return true;
    }

    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        EnergyItems.checkAndSetItemOwner(par1ItemStack, par3EntityPlayer);
        int damage = this.getDamage(par1ItemStack);
        return par1ItemStack;
    }

    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
    {
        par3List.add("Store life to smelt");
        par3List.add("stuff in the furnace.");

        if (!(par1ItemStack.getTagCompound() == null))
        {
            par3List.add("Current owner: " + par1ItemStack.getTagCompound().getString("ownerName"));
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

            World world = MinecraftServer.getServer().worldServers[0];
            LifeEssenceNetwork data = (LifeEssenceNetwork) world.loadItemData(LifeEssenceNetwork.class, ownerName);

            if (data == null)
            {
                data = new LifeEssenceNetwork(ownerName);
                world.setItemData(ownerName, data);
            }

            if (data.currentEssence >= this.getEnergyUsed())
            {
                return true;
            }
        }
        return false;
    }
}

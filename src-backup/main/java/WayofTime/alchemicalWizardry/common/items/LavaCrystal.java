package WayofTime.alchemicalWizardry.common.items;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.api.soulNetwork.LifeEssenceNetwork;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LavaCrystal extends EnergyItems
{
    public LavaCrystal()
    {
        super();
        //setMaxDamage(1000);
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
        //if(!syphonBatteries(itemStack, null, 10))
        {
            syphonWhileInContainer(itemStack, this.getEnergyUsed());
            ItemStack copiedStack = itemStack.copy();
            copiedStack.setItemDamage(copiedStack.getItemDamage());
            copiedStack.stackSize = 1;
            return copiedStack;
        }
        //return itemStack;
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

        if (!(par1ItemStack.stackTagCompound == null))
        {
            par3List.add("Current owner: " + par1ItemStack.stackTagCompound.getString("ownerName"));
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

//        	EntityPlayer ownerEntity = MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(ist.getTagCompound().getString("ownerName"));
//            if(ownerEntity==null){return false;}
//            NBTTagCompound tag = ownerEntity.getEntityData();
//            int currentEssence = tag.getInteger("currentEssence");
//            if(currentEssence>=damageToBeDone)
//            {
//            	tag.setInteger("currentEssence", currentEssence-damageToBeDone);
//            	return true;
//            }
        }

        return false;
    }
}

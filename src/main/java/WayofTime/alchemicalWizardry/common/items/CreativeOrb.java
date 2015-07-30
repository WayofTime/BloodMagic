package WayofTime.alchemicalWizardry.common.items;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import WayofTime.alchemicalWizardry.api.items.interfaces.IBindable;
import WayofTime.alchemicalWizardry.api.soulNetwork.LifeEssenceNetwork;
import WayofTime.alchemicalWizardry.api.soulNetwork.SoulNetworkHandler;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;

public class CreativeOrb extends Item implements IBindable
{
    public CreativeOrb()
    {
        super();
        setMaxStackSize(1);
    }

    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
    {
        par3List.add(StatCollector.translateToLocal("tooltip.mode.creative"));
        par3List.add(StatCollector.translateToLocal("tooltip.cheatyitem.desc1"));
        par3List.add(StatCollector.translateToLocal("tooltip.cheatyitem.desc2"));

        if (!(par1ItemStack.getTagCompound() == null))
        {
            par3List.add(StatCollector.translateToLocal("tooltip.owner.currentowner") + " " + par1ItemStack.getTagCompound().getString("ownerName"));
        }
    }

    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        World world = par3EntityPlayer.worldObj;

        if (!BindableItems.checkAndSetItemOwner(par1ItemStack, par3EntityPlayer) || par3EntityPlayer instanceof FakePlayer)
        {
            return par1ItemStack;
        }

        if (world != null)
        {
            double posX = par3EntityPlayer.posX;
            double posY = par3EntityPlayer.posY;
            double posZ = par3EntityPlayer.posZ;
            world.playSoundEffect((double) ((float) posX + 0.5F), (double) ((float) posY + 0.5F), (double) ((float) posZ + 0.5F), "random.fizz", 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);
            SpellHelper.sendIndexedParticleToAllAround(world, posX, posY, posZ, 20, world.provider.getDimensionId(), 4, posX, posY, posZ);
        }

        if (par3EntityPlayer.worldObj.isRemote)
        {
            return par1ItemStack;
        }

        NBTTagCompound itemTag = par1ItemStack.getTagCompound();

        if (itemTag == null || itemTag.getString("ownerName").equals(""))
        {
            return par1ItemStack;
        }

        if (par3EntityPlayer.isSneaking())
        {
            SoulNetworkHandler.setCurrentEssence(itemTag.getString("ownerName"), 0);
        } else
        {
            SoulNetworkHandler.addCurrentEssenceToMaximum(itemTag.getString("ownerName"), 1000000, Integer.MAX_VALUE);
        }
        return par1ItemStack;
    }

    @Override
    public ItemStack getContainerItem(ItemStack itemStack)
    {
        return itemStack;
    }

    @Override
    public boolean hasContainerItem(ItemStack itemStack)
    {
        return true;
    }

    public int getCurrentEssence(ItemStack par1ItemStack)
    {
        if (par1ItemStack == null)
        {
            return 0;
        }

        NBTTagCompound itemTag = par1ItemStack.getTagCompound();

        if (itemTag == null || itemTag.getString("ownerName").equals(""))
        {
            return 0;
        }

        String owner = itemTag.getString("ownerName");
        World worldSave = MinecraftServer.getServer().worldServers[0];
        LifeEssenceNetwork data = (LifeEssenceNetwork) worldSave.loadItemData(LifeEssenceNetwork.class, owner);

        if (data == null)
        {
            data = new LifeEssenceNetwork(owner);
            worldSave.setItemData(owner, data);
        }

        return data.currentEssence;
    }
}

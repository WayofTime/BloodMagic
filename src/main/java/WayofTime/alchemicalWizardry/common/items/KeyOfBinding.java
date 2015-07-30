package WayofTime.alchemicalWizardry.common.items;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import WayofTime.alchemicalWizardry.ModItems;
import WayofTime.alchemicalWizardry.api.items.interfaces.IBindable;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;

public class KeyOfBinding extends BindableItems
{
    public KeyOfBinding()
    {
        super();
        setMaxStackSize(1);
        this.setEnergyUsed(1000);
        this.hasSubtypes = true;
    }

    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
    {
        par3List.add(StatCollector.translateToLocal("tooltip.diablokey.desc"));
        if (!(par1ItemStack.getTagCompound() == null))
        {
            par3List.add(StatCollector.translateToLocal("tooltip.owner.currentowner") + " " + par1ItemStack.getTagCompound().getString("ownerName"));
        }
    }

    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        World world = par3EntityPlayer.worldObj;
        if (!BindableItems.checkAndSetItemOwner(par1ItemStack, par3EntityPlayer) || par3EntityPlayer instanceof FakePlayer || par3EntityPlayer instanceof EntityPlayerMP)
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

        if (!par3EntityPlayer.worldObj.isRemote && !(par3EntityPlayer.getClass().equals(EntityPlayerMP.class)))
        {
            return par1ItemStack;
        }

        if (par3EntityPlayer.isSneaking())
        {
            return par1ItemStack;
        }

        NBTTagCompound itemTag = par1ItemStack.getTagCompound();

        if (itemTag == null || itemTag.getString("ownerName").equals(""))
        {
            return par1ItemStack;
        }

        String ownerName = itemTag.getString("ownerName");
        ItemStack[] inv = par3EntityPlayer.inventory.mainInventory;

        for (ItemStack itemStack : inv)
        {
            if (itemStack == null)
            {
                continue;
            }

            Item item = itemStack.getItem();

            if (item instanceof KeyOfBinding)
            {
                continue;
            }

            if (item instanceof IBindable)
            {
                BindableItems.checkAndSetItemOwner(itemStack, ownerName);
            }
        }

        return par1ItemStack;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item id, CreativeTabs creativeTab, List list)
    {
        list.add(new ItemStack(ModItems.itemKeyOfDiablo));
        ItemStack boundKey = new ItemStack(ModItems.itemKeyOfDiablo);
        BindableItems.checkAndSetItemOwner(boundKey, "Server-wide Soul Network");
        list.add(boundKey);
    }
}

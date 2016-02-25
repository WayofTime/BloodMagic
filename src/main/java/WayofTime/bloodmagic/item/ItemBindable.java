package WayofTime.bloodmagic.item;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.BloodMagicAPI;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.iface.IBindable;
import WayofTime.bloodmagic.api.network.SoulNetwork;
import WayofTime.bloodmagic.api.util.helper.NBTHelper;
import WayofTime.bloodmagic.api.util.helper.NetworkHelper;
import WayofTime.bloodmagic.api.util.helper.PlayerHelper;
import WayofTime.bloodmagic.util.helper.TextHelper;

import com.google.common.base.Strings;

public class ItemBindable extends Item implements IBindable
{
    private int lpUsed;

    public ItemBindable()
    {
        super();

        setCreativeTab(BloodMagic.tabBloodMagic);
        setMaxStackSize(1);
    }

    /**
     * Syphons from the owner's network if possible - if not enough LP is found,
     * it will instead take the LP from the holder of the item.
     * 
     * @param stack
     *        - The ItemStack to syphon from
     * @param player
     *        - The Player using the item
     * @param lpUsed
     *        - The amount of LP to syphon
     * 
     * @return Whether syphoning was successful or not
     */
    public static boolean syphonNetwork(ItemStack stack, EntityPlayer player, int lpUsed)
    {
        if (player == null)
            return false;

        if (!player.worldObj.isRemote)
        {
            if (stack != null && stack.getItem() instanceof IBindable)
            {
                IBindable itemBindable = (IBindable) stack.getItem();
                String owner = itemBindable.getOwnerUUID(stack);

                if (Strings.isNullOrEmpty(owner))
                    return false;

                SoulNetwork network = NetworkHelper.getSoulNetwork(owner);
                return NetworkHelper.syphonAndDamage(network, player, lpUsed);
            }

        } else
        {
            double posX = player.posX;
            double posY = player.posY;
            double posZ = player.posZ;

            // SpellHelper.sendIndexedParticleToAllAround(player.worldObj, posX,posY, posZ, 20, player.worldObj.provider.getDimensionId(), 4, posX, posY, posZ);
            player.worldObj.playSoundEffect((double) ((float) posX + 0.5F), (double) ((float) posY + 0.5F), (double) ((float) posZ + 0.5F), "random.fizz", 0.5F, 2.6F + (player.worldObj.rand.nextFloat() - player.worldObj.rand.nextFloat()) * 0.8F);
        }
        return true;
    }

    /**
     * This method is to be used for when you want to drain from a network
     * without an online player. This will not take health from the owner if it
     * fails to find sufficient LP.
     * 
     * @param stack
     *        - The ItemStack to syphon from.
     * @param lpUsed
     *        - The amount of LP to syphon
     * 
     * @return - If syphoning was successful or not
     */
    public static boolean syphonNetwork(ItemStack stack, int lpUsed)
    {
        if (stack.getItem() instanceof IBindable)
        {
            IBindable bindable = (IBindable) stack.getItem();
            return !Strings.isNullOrEmpty(bindable.getOwnerUUID(stack)) && NetworkHelper.syphonFromContainer(stack, lpUsed);
        }

        return false;
    }

    public static boolean canSyphonFromNetwork(ItemStack stack, int lpRequested)
    {
        if (stack.getItem() instanceof IBindable)
        {
            IBindable bindable = (IBindable) stack.getItem();
            return !Strings.isNullOrEmpty(bindable.getOwnerUUID(stack)) && NetworkHelper.canSyphonFromContainer(stack, lpRequested);
        }

        return false;
    }

    public static void hurtPlayer(EntityPlayer user, int lpSyphoned)
    {
        if (user != null)
        {
            if (lpSyphoned < 100 && lpSyphoned > 0)
            {
                if (!user.capabilities.isCreativeMode)
                {
                    user.attackEntityFrom(BloodMagicAPI.getDamageSource(), 0F); // Emulate an attack
                    user.setHealth(user.getHealth() - 1);

                    if (user.getHealth() <= 0.0005f)
                        user.onDeath(BloodMagicAPI.getDamageSource());
                }
            } else if (lpSyphoned >= 100)
            {
                if (!user.capabilities.isCreativeMode)
                {
                    for (int i = 0; i < ((lpSyphoned + 99) / 100); i++)
                    {
                        user.attackEntityFrom(BloodMagicAPI.getDamageSource(), 0F); // Emulate an attack
                        user.setHealth(user.getHealth() - 1);

                        if (user.getHealth() <= 0.0005f)
                        {
                            user.onDeath(BloodMagicAPI.getDamageSource());
                            break;
                        }
                    }
                }
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced)
    {
        NBTHelper.checkNBT(stack);

        if (!Strings.isNullOrEmpty(stack.getTagCompound().getString(Constants.NBT.OWNER_UUID)))
            tooltip.add(TextHelper.localizeEffect("tooltip.BloodMagic.currentOwner", PlayerHelper.getUsernameFromStack(stack)));
    }

    public int getLPUsed()
    {
        return this.lpUsed;
    }

    protected void setLPUsed(int lpUsed)
    {
        this.lpUsed = lpUsed;
    }

    public String getBindableOwner(ItemStack stack)
    {
        stack = NBTHelper.checkNBT(stack);

        return stack.getTagCompound().getString(Constants.NBT.OWNER_UUID);
    }

    // IBindable

    @Override
    public boolean onBind(EntityPlayer player, ItemStack stack)
    {
        return true;
    }

    @Override
    public String getOwnerName(ItemStack stack)
    {
        return stack != null ? NBTHelper.checkNBT(stack).getTagCompound().getString(Constants.NBT.OWNER_NAME) : null;
    }

    @Override
    public String getOwnerUUID(ItemStack stack)
    {
        return stack != null ? NBTHelper.checkNBT(stack).getTagCompound().getString(Constants.NBT.OWNER_UUID) : null;
    }
}

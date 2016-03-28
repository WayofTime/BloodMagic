package WayofTime.bloodmagic.api;

import WayofTime.bloodmagic.api.event.ItemBindEvent;
import WayofTime.bloodmagic.api.iface.IBindable;
import WayofTime.bloodmagic.api.network.SoulNetwork;
import WayofTime.bloodmagic.api.orb.IBloodOrb;
import WayofTime.bloodmagic.api.util.helper.BindableHelper;
import WayofTime.bloodmagic.api.util.helper.NBTHelper;
import WayofTime.bloodmagic.api.util.helper.NetworkHelper;
import WayofTime.bloodmagic.api.util.helper.PlayerHelper;
import com.google.common.base.Strings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;

@Deprecated
public class DinnerBeforeDessert
{
    // Temporary binding method until PlayerInteractEvent is re-implemented in Forge 1.9.
    // Returns false if binding fails. 
    public static boolean bindMe(World world, EntityPlayer player, ItemStack stack)
    {
        if (stack == null)
            return false;
        
        if (PlayerHelper.isFakePlayer(player))
            return false;
        
        if (!(stack.getItem() instanceof IBindable))
            return false;

        NBTHelper.checkNBT(stack);
        IBindable bindable = (IBindable) stack.getItem();

        if (Strings.isNullOrEmpty(bindable.getOwnerUUID(stack)))
        {
            if (bindable.onBind(player, stack))
            {
                String uuid = PlayerHelper.getUUIDFromPlayer(player).toString();
                ItemBindEvent toPost = new ItemBindEvent(player, uuid, stack);
                if (MinecraftForge.EVENT_BUS.post(toPost) || toPost.getResult() == Event.Result.DENY)
                    return false;

                BindableHelper.setItemOwnerUUID(stack, uuid);
                BindableHelper.setItemOwnerName(stack, player.getDisplayNameString());
                return true;
            }
        } else if (bindable.getOwnerUUID(stack).equals(PlayerHelper.getUUIDFromPlayer(player).toString()) && !bindable.getOwnerName(stack).equals(player.getDisplayNameString()))
        {
            BindableHelper.setItemOwnerName(stack, player.getDisplayNameString());
            return true;
        }

        return false;
    }

    // Temporary Orb tier setting until PlayerInteractEvent is re-implemented in Forge 1.9.
    public static void setOrbTier(EntityPlayer player, ItemStack stack)
    {
        if (stack == null)
            return;

        if (!(stack.getItem() instanceof IBloodOrb))
            return;

        NBTHelper.checkNBT(stack);
        IBloodOrb bloodOrb = (IBloodOrb) stack.getItem();
        SoulNetwork network = NetworkHelper.getSoulNetwork(player);

        if (bloodOrb.getOrbLevel(stack.getItemDamage()) > network.getOrbTier())
            network.setOrbTier(bloodOrb.getOrbLevel(stack.getItemDamage()));
    }
}

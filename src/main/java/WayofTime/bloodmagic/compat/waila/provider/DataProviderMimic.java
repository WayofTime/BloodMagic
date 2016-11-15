package WayofTime.bloodmagic.compat.waila.provider;

import WayofTime.bloodmagic.tile.TileMimic;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class DataProviderMimic implements IWailaDataProvider
{

    @Override
    public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config)
    {
        if (accessor.getNBTData().getBoolean("hasItem"))
            return ItemStack.loadItemStackFromNBT(accessor.getNBTData());

        return new ItemStack(accessor.getBlock(), 1, accessor.getMetadata());
    }

    @Override
    public List<String> getWailaHead(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config)
    {
        return null;
    }

    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config)
    {
        return null;
    }

    @Override
    public List<String> getWailaTail(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config)
    {
        return null;
    }

    @Override
    public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world, BlockPos pos)
    {
        if (te instanceof TileMimic && ((TileMimic) te).getStackInSlot(0) != null)
        {
            tag.setBoolean("hasItem", true);
            ((TileMimic) te).getStackInSlot(0).writeToNBT(tag);
        }
        return tag;
    }
}

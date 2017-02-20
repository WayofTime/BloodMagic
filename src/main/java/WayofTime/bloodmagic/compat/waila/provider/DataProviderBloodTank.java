package WayofTime.bloodmagic.compat.waila.provider;

import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.block.BlockBloodTank;
import WayofTime.bloodmagic.tile.TileBloodTank;
import WayofTime.bloodmagic.util.helper.TextHelper;
import com.google.common.base.Strings;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public class DataProviderBloodTank implements IWailaDataProvider
{
    @Override
    public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config)
    {
        return null;
    }

    @Override
    public List<String> getWailaHead(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config)
    {
        return null;
    }

    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config)
    {
        if (!config.getConfig(Constants.Compat.WAILA_CONFIG_BLOOD_TANK) && !config.getConfig("capability.tankinfo"))
            return currenttip;

        if (accessor.getPlayer().isSneaking() || config.getConfig(Constants.Compat.WAILA_CONFIG_BYPASS_SNEAK))
        {
            if (accessor.getBlock() instanceof BlockBloodTank && accessor.getTileEntity() instanceof TileBloodTank)
            {
                TileBloodTank bloodTank = (TileBloodTank) accessor.getTileEntity();
                NBTTagCompound tag = accessor.getNBTData();
                int capacity = accessor.getNBTData().getInteger(Constants.NBT.ALTAR_CAPACITY);
                currenttip.add(TextHelper.localizeEffect("tooltip.bloodmagic.tier", bloodTank.getBlockMetadata() + 1));
                currenttip.add(TextHelper.localizeEffect("tooltip.bloodmagic.fluid.capacity", capacity));

                FluidStack fluidStack = FluidStack.loadFluidStackFromNBT(tag.getCompoundTag(Constants.NBT.TANK));
                if (fluidStack != null)
                {
                    currenttip.add(TextHelper.localizeEffect("tooltip.bloodmagic.fluid.type", fluidStack.getLocalizedName()));
                    currenttip.add(TextHelper.localizeEffect("tooltip.bloodmagic.fluid.amount", fluidStack.amount, capacity));
                }
            }
        }
        else
        {
            currenttip.add(TextHelper.localizeEffect("waila.bloodmagic.sneak"));
        }

        return currenttip;
    }

    @Override
    public List<String> getWailaTail(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config)
    {
        return null;
    }

    @Override
    public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world, BlockPos pos)
    {
        if (te != null)
            te.writeToNBT(tag);
        return tag;
    }
}

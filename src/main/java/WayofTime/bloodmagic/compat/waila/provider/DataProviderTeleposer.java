package WayofTime.bloodmagic.compat.waila.provider;

import java.util.List;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.block.BlockTeleposer;
import WayofTime.bloodmagic.item.ItemTelepositionFocus;
import WayofTime.bloodmagic.tile.TileTeleposer;
import WayofTime.bloodmagic.util.helper.TextHelper;

public class DataProviderTeleposer implements IWailaDataProvider
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
        if (!config.getConfig(Constants.Compat.WAILA_CONFIG_TELEPOSER))
            return currenttip;

        if (accessor.getPlayer().isSneaking() || config.getConfig(Constants.Compat.WAILA_CONFIG_BYPASS_SNEAK))
        {
            if (accessor.getBlock() instanceof BlockTeleposer && accessor.getTileEntity() instanceof TileTeleposer)
            {
                TileTeleposer teleposer = (TileTeleposer) accessor.getTileEntity();
                if (teleposer.getStackInSlot(0) != null)
                {
                    ItemStack contained = teleposer.getStackInSlot(0);
                    BlockPos toPos = ((ItemTelepositionFocus) contained.getItem()).getBlockPos(contained);
                    int dimensionID = contained.getTagCompound().getInteger(Constants.NBT.DIMENSION_ID);

                    currenttip.add(TextHelper.localizeEffect("tooltip.BloodMagic.telepositionFocus.coords", toPos.getX(), toPos.getY(), toPos.getZ()));
                    currenttip.add(TextHelper.localizeEffect("tooltip.BloodMagic.telepositionFocus.dimension", dimensionID));
                }
            }
        } else
        {
            currenttip.add(TextHelper.localizeEffect("waila.BloodMagic.sneak"));
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
        return null;
    }
}

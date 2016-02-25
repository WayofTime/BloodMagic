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
import WayofTime.bloodmagic.registry.ModBlocks;
import WayofTime.bloodmagic.registry.ModItems;
import WayofTime.bloodmagic.tile.TileAlchemyArray;
import WayofTime.bloodmagic.util.helper.TextHelper;

public class DataProviderAlchemyArray implements IWailaDataProvider
{
    @Override
    public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config)
    {
        return new ItemStack(ModItems.arcaneAshes).setStackDisplayName(TextHelper.getFormattedText(ModBlocks.alchemyArray.getLocalizedName()));
    }

    @Override
    public List<String> getWailaHead(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config)
    {
        return null;
    }

    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config)
    {
        if (!config.getConfig(Constants.Compat.WAILA_CONFIG_ARRAY))
            return currenttip;

        if (accessor.getPlayer().isSneaking() || config.getConfig(Constants.Compat.WAILA_CONFIG_BYPASS_SNEAK))
        {
            TileEntity tile = accessor.getTileEntity();
            if (tile instanceof TileAlchemyArray)
            {
                TileAlchemyArray tileArray = (TileAlchemyArray) tile;
                if (tileArray.getStackInSlot(0) != null)
                    currenttip.add(TextHelper.localize("waila.BloodMagic.array.reagent", tileArray.getStackInSlot(0).getDisplayName()));

                if (tileArray.getStackInSlot(1) != null)
                    currenttip.add(TextHelper.localize("waila.BloodMagic.array.catalyst", tileArray.getStackInSlot(1).getDisplayName()));
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

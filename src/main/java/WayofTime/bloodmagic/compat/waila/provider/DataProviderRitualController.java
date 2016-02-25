package WayofTime.bloodmagic.compat.waila.provider;

import java.util.List;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import WayofTime.bloodmagic.api.BlockStack;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.registry.ImperfectRitualRegistry;
import WayofTime.bloodmagic.api.registry.RitualRegistry;
import WayofTime.bloodmagic.api.ritual.imperfect.ImperfectRitual;
import WayofTime.bloodmagic.api.util.helper.PlayerHelper;
import WayofTime.bloodmagic.block.BlockRitualController;
import WayofTime.bloodmagic.tile.TileImperfectRitualStone;
import WayofTime.bloodmagic.tile.TileMasterRitualStone;
import WayofTime.bloodmagic.util.helper.TextHelper;

public class DataProviderRitualController implements IWailaDataProvider
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
        if (!config.getConfig(Constants.Compat.WAILA_CONFIG_RITUAL))
            return currenttip;

        if (accessor.getPlayer().isSneaking() || config.getConfig(Constants.Compat.WAILA_CONFIG_BYPASS_SNEAK))
        {
            if (accessor.getBlock() instanceof BlockRitualController)
            {
                if (accessor.getBlock().getMetaFromState(accessor.getBlockState()) == 0 && accessor.getTileEntity() instanceof TileMasterRitualStone)
                {
                    TileMasterRitualStone mrs = (TileMasterRitualStone) accessor.getTileEntity();

                    if (mrs.getCurrentRitual() != null)
                    {
                        currenttip.add(TextHelper.localizeEffect(mrs.getCurrentRitual().getUnlocalizedName()));
                        currenttip.add(TextHelper.localizeEffect("tooltip.BloodMagic.currentOwner", PlayerHelper.getUsernameFromUUID(mrs.getOwner())));
                        if (!RitualRegistry.ritualEnabled(mrs.getCurrentRitual()))
                            currenttip.add(TextHelper.localizeEffect("tooltip.BloodMagic.config.disabled"));
                    } else
                    {
                        currenttip.add(TextHelper.localizeEffect("tooltip.BloodMagic.deactivated"));
                    }
                }

                if (accessor.getBlock().getMetaFromState(accessor.getBlockState()) == 1 && accessor.getTileEntity() instanceof TileImperfectRitualStone)
                {
                    if (accessor.getWorld().getBlockState(accessor.getPosition().up()).getBlock() != null)
                    {
                        Block up = accessor.getWorld().getBlockState(accessor.getPosition().up()).getBlock();
                        int meta = up.getMetaFromState(accessor.getWorld().getBlockState(accessor.getPosition().up()));
                        BlockStack blockStack = new BlockStack(up, meta);
                        ImperfectRitual ritual = ImperfectRitualRegistry.getRitualForBlock(blockStack);

                        if (ritual != null)
                        {
                            currenttip.add(TextHelper.localizeEffect(ritual.getUnlocalizedName()));
                            if (!ImperfectRitualRegistry.ritualEnabled(ritual))
                                currenttip.add(TextHelper.localizeEffect("tooltip.BloodMagic.config.disabled"));
                        }
                    }
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

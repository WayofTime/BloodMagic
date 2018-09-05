package com.wayoftime.bloodmagic.compat.waila;

import com.wayoftime.bloodmagic.tile.TileBloodAltar;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.List;

public class DataProviderBloodAltar implements IWailaDataProvider {

    static final IWailaDataProvider INSTANCE = new DataProviderBloodAltar();

    @Nonnull
    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> tooltip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        tooltip.add(I18n.format("tooltip.bloodmagic:tier", I18n.format("enchantment.level." + (accessor.getNBTData().getInteger("tier") + 1))));
        if (accessor.getNBTData().hasKey("progress"))
            tooltip.add(I18n.format("tooltip.bloodmagic:progress", String.valueOf(accessor.getNBTData().getFloat("progress") * 100)));
        return tooltip;
    }

    @Nonnull
    @Override
    public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world, BlockPos pos) {
        TileBloodAltar altar = (TileBloodAltar) te;
        tag.setInteger("tier", altar.getCurrentTier().ordinal());
        if (altar.isCrafting())
            tag.setFloat("progress", altar.getProgress());
        return tag;
    }
}

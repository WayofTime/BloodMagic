package WayofTime.bloodmagic.compat.waila.provider;

import WayofTime.bloodmagic.util.Constants;
import WayofTime.bloodmagic.tile.TileBloodTank;
import WayofTime.bloodmagic.util.helper.TextHelper;
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

import javax.annotation.Nonnull;
import java.util.List;

public class DataProviderBloodTank implements IWailaDataProvider {

    public static final IWailaDataProvider INSTANCE = new DataProviderBloodTank();

    @Nonnull
    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        if (!config.getConfig(Constants.Compat.WAILA_CONFIG_BLOOD_TANK) && !config.getConfig("capability.tankinfo"))
            return currenttip;

        currenttip.add(TextHelper.localizeEffect("tooltip.bloodmagic.tier", accessor.getNBTData().getInteger("tier")));
        currenttip.add(TextHelper.localizeEffect("tooltip.bloodmagic.fluid.capacity", accessor.getNBTData().getInteger("capacity")));
        if (accessor.getNBTData().hasKey("fluid")) {
            FluidStack fluidStack = FluidStack.loadFluidStackFromNBT(accessor.getNBTData().getCompoundTag("fluid"));
            currenttip.add(TextHelper.localizeEffect("tooltip.bloodmagic.fluid.type", fluidStack.getLocalizedName()));
            currenttip.add(TextHelper.localizeEffect("tooltip.bloodmagic.fluid.amount", fluidStack.amount, accessor.getNBTData().getInteger("capacity")));
        }

        return currenttip;
    }

    @Nonnull
    @Override
    public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world, BlockPos pos) {
        TileBloodTank tank = (TileBloodTank) te;
        tag.setInteger("tier", tank.getBlockMetadata() + 1);
        tag.setInteger("capacity", tank.capacity);
        if (tank.getTank().getFluid() != null)
            tag.setTag("fluid", tank.getTank().getFluid().writeToNBT(new NBTTagCompound()));
        return tag;
    }
}

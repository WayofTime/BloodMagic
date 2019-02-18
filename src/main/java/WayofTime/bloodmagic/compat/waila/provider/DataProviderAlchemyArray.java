package WayofTime.bloodmagic.compat.waila.provider;

import WayofTime.bloodmagic.util.Constants;
import WayofTime.bloodmagic.core.RegistrarBloodMagicBlocks;
import WayofTime.bloodmagic.core.RegistrarBloodMagicItems;
import WayofTime.bloodmagic.tile.TileAlchemyArray;
import WayofTime.bloodmagic.util.helper.TextHelper;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.List;

public class DataProviderAlchemyArray implements IWailaDataProvider {

    public static final IWailaDataProvider INSTANCE = new DataProviderAlchemyArray();

    @Nonnull
    @Override
    public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return new ItemStack(RegistrarBloodMagicItems.ARCANE_ASHES).setStackDisplayName(TextFormatting.WHITE + RegistrarBloodMagicBlocks.ALCHEMY_ARRAY.getLocalizedName());
    }

    @Nonnull
    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        if (!config.getConfig(Constants.Compat.WAILA_CONFIG_ARRAY))
            return currenttip;

        if (accessor.getNBTData().hasKey("reagent"))
            currenttip.add(TextHelper.localize("waila.bloodmagic.array.reagent", accessor.getNBTData().getString("reagent")));
        if (accessor.getNBTData().hasKey("catalyst"))
            currenttip.add(TextHelper.localize("waila.bloodmagic.array.catalyst", accessor.getNBTData().getString("catalyst")));

        return currenttip;
    }

    @Nonnull
    @Override
    public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world, BlockPos pos) {
        TileAlchemyArray alchemyArray = (TileAlchemyArray) te;
        if (!alchemyArray.getStackInSlot(0).isEmpty())
            tag.setString("reagent", alchemyArray.getStackInSlot(0).getDisplayName());
        if (!alchemyArray.getStackInSlot(1).isEmpty())
            tag.setString("catalyst", alchemyArray.getStackInSlot(1).getDisplayName());
        return tag;
    }
}

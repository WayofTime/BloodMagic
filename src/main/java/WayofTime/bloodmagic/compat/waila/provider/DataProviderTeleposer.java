package WayofTime.bloodmagic.compat.waila.provider;

import WayofTime.bloodmagic.util.Constants;
import WayofTime.bloodmagic.item.ItemTelepositionFocus;
import WayofTime.bloodmagic.tile.TileTeleposer;
import WayofTime.bloodmagic.util.helper.TextHelper;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import org.apache.commons.lang3.text.WordUtils;

import javax.annotation.Nonnull;
import java.util.List;

public class DataProviderTeleposer implements IWailaDataProvider {

    public static final IWailaDataProvider INSTANCE = new DataProviderTeleposer();

    @Nonnull
    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        if (!config.getConfig(Constants.Compat.WAILA_CONFIG_TELEPOSER))
            return currenttip;

        if (accessor.getNBTData().hasKey("focus")) {
            NBTTagCompound focusData = accessor.getNBTData().getCompoundTag("focus");
            BlockPos boundPos = NBTUtil.getPosFromTag(focusData.getCompoundTag("pos"));
            int boundDim = focusData.getInteger("dim");
            String dimName = WordUtils.capitalizeFully(DimensionManager.getProviderType(boundDim).getName().replace("_", " "));

            currenttip.add(TextHelper.localizeEffect("tooltip.bloodmagic.telepositionFocus.bound", dimName, boundPos.getX(), boundPos.getY(), boundPos.getZ()));
        }

        return currenttip;
    }

    @Override
    public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world, BlockPos pos) {
        TileTeleposer teleposer = (TileTeleposer) te;
        ItemStack contained = teleposer.getStackInSlot(0);
        if (!contained.isEmpty() && contained.hasTagCompound()) {
            ItemTelepositionFocus focus = (ItemTelepositionFocus) contained.getItem();
            NBTTagCompound focusData = new NBTTagCompound();
            focusData.setTag("pos", NBTUtil.createPosTag(focus.getBlockPos(contained)));
            focusData.setInteger("dim", contained.getTagCompound().getInteger(Constants.NBT.DIMENSION_ID));
            tag.setTag("focus", focusData);
        }
        return tag;
    }
}

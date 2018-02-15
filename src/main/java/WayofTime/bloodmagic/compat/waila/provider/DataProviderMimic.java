package WayofTime.bloodmagic.compat.waila.provider;

import WayofTime.bloodmagic.tile.TileMimic;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import javax.annotation.Nonnull;

public class DataProviderMimic implements IWailaDataProvider {

    public static final IWailaDataProvider INSTANCE = new DataProviderMimic();

    @Nonnull
    @Override
    public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) {
        if (accessor.getNBTData().hasKey("mimiced")) {
            NBTTagCompound mimiced = accessor.getNBTData().getCompoundTag("mimiced");
            Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(mimiced.getString("id")));
            int meta = mimiced.getInteger("data");
            ItemStack ret = new ItemStack(item, 1, meta);
            if (mimiced.hasKey("nbt"))
                ret.setTagCompound(mimiced.getCompoundTag("nbt"));

            return ret;
        }

        return ItemStack.EMPTY;
    }

    @Nonnull
    @Override
    public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world, BlockPos pos) {
        TileMimic mimic = (TileMimic) te;
        ItemStack mimiced = mimic.getStackInSlot(0);
        if (!mimiced.isEmpty()) {
            NBTTagCompound item = new NBTTagCompound();
            item.setString("id", mimiced.getItem().getRegistryName().toString());
            item.setInteger("data", mimiced.getMetadata());
            NBTTagCompound shareTag = mimiced.getItem().getNBTShareTag(mimiced);
            if (shareTag != null)
                item.setTag("nbt", shareTag);

            tag.setTag("mimiced", item);
        }
        return tag;
    }
}

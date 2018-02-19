package WayofTime.bloodmagic.compat.waila.provider;

import WayofTime.bloodmagic.ConfigHandler;
import WayofTime.bloodmagic.util.Constants;
import WayofTime.bloodmagic.core.RegistrarBloodMagicItems;
import WayofTime.bloodmagic.item.sigil.ItemSigilDivination;
import WayofTime.bloodmagic.tile.TileAltar;
import WayofTime.bloodmagic.util.helper.TextHelper;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Integrated from WailaPlugins by <a
 * href="https://github.com/tterrag1098">tterrag1098</a>. Originally implemented
 * in ImLookingAtBlood by <a href="https://github.com/Pokefenn">Pokefenn</a>.
 */
public class DataProviderBloodAltar implements IWailaDataProvider {

    public static final IWailaDataProvider INSTANCE = new DataProviderBloodAltar();

    @Nonnull
    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        if (!config.getConfig(Constants.Compat.WAILA_CONFIG_ALTAR))
            return currenttip;

        if (accessor.getNBTData().hasKey("altar")) {
            NBTTagCompound altarData = accessor.getNBTData().getCompoundTag("altar");
            currenttip.add(TextHelper.localizeEffect("tooltip.bloodmagic.sigil.seer.currentAltarTier", altarData.getInteger("tier")));
            currenttip.add(TextHelper.localizeEffect("tooltip.bloodmagic.sigil.seer.currentAltarCapacity", altarData.getInteger("capacity")));
            currenttip.add(TextHelper.localizeEffect("tooltip.bloodmagic.sigil.seer.currentEssence", altarData.getInteger("stored")));

            if (altarData.hasKey("charge")) {
                currenttip.add(TextHelper.localizeEffect("tooltip.bloodmagic.sigil.seer.currentAltarProgress.percent", altarData.getInteger("progress") + "%"));
                currenttip.add(TextHelper.localizeEffect("tooltip.bloodmagic.sigil.seer.currentCharge", altarData.getInteger("charge")));
            }
        }

        return currenttip;
    }

    @Nonnull
    @Override
    public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world, BlockPos pos) {
        TileAltar altar = (TileAltar) te;

        boolean hasSigil = false;
        boolean hasSeer = false;

        switch (ConfigHandler.compat.wailaAltarDisplayMode) {
            case ALWAYS: {
                hasSigil = hasSeer = true;
                break;
            }
            case SIGIL_HELD: {
                hasSeer = holdingSeerSigil(player);
                hasSigil = hasSeer || holdingDivinationSigil(player);
                break;
            }
            case SIGIL_CONTAINED: {
                hasSeer = hasStack(new ItemStack(RegistrarBloodMagicItems.SIGIL_SEER), player);
                hasSigil = hasSeer || hasStack(new ItemStack(RegistrarBloodMagicItems.SIGIL_DIVINATION), player);
                break;
            }
        }

        if (!hasSeer && !hasSigil)
            return tag;

        NBTTagCompound altarData = new NBTTagCompound();
        altarData.setInteger("tier", altar.getTier().toInt());
        altarData.setInteger("capacity", altar.getCapacity());
        altarData.setInteger("stored", altar.getCurrentBlood());
        if (hasSeer) {
            altarData.setInteger("progress", (int) (((double) altar.getProgress() / (double) altar.getLiquidRequired() * 100) / altar.getStackInSlot(0).getCount()));
            altarData.setInteger("charge", altar.getTotalCharge());
        }

        tag.setTag("altar", altarData);

        return tag;
    }

    public static boolean hasStack(ItemStack stack, EntityPlayer player) {
        for (ItemStack inventoryStack : player.inventory.mainInventory)
            if (inventoryStack != null && inventoryStack.isItemEqual(stack))
                return true;

        return false;
    }

    private static boolean holdingSeerSigil(EntityPlayer player) {
        if (player.getHeldItemMainhand().getItem() == RegistrarBloodMagicItems.SIGIL_SEER)
            return true;

        if (player.getHeldItemOffhand().getItem() == RegistrarBloodMagicItems.SIGIL_SEER)
            return true;

        return false;
    }

    private static boolean holdingDivinationSigil(EntityPlayer player) {
        if (player.getHeldItemMainhand().getItem() instanceof ItemSigilDivination)
            return true;

        if (!player.getHeldItemOffhand().isEmpty() && player.getHeldItemOffhand().getItem() instanceof ItemSigilDivination)
            return true;

        return false;
    }
}

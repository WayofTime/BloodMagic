package WayofTime.bloodmagic.compat.waila.provider;

import WayofTime.bloodmagic.ConfigHandler;
import WayofTime.bloodmagic.compat.waila.BloodMagicHwylaPlugin;
import WayofTime.bloodmagic.core.RegistrarBloodMagicItems;
import WayofTime.bloodmagic.item.sigil.ItemSigilDivination;
import WayofTime.bloodmagic.tile.TileAltar;
import mcp.mobius.waila.api.IComponentProvider;
import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerDataProvider;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import java.util.List;

/**
 * Integrated from WailaPlugins by <a
 * href="https://github.com/tterrag1098">tterrag1098</a>. Originally implemented
 * in ImLookingAtBlood by <a href="https://github.com/Pokefenn">Pokefenn</a>.
 */
public class DataProviderBloodAltar implements IComponentProvider, IServerDataProvider<TileEntity> {

    public static final DataProviderBloodAltar INSTANCE = new DataProviderBloodAltar();

    @Override
    public void appendBody(List<ITextComponent> tooltip, IDataAccessor accessor, IPluginConfig config) {
        if (!config.get(BloodMagicHwylaPlugin.CONFIG_SHOW_ALTAR_STATS))
            return;

        if (accessor.getServerData().contains("altar")) {
            CompoundNBT altarData = accessor.getServerData().getCompound("altar");
            tooltip.add(new TranslationTextComponent("tooltip.bloodmagic.sigil.seer.currentAltarTier", altarData.getInt("tier")));
            tooltip.add(new TranslationTextComponent("tooltip.bloodmagic.sigil.seer.currentAltarCapacity", altarData.getInt("capacity")));
            tooltip.add(new TranslationTextComponent("tooltip.bloodmagic.sigil.seer.currentEssence", altarData.getInt("stored")));

            if (altarData.contains("charge")) {
                tooltip.add(new TranslationTextComponent("tooltip.bloodmagic.sigil.seer.currentAltarProgress.percent", altarData.getInt("progress") + "%"));
                tooltip.add(new TranslationTextComponent("tooltip.bloodmagic.sigil.seer.currentCharge", altarData.getInt("charge")));
            }
        }
    }

    @Override
    public void appendServerData(CompoundNBT tag, ServerPlayerEntity player, World world, TileEntity tileEntity) {
        TileAltar altar = (TileAltar) tileEntity;

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
            return;

        CompoundNBT altarData = new CompoundNBT();
        altarData.putInt("tier", altar.getTier().toInt());
        altarData.putInt("capacity", altar.getCapacity());
        altarData.putInt("stored", altar.getCurrentBlood());
        if (hasSeer) {
            altarData.putInt("progress", (int) (((double) altar.getProgress() / (double) altar.getLiquidRequired() * 100) / altar.getStackInSlot(0).getCount()));
            altarData.putInt("charge", altar.getTotalCharge());
        }

        tag.put("altar", altarData);
    }

    public static boolean hasStack(ItemStack stack, PlayerEntity player) {
        for (ItemStack inventoryStack : player.inventory.mainInventory)
            if (inventoryStack != null && inventoryStack.isItemEqual(stack))
                return true;

        return false;
    }

    private static boolean holdingSeerSigil(PlayerEntity player) {
        if (player.getHeldItemMainhand().getItem() == RegistrarBloodMagicItems.SIGIL_SEER)
            return true;

        if (player.getHeldItemOffhand().getItem() == RegistrarBloodMagicItems.SIGIL_SEER)
            return true;

        return false;
    }

    private static boolean holdingDivinationSigil(PlayerEntity player) {
        if (player.getHeldItemMainhand().getItem() instanceof ItemSigilDivination)
            return true;

        if (!player.getHeldItemOffhand().isEmpty() && player.getHeldItemOffhand().getItem() instanceof ItemSigilDivination)
            return true;

        return false;
    }
}

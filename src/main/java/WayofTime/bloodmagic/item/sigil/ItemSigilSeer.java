package WayofTime.bloodmagic.item.sigil;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.altar.IBloodAltar;
import WayofTime.bloodmagic.api.iface.IAltarReader;
import WayofTime.bloodmagic.api.util.helper.NetworkHelper;
import WayofTime.bloodmagic.tile.TileIncenseAltar;
import WayofTime.bloodmagic.util.ChatUtil;
import WayofTime.bloodmagic.util.helper.TextHelper;

public class ItemSigilSeer extends ItemSigilBase implements IAltarReader
{
    public ItemSigilSeer()
    {
        super("seer");
        setRegistryName(Constants.BloodMagicItem.SIGIL_SEER.getRegName());
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
    {
        super.onItemRightClick(stack, world, player);

        if (!world.isRemote)
        {
            MovingObjectPosition position = getMovingObjectPositionFromPlayer(world, player, false);

            if (position == null)
            {
                int currentEssence = NetworkHelper.getSoulNetwork(getOwnerUUID(stack)).getCurrentEssence();
                ChatUtil.sendNoSpam(player, new ChatComponentText(TextHelper.localize(tooltipBase + "currentEssence", currentEssence)));
                return stack;
            } else
            {
                if (position.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
                {

                    TileEntity tile = world.getTileEntity(position.getBlockPos());

                    if (tile != null && tile instanceof IBloodAltar)
                    {
                        IBloodAltar altar = (IBloodAltar) tile;
                        int tier = altar.getTier().ordinal() + 1;
                        int currentEssence = altar.getCurrentBlood();
                        int capacity = altar.getCapacity();
                        int charge = altar.getTotalCharge();
                        altar.checkTier();
                        if (tile instanceof IInventory)
                        {
                            if (((IInventory) tile).getStackInSlot(0) != null)
                            {
                                int progress = altar.getProgress();
                                int totalLiquidRequired = altar.getLiquidRequired() * ((IInventory) tile).getStackInSlot(0).stackSize;
                                int consumptionRate = (int) (altar.getConsumptionRate() * (altar.getConsumptionMultiplier() + 1));
                                ChatUtil.sendNoSpam(player, TextHelper.localize(tooltipBase + "currentAltarProgress", progress, totalLiquidRequired), TextHelper.localize(tooltipBase + "currentAltarConsumptionRate", consumptionRate), TextHelper.localize(tooltipBase + "currentAltarTier", tier), TextHelper.localize(tooltipBase + "currentEssence", currentEssence), TextHelper.localize(tooltipBase + "currentAltarCapacity", capacity), TextHelper.localize(tooltipBase + "currentCharge", charge));
                            } else
                            {
                                ChatUtil.sendNoSpam(player, TextHelper.localize(tooltipBase + "currentAltarTier", tier), TextHelper.localize(tooltipBase + "currentEssence", currentEssence), TextHelper.localize(tooltipBase + "currentAltarCapacity", capacity), TextHelper.localize(tooltipBase + "currentCharge", charge));
                            }
                        }
                    } else if (tile != null && tile instanceof TileIncenseAltar)
                    {
                        TileIncenseAltar altar = (TileIncenseAltar) tile;
                        altar.recheckConstruction();
                        double tranquility = altar.tranquility;
                        ChatUtil.sendNoSpam(player, TextHelper.localize(tooltipBase + "currentTranquility", (int) ((100D * (int) (100 * tranquility)) / 100d)), TextHelper.localize(tooltipBase + "currentBonus", (int) (100 * altar.incenseAddition)));
                    } else
                    {
                        int currentEssence = NetworkHelper.getSoulNetwork(getOwnerUUID(stack)).getCurrentEssence();
                        ChatUtil.sendNoSpam(player, TextHelper.localize(tooltipBase + "currentEssence", currentEssence));
                    }

                    return stack;
                }
            }
        }

        return stack;
    }
}

package WayofTime.bloodmagic.item.sigil;

import WayofTime.bloodmagic.api.altar.IBloodAltar;
import WayofTime.bloodmagic.api.iface.IAltarReader;
import WayofTime.bloodmagic.api.util.helper.BindableHelper;
import WayofTime.bloodmagic.api.util.helper.NetworkHelper;
import WayofTime.bloodmagic.api.util.helper.PlayerHelper;
import WayofTime.bloodmagic.tile.TileInventory;
import WayofTime.bloodmagic.util.ChatUtil;
import WayofTime.bloodmagic.util.helper.TextHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class ItemSigilSeer extends ItemSigilBase implements IAltarReader {

    public ItemSigilSeer() {
        super("seer");
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {

        if (PlayerHelper.isFakePlayer(player))
            return stack;

        super.onItemRightClick(stack, world, player);

        if (!world.isRemote) {
            MovingObjectPosition position = getMovingObjectPositionFromPlayer(world, player, false);
            int currentEssence = NetworkHelper.getSoulNetwork(BindableHelper.getOwnerName(stack), world).getCurrentEssence();

            if (position == null) {
                ChatUtil.sendNoSpam(player, new ChatComponentText(TextHelper.localize(tooltipBase + "currentEssence", currentEssence)));
                return stack;
            } else {
                if (position.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {

                    TileEntity tile = world.getTileEntity(position.getBlockPos());

                    if (tile != null && tile instanceof IBloodAltar) {
                        IBloodAltar altar = (IBloodAltar) tile;
                        int tier = altar.getTier().ordinal() + 1;
                        currentEssence = altar.getCurrentBlood();
                        int capacity = altar.getCapacity();
                        altar.checkTier();
                        if (tile instanceof IInventory) {
                            if (((IInventory) tile).getStackInSlot(0) != null) {
                                int progress = altar.getProgress();
                                int totalLiquidRequired = altar.getLiquidRequired() * ((IInventory) tile).getStackInSlot(0).stackSize;
                                int consumptionRate = (int) (altar.getConsumptionRate() * (altar.getConsumptionMultiplier() + 1));
                                ChatUtil.sendNoSpam(player, TextHelper.localize(tooltipBase + "currentAltarProgress", progress, totalLiquidRequired), TextHelper.localize(tooltipBase + "currentAltarConsumptionRate", consumptionRate), TextHelper.localize(tooltipBase + "currentAltarTier", tier), TextHelper.localize(tooltipBase + "currentEssence", currentEssence), TextHelper.localize(tooltipBase + "currentAltarCapacity", capacity));
                            }
                            else {
                                ChatUtil.sendNoSpam(player, TextHelper.localize(tooltipBase + "currentAltarTier", tier), TextHelper.localize(tooltipBase + "currentEssence", currentEssence), TextHelper.localize(tooltipBase + "currentAltarCapacity", capacity));
                            }
                        }
                    } else {
                        ChatUtil.sendNoSpam(player, TextHelper.localize(tooltipBase + "currentEssence", currentEssence));
                    }

                    return stack;
                }
            }
        }

        return stack;
    }
}

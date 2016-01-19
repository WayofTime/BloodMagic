package WayofTime.bloodmagic.item.sigil;

import WayofTime.bloodmagic.api.Constants;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import WayofTime.bloodmagic.api.altar.IBloodAltar;
import WayofTime.bloodmagic.api.iface.IAltarReader;
import WayofTime.bloodmagic.api.util.helper.BindableHelper;
import WayofTime.bloodmagic.api.util.helper.NetworkHelper;
import WayofTime.bloodmagic.util.ChatUtil;
import WayofTime.bloodmagic.util.helper.TextHelper;

public class ItemSigilDivination extends ItemSigilBase implements IAltarReader
{
    public ItemSigilDivination()
    {
        super("divination");
        setRegistryName(Constants.BloodMagicItem.SIGIL_DIVINATION.getRegName());
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
    {
        super.onItemRightClick(stack, world, player);

        if (!world.isRemote)
        {
            MovingObjectPosition position = getMovingObjectPositionFromPlayer(world, player, false);
            int currentEssence = NetworkHelper.getSoulNetwork(BindableHelper.getOwnerUUID(stack)).getCurrentEssence();

            if (position == null)
            {
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
                        currentEssence = altar.getCurrentBlood();
                        int capacity = altar.getCapacity();
                        altar.checkTier();
                        ChatUtil.sendNoSpam(player, TextHelper.localize(tooltipBase + "currentAltarTier", tier), TextHelper.localize(tooltipBase + "currentEssence", currentEssence), TextHelper.localize(tooltipBase + "currentAltarCapacity", capacity));

                    } else
                    {
                        ChatUtil.sendNoSpam(player, TextHelper.localize(tooltipBase + "currentEssence", currentEssence));
                    }

                    return stack;
                }
            }
        }

        return stack;
    }
}

package WayofTime.alchemicalWizardry.item.sigil;

import WayofTime.alchemicalWizardry.api.iface.IBloodAltar;
import WayofTime.alchemicalWizardry.api.util.helper.BindableHelper;
import WayofTime.alchemicalWizardry.api.util.helper.NetworkHelper;
import WayofTime.alchemicalWizardry.api.util.helper.TextHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class ItemSigilDivination extends ItemSigilBase {

    public ItemSigilDivination() {
        super("divination");
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        super.onItemRightClick(stack, world, player);

        if (!world.isRemote && syphonBatteries(stack, player, getEnergyUsed())) {
            MovingObjectPosition position = getMovingObjectPositionFromPlayer(world, player, false);
            int currentEssence = NetworkHelper.getCurrentEssence(BindableHelper.getOwnerName(stack));

            if (position == null) {
                player.addChatComponentMessage(new ChatComponentText(TextHelper.localize("message.divinationsigil.currentessence", currentEssence)));
                return stack;
            } else {
                if (position.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                    TileEntity tile = world.getTileEntity(position.getBlockPos());

                    if (!(tile instanceof IBloodAltar)) {
                        player.addChatComponentMessage(new ChatComponentText(TextHelper.localize("message.divinationsigil.currentessence", currentEssence)));
                        return stack;
                    }
                }
            }
        }

        return stack;
    }
}

package WayofTime.alchemicalWizardry.common.items.energy;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.api.items.interfaces.IReagentManipulator;
import WayofTime.alchemicalWizardry.common.tileEntity.TEReagentConduit;

public class ItemDestinationClearer extends Item implements IReagentManipulator
{
    public ItemDestinationClearer()
    {
        super();
        this.setCreativeTab(AlchemicalWizardry.tabBloodMagic);
        this.maxStackSize = 1;
    }

    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
    {
        par3List.add(StatCollector.translateToLocal("tooltip.destclearer.desc1"));
        par3List.add(StatCollector.translateToLocal("tooltip.destclearer.desc2"));
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player)
    {
        if (world.isRemote)
        {
            return itemStack;
        }

        MovingObjectPosition movingobjectposition = this.getMovingObjectPositionFromPlayer(world, player, false);

        if (movingobjectposition == null)
        {
            return itemStack;
        } else
        {
            if (movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
            {
                BlockPos pos = movingobjectposition.func_178782_a();

                TileEntity tile = world.getTileEntity(pos);

                if (!(tile instanceof TEReagentConduit))
                {
                    return itemStack;
                }

                TEReagentConduit relay = (TEReagentConduit) tile;

                relay.reagentTargetList.clear();

                player.addChatComponentMessage(new ChatComponentTranslation("message.destinationclearer.cleared"));
            }
        }

        return itemStack;
    }
}

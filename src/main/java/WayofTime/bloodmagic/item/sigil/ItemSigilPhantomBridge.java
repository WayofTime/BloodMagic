package WayofTime.bloodmagic.item.sigil;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import WayofTime.bloodmagic.registry.ModBlocks;

public class ItemSigilPhantomBridge extends ItemSigilToggleableBase
{
    public ItemSigilPhantomBridge()
    {
        super("phantomBridge", 100);
    }

    @Override
    public void onSigilUpdate(ItemStack stack, World world, EntityPlayer player, int itemSlot, boolean isSelected)
    {
        if ((!player.onGround && !player.isRiding()) && !player.isSneaking())
            return;

        int range = 2;
        int verticalOffset = -1;

        if (player.isSneaking())
            verticalOffset--;

        int posX = (int) Math.round(player.posX - 0.5f);
        int posY = (int) player.posY;
        int posZ = (int) Math.round(player.posZ - 0.5f);

        for (int ix = posX - range; ix <= posX + range; ix++)
        {
            for (int iz = posZ - range; iz <= posZ + range; iz++)
            {
                BlockPos blockPos = new BlockPos(ix, posY + verticalOffset, iz);

                if (world.isAirBlock(blockPos))
                    world.setBlockState(blockPos, ModBlocks.PHANTOM_BLOCK.getDefaultState());
            }
        }
    }
}

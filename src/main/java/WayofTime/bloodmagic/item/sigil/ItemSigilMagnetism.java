package WayofTime.bloodmagic.item.sigil;

import java.util.List;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import WayofTime.bloodmagic.api.Constants;

public class ItemSigilMagnetism extends ItemSigilToggleableBase
{
    public ItemSigilMagnetism()
    {
        super("magnetism", 50);
    }

    @Override
    public void onSigilUpdate(ItemStack stack, World world, EntityPlayer player, int itemSlot, boolean isSelected)
    {
        int range = 5;
        int verticalRange = 5;
        float posX = Math.round(player.posX);
        float posY = (float) (player.posY - player.getEyeHeight());
        float posZ = Math.round(player.posZ);
        List<EntityItem> entities = player.worldObj.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(posX - 0.5f, posY - 0.5f, posZ - 0.5f, posX + 0.5f, posY + 0.5f, posZ + 0.5f).expand(range, verticalRange, range));
        List<EntityXPOrb> xpOrbs = player.worldObj.getEntitiesWithinAABB(EntityXPOrb.class, new AxisAlignedBB(posX - 0.5f, posY - 0.5f, posZ - 0.5f, posX + 0.5f, posY + 0.5f, posZ + 0.5f).expand(range, verticalRange, range));

        for (EntityItem entity : entities)
        {
            if (entity != null && !world.isRemote)
            {
                entity.onCollideWithPlayer(player);
            }
        }

        for (EntityXPOrb xpOrb : xpOrbs)
        {
            if (xpOrb != null && !world.isRemote)
            {
                xpOrb.onCollideWithPlayer(player);
            }
        }
    }
}

package WayofTime.bloodmagic.item.sigil;

import WayofTime.bloodmagic.util.helper.PlayerHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

import java.util.List;

public class ItemSigilMagnetism extends ItemSigilToggleableBase {
    public ItemSigilMagnetism() {
        super("magnetism", 50);
    }

    @Override
    public void onSigilUpdate(ItemStack stack, World world, EntityPlayer player, int itemSlot, boolean isSelected) {
        if (PlayerHelper.isFakePlayer(player))
            return;

        int range = 5;
        int verticalRange = 5;
        float posX = Math.round(player.posX);
        float posY = (float) (player.posY - player.getEyeHeight());
        float posZ = Math.round(player.posZ);
        List<EntityItem> entities = player.getEntityWorld().getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(posX - 0.5f, posY - 0.5f, posZ - 0.5f, posX + 0.5f, posY + 0.5f, posZ + 0.5f).grow(range, verticalRange, range));
        List<EntityXPOrb> xpOrbs = player.getEntityWorld().getEntitiesWithinAABB(EntityXPOrb.class, new AxisAlignedBB(posX - 0.5f, posY - 0.5f, posZ - 0.5f, posX + 0.5f, posY + 0.5f, posZ + 0.5f).grow(range, verticalRange, range));

        for (EntityItem entity : entities) {
            if (entity != null && !world.isRemote && !entity.isDead) {
                entity.onCollideWithPlayer(player);
            }
        }

        for (EntityXPOrb xpOrb : xpOrbs) {
            if (xpOrb != null && !world.isRemote) {
                xpOrb.onCollideWithPlayer(player);
            }
        }
    }
}

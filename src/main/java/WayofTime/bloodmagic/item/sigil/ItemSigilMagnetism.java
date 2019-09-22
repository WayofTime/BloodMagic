package WayofTime.bloodmagic.item.sigil;

import WayofTime.bloodmagic.util.helper.PlayerHelper;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

import java.util.List;

public class ItemSigilMagnetism extends ItemSigilToggleableBase {
    public ItemSigilMagnetism() {
        super("magnetism", 50);
    }

    @Override
    public void onSigilUpdate(ItemStack stack, World world, PlayerEntity player, int itemSlot, boolean isSelected) {
        if (PlayerHelper.isFakePlayer(player))
            return;

        int range = 5;
        int verticalRange = 5;
        float posX = Math.round(player.posX);
        float posY = (float) (player.posY - player.getEyeHeight());
        float posZ = Math.round(player.posZ);
        List<ItemEntity> entities = player.getEntityWorld().getEntitiesWithinAABB(ItemEntity.class, new AxisAlignedBB(posX - 0.5f, posY - 0.5f, posZ - 0.5f, posX + 0.5f, posY + 0.5f, posZ + 0.5f).grow(range, verticalRange, range));
        List<ExperienceOrbEntity> xpOrbs = player.getEntityWorld().getEntitiesWithinAABB(ExperienceOrbEntity.class, new AxisAlignedBB(posX - 0.5f, posY - 0.5f, posZ - 0.5f, posX + 0.5f, posY + 0.5f, posZ + 0.5f).grow(range, verticalRange, range));

        for (ItemEntity entity : entities) {
            if (entity != null && !world.isRemote && !entity.isDead) {
                entity.onCollideWithPlayer(player);
            }
        }

        for (ExperienceOrbEntity xpOrb : xpOrbs) {
            if (xpOrb != null && !world.isRemote) {
                xpOrb.onCollideWithPlayer(player);
            }
        }
    }
}

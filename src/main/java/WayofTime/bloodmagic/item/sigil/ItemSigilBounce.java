package WayofTime.bloodmagic.item.sigil;

import WayofTime.bloodmagic.api.util.helper.PlayerHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import WayofTime.bloodmagic.registry.ModPotions;

public class ItemSigilBounce extends ItemSigilToggleableBase
{
    public ItemSigilBounce()
    {
        super("bounce", 100);
    }

    @Override
    public void onSigilUpdate(ItemStack stack, World world, EntityPlayer player, int itemSlot, boolean isSelected)
    {
        if (PlayerHelper.isFakePlayer(player))
            return;

        player.addPotionEffect(new PotionEffect(ModPotions.bounce, 2, 0, true, false));
    }
}

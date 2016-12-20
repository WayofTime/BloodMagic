package WayofTime.bloodmagic.item.sigil;

import WayofTime.bloodmagic.api.util.helper.PlayerHelper;
import WayofTime.bloodmagic.registry.ModPotions;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class ItemSigilWhirlwind extends ItemSigilToggleableBase
{
    public ItemSigilWhirlwind()
    {
        super("whirlwind", 250);
    }

    @Override
    public void onSigilUpdate(ItemStack stack, World world, EntityPlayer player, int itemSlot, boolean isSelected)
    {
        if (PlayerHelper.isFakePlayer(player))
            return;

        player.addPotionEffect(new PotionEffect(ModPotions.whirlwind, 2, 0, true, false));
    }
}

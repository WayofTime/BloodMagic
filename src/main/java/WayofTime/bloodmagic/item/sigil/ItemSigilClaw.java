package WayofTime.bloodmagic.item.sigil;

import WayofTime.bloodmagic.api.util.helper.PlayerHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import WayofTime.bloodmagic.registry.ModPotions;

public class ItemSigilClaw extends ItemSigilToggleableBase
{
    public ItemSigilClaw()
    {
        super("claw", 100);
    }

    @Override
    public void onSigilUpdate(ItemStack stack, World world, EntityPlayer player, int itemSlot, boolean isSelected)
    {
        if (PlayerHelper.isFakePlayer(player))
            return;

        player.addPotionEffect(new PotionEffect(ModPotions.cling, 2, 0, true, false));
    }
}

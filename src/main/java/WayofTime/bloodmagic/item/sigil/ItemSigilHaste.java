package WayofTime.bloodmagic.item.sigil;

import WayofTime.bloodmagic.registry.ModPotions;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class ItemSigilHaste extends ItemSigilToggleable
{
    public ItemSigilHaste()
    {
        super("haste", 250);
    }

    @Override
    public void onSigilUpdate(ItemStack stack, World world, EntityPlayer player, int itemSlot, boolean isSelected)
    {
        player.addPotionEffect(new PotionEffect(ModPotions.boost.id, 2, 0, true, false));
    }
}

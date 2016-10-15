package WayofTime.bloodmagic.item.sigil;

import net.minecraft.enchantment.EnchantmentFrostWalker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemSigilFrost extends ItemSigilToggleableBase
{
    public ItemSigilFrost()
    {
        super("frost", 100);
    }

    @Override
    public void onSigilUpdate(ItemStack stack, World world, EntityPlayer player, int itemSlot, boolean isSelected)
    {
        EnchantmentFrostWalker.freezeNearby(player, world, player.getPosition(), 1);
    }
}

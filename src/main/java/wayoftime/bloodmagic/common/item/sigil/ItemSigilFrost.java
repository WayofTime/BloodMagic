package wayoftime.bloodmagic.common.item.sigil;

import net.minecraft.world.item.enchantment.FrostWalkerEnchantment;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import wayoftime.bloodmagic.util.helper.PlayerHelper;

public class ItemSigilFrost extends ItemSigilToggleableBase
{
	public ItemSigilFrost()
	{
		super("frost", 100);
	}

	@Override
	public void onSigilUpdate(ItemStack stack, Level world, Player player, int itemSlot, boolean isSelected)
	{
		if (PlayerHelper.isFakePlayer(player))
			return;

		FrostWalkerEnchantment.onEntityMoved(player, world, player.blockPosition(), 1);
	}
}

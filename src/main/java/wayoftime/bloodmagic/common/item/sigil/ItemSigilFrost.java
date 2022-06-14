package wayoftime.bloodmagic.common.item.sigil;

import net.minecraft.enchantment.FrostWalkerEnchantment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import wayoftime.bloodmagic.util.helper.PlayerHelper;

public class ItemSigilFrost extends ItemSigilToggleableBase
{
	public ItemSigilFrost()
	{
		super("frost", 100);
	}

	@Override
	public void onSigilUpdate(ItemStack stack, World world, PlayerEntity player, int itemSlot, boolean isSelected)
	{
		if (PlayerHelper.isFakePlayer(player))
			return;

		FrostWalkerEnchantment.onEntityMoved(player, world, player.blockPosition(), 1);
	}
}

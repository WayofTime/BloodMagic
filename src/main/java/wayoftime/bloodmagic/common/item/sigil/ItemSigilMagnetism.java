package wayoftime.bloodmagic.common.item.sigil;

import java.util.List;

import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.Level;
import wayoftime.bloodmagic.util.helper.PlayerHelper;

public class ItemSigilMagnetism extends ItemSigilToggleableBase
{
	public ItemSigilMagnetism()
	{
		super("magnetism", 50);
	}

	@Override
	public void onSigilUpdate(ItemStack stack, Level world, Player player, int itemSlot, boolean isSelected)
	{
		if (PlayerHelper.isFakePlayer(player))
			return;

		int range = 5;
		int verticalRange = 5;
		float posX = Math.round(player.getX());
		float posY = (float) (player.getY() - player.getEyeHeight());
		float posZ = Math.round(player.getZ());
		List<ItemEntity> entities = player.getCommandSenderWorld().getEntitiesOfClass(ItemEntity.class, new AABB(posX - 0.5f, posY - 0.5f, posZ - 0.5f, posX + 0.5f, posY + 0.5f, posZ + 0.5f).inflate(range, verticalRange, range));
		List<ExperienceOrb> xpOrbs = player.getCommandSenderWorld().getEntitiesOfClass(ExperienceOrb.class, new AABB(posX - 0.5f, posY - 0.5f, posZ - 0.5f, posX + 0.5f, posY + 0.5f, posZ + 0.5f).inflate(range, verticalRange, range));

		for (ItemEntity entity : entities)
		{
			if (entity != null && !world.isClientSide && entity.isAlive())
			{
				entity.playerTouch(player);
			}
		}

		for (ExperienceOrb xpOrb : xpOrbs)
		{
			if (xpOrb != null && !world.isClientSide)
			{
				xpOrb.playerTouch(player);
			}
		}
	}
}

package wayoftime.bloodmagic.common.item.sigil;

import java.util.List;

import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import wayoftime.bloodmagic.util.helper.PlayerHelper;

public class ItemSigilMagnetism extends ItemSigilToggleableBase
{
	public ItemSigilMagnetism()
	{
		super("magnetism", 50);
	}

	@Override
	public void onSigilUpdate(ItemStack stack, World world, PlayerEntity player, int itemSlot, boolean isSelected)
	{
		if (PlayerHelper.isFakePlayer(player))
			return;

		int range = 5;
		int verticalRange = 5;
		float posX = Math.round(player.getX());
		float posY = (float) (player.getY() - player.getEyeHeight());
		float posZ = Math.round(player.getZ());
		List<ItemEntity> entities = player.getCommandSenderWorld().getEntitiesOfClass(ItemEntity.class, new AxisAlignedBB(posX - 0.5f, posY - 0.5f, posZ - 0.5f, posX + 0.5f, posY + 0.5f, posZ + 0.5f).inflate(range, verticalRange, range));
		List<ExperienceOrbEntity> xpOrbs = player.getCommandSenderWorld().getEntitiesOfClass(ExperienceOrbEntity.class, new AxisAlignedBB(posX - 0.5f, posY - 0.5f, posZ - 0.5f, posX + 0.5f, posY + 0.5f, posZ + 0.5f).inflate(range, verticalRange, range));

		for (ItemEntity entity : entities)
		{
			if (entity != null && !world.isClientSide && entity.isAlive())
			{
				entity.playerTouch(player);
			}
		}

		for (ExperienceOrbEntity xpOrb : xpOrbs)
		{
			if (xpOrb != null && !world.isClientSide)
			{
				xpOrb.playerTouch(player);
			}
		}
	}
}

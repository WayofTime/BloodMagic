package wayoftime.bloodmagic.common.item.sigil;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import wayoftime.bloodmagic.tile.TileSpectral;
import wayoftime.bloodmagic.util.helper.PlayerHelper;

public class ItemSigilSuppression extends ItemSigilToggleableBase
{
	public ItemSigilSuppression()
	{
		super("suppression", 400);
	}

	@Override
	public void onSigilUpdate(ItemStack stack, World world, PlayerEntity player, int itemSlot, boolean isSelected)
	{
		if (PlayerHelper.isFakePlayer(player))
			return;

		int x = (int) player.getPosX();
		int y = (int) player.getPosY();
		int z = (int) player.getPosZ();
		final int radius = 5;
		final int refresh = 100;

		for (int i = -radius; i <= radius; i++)
		{
			for (int j = -radius; j <= radius; j++)
			{
				for (int k = -radius; k <= radius; k++)
				{
					if (i * i + j * j + k * k >= (radius + 0.50f) * (radius + 0.50f))
					{
						continue;
					}

					BlockPos blockPos = new BlockPos(x + i, y + j, z + k);
					BlockState state = world.getBlockState(blockPos);

					TileSpectral.createOrRefreshSpectralBlock(world, blockPos);

//					if (Utils.isBlockLiquid(state) && world.getTileEntity(blockPos) == null)
//						
//					else
//					{
//						TileEntity tile = world.getTileEntity(blockPos);
//						if (tile instanceof TileSpectralBlock)
//							((TileSpectral) tile).resetDuration(refresh);
//					}
				}
			}
		}

//		int range = 5;
//		int verticalRange = 5;
//		float posX = Math.round(player.getPosX());
//		float posY = (float) (player.getPosY() - player.getEyeHeight());
//		float posZ = Math.round(player.getPosZ());
//		List<ItemEntity> entities = player.getEntityWorld().getEntitiesWithinAABB(ItemEntity.class, new AxisAlignedBB(posX - 0.5f, posY - 0.5f, posZ - 0.5f, posX + 0.5f, posY + 0.5f, posZ + 0.5f).grow(range, verticalRange, range));
//		List<ExperienceOrbEntity> xpOrbs = player.getEntityWorld().getEntitiesWithinAABB(ExperienceOrbEntity.class, new AxisAlignedBB(posX - 0.5f, posY - 0.5f, posZ - 0.5f, posX + 0.5f, posY + 0.5f, posZ + 0.5f).grow(range, verticalRange, range));
//
//		for (ItemEntity entity : entities)
//		{
//			if (entity != null && !world.isRemote && entity.isAlive())
//			{
//				entity.onCollideWithPlayer(player);
//			}
//		}
//
//		for (ExperienceOrbEntity xpOrb : xpOrbs)
//		{
//			if (xpOrb != null && !world.isRemote)
//			{
//				xpOrb.onCollideWithPlayer(player);
//			}
//		}
	}
}

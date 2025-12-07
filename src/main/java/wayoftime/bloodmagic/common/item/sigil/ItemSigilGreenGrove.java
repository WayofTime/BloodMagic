package wayoftime.bloodmagic.common.item.sigil;

import com.agricraft.agricraft.common.block.entity.CropBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import wayoftime.bloodmagic.core.data.SoulTicket;
import wayoftime.bloodmagic.util.helper.NetworkHelper;
import wayoftime.bloodmagic.util.helper.PlayerHelper;
import wayoftime.bloodmagic.ConfigManager;

public class ItemSigilGreenGrove extends ItemSigilToggleableBase
{
	public ItemSigilGreenGrove()
	{
		super("green_grove", 150);
	}

	@Override
	public int getLpUsed()
	{
		return ConfigManager.COMMON.sigilGreenGroveCost.get();
	}

	@Override
	public boolean onSigilUse(ItemStack stack, Player player, Level world, BlockPos blockPos, Direction side, Vec3 vec)
	{
		if (PlayerHelper.isFakePlayer(player))
			return false;

		if (!world.isClientSide && NetworkHelper.getSoulNetwork(player).syphonAndDamage(player, SoulTicket.item(stack, world, player, getLpUsed())).isSuccess() && applyBonemeal(stack, world, blockPos, player))
		{
			world.levelEvent(2005, blockPos, 0);
			return true;
		}

		return false;
	}

	@Override
	public void onSigilUpdate(ItemStack stack, Level worldIn, Player player, int itemSlot, boolean isSelected)
	{
		if (PlayerHelper.isFakePlayer(player))
			return;

		int range = 3;
		int verticalRange = 2;
		int posX = (int) Math.round(player.getX() - 0.5f);
		int posY = (int) player.getY();
		int posZ = (int) Math.round(player.getZ() - 0.5f);
		if (worldIn instanceof ServerLevel)
		{
			ServerLevel serverWorld = (ServerLevel) worldIn;
			for (int ix = posX - range; ix <= posX + range; ix++)
			{
				for (int iz = posZ - range; iz <= posZ + range; iz++)
				{
					for (int iy = posY - verticalRange; iy <= posY + verticalRange; iy++)
					{
						BlockPos blockPos = new BlockPos(ix, iy, iz);
						BlockState state = worldIn.getBlockState(blockPos);

//						if (!BloodMagicAPI.INSTANCE.getBlacklist().getGreenGrove().contains(state))
						{
							if (state.getBlock() instanceof BonemealableBlock && state.getBlock() != Blocks.GRASS_BLOCK)
							{
								if (worldIn.random.nextInt(50) == 0)
								{

                                    if (worldIn.getBlockEntity(blockPos) instanceof CropBlockEntity crop) {
                                        crop.removeWeeds();
                                    }
									BlockState preBlockState = worldIn.getBlockState(blockPos);
									if (((BonemealableBlock) state.getBlock()).isValidBonemealTarget(serverWorld, blockPos, preBlockState, worldIn.isClientSide))
									{
										((BonemealableBlock) state.getBlock()).performBonemeal(serverWorld, worldIn.random, blockPos, state);
                                        if (worldIn.getBlockEntity(blockPos) instanceof CropBlockEntity crop) {
                                            crop.removeWeeds();
                                        }

										BlockState newState = worldIn.getBlockState(blockPos);
										if (!newState.equals(preBlockState) && !worldIn.isClientSide)
											worldIn.levelEvent(2005, blockPos, 0);
									}
								}
							}
						}
					}
				}
			}
		}

	}

	private static boolean applyBonemeal(ItemStack stack, Level worldIn, BlockPos pos, Player player)
	{
		BlockState blockstate = worldIn.getBlockState(pos);
		int hook = net.minecraftforge.event.ForgeEventFactory.onApplyBonemeal(player, worldIn, pos, blockstate, stack);
		if (hook != 0)
			return hook > 0;
		if (blockstate.getBlock() instanceof BonemealableBlock)
		{
			BonemealableBlock igrowable = (BonemealableBlock) blockstate.getBlock();
			if (igrowable.isValidBonemealTarget(worldIn, pos, blockstate, worldIn.isClientSide))
			{
				if (worldIn instanceof ServerLevel)
				{
                    if (worldIn.getBlockEntity(pos) instanceof CropBlockEntity crop) {
                        crop.removeWeeds();
                    }
					if (igrowable.isBonemealSuccess(worldIn, worldIn.random, pos, blockstate))
					{
						igrowable.performBonemeal((ServerLevel) worldIn, worldIn.random, pos, blockstate);
					}
                    if (worldIn.getBlockEntity(pos) instanceof CropBlockEntity crop) {
                        crop.removeWeeds();
                    }

				}

				return true;
			}
		}

		return false;
	}

}

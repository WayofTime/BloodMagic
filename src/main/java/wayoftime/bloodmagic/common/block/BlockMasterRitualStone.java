package wayoftime.bloodmagic.common.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ToolType;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.item.IBindable;
import wayoftime.bloodmagic.common.item.ItemActivationCrystal;
import wayoftime.bloodmagic.ritual.Ritual;
import wayoftime.bloodmagic.tile.TileMasterRitualStone;
import wayoftime.bloodmagic.util.helper.RitualHelper;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class BlockMasterRitualStone extends Block
{
	public final boolean isInverted;

	public BlockMasterRitualStone(boolean isInverted)
	{
		super(Properties.of(Material.STONE).sound(SoundType.STONE).strength(2.0F, 5.0F).harvestTool(ToolType.PICKAXE).harvestLevel(2));
		this.isInverted = isInverted;
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult blockRayTraceResult)
	{
		ItemStack heldItem = player.getItemInHand(hand);
		BlockEntity tile = world.getBlockEntity(pos);

		if (tile instanceof TileMasterRitualStone)
		{
			if (heldItem.getItem() instanceof ItemActivationCrystal)
			{
				if (((IBindable) heldItem.getItem()).getBinding(heldItem) == null)
					return InteractionResult.FAIL;

				String key = RitualHelper.getValidRitual(world, pos);
				if (!key.isEmpty())
				{
					Ritual ritual = BloodMagic.RITUAL_MANAGER.getRitual(key);
					if (ritual != null)
					{
						Direction direction = RitualHelper.getDirectionOfRitual(world, pos, ritual);
						// TODO: Give a message stating that this ritual is not a valid ritual.
						if (direction != null && RitualHelper.checkValidRitual(world, pos, ritual, direction))
						{
							if (((TileMasterRitualStone) tile).activateRitual(heldItem, player, BloodMagic.RITUAL_MANAGER.getRitual(key)))
							{
								((TileMasterRitualStone) tile).setDirection(direction);
								if (isInverted)
									((TileMasterRitualStone) tile).setInverted(true);
							}
						} else
						{
							player.displayClientMessage(new TranslatableComponent("chat.bloodmagic.ritual.notValid"), true);
						}
					} else
					{
						player.displayClientMessage(new TranslatableComponent("chat.bloodmagic.ritual.notValid"), true);
					}
				} else
				{
					player.displayClientMessage(new TranslatableComponent("chat.bloodmagic.ritual.notValid"), true);
				}
			}
		}

		return InteractionResult.FAIL;
	}

	@Override
	public void destroy(LevelAccessor world, BlockPos blockPos, BlockState blockState)
	{
		TileMasterRitualStone tile = (TileMasterRitualStone) world.getBlockEntity(blockPos);
		if (tile != null)
			((TileMasterRitualStone) tile).stopRitual(Ritual.BreakType.BREAK_MRS);

		super.destroy(world, blockPos, blockState);
	}

	@Override
	public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving)
	{
		if (!state.is(newState.getBlock()))
		{
			BlockEntity tile = worldIn.getBlockEntity(pos);
			if (tile instanceof TileMasterRitualStone)
			{
				((TileMasterRitualStone) tile).stopRitual(Ritual.BreakType.BREAK_MRS);
			}

			super.onRemove(state, worldIn, pos, newState, isMoving);
		}
	}

	@Override
	public void wasExploded(Level world, BlockPos pos, Explosion explosion)
	{
		BlockEntity tile = world.getBlockEntity(pos);

		if (tile instanceof TileMasterRitualStone)
			((TileMasterRitualStone) tile).stopRitual(Ritual.BreakType.EXPLOSION);
	}

	@Override
	public boolean hasTileEntity(BlockState state)
	{
		return true;
	}

	@Override
	public BlockEntity createTileEntity(BlockState state, BlockGetter world)
	{
		return new TileMasterRitualStone();
	}
}
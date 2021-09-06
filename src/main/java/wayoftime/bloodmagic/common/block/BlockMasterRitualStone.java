package wayoftime.bloodmagic.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.item.IBindable;
import wayoftime.bloodmagic.common.item.ItemActivationCrystal;
import wayoftime.bloodmagic.ritual.Ritual;
import wayoftime.bloodmagic.tile.TileMasterRitualStone;
import wayoftime.bloodmagic.util.helper.RitualHelper;

public class BlockMasterRitualStone extends Block
{
	public final boolean isInverted;

	public BlockMasterRitualStone(boolean isInverted)
	{
		super(Properties.create(Material.ROCK).sound(SoundType.STONE).hardnessAndResistance(2.0F, 5.0F).harvestTool(ToolType.PICKAXE).harvestLevel(2));
		this.isInverted = isInverted;
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult blockRayTraceResult)
	{
		ItemStack heldItem = player.getHeldItem(hand);
		TileEntity tile = world.getTileEntity(pos);

		if (tile instanceof TileMasterRitualStone)
		{
			if (heldItem.getItem() instanceof ItemActivationCrystal)
			{
				if (((IBindable) heldItem.getItem()).getBinding(heldItem) == null)
					return ActionResultType.FAIL;

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
							player.sendStatusMessage(new TranslationTextComponent("chat.bloodmagic.ritual.notValid"), true);
						}
					} else
					{
						player.sendStatusMessage(new TranslationTextComponent("chat.bloodmagic.ritual.notValid"), true);
					}
				} else
				{
					player.sendStatusMessage(new TranslationTextComponent("chat.bloodmagic.ritual.notValid"), true);
				}
			}
		}

		return ActionResultType.FAIL;
	}

	@Override
	public void onPlayerDestroy(IWorld world, BlockPos blockPos, BlockState blockState)
	{
		TileMasterRitualStone tile = (TileMasterRitualStone) world.getTileEntity(blockPos);
		if (tile != null)
			((TileMasterRitualStone) tile).stopRitual(Ritual.BreakType.BREAK_MRS);

		super.onPlayerDestroy(world, blockPos, blockState);
	}

	@Override
	public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving)
	{
		if (!state.isIn(newState.getBlock()))
		{
			TileEntity tile = worldIn.getTileEntity(pos);
			if (tile instanceof TileMasterRitualStone)
			{
				((TileMasterRitualStone) tile).stopRitual(Ritual.BreakType.BREAK_MRS);
			}

			super.onReplaced(state, worldIn, pos, newState, isMoving);
		}
	}

	@Override
	public void onExplosionDestroy(World world, BlockPos pos, Explosion explosion)
	{
		TileEntity tile = world.getTileEntity(pos);

		if (tile instanceof TileMasterRitualStone)
			((TileMasterRitualStone) tile).stopRitual(Ritual.BreakType.EXPLOSION);
	}

	@Override
	public boolean hasTileEntity(BlockState state)
	{
		return true;
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world)
	{
		return new TileMasterRitualStone();
	}
}
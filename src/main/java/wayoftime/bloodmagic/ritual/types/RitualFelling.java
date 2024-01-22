package wayoftime.bloodmagic.ritual.types;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.ritual.AreaDescriptor;
import wayoftime.bloodmagic.ritual.EnumRuneType;
import wayoftime.bloodmagic.ritual.IMasterRitualStone;
import wayoftime.bloodmagic.ritual.Ritual;
import wayoftime.bloodmagic.ritual.RitualComponent;
import wayoftime.bloodmagic.ritual.RitualRegister;
import wayoftime.bloodmagic.util.Utils;

@RitualRegister("felling")
public class RitualFelling extends Ritual
{
	public static final String FELLING_RANGE = "fellingRange";
	public static final String CHEST_RANGE = "chest";

	private ArrayList<BlockPos> treePartsCache;
	private Iterator<BlockPos> blockPosIterator;

	private boolean cached = false;
	private BlockPos currentPos;

	private static final ItemStack mockAxe = new ItemStack(Items.DIAMOND_AXE, 1);

	public RitualFelling()
	{
		super("ritualFelling", 0, 20000, "ritual." + BloodMagic.MODID + ".fellingRitual");
		addBlockRange(FELLING_RANGE, new AreaDescriptor.Rectangle(new BlockPos(-10, -3, -10), new BlockPos(11, 27, 11)));
		addBlockRange(CHEST_RANGE, new AreaDescriptor.Rectangle(new BlockPos(0, 1, 0), 1));

		setMaximumVolumeAndDistanceOfRange(FELLING_RANGE, 14000, 15, 30);
		setMaximumVolumeAndDistanceOfRange(CHEST_RANGE, 1, 3, 3);

		treePartsCache = new ArrayList<>();
	}

	@Override
	public void performRitual(IMasterRitualStone masterRitualStone)
	{
		Level world = masterRitualStone.getWorldObj();
		int currentEssence = masterRitualStone.getOwnerNetwork().getCurrentEssence();

		BlockPos masterPos = masterRitualStone.getMasterBlockPos();
		AreaDescriptor chestRange = masterRitualStone.getBlockRange(CHEST_RANGE);
		BlockEntity tileInventory = world.getBlockEntity(chestRange.getContainedPositions(masterPos).get(0));

		if (world.isClientSide)
		{
			return;
		}

		if (tileInventory != null && Utils.getNumberOfFreeSlots(tileInventory, Direction.DOWN) < 1)
		{
			return;
		}

		if (currentEssence < getRefreshCost())
		{
			masterRitualStone.getOwnerNetwork().causeNausea();
			return;
		}

		if (!cached || treePartsCache.isEmpty())
		{
			for (BlockPos blockPos : masterRitualStone.getBlockRange(FELLING_RANGE).getContainedPositions(masterRitualStone.getMasterBlockPos()))
			{
				if (!treePartsCache.contains(blockPos))
					if (!world.isEmptyBlock(blockPos) && (world.getBlockState(blockPos).is(BlockTags.LOGS) || world.getBlockState(blockPos).is(BlockTags.LEAVES)))
					{
						treePartsCache.add(blockPos);
					}
			}

			cached = true;
			blockPosIterator = treePartsCache.iterator();
		}

		if (blockPosIterator.hasNext() && tileInventory != null)
		{
			masterRitualStone.getOwnerNetwork().syphon(masterRitualStone.ticket(getRefreshCost()));
			currentPos = blockPosIterator.next();
			IItemHandler inventory = Utils.getInventory(tileInventory, Direction.DOWN);
			BlockState state = world.getBlockState(currentPos);
			placeInInventory(state, world, currentPos, inventory);

			BlockPlaceContext ctx = new BlockPlaceContext(world, null, InteractionHand.MAIN_HAND, ItemStack.EMPTY, BlockHitResult.miss(new Vec3(0, 0, 0), Direction.UP, currentPos));
			spawnParticlesAndSound((ServerLevel) world, currentPos, state, ctx);

			world.setBlockAndUpdate(currentPos, Blocks.AIR.defaultBlockState());
			blockPosIterator.remove();
		}
	}

	public void spawnParticlesAndSound(ServerLevel world, BlockPos pos, BlockState state, BlockPlaceContext context)
	{
		SoundType soundtype = state.getSoundType(world, pos, context.getPlayer());
		world.playSound(context.getPlayer(), pos, state.getSoundType(world, pos, context.getPlayer()).getPlaceSound(), SoundSource.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);

		BlockParticleOption particleData = new BlockParticleOption(ParticleTypes.BLOCK, state);

		world.sendParticles(particleData, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 8, 0.2, 0.2, 0.2, 0.03);
	}

	@Override
	public int getRefreshCost()
	{
		return 10;
	}

	@Override
	public int getRefreshTime()
	{
		return 1;
	}

	@Override
	public void gatherComponents(Consumer<RitualComponent> components)
	{
		addCornerRunes(components, 1, 0, EnumRuneType.EARTH);
		addCornerRunes(components, 1, 1, EnumRuneType.EARTH);
	}

	@Override
	public Ritual getNewCopy()
	{
		return new RitualFelling();
	}

	private void placeInInventory(BlockState choppedState, Level world, BlockPos choppedPos, @Nullable IItemHandler inventory)
	{
		if (inventory == null)
			return;

		LootParams.Builder lootBuilder = new LootParams.Builder((ServerLevel) world);
		Vec3 blockCenter = new Vec3(choppedPos.getX() + 0.5, choppedPos.getY() + 0.5, choppedPos.getZ() + 0.5);
		List<ItemStack> silkDrops = choppedState.getDrops(lootBuilder.withParameter(LootContextParams.ORIGIN, blockCenter).withParameter(LootContextParams.TOOL, mockAxe));

//		ItemHandlerHelper.insertItem(inventory, stack, simulate)
		for (ItemStack stack : silkDrops)
		{
			ItemStack remainder = ItemHandlerHelper.insertItem(inventory, stack, false);
			if (!remainder.isEmpty())
				world.addFreshEntity(new ItemEntity(world, choppedPos.getX() + 0.4, choppedPos.getY() + 2, choppedPos.getZ() + 0.4, remainder));
		}
	}
}

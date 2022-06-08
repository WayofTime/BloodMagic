package wayoftime.bloodmagic.ritual.types;

import java.util.function.Consumer;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.ritual.AreaDescriptor;
import wayoftime.bloodmagic.ritual.EnumRuneType;
import wayoftime.bloodmagic.ritual.IMasterRitualStone;
import wayoftime.bloodmagic.ritual.Ritual;
import wayoftime.bloodmagic.ritual.RitualComponent;
import wayoftime.bloodmagic.ritual.RitualRegister;
import wayoftime.bloodmagic.util.Utils;

@RitualRegister("placer")
public class RitualPlacer extends Ritual
{
	public static final String PLACER_RANGE = "placerRange";
	public static final String CHEST_RANGE = "chest";

	public RitualPlacer()
	{
		super("ritualPlacer", 0, 5000, "ritual." + BloodMagic.MODID + ".placerRitual");
		addBlockRange(PLACER_RANGE, new AreaDescriptor.Rectangle(new BlockPos(-2, 0, -2), 5, 1, 5));
		addBlockRange(CHEST_RANGE, new AreaDescriptor.Rectangle(new BlockPos(0, 1, 0), 1));

		setMaximumVolumeAndDistanceOfRange(PLACER_RANGE, 300, 7, 7);
		setMaximumVolumeAndDistanceOfRange(CHEST_RANGE, 1, 3, 3);
	}

	@Override
	public void performRitual(IMasterRitualStone masterRitualStone)
	{
		World world = masterRitualStone.getWorldObj();
		BlockPos masterPos = masterRitualStone.getMasterBlockPos();
		AreaDescriptor chestRange = masterRitualStone.getBlockRange(CHEST_RANGE);
		TileEntity tileEntity = world.getTileEntity(chestRange.getContainedPositions(masterPos).get(0));

		int currentEssence = masterRitualStone.getOwnerNetwork().getCurrentEssence();

		if (currentEssence < getRefreshCost())
		{
			masterRitualStone.getOwnerNetwork().causeNausea();
			return;
		}

		AreaDescriptor areaDescriptor = masterRitualStone.getBlockRange(PLACER_RANGE);

		if (tileEntity != null)
		{

			{
				IItemHandler itemHandler = Utils.getInventory(tileEntity, null);

				if (itemHandler.getSlots() <= 0)
				{
					return;
				}

				posLoop: for (BlockPos blockPos : areaDescriptor.getContainedPositions(masterRitualStone.getMasterBlockPos()))
				{
					BlockItemUseContext ctx = new BlockItemUseContext(world, null, Hand.MAIN_HAND, ItemStack.EMPTY, BlockRayTraceResult.createMiss(new Vector3d(0, 0, 0), Direction.UP, blockPos));
					if (!world.getBlockState(blockPos).isReplaceable(ctx))
						continue;

					for (int invSlot = 0; invSlot < itemHandler.getSlots(); invSlot++)
					{
						ItemStack stack = itemHandler.extractItem(invSlot, 1, true);
						if (stack.isEmpty() || !(stack.getItem() instanceof BlockItem))
							continue;

						BlockState placeState = Block.getBlockFromItem(itemHandler.getStackInSlot(invSlot).getItem()).getDefaultState();
						world.setBlockState(blockPos, placeState);
						itemHandler.extractItem(invSlot, 1, false);
						tileEntity.markDirty();
						masterRitualStone.getOwnerNetwork().syphon(masterRitualStone.ticket(getRefreshCost()));
						break posLoop; // Break instead of return in case we add things later
					}
				}
			}
		}
	}

	@Override
	public int getRefreshCost()
	{
		return 50;
	}

	@Override
	public void gatherComponents(Consumer<RitualComponent> components)
	{
		addRune(components, 3, 0, 3, EnumRuneType.EARTH);
		addRune(components, 3, 0, -3, EnumRuneType.EARTH);
		addRune(components, -3, 0, 3, EnumRuneType.EARTH);
		addRune(components, -3, 0, -3, EnumRuneType.EARTH);

		addRune(components, 3, 0, 2, EnumRuneType.WATER);
		addRune(components, 3, 0, -2, EnumRuneType.WATER);
		addRune(components, 2, 0, 3, EnumRuneType.WATER);
		addRune(components, 2, 0, -3, EnumRuneType.WATER);
		addRune(components, -2, 0, 3, EnumRuneType.WATER);
		addRune(components, -2, 0, -3, EnumRuneType.WATER);
		addRune(components, -3, 0, 2, EnumRuneType.WATER);
		addRune(components, -3, 0, -2, EnumRuneType.WATER);
	}

	@Override
	public Ritual getNewCopy()
	{
		return new RitualPlacer();
	}
}
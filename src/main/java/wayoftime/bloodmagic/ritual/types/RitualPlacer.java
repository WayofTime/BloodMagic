package wayoftime.bloodmagic.ritual.types;

import java.util.function.Consumer;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
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
		Level world = masterRitualStone.getWorldObj();
		BlockPos masterPos = masterRitualStone.getMasterBlockPos();
		AreaDescriptor chestRange = masterRitualStone.getBlockRange(CHEST_RANGE);
		BlockEntity tileEntity = world.getBlockEntity(chestRange.getContainedPositions(masterPos).get(0));

		int currentEssence = masterRitualStone.getOwnerNetwork().getCurrentEssence();

		if (currentEssence < getRefreshCost())
		{
			masterRitualStone.getOwnerNetwork().causeNausea();
			return;
		}

		AreaDescriptor areaDescriptor = masterRitualStone.getBlockRange(PLACER_RANGE);

		if (tileEntity != null)
		{
			IItemHandler itemHandler = Utils.getInventory(tileEntity, null);

			if (itemHandler.getSlots() <= 0)
			{
				return;
			}

			posLoop: for (BlockPos blockPos : areaDescriptor.getContainedPositions(masterRitualStone.getMasterBlockPos()))
			{
				BlockPlaceContext ctx = new BlockPlaceContext(world, null, InteractionHand.MAIN_HAND, ItemStack.EMPTY, BlockHitResult.miss(new Vec3(0, 0, 0), Direction.UP, blockPos));
				if (!world.getBlockState(blockPos).canBeReplaced(ctx))
					continue;

				for (int invSlot = 0; invSlot < itemHandler.getSlots(); invSlot++)
				{
					ItemStack stack = itemHandler.extractItem(invSlot, 1, true);
					if (stack.isEmpty() || !(stack.getItem() instanceof BlockItem))
						continue;

					InteractionResult result = ((BlockItem) stack.getItem()).place(ctx);
					if (result.consumesAction())
					{
						itemHandler.extractItem(invSlot, 1, false);
						tileEntity.setChanged();
						masterRitualStone.getOwnerNetwork().syphon(masterRitualStone.ticket(getRefreshCost()));
						break posLoop; // Break instead of return in case we add things later
					}

//					BlockState placeState = Block.getBlockFromItem(itemHandler.getStackInSlot(invSlot).getItem()).getDefaultState();
//					world.setBlockState(blockPos, placeState);

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
	public int getRefreshTime()
	{
		return 5;
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
package wayoftime.bloodmagic.ritual.types;

import java.util.List;
import java.util.function.Consumer;

import net.minecraft.entity.item.ItemFrameEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.item.ILivingUpgradePointsProvider;
import wayoftime.bloodmagic.core.LivingArmorRegistrar;
import wayoftime.bloodmagic.core.living.LivingStats;
import wayoftime.bloodmagic.core.living.LivingUpgrade;
import wayoftime.bloodmagic.core.living.LivingUtil;
import wayoftime.bloodmagic.impl.BloodMagicAPI;
import wayoftime.bloodmagic.recipe.RecipeLivingDowngrade;
import wayoftime.bloodmagic.ritual.AreaDescriptor;
import wayoftime.bloodmagic.ritual.EnumRuneType;
import wayoftime.bloodmagic.ritual.IMasterRitualStone;
import wayoftime.bloodmagic.ritual.Ritual;
import wayoftime.bloodmagic.ritual.RitualComponent;
import wayoftime.bloodmagic.ritual.RitualRegister;

@RitualRegister("downgrade")
public class RitualLivingDowngrade extends Ritual
{
	public static final String DOWNGRADE_RANGE = "containmentRange";

	public RitualLivingDowngrade()
	{
		super("ritualDowngrade", 0, 10000, "ritual." + BloodMagic.MODID + ".downgradeRitual");
		addBlockRange(DOWNGRADE_RANGE, new AreaDescriptor.Rectangle(new BlockPos(-3, 0, -3), 7));
	}

	@Override
	public void performRitual(IMasterRitualStone masterRitualStone)
	{
		World world = masterRitualStone.getWorldObj();
		BlockPos masterPos = masterRitualStone.getMasterBlockPos();
		Direction direction = masterRitualStone.getDirection();

		PlayerEntity selectedPlayer = null;
		AreaDescriptor downgradeRange = masterRitualStone.getBlockRange(DOWNGRADE_RANGE);

		for (PlayerEntity player : world.getEntitiesWithinAABB(PlayerEntity.class, downgradeRange.getAABB(masterRitualStone.getMasterBlockPos())))
		{
			if (player.isCrouching() && LivingUtil.hasFullSet(player))
			{
				selectedPlayer = player;
				break;
			}
		}

		if (selectedPlayer == null)
		{

			return;
		}

		LivingStats playerStats = LivingStats.fromPlayer(selectedPlayer, true);

		ItemStack focusStack = getStackFromItemFrame(world, masterPos, direction);
		if (focusStack.isEmpty())
		{
			return;
		}

		RecipeLivingDowngrade downgradeRecipe = BloodMagicAPI.INSTANCE.getRecipeRegistrar().getLivingDowngrade(world, focusStack);
		if (downgradeRecipe == null)
		{
			return;
		}

//		LivingUpgrade downgrade = LivingArmorRegistrar.UPGRADE_MAP.get(downgradeRecipe.getLivingArmourResource());
		LivingUpgrade downgrade = LivingArmorRegistrar.UPGRADE_MAP.getOrDefault(downgradeRecipe.getLivingArmourResource(), LivingUpgrade.DUMMY);
//		if (upgrade == LivingUpgrade.DUMMY)
//			return;

		if (downgrade == LivingUpgrade.DUMMY)
		{
			// Recipe is broken! No downgrade returned.
			return;
		}

		System.out.println("Found a downgrade!");

		BlockPos chestOffsetPos = new BlockPos(0, 1, 0);
		chestOffsetPos = chestOffsetPos.offset(direction, 2);

		BlockPos chestPos = masterPos.add(chestOffsetPos);

		// TODO: Change when chest logic is implemented.
		int wantedLevel = 3;
		int playerDowngradeLevel = playerStats.getLevel(downgradeRecipe.getLivingArmourResource());

		if (playerDowngradeLevel >= wantedLevel)
		{
			System.out.println("The player's downgrade's level is greater than or equal to the requested level!");
			return;
		}

		int playerInitialPoints = 0;
		if (playerDowngradeLevel > 0)
		{
			playerInitialPoints = playerStats.getUpgrades().getOrDefault(downgrade, 0d).intValue();
		}

		// Cost check logic.
		int totalRequiredPoints = downgrade.getLevelCost(wantedLevel);

		TileEntity tile = world.getTileEntity(chestPos);

		if (tile == null)
		{
			return;
		}

		int availablePoints = 0;
		Direction accessDir = Direction.DOWN;

		LazyOptional<IItemHandler> capability = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, accessDir);
		if (capability.isPresent())
		{
			IItemHandler handler = capability.resolve().get();

			for (int i = 0; i < handler.getSlots(); i++)
			{
				availablePoints += getAvailablePointsFromStack(handler.getStackInSlot(i));
			}
		} else if (tile instanceof IInventory)
		{
			for (int i = 0; i < ((IInventory) tile).getSizeInventory(); i++)
			{
				availablePoints += getAvailablePointsFromStack(((IInventory) tile).getStackInSlot(i));
			}
		}

		System.out.println("Number of available points found: " + availablePoints);

		int requiredPoints = totalRequiredPoints - playerInitialPoints;

		if (availablePoints < requiredPoints)
		{
			// Can't upgrade! Not enough points
			// TODO: Add smoke particles to indicate this?

			return;
		}

		// Consumption logic

	}

	public int getAvailablePointsFromStack(ItemStack stack)
	{
		if (stack.isEmpty())
		{
			return 0;
		}

		if (stack.getItem() instanceof ILivingUpgradePointsProvider)
		{
			return ((ILivingUpgradePointsProvider) stack.getItem()).getContainedUpgradePoints(stack);
		}

		return 0;
	}

	public ItemStack getStackFromItemFrame(World world, BlockPos masterPos, Direction direction)
	{
		BlockPos offsetPos = new BlockPos(0, 3, 0);
		offsetPos = offsetPos.offset(direction, 2);

		AxisAlignedBB bb = new AxisAlignedBB(masterPos.add(offsetPos));
		List<ItemFrameEntity> frames = world.getEntitiesWithinAABB(ItemFrameEntity.class, bb);
		for (ItemFrameEntity frame : frames)
		{
			if (!frame.getDisplayedItem().isEmpty())
			{
				return frame.getDisplayedItem();
			}
		}

		return ItemStack.EMPTY;
	}

	@Override
	public int getRefreshCost()
	{
		return 10;// Temporary
	}

	@Override
	public int getRefreshTime()
	{
		return 1;
	}

//    @Override
//    public void readFromNBT(NBTTagCompound tag)
//    {
//        super.readFromNBT(tag);
//        tag
//    }

	@Override
	public void gatherComponents(Consumer<RitualComponent> components)
	{
		addRune(components, 0, 0, -1, EnumRuneType.AIR);
		addRune(components, 0, 0, -2, EnumRuneType.DUSK);
		addRune(components, 0, 1, -3, EnumRuneType.DUSK);
		addRune(components, 0, 2, -3, EnumRuneType.BLANK);
		addRune(components, 0, 3, -3, EnumRuneType.BLANK);
		addRune(components, 0, 1, -4, EnumRuneType.FIRE);

		for (int i = 1; i <= 3; i++)
		{
			addRune(components, 0, 0, i, EnumRuneType.AIR);
		}

		for (int sgn = -1; sgn <= 1; sgn += 2)
		{
			addRune(components, sgn, 0, 4, EnumRuneType.AIR);
			addRune(components, sgn * 2, 0, 2, EnumRuneType.AIR);
			addRune(components, sgn * 3, 0, 2, EnumRuneType.AIR);
			addRune(components, sgn * 3, 0, 3, EnumRuneType.AIR);
			addRune(components, sgn, 0, 0, EnumRuneType.EARTH);
			addRune(components, sgn, 0, 1, EnumRuneType.EARTH);
			addRune(components, sgn * 2, 0, -1, EnumRuneType.FIRE);
			addRune(components, sgn * 2, 0, -2, EnumRuneType.FIRE);
			addRune(components, sgn * 3, 0, -2, EnumRuneType.FIRE);
			addRune(components, sgn * 3, 0, -3, EnumRuneType.FIRE);
			addRune(components, sgn * 3, 0, -4, EnumRuneType.FIRE);
			addRune(components, sgn, 1, -1, EnumRuneType.AIR);
			addRune(components, sgn, 1, -2, EnumRuneType.AIR);
			addRune(components, sgn, 1, -4, EnumRuneType.FIRE);
			addRune(components, sgn * 2, 1, -4, EnumRuneType.FIRE);
			addRune(components, sgn, 0, -3, EnumRuneType.EARTH);
			addRune(components, sgn, 0, -4, EnumRuneType.EARTH);
			addRune(components, sgn, 0, -5, EnumRuneType.EARTH);
			addRune(components, sgn, 1, -5, EnumRuneType.EARTH);
			addRune(components, sgn, 2, -5, EnumRuneType.EARTH);
			addRune(components, sgn, 3, -5, EnumRuneType.EARTH);
			addRune(components, sgn, 3, -4, EnumRuneType.EARTH);
		}
	}

	@Override
	public Ritual getNewCopy()
	{
		return new RitualLivingDowngrade();
	}
}
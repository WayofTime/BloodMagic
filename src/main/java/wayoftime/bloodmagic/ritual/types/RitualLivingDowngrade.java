package wayoftime.bloodmagic.ritual.types;

import java.util.List;
import java.util.function.Consumer;

import net.minecraft.entity.item.ItemFrameEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import wayoftime.bloodmagic.BloodMagic;
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
		RecipeLivingDowngrade downgradeRecipe = BloodMagicAPI.INSTANCE.getRecipeRegistrar().getLivingDowngrade(world, focusStack);
		if (downgradeRecipe == null)
		{
			return;
		}

		LivingUpgrade downgrade = LivingArmorRegistrar.UPGRADE_MAP.get(downgradeRecipe.getLivingArmourResource());
		if (downgrade == null)
		{
			// Recipe is broken! No downgrade returned.
			return;
		}

		BlockPos chestOffsetPos = new BlockPos(0, 1, 0);
		chestOffsetPos = chestOffsetPos.offset(direction, 2);

		BlockPos chestPos = masterPos.add(chestOffsetPos);

		// TODO: Change when chest logic is implemented.
		int wantedLevel = 3;

		// Cost check logic.
		int requiredExp = downgrade.getLevelExp(wantedLevel);

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
package wayoftime.bloodmagic.ritual.types;

import java.util.List;
import java.util.function.Consumer;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.entity.projectile.EntityMeteor;
import wayoftime.bloodmagic.impl.BloodMagicAPI;
import wayoftime.bloodmagic.recipe.RecipeMeteor;
import wayoftime.bloodmagic.ritual.AreaDescriptor;
import wayoftime.bloodmagic.ritual.EnumRuneType;
import wayoftime.bloodmagic.ritual.IMasterRitualStone;
import wayoftime.bloodmagic.ritual.Ritual;
import wayoftime.bloodmagic.ritual.RitualComponent;
import wayoftime.bloodmagic.ritual.RitualRegister;

@RitualRegister("meteor")
public class RitualMeteor extends Ritual
{
	public static final String CHECK_RANGE = "itemRange";

	public RitualMeteor()
	{
		super("ritualMeteor", 0, 250000, "ritual." + BloodMagic.MODID + ".meteorRitual");
		addBlockRange(CHECK_RANGE, new AreaDescriptor.Rectangle(new BlockPos(0, 1, 0), 1, 1, 1));

		setMaximumVolumeAndDistanceOfRange(CHECK_RANGE, 27, 10, 10);
	}

	@Override
	public void performRitual(IMasterRitualStone masterRitualStone)
	{
		Level world = masterRitualStone.getWorldObj();

		if (world.isClientSide)
		{
			return;
		}

		AreaDescriptor itemRange = masterRitualStone.getBlockRange(CHECK_RANGE);

		List<ItemEntity> itemList = world.getEntitiesOfClass(ItemEntity.class, itemRange.getAABB(masterRitualStone.getMasterBlockPos()));

		for (ItemEntity entityItem : itemList)
		{
			if (!entityItem.isAlive())
			{
				continue;
			}

			ItemStack stack = entityItem.getItem();

			RecipeMeteor recipe = BloodMagicAPI.INSTANCE.getRecipeRegistrar().getMeteor(world, stack);
			if (recipe != null)
			{
				int syphonAmount = recipe.getSyphon();
				int currentEssence = masterRitualStone.getOwnerNetwork().getCurrentEssence();
//				syphonAmount = 0;

				if (currentEssence < syphonAmount)
				{
					return;
				}

				if (syphonAmount > 0)
					masterRitualStone.getOwnerNetwork().syphon(masterRitualStone.ticket(syphonAmount));

				EntityMeteor meteor = new EntityMeteor(world, masterRitualStone.getMasterBlockPos().getX() + 0.5, world.getMaxBuildHeight() + 10, masterRitualStone.getMasterBlockPos().getZ() + 0.5);
				meteor.lerpMotion(0, -0.1, 0);
				meteor.setContainedStack(stack.split(1));
				world.addFreshEntity(meteor);

				if (stack.isEmpty())
					entityItem.remove(RemovalReason.KILLED);

//				entityItem.getItem().setCount(newStack.getCount());

//				masterRitualStone.setActive(false);

				return;
			}
		}
	}

	@Override
	public int getRefreshTime()
	{
		return 20;
	}

	@Override
	public int getRefreshCost()
	{
		return 0;
	}

	@Override
	public void gatherComponents(Consumer<RitualComponent> components)
	{
		addRune(components, 2, 0, 0, EnumRuneType.FIRE);
		addRune(components, -2, 0, 0, EnumRuneType.FIRE);
		addRune(components, 0, 0, 2, EnumRuneType.FIRE);
		addRune(components, 0, 0, -2, EnumRuneType.FIRE);
		addRune(components, 3, 0, 1, EnumRuneType.AIR);
		addRune(components, 3, 0, -1, EnumRuneType.AIR);
		addRune(components, -3, 0, 1, EnumRuneType.AIR);
		addRune(components, -3, 0, -1, EnumRuneType.AIR);
		addRune(components, 1, 0, 3, EnumRuneType.AIR);
		addRune(components, -1, 0, 3, EnumRuneType.AIR);
		addRune(components, 1, 0, -3, EnumRuneType.AIR);
		addRune(components, -1, 0, -3, EnumRuneType.AIR);
		addRune(components, 4, 0, 2, EnumRuneType.AIR);
		addRune(components, 4, 0, -2, EnumRuneType.AIR);
		addRune(components, -4, 0, 2, EnumRuneType.AIR);
		addRune(components, -4, 0, -2, EnumRuneType.AIR);
		addRune(components, 2, 0, 4, EnumRuneType.AIR);
		addRune(components, -2, 0, 4, EnumRuneType.AIR);
		addRune(components, 2, 0, -4, EnumRuneType.AIR);
		addRune(components, -2, 0, -4, EnumRuneType.AIR);
		addRune(components, 5, 0, 3, EnumRuneType.DUSK);
		addRune(components, 5, 0, -3, EnumRuneType.DUSK);
		addRune(components, -5, 0, 3, EnumRuneType.DUSK);
		addRune(components, -5, 0, -3, EnumRuneType.DUSK);
		addRune(components, 3, 0, 5, EnumRuneType.DUSK);
		addRune(components, -3, 0, 5, EnumRuneType.DUSK);
		addRune(components, 3, 0, -5, EnumRuneType.DUSK);
		addRune(components, -3, 0, -5, EnumRuneType.DUSK);
		addRune(components, -4, 0, -4, EnumRuneType.DUSK);
		addRune(components, -4, 0, 4, EnumRuneType.DUSK);
		addRune(components, 4, 0, 4, EnumRuneType.DUSK);
		addRune(components, 4, 0, -4, EnumRuneType.DUSK);

		for (int i = 4; i <= 6; i++)
		{
			addRune(components, i, 0, 0, EnumRuneType.EARTH);
			addRune(components, -i, 0, 0, EnumRuneType.EARTH);
			addRune(components, 0, 0, i, EnumRuneType.EARTH);
			addRune(components, 0, 0, -i, EnumRuneType.EARTH);
		}

		addRune(components, 8, 0, 0, EnumRuneType.EARTH);
		addRune(components, -8, 0, 0, EnumRuneType.EARTH);
		addRune(components, 0, 0, 8, EnumRuneType.EARTH);
		addRune(components, 0, 0, -8, EnumRuneType.EARTH);
		addRune(components, 8, 1, 0, EnumRuneType.EARTH);
		addRune(components, -8, 1, 0, EnumRuneType.EARTH);
		addRune(components, 0, 1, 8, EnumRuneType.EARTH);
		addRune(components, 0, 1, -8, EnumRuneType.EARTH);
		addRune(components, 7, 1, 0, EnumRuneType.EARTH);
		addRune(components, -7, 1, 0, EnumRuneType.EARTH);
		addRune(components, 0, 1, 7, EnumRuneType.EARTH);
		addRune(components, 0, 1, -7, EnumRuneType.EARTH);
		addRune(components, 7, 2, 0, EnumRuneType.FIRE);
		addRune(components, -7, 2, 0, EnumRuneType.FIRE);
		addRune(components, 0, 2, 7, EnumRuneType.FIRE);
		addRune(components, 0, 2, -7, EnumRuneType.FIRE);
		addRune(components, 6, 2, 0, EnumRuneType.FIRE);
		addRune(components, -6, 2, 0, EnumRuneType.FIRE);
		addRune(components, 0, 2, 6, EnumRuneType.FIRE);
		addRune(components, 0, 2, -6, EnumRuneType.FIRE);
		addRune(components, 6, 3, 0, EnumRuneType.WATER);
		addRune(components, -6, 3, 0, EnumRuneType.WATER);
		addRune(components, 0, 3, 6, EnumRuneType.WATER);
		addRune(components, 0, 3, -6, EnumRuneType.WATER);
		addRune(components, 5, 3, 0, EnumRuneType.WATER);
		addRune(components, -5, 3, 0, EnumRuneType.WATER);
		addRune(components, 0, 3, 5, EnumRuneType.WATER);
		addRune(components, 0, 3, -5, EnumRuneType.WATER);
		addRune(components, 5, 4, 0, EnumRuneType.AIR);
		addRune(components, -5, 4, 0, EnumRuneType.AIR);
		addRune(components, 0, 4, 5, EnumRuneType.AIR);
		addRune(components, 0, 4, -5, EnumRuneType.AIR);

		for (int i = -1; i <= 1; i++)
		{
			addRune(components, i, 4, 4, EnumRuneType.AIR);
			addRune(components, i, 4, -4, EnumRuneType.AIR);
			addRune(components, 4, 4, i, EnumRuneType.AIR);
			addRune(components, -4, 4, i, EnumRuneType.AIR);
		}

		addRune(components, 2, 4, 4, EnumRuneType.WATER);
		addRune(components, 4, 4, 2, EnumRuneType.WATER);
		addRune(components, 2, 4, -4, EnumRuneType.WATER);
		addRune(components, -4, 4, 2, EnumRuneType.WATER);
		addRune(components, -2, 4, 4, EnumRuneType.WATER);
		addRune(components, 4, 4, -2, EnumRuneType.WATER);
		addRune(components, -2, 4, -4, EnumRuneType.WATER);
		addRune(components, -4, 4, -2, EnumRuneType.WATER);
		addRune(components, 2, 4, 3, EnumRuneType.FIRE);
		addRune(components, 3, 4, 2, EnumRuneType.FIRE);
		addRune(components, 3, 4, 3, EnumRuneType.FIRE);
		addRune(components, -2, 4, 3, EnumRuneType.FIRE);
		addRune(components, 3, 4, -2, EnumRuneType.FIRE);
		addRune(components, 3, 4, -3, EnumRuneType.FIRE);
		addRune(components, 2, 4, -3, EnumRuneType.FIRE);
		addRune(components, -3, 4, 2, EnumRuneType.FIRE);
		addRune(components, -3, 4, 3, EnumRuneType.FIRE);
		addRune(components, -2, 4, -3, EnumRuneType.FIRE);
		addRune(components, -3, 4, -2, EnumRuneType.FIRE);
		addRune(components, -3, 4, -3, EnumRuneType.FIRE);
	}

	@Override
	public Ritual getNewCopy()
	{
		return new RitualMeteor();
	}
}

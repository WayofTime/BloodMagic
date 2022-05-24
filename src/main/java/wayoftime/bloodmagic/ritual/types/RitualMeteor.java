package wayoftime.bloodmagic.ritual.types;

import java.util.List;
import java.util.function.Consumer;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
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
		super("ritualMeteor", 0, 25000, "ritual." + BloodMagic.MODID + ".meteorRitual");
		addBlockRange(CHECK_RANGE, new AreaDescriptor.Rectangle(new BlockPos(0, 1, 0), 1, 1, 1));

		setMaximumVolumeAndDistanceOfRange(CHECK_RANGE, 27, 10, 10);
	}

	@Override
	public void performRitual(IMasterRitualStone masterRitualStone)
	{
		World world = masterRitualStone.getWorldObj();

		if (world.isRemote)
		{
			return;
		}

		AreaDescriptor itemRange = masterRitualStone.getBlockRange(CHECK_RANGE);

		List<ItemEntity> itemList = world.getEntitiesWithinAABB(ItemEntity.class, itemRange.getAABB(masterRitualStone.getMasterBlockPos()));

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

				EntityMeteor meteor = new EntityMeteor(world, masterRitualStone.getMasterBlockPos().getX() + 0.5, masterRitualStone.getMasterBlockPos().getY() + 30, masterRitualStone.getMasterBlockPos().getZ() + 0.5);
				meteor.setVelocity(0, -0.1, 0);
				meteor.setContainedStack(stack.split(1));
				world.addEntity(meteor);

				if (stack.isEmpty())
					entityItem.remove();

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
		addCornerRunes(components, 1, 0, EnumRuneType.DUSK);
		addCornerRunes(components, 2, 0, EnumRuneType.DUSK);
		addOffsetRunes(components, 1, 2, 0, EnumRuneType.DUSK);
		addCornerRunes(components, 1, 1, EnumRuneType.DUSK);
	}

	@Override
	public Ritual getNewCopy()
	{
		return new RitualMeteor();
	}
}

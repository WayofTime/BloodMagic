package wayoftime.bloodmagic.ritual.types;

import java.util.List;
import java.util.function.Consumer;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.core.living.LivingStats;
import wayoftime.bloodmagic.core.living.LivingUtil;
import wayoftime.bloodmagic.ritual.AreaDescriptor;
import wayoftime.bloodmagic.ritual.EnumRuneType;
import wayoftime.bloodmagic.ritual.IMasterRitualStone;
import wayoftime.bloodmagic.ritual.Ritual;
import wayoftime.bloodmagic.ritual.RitualComponent;
import wayoftime.bloodmagic.ritual.RitualRegister;

@RitualRegister("armour_evolve")
public class RitualArmourEvolve extends Ritual
{
	public static final String CHECK_RANGE = "fillRange";

	public RitualArmourEvolve()
	{
		super("ritualArmourEvolve", 0, 50000, "ritual." + BloodMagic.MODID + ".armourEvolveRitual");
		addBlockRange(CHECK_RANGE, new AreaDescriptor.Rectangle(new BlockPos(0, 1, 0), 1, 2, 1));
	}

	@Override
	public void performRitual(IMasterRitualStone masterRitualStone)
	{
		World world = masterRitualStone.getWorldObj();

		if (world.isRemote)
		{
			return;
		}

		BlockPos pos = masterRitualStone.getMasterBlockPos();

		AreaDescriptor checkRange = masterRitualStone.getBlockRange(CHECK_RANGE);

		List<PlayerEntity> playerList = world.getEntitiesWithinAABB(PlayerEntity.class, checkRange.getAABB(pos));

		for (PlayerEntity player : playerList)
		{
			if (LivingUtil.hasFullSet(player))
			{
				ItemStack chestStack = player.getItemStackFromSlot(EquipmentSlotType.CHEST);
				LivingStats stats = LivingStats.fromPlayer(player);

				if (stats != null && stats.getMaxPoints() < 300)
				{
					stats.setMaxPoints(300);
					LivingStats.toPlayer(player, stats);
//					((ItemLivingArmour) chestStack.getItem()).setLivingArmour(chestStack, armour, true);

					masterRitualStone.setActive(false);

					LightningBoltEntity lightningboltentity = EntityType.LIGHTNING_BOLT.create(world);
//						LightningBoltEntity lightning = new LightningBoltEntity(world, pos.getX() + dispX, pos.getY(), pos.getZ() + dispZ);
					lightningboltentity.setPosition(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
//					lightningboltentity.setEffectOnly(true);
					world.addEntity(lightningboltentity);
				}

			}
		}
	}

	@Override
	public int getRefreshTime()
	{
		return 1;
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
		addCornerRunes(components, 2, 0, EnumRuneType.FIRE);
		addOffsetRunes(components, 1, 2, 0, EnumRuneType.FIRE);
		addCornerRunes(components, 1, 1, EnumRuneType.DUSK);
		addParallelRunes(components, 4, 0, EnumRuneType.EARTH);
		addCornerRunes(components, 1, 3, EnumRuneType.DUSK);
		addParallelRunes(components, 1, 4, EnumRuneType.EARTH);

		for (int i = 0; i < 4; i++)
		{
			addCornerRunes(components, 3, i, EnumRuneType.EARTH);
		}
	}

	@Override
	public Ritual getNewCopy()
	{
		return new RitualArmourEvolve();
	}
}
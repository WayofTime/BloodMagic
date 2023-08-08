package wayoftime.bloodmagic.ritual.types;

import java.util.List;
import java.util.function.Consumer;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.registries.BloodMagicDamageTypes;
import wayoftime.bloodmagic.common.tile.TileAltar;
import wayoftime.bloodmagic.impl.BloodMagicAPI;
import wayoftime.bloodmagic.ritual.AreaDescriptor;
import wayoftime.bloodmagic.ritual.EnumRuneType;
import wayoftime.bloodmagic.ritual.IMasterRitualStone;
import wayoftime.bloodmagic.ritual.Ritual;
import wayoftime.bloodmagic.ritual.RitualComponent;
import wayoftime.bloodmagic.ritual.RitualRegister;

@RitualRegister("well_of_suffering")
public class RitualWellOfSuffering extends Ritual
{
	public static final String ALTAR_RANGE = "altar";
	public static final String DAMAGE_RANGE = "damage";

	public static final int SACRIFICE_AMOUNT = 25;

	public BlockPos altarOffsetPos = new BlockPos(0, 0, 0); // TODO: Save!

	public RitualWellOfSuffering()
	{
		super("ritualWellOfSuffering", 0, 40000, "ritual." + BloodMagic.MODID + ".wellOfSufferingRitual");
		addBlockRange(ALTAR_RANGE, new AreaDescriptor.Rectangle(new BlockPos(-5, -10, -5), 11, 21, 11));
		addBlockRange(DAMAGE_RANGE, new AreaDescriptor.Rectangle(new BlockPos(-10, -10, -10), 21));

		setMaximumVolumeAndDistanceOfRange(ALTAR_RANGE, 0, 10, 15);
		setMaximumVolumeAndDistanceOfRange(DAMAGE_RANGE, 0, 15, 15);
	}

	@Override
	public void performRitual(IMasterRitualStone masterRitualStone)
	{
		Level world = masterRitualStone.getWorldObj();
		int currentEssence = masterRitualStone.getOwnerNetwork().getCurrentEssence();

		if (currentEssence < getRefreshCost())
		{
			masterRitualStone.getOwnerNetwork().causeNausea();
			return;
		}

		BlockPos pos = masterRitualStone.getMasterBlockPos();

		int maxEffects = currentEssence / getRefreshCost();
		int totalEffects = 0;

		BlockPos altarPos = pos.offset(altarOffsetPos);

		BlockEntity tile = world.getBlockEntity(altarPos);

		AreaDescriptor altarRange = masterRitualStone.getBlockRange(ALTAR_RANGE);

		if (!altarRange.isWithinArea(altarOffsetPos) || !(tile instanceof TileAltar))
		{
			for (BlockPos newPos : altarRange.getContainedPositions(pos))
			{
				BlockEntity nextTile = world.getBlockEntity(newPos);
				if (nextTile instanceof TileAltar)
				{
					tile = nextTile;
					altarOffsetPos = newPos.subtract(pos);

					altarRange.resetCache();
					break;
				}
			}
		}

		if (tile instanceof TileAltar)
		{
			TileAltar tileAltar = (TileAltar) tile;

			AreaDescriptor damageRange = masterRitualStone.getBlockRange(DAMAGE_RANGE);
			AABB range = damageRange.getAABB(pos);

			List<LivingEntity> entities = world.getEntitiesOfClass(LivingEntity.class, range);

			for (LivingEntity entity : entities)
			{
//				EntityEntry entityEntry = ForgeRegistries.ENTITIES.getKe;
				ResourceLocation id = ForgeRegistries.ENTITY_TYPES.getKey(entity.getType());

				if (BloodMagicAPI.INSTANCE.getBlacklist().getSacrifice().contains(id))
					continue;

				int lifeEssenceRatio = BloodMagicAPI.INSTANCE.getValueManager().getSacrificial().getOrDefault(id, SACRIFICE_AMOUNT);

				if (lifeEssenceRatio <= 0)
					continue;

				if (entity.isAlive() && !(entity instanceof Player))
				{
					if (entity.hurt(entity.damageSources().source(BloodMagicDamageTypes.RITUAL), 1))
					{
						if (entity.isBaby())
							lifeEssenceRatio *= 0.5F;

						tileAltar.sacrificialDaggerCall(lifeEssenceRatio, true);

						totalEffects++;

						if (totalEffects >= maxEffects)
						{
							break;
						}
					}
				}
			}
		}

		masterRitualStone.getOwnerNetwork().syphon(masterRitualStone.ticket(getRefreshCost() * totalEffects));
	}

	@Override
	public int getRefreshTime()
	{
		return 25;
	}

	@Override
	public int getRefreshCost()
	{
		return 2;
	}

	@Override
	public void gatherComponents(Consumer<RitualComponent> components)
	{
		addCornerRunes(components, 1, 0, EnumRuneType.FIRE);
		addCornerRunes(components, 2, -1, EnumRuneType.FIRE);
		addParallelRunes(components, 2, -1, EnumRuneType.EARTH);
		addCornerRunes(components, -3, -1, EnumRuneType.DUSK);
		addOffsetRunes(components, 2, 4, -1, EnumRuneType.WATER);
		addOffsetRunes(components, 1, 4, 0, EnumRuneType.WATER);
		addParallelRunes(components, 4, 1, EnumRuneType.AIR);
	}

	@Override
	public Ritual getNewCopy()
	{
		return new RitualWellOfSuffering();
	}
}
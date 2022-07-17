package wayoftime.bloodmagic.ritual.types;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

import com.google.common.collect.Lists;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.ConfigManager;
import wayoftime.bloodmagic.common.block.BlockInversionPillarEnd;
import wayoftime.bloodmagic.common.block.BloodMagicBlocks;
import wayoftime.bloodmagic.common.block.type.PillarCapType;
import wayoftime.bloodmagic.common.dimension.DungeonDimensionHelper;
import wayoftime.bloodmagic.common.item.ItemActivationCrystal;
import wayoftime.bloodmagic.common.tile.TileInversionPillar;
import wayoftime.bloodmagic.ritual.EnumRuneType;
import wayoftime.bloodmagic.ritual.IMasterRitualStone;
import wayoftime.bloodmagic.ritual.Ritual;
import wayoftime.bloodmagic.ritual.RitualComponent;
import wayoftime.bloodmagic.ritual.RitualRegister;
import wayoftime.bloodmagic.structures.DungeonSynthesizer;
import wayoftime.bloodmagic.util.helper.NetworkHelper;

@RitualRegister("standard_dungeon")
public class RitualStandardDungeon extends Ritual
{
	public RitualStandardDungeon()
	{
		super("ritualStandardDungeon", 0, 150000, "ritual." + BloodMagic.MODID + ".standardDungeonRitual");
	}

	public boolean activateRitual(IMasterRitualStone masterRitualStone, Player player, UUID owner)
	{
		if (ConfigManager.COMMON.makeDungeonRitualCreativeOnly.get())
		{
			ItemStack heldStack = player.getUseItem();
			if (heldStack.getItem() instanceof ItemActivationCrystal)
			{
				int crystalLevel = ((ItemActivationCrystal) heldStack.getItem()).getCrystalLevel(heldStack);
				return crystalLevel == Integer.MAX_VALUE;
			}

			return false;
		}
		return true;
	}

	@Override
	public void performRitual(IMasterRitualStone masterRitualStone)
	{
		Level world = masterRitualStone.getWorldObj();
		int currentEssence = masterRitualStone.getOwnerNetwork().getCurrentEssence();
		BlockPos masterPos = masterRitualStone.getMasterBlockPos();

		if (!world.isClientSide && world instanceof ServerLevel)
		{
//			DungeonDimensionHelper.test(world);
			ServerLevel dungeonWorld = DungeonDimensionHelper.getDungeonWorld(world);
			if (dungeonWorld != null)
			{
				List<RitualComponent> components = Lists.newArrayList();
				gatherComponents(components::add);

				for (RitualComponent component : components)
				{
					BlockPos newPos = masterPos.offset(component.getOffset(masterRitualStone.getDirection()));
					world.setBlockAndUpdate(newPos, Blocks.SMOOTH_STONE.defaultBlockState());
				}

				BlockPos dungeonSpawnLocation = NetworkHelper.getSpawnPositionOfDungeon();
				DungeonSynthesizer dungeon = new DungeonSynthesizer();
////			ResourceLocation initialType = new ResourceLocation("bloodmagic:room_pools/test_pool_1");
				ResourceLocation initialType = new ResourceLocation("bloodmagic:room_pools/entrances/standard_dungeon_entrances");
				BlockPos[] positions = dungeon.generateInitialRoom(initialType, world.random, dungeonWorld, dungeonSpawnLocation);

				BlockPos pillarPos = masterPos.relative(Direction.UP, 4);
				BlockPos safePlayerPosition = positions[0];

				BlockPos dungeonPortalPos = positions[1];
				BlockPos overworldPlayerPos = masterPos.relative(Direction.UP).relative(masterRitualStone.getDirection(), 2);

				spawnPortalPillar(world, dungeonWorld, pillarPos, safePlayerPosition);
				spawnPortalPillar(dungeonWorld, world, dungeonPortalPos, overworldPlayerPos);

				LightningBolt lightningboltentity = EntityType.LIGHTNING_BOLT.create(world);
//				LightningBoltEntity lightning = new LightningBoltEntity(world, pos.getX() + dispX, pos.getY(), pos.getZ() + dispZ);
				lightningboltentity.setPos(masterPos.getX(), masterPos.getY() + 1, masterPos.getZ());
				lightningboltentity.setVisualOnly(true);
				world.addFreshEntity(lightningboltentity);

				NetworkHelper.incrementDungeonCounter();

//				world.setBlockState(pillarPos, BloodMagicBlocks.INVERSION_PILLAR.get().getDefaultState());
//				TileEntity tile = world.getTileEntity(pillarPos);
//				if (tile instanceof TileInversionPillar)
//				{
//					TileInversionPillar tileInversion = (TileInversionPillar) tile;
//					tileInversion.setDestination(dungeonWorld, safePlayerPosition);
//				}
//
//				dungeonWorld.setBlockState(dungeonPortalPos, BloodMagicBlocks.INVERSION_PILLAR.get().getDefaultState());
//				tile = dungeonWorld.getTileEntity(dungeonPortalPos);
//				if (tile instanceof TileInversionPillar)
//				{
//					TileInversionPillar tileInversion = (TileInversionPillar) tile;
//					tileInversion.setDestination(world, overworldPlayerPos);
//				}
			}
			world.setBlockAndUpdate(masterPos, Blocks.AIR.defaultBlockState());
////			System.out.println("Test");
////			DungeonTester.testDungeonElementWithOutput((ServerWorld) world, player.getPosition());
//			DungeonSynthesizer dungeon = new DungeonSynthesizer();
////			ResourceLocation initialType = new ResourceLocation("bloodmagic:room_pools/test_pool_1");
//			ResourceLocation initialType = new ResourceLocation("bloodmagic:room_pools/entrances/mini_dungeon_entrances");
//			BlockPos safePlayerPosition = dungeon.generateInitialRoom(initialType, world.rand, (ServerWorld) world, masterPos);
//
//			AxisAlignedBB bb = new AxisAlignedBB(masterPos).expand(5, 5, 5);
//
//			List<PlayerEntity> players = world.getEntitiesWithinAABB(PlayerEntity.class, bb);
//
//			for (PlayerEntity player : players)
//			{
//				player.setPositionAndUpdate(safePlayerPosition.getX(), safePlayerPosition.getY(), safePlayerPosition.getZ());
//			}
		}
	}

	public void spawnPortalPillar(Level spawnWorld, Level destinationWorld, BlockPos pillarPos, BlockPos safePlayerPos)
	{
		spawnWorld.setBlockAndUpdate(pillarPos, BloodMagicBlocks.INVERSION_PILLAR.get().defaultBlockState());
		BlockEntity tile = spawnWorld.getBlockEntity(pillarPos);
		if (tile instanceof TileInversionPillar)
		{
			TileInversionPillar tileInversion = (TileInversionPillar) tile;
			tileInversion.setDestination(destinationWorld, safePlayerPos);
			spawnWorld.setBlockAndUpdate(pillarPos.relative(Direction.DOWN), BloodMagicBlocks.INVERSION_PILLAR_CAP.get().defaultBlockState().setValue(BlockInversionPillarEnd.TYPE, PillarCapType.BOTTOM));
			spawnWorld.setBlockAndUpdate(pillarPos.relative(Direction.UP), BloodMagicBlocks.INVERSION_PILLAR_CAP.get().defaultBlockState().setValue(BlockInversionPillarEnd.TYPE, PillarCapType.TOP));
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
//		addParallelRunes(components, 1, 4, EnumRuneType.AIR);
//		addParallelRunes(components, 2, 4, EnumRuneType.EARTH);
//		addCornerRunes(components, 1, 0, EnumRuneType.EARTH);
//		addParallelRunes(components, 2, 0, EnumRuneType.EARTH);
//		addCornerRunes(components, 2, 0, EnumRuneType.FIRE);
//		for (int i = -1; i <= 1; i++)
//		{
//			addRune(components, i, 0, 3, EnumRuneType.WATER);
//			addRune(components, i, 0, -3, EnumRuneType.WATER);
//			addRune(components, 3, 0, i, EnumRuneType.FIRE);
//			addRune(components, -3, 0, i, EnumRuneType.FIRE);
//		}
//
//		for (int j = 1; j <= 4; j++)
//		{
//			addRune(components, 0, j, 0, EnumRuneType.DUSK);
//		}

		addRune(components, 0, 0, 1, EnumRuneType.FIRE);
		addRune(components, -1, 0, 0, EnumRuneType.FIRE);
		addRune(components, 0, 0, -1, EnumRuneType.WATER);
		addRune(components, 1, 0, 0, EnumRuneType.WATER);
		addRune(components, -1, 0, 1, EnumRuneType.DUSK);
		addRune(components, 1, 0, -1, EnumRuneType.DUSK);

		addRune(components, 2, 0, 2, EnumRuneType.WATER);
		addRune(components, 2, 0, 1, EnumRuneType.WATER);
		addRune(components, 1, 0, 2, EnumRuneType.WATER);
		addRune(components, -2, 0, -2, EnumRuneType.FIRE);
		addRune(components, -2, 0, -1, EnumRuneType.FIRE);
		addRune(components, -1, 0, -2, EnumRuneType.FIRE);

		addRune(components, -3, 0, 3, EnumRuneType.AIR);
		addRune(components, -2, 0, 3, EnumRuneType.AIR);
		addRune(components, -1, 0, 3, EnumRuneType.AIR);
		addRune(components, -3, 0, 2, EnumRuneType.AIR);
		addRune(components, -3, 0, 1, EnumRuneType.AIR);
		addRune(components, 3, 0, -3, EnumRuneType.EARTH);
		addRune(components, 2, 0, -3, EnumRuneType.EARTH);
		addRune(components, 1, 0, -3, EnumRuneType.EARTH);
		addRune(components, 3, 0, -2, EnumRuneType.EARTH);
		addRune(components, 3, 0, -1, EnumRuneType.EARTH);

		addRune(components, 3, 0, 3, EnumRuneType.DUSK);
		addRune(components, 3, 0, 4, EnumRuneType.DUSK);
		addRune(components, 4, 0, 3, EnumRuneType.DUSK);
		addRune(components, 2, 0, 4, EnumRuneType.DUSK);
		addRune(components, 4, 0, 2, EnumRuneType.DUSK);
		addRune(components, -3, 0, -3, EnumRuneType.DUSK);
		addRune(components, -3, 0, -4, EnumRuneType.DUSK);
		addRune(components, -4, 0, -3, EnumRuneType.DUSK);
		addRune(components, -2, 0, -4, EnumRuneType.DUSK);
		addRune(components, -4, 0, -2, EnumRuneType.DUSK);

		addCornerRunes(components, 3, 4, EnumRuneType.EARTH);
		addOffsetRunes(components, 2, 3, 4, EnumRuneType.DUSK);
		addOffsetRunes(components, 2, 4, 4, EnumRuneType.DUSK);
		addOffsetRunes(components, 1, 4, 4, EnumRuneType.AIR);
		addParallelRunes(components, 4, 4, EnumRuneType.AIR);

		addRune(components, 0, 2, 0, EnumRuneType.EARTH);
		addRune(components, 0, 3, 0, EnumRuneType.DUSK);
		addRune(components, 0, 4, 0, EnumRuneType.DUSK);
		addRune(components, 0, 5, 0, EnumRuneType.DUSK);
		addRune(components, 0, 6, 0, EnumRuneType.AIR);
	}

	@Override
	public Ritual getNewCopy()
	{
		return new RitualStandardDungeon();
	}
}

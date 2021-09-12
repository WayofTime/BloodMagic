package wayoftime.bloodmagic.ritual.types;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.ConfigManager;
import wayoftime.bloodmagic.common.item.ItemActivationCrystal;
import wayoftime.bloodmagic.ritual.EnumRuneType;
import wayoftime.bloodmagic.ritual.IMasterRitualStone;
import wayoftime.bloodmagic.ritual.Ritual;
import wayoftime.bloodmagic.ritual.RitualComponent;
import wayoftime.bloodmagic.ritual.RitualRegister;
import wayoftime.bloodmagic.structures.DungeonSynthesizer;

@RitualRegister("simple_dungeon")
public class RitualSimpleDungeon extends Ritual
{

	public RitualSimpleDungeon()
	{
		super("ritualSimpleDungeon", 10, 80000, "ritual." + BloodMagic.MODID + ".simpleDungeonRitual");
	}

	public boolean activateRitual(IMasterRitualStone masterRitualStone, PlayerEntity player, UUID owner)
	{
		if (ConfigManager.COMMON.makeDungeonRitualCreativeOnly.get())
		{
			ItemStack heldStack = player.getActiveItemStack();
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
		World world = masterRitualStone.getWorldObj();
		int currentEssence = masterRitualStone.getOwnerNetwork().getCurrentEssence();
		BlockPos masterPos = masterRitualStone.getMasterBlockPos();

		if (!world.isRemote && world instanceof ServerWorld)
		{
//			System.out.println("Test");
//			DungeonTester.testDungeonElementWithOutput((ServerWorld) world, player.getPosition());
			DungeonSynthesizer dungeon = new DungeonSynthesizer();
//			ResourceLocation initialType = new ResourceLocation("bloodmagic:room_pools/test_pool_1");
			ResourceLocation initialType = new ResourceLocation("bloodmagic:room_pools/entrances/mini_dungeon_entrances");
			BlockPos safePlayerPosition = dungeon.generateInitialRoom(initialType, world.rand, (ServerWorld) world, masterPos);

			AxisAlignedBB bb = new AxisAlignedBB(masterPos).expand(5, 5, 5);

			List<PlayerEntity> players = world.getEntitiesWithinAABB(PlayerEntity.class, bb);

			for (PlayerEntity player : players)
			{
				player.setPositionAndUpdate(safePlayerPosition.getX(), safePlayerPosition.getY(), safePlayerPosition.getZ());
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
		addParallelRunes(components, 1, 0, EnumRuneType.AIR);
		addCornerRunes(components, 1, 0, EnumRuneType.EARTH);
		addParallelRunes(components, 2, 0, EnumRuneType.EARTH);
		addCornerRunes(components, 2, 0, EnumRuneType.FIRE);
		for (int i = -1; i <= 1; i++)
		{
			addRune(components, i, 0, 3, EnumRuneType.WATER);
			addRune(components, i, 0, -3, EnumRuneType.WATER);
			addRune(components, 3, 0, i, EnumRuneType.FIRE);
			addRune(components, -3, 0, i, EnumRuneType.FIRE);
		}
	}

	@Override
	public Ritual getNewCopy()
	{
		return new RitualSimpleDungeon();
	}
}

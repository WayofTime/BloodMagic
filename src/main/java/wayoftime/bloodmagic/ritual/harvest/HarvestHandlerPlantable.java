package wayoftime.bloodmagic.ritual.harvest;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CropsBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;
import wayoftime.bloodmagic.util.BMLog;

/**
 * Harvest handler for standard plantable crops such as Wheat, Potatoes, and
 * Netherwart. <br>
 * Register a new crop for this handler with
 * {@link HarvestRegistry#registerStandardCrop(Block, int)}
 */

public class HarvestHandlerPlantable implements IHarvestHandler
{
	private static final ItemStack mockHoe = new ItemStack(Items.DIAMOND_HOE, 1);

	public HarvestHandlerPlantable()
	{
		HarvestRegistry.registerStandardCrop(Blocks.CARROTS, 7);
		HarvestRegistry.registerStandardCrop(Blocks.WHEAT, 7);
		HarvestRegistry.registerStandardCrop(Blocks.POTATOES, 7);
		HarvestRegistry.registerStandardCrop(Blocks.BEETROOTS, 3);
		HarvestRegistry.registerStandardCrop(Blocks.NETHER_WART, 3);

		addThirdPartyCrop("actuallyadditions", "flax_block", 7);
		addThirdPartyCrop("actuallyadditions", "canola_block", 7);
		addThirdPartyCrop("actuallyadditions", "rice_block", 7);

		addThirdPartyCrop("extrautils2", "redorchid", 6);
		addThirdPartyCrop("extrautils2", "enderlily", 7);

		addThirdPartyCrop("roots", "moonglow", 7);
		addThirdPartyCrop("roots", "terra_moss", 7);
		addThirdPartyCrop("roots", "pereskia", 7);
		addThirdPartyCrop("roots", "wildroot", 7);
		addThirdPartyCrop("roots", "aubergine", 7);
		addThirdPartyCrop("roots", "spirit_herb", 7);

		addPamCrops();
	}

	@Override
	public boolean harvest(World world, BlockPos pos, BlockState state, List<ItemStack> drops)
	{
//		NonNullList<ItemStack> blockDrops = NonNullList.create();
//		state.getBlock().getDrops(blockDrops, world, pos, state, 0);
		boolean foundSeed = false;
		LootContext.Builder lootBuilder = new LootContext.Builder((ServerWorld) world);
		Vector3d blockCenter = new Vector3d(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
		List<ItemStack> blockDrops = state.getDrops(lootBuilder.withParameter(LootParameters.field_237457_g_, blockCenter).withParameter(LootParameters.TOOL, mockHoe));

//		System.out.println("Size of list: " + blockDrops.size());

		for (ItemStack stack : blockDrops)
		{
			if (stack.isEmpty())
				continue;

			// This hurts my soul.
			if (stack.getItem() instanceof BlockItem && ((BlockItem) stack.getItem()).getBlock() == state.getBlock())
			{
				stack.shrink(1);
				foundSeed = true;
				break;
			}
		}

//		System.out.println("Found seed: " + foundSeed);

		if (foundSeed)
		{
			world.setBlockState(pos, state.getBlock().getDefaultState());
			world.playEvent(2001, pos, Block.getStateId(state));
			for (ItemStack stack : blockDrops)
			{
				if (stack.isEmpty())
					continue;

				drops.add(stack);
			}

			return true;
		}

		return false;
	}

	@Override
	public boolean test(World world, BlockPos pos, BlockState state)
	{
//		state.hasProperty(null);
		return HarvestRegistry.getStandardCrops().containsKey(state.getBlock()) && state.getBlock() instanceof CropsBlock && ((CropsBlock) state.getBlock()).isMaxAge(state);
//		return HarvestRegistry.getStandardCrops().containsKey(state.getBlock()) && state.getBlock().getMetaFromState(state) == HarvestRegistry.getStandardCrops().get(state.getBlock());
	}

	private static void addThirdPartyCrop(String modid, String regName, int matureMeta)
	{
		if (!ModList.get().isLoaded(modid))
			return;

		Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(modid, regName));
		if (block != null && block != Blocks.AIR)
			HarvestRegistry.registerStandardCrop(block, matureMeta);
	}

	private static void addPamCrops()
	{
		if (!ModList.get().isLoaded("harvestcraft"))
			return;

		try
		{
			Class<?> pamRegistry = Class.forName("com.pam.harvestcraft.blocks.CropRegistry");
			Field names = pamRegistry.getDeclaredField("cropNames");
			Method getCrop = pamRegistry.getMethod("getCrop", String.class);
			for (String name : (String[]) names.get(null))
			{
				CropsBlock crop = (CropsBlock) getCrop.invoke(null, name);
				HarvestRegistry.registerStandardCrop(crop, crop.getMaxAge());
			}
		} catch (ClassNotFoundException e)
		{
			BMLog.DEFAULT.error("HarvestCraft integration cancelled; unable to find registry class");
		} catch (NoSuchMethodException | NoSuchFieldException e)
		{
			BMLog.DEFAULT.error("HarvestCraft integration cancelled; unable to find crop name mapper");
		} catch (IllegalAccessException | InvocationTargetException e)
		{
			BMLog.DEFAULT.error("HarvestCraft integration cancelled; crop name lookup broke");
		}
	}
}

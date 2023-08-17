package wayoftime.bloodmagic.ritual.harvest;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;
import wayoftime.bloodmagic.common.block.BloodMagicBlocks;
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
		HarvestRegistry.registerStandardCrop(BloodMagicBlocks.GROWING_DOUBT.get(), 7);
		HarvestRegistry.registerStandardCrop(BloodMagicBlocks.WEAK_TAU.get(), 7);
		HarvestRegistry.registerStandardCrop(BloodMagicBlocks.STRONG_TAU.get(), 7);

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

		addMysticalCrops();
	}

	@Override
	public boolean harvest(Level world, BlockPos pos, BlockState state, List<ItemStack> drops)
	{
//		NonNullList<ItemStack> blockDrops = NonNullList.create();
//		state.getBlock().getDrops(blockDrops, world, pos, state, 0);
		boolean foundSeed = false;
		LootParams.Builder lootBuilder = new LootParams.Builder((ServerLevel) world);
		Vec3 blockCenter = new Vec3(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
		List<ItemStack> blockDrops = state.getDrops(lootBuilder.withParameter(LootContextParams.ORIGIN, blockCenter).withParameter(LootContextParams.TOOL, mockHoe));

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
			world.setBlockAndUpdate(pos, state.getBlock().defaultBlockState());
			world.levelEvent(2001, pos, Block.getId(state));
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
	public boolean test(Level world, BlockPos pos, BlockState state)
	{
//		state.hasProperty(null);
		return HarvestRegistry.getStandardCrops().containsKey(state.getBlock()) && state.getBlock() instanceof CropBlock && ((CropBlock) state.getBlock()).isMaxAge(state);
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
				CropBlock crop = (CropBlock) getCrop.invoke(null, name);
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

	private static void addMysticalCrops()
	{
		if (!ModList.get().isLoaded("mysticalagriculture"))
			return;

		try
		{
			Class<?> mysticalAPI = Class.forName("com.blakebr0.mysticalagriculture.api.MysticalAgricultureAPI");
			Method getRegistry = mysticalAPI.getMethod("getCropRegistry");
			Object registry = getRegistry.invoke(null);

			Class<?> mysticalRegistry = Class.forName("com.blakebr0.mysticalagriculture.api.registry.ICropRegistry");
			Method getCrops = mysticalRegistry.getMethod("getCrops");
			@SuppressWarnings("unchecked")
			List<Object> crops = (List<Object>) getCrops.invoke(registry);

			Class<?> mysticalCrop = Class.forName("com.blakebr0.mysticalagriculture.api.crop.ICrop");
			Method getCrop = mysticalCrop.getMethod("getCrop");

			for (Object maCrop : crops)
			{
				CropBlock crop = (CropBlock) getCrop.invoke(maCrop);
				HarvestRegistry.registerStandardCrop(crop, crop.getMaxAge());
			}
		} catch (ClassNotFoundException e)
		{
			BMLog.DEFAULT.error("MysticalAgriculture integration cancelled: unable to find a class: " + e.getMessage());
		} catch (NoSuchMethodException e)
		{
			BMLog.DEFAULT.error("MysticalAgriculture integration cancelled: unable to find a method: " + e.getMessage());
		} catch (IllegalAccessException | InvocationTargetException e)
		{
			BMLog.DEFAULT.error("MysticalAgriculture integration cancelled: failed to invoke a method: " + e.getMessage());
		}
	}
}

package wayoftime.bloodmagic.impl;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.GrassBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import wayoftime.bloodmagic.altar.ComponentType;
import wayoftime.bloodmagic.api.IBloodMagicAPI;
import wayoftime.bloodmagic.common.block.BloodMagicBlocks;
import wayoftime.bloodmagic.common.fluid.BloodMagicFluids;
import wayoftime.bloodmagic.incense.EnumTranquilityType;
import wayoftime.bloodmagic.incense.IncenseTranquilityRegistry;
import wayoftime.bloodmagic.incense.TranquilityStack;

public class BloodMagicCorePlugin
{
	public static final BloodMagicCorePlugin INSTANCE = new BloodMagicCorePlugin();

	public void register(IBloodMagicAPI apiInterface)
	{
		BloodMagicAPI api = (BloodMagicAPI) apiInterface;
		api.getValueManager().setTranquility(Blocks.LAVA, new TranquilityStack(EnumTranquilityType.LAVA, 1.2D));
//        api.getValueManager().setTranquility(Blocks.FLOWING_LAVA, new TranquilityStack(EnumTranquilityType.LAVA, 1.2D));
		api.getValueManager().setTranquility(Blocks.WATER, new TranquilityStack(EnumTranquilityType.WATER, 1.0D));
//        api.getValueManager().setTranquility(Blocks.water, new TranquilityStack(EnumTranquilityType.WATER, 1.0D));
		api.getValueManager().setTranquility(BloodMagicFluids.LIFE_ESSENCE_BLOCK.get(), new TranquilityStack(EnumTranquilityType.WATER, 1.5D));
		api.getValueManager().setTranquility(Blocks.NETHERRACK, new TranquilityStack(EnumTranquilityType.FIRE, 0.5D));
		api.getValueManager().setTranquility(Blocks.DIRT, new TranquilityStack(EnumTranquilityType.EARTHEN, 0.25D));
		api.getValueManager().setTranquility(Blocks.FARMLAND, new TranquilityStack(EnumTranquilityType.EARTHEN, 1.0D));
		api.getValueManager().setTranquility(Blocks.POTATOES, new TranquilityStack(EnumTranquilityType.CROP, 1.0D));
		api.getValueManager().setTranquility(Blocks.CARROTS, new TranquilityStack(EnumTranquilityType.CROP, 1.0D));
		api.getValueManager().setTranquility(Blocks.WHEAT, new TranquilityStack(EnumTranquilityType.CROP, 1.0D));
		api.getValueManager().setTranquility(Blocks.NETHER_WART, new TranquilityStack(EnumTranquilityType.CROP, 1.0D));
		api.getValueManager().setTranquility(Blocks.BEETROOTS, new TranquilityStack(EnumTranquilityType.CROP, 1.0D));

		//Added some blocks to the list at Tara's suggestion.
		api.getValueManager().setTranquility(Blocks.CRIMSON_NYLIUM, new TranquilityStack(EnumTranquilityType.FIRE, 0.75D));
		api.getValueManager().setTranquility(Blocks.WARPED_NYLIUM, new TranquilityStack(EnumTranquilityType.FIRE, 0.75D));
		api.getValueManager().setTranquility(Blocks.NETHER_WART_BLOCK, new TranquilityStack(EnumTranquilityType.PLANT, 1.0D));
		api.getValueManager().setTranquility(Blocks.WARPED_WART_BLOCK, new TranquilityStack(EnumTranquilityType.PLANT, 1.0D));
		api.getValueManager().setTranquility(Blocks.SOUL_SAND, new TranquilityStack(EnumTranquilityType.EARTHEN, 0.75D));
		api.getValueManager().setTranquility(Blocks.SOUL_SOIL, new TranquilityStack(EnumTranquilityType.EARTHEN, 0.75D));
		api.getValueManager().setTranquility(Blocks.SOUL_FIRE, new TranquilityStack(EnumTranquilityType.FIRE, 1.2D));
		api.getValueManager().setTranquility(Blocks.VINE, new TranquilityStack(EnumTranquilityType.PLANT, 0.25D));
		api.getValueManager().setTranquility(Blocks.WEEPING_VINES, new TranquilityStack(EnumTranquilityType.PLANT, 0.5D));
		api.getValueManager().setTranquility(Blocks.TWISTING_VINES, new TranquilityStack(EnumTranquilityType.PLANT, 0.5D));
		api.getValueManager().setTranquility(Blocks.CANDLE, new TranquilityStack(EnumTranquilityType.FIRE, 0.2D));
		api.getValueManager().setTranquility(Blocks.WHITE_CANDLE, new TranquilityStack(EnumTranquilityType.FIRE, 0.2D));
		api.getValueManager().setTranquility(Blocks.ORANGE_CANDLE, new TranquilityStack(EnumTranquilityType.FIRE, 0.2D));
		api.getValueManager().setTranquility(Blocks.MAGENTA_CANDLE, new TranquilityStack(EnumTranquilityType.FIRE, 0.2D));
		api.getValueManager().setTranquility(Blocks.LIGHT_BLUE_CANDLE, new TranquilityStack(EnumTranquilityType.FIRE, 0.2D));
		api.getValueManager().setTranquility(Blocks.YELLOW_CANDLE, new TranquilityStack(EnumTranquilityType.FIRE, 0.2D));
		api.getValueManager().setTranquility(Blocks.LIME_CANDLE, new TranquilityStack(EnumTranquilityType.FIRE, 0.2D));
		api.getValueManager().setTranquility(Blocks.PINK_CANDLE, new TranquilityStack(EnumTranquilityType.FIRE, 0.2D));
		api.getValueManager().setTranquility(Blocks.GRAY_CANDLE, new TranquilityStack(EnumTranquilityType.FIRE, 0.2D));
		api.getValueManager().setTranquility(Blocks.LIGHT_GRAY_CANDLE, new TranquilityStack(EnumTranquilityType.FIRE, 0.2D));
		api.getValueManager().setTranquility(Blocks.CYAN_CANDLE, new TranquilityStack(EnumTranquilityType.FIRE, 0.2D));
		api.getValueManager().setTranquility(Blocks.PURPLE_CANDLE, new TranquilityStack(EnumTranquilityType.FIRE, 0.2D));
		api.getValueManager().setTranquility(Blocks.BLUE_CANDLE, new TranquilityStack(EnumTranquilityType.FIRE, 0.2D));
		api.getValueManager().setTranquility(Blocks.BROWN_CANDLE, new TranquilityStack(EnumTranquilityType.FIRE, 0.2D));
		api.getValueManager().setTranquility(Blocks.GREEN_CANDLE, new TranquilityStack(EnumTranquilityType.FIRE, 0.2D));
		api.getValueManager().setTranquility(Blocks.RED_CANDLE, new TranquilityStack(EnumTranquilityType.FIRE, 0.2D));
		api.getValueManager().setTranquility(Blocks.BLACK_CANDLE, new TranquilityStack(EnumTranquilityType.FIRE, 0.2D));

		apiInterface.registerTranquilityHandler(state -> state.getBlock() instanceof LeavesBlock, EnumTranquilityType.PLANT.name(), 1.0D);
		apiInterface.registerTranquilityHandler(state -> state.getBlock() instanceof FireBlock, EnumTranquilityType.FIRE.name(), 1.0D);
		apiInterface.registerTranquilityHandler(state -> state.getBlock() instanceof GrassBlock, EnumTranquilityType.EARTHEN.name(), 0.5D);

		apiInterface.registerTranquilityHandler(state -> state.is(BlockTags.LOGS), EnumTranquilityType.TREE.name(), 1.0D);
		apiInterface.registerTranquilityHandler(state -> state.getBlock() instanceof SimpleWaterloggedBlock && state.getFluidState().isSource(), EnumTranquilityType.WATER.name(), 1.0D);

		IncenseTranquilityRegistry.registerTranquilityHandler((world, pos, block, state) -> BloodMagicAPI.INSTANCE.getValueManager().getTranquility().get(state));

		apiInterface.registerAltarComponent(Blocks.GLOWSTONE.defaultBlockState(), ComponentType.GLOWSTONE.name());
		apiInterface.registerAltarComponent(Blocks.SEA_LANTERN.defaultBlockState(), ComponentType.GLOWSTONE.name());
		apiInterface.registerAltarComponent(Blocks.SHROOMLIGHT.defaultBlockState(), ComponentType.GLOWSTONE.name());

		apiInterface.registerAltarComponent(BloodMagicBlocks.BLOODSTONE.get().defaultBlockState(), ComponentType.BLOODSTONE.name());
		apiInterface.registerAltarComponent(BloodMagicBlocks.BLOODSTONE_BRICK.get().defaultBlockState(), ComponentType.BLOODSTONE.name());
//		apiInterface.registerAltarComponent(Blocks.BEACON.defaultBlockState(), ComponentType.BEACON.name());
		apiInterface.registerAltarComponent(BloodMagicBlocks.HELLFORGED_BLOCK.get().defaultBlockState(), ComponentType.BEACON.name());

		apiInterface.registerAltarComponent(BloodMagicBlocks.BLANK_RUNE.get().defaultBlockState(), ComponentType.BLOODRUNE.name());
		apiInterface.registerAltarComponent(BloodMagicBlocks.SPEED_RUNE.get().defaultBlockState(), ComponentType.BLOODRUNE.name());
		apiInterface.registerAltarComponent(BloodMagicBlocks.SACRIFICE_RUNE.get().defaultBlockState(), ComponentType.BLOODRUNE.name());
		apiInterface.registerAltarComponent(BloodMagicBlocks.SELF_SACRIFICE_RUNE.get().defaultBlockState(), ComponentType.BLOODRUNE.name());
		apiInterface.registerAltarComponent(BloodMagicBlocks.DISPLACEMENT_RUNE.get().defaultBlockState(), ComponentType.BLOODRUNE.name());
		apiInterface.registerAltarComponent(BloodMagicBlocks.CAPACITY_RUNE.get().defaultBlockState(), ComponentType.BLOODRUNE.name());
		apiInterface.registerAltarComponent(BloodMagicBlocks.AUGMENTED_CAPACITY_RUNE.get().defaultBlockState(), ComponentType.BLOODRUNE.name());
		apiInterface.registerAltarComponent(BloodMagicBlocks.ORB_RUNE.get().defaultBlockState(), ComponentType.BLOODRUNE.name());
		apiInterface.registerAltarComponent(BloodMagicBlocks.ACCELERATION_RUNE.get().defaultBlockState(), ComponentType.BLOODRUNE.name());
		apiInterface.registerAltarComponent(BloodMagicBlocks.CHARGING_RUNE.get().defaultBlockState(), ComponentType.BLOODRUNE.name());

		apiInterface.registerAltarComponent(BloodMagicBlocks.SPEED_RUNE_2.get().defaultBlockState(), ComponentType.BLOODRUNE.name());
		apiInterface.registerAltarComponent(BloodMagicBlocks.SACRIFICE_RUNE_2.get().defaultBlockState(), ComponentType.BLOODRUNE.name());
		apiInterface.registerAltarComponent(BloodMagicBlocks.SELF_SACRIFICE_RUNE_2.get().defaultBlockState(), ComponentType.BLOODRUNE.name());
		apiInterface.registerAltarComponent(BloodMagicBlocks.DISPLACEMENT_RUNE_2.get().defaultBlockState(), ComponentType.BLOODRUNE.name());
		apiInterface.registerAltarComponent(BloodMagicBlocks.CAPACITY_RUNE_2.get().defaultBlockState(), ComponentType.BLOODRUNE.name());
		apiInterface.registerAltarComponent(BloodMagicBlocks.AUGMENTED_CAPACITY_RUNE_2.get().defaultBlockState(), ComponentType.BLOODRUNE.name());
		apiInterface.registerAltarComponent(BloodMagicBlocks.ORB_RUNE_2.get().defaultBlockState(), ComponentType.BLOODRUNE.name());
		apiInterface.registerAltarComponent(BloodMagicBlocks.ACCELERATION_RUNE_2.get().defaultBlockState(), ComponentType.BLOODRUNE.name());
		apiInterface.registerAltarComponent(BloodMagicBlocks.CHARGING_RUNE_2.get().defaultBlockState(), ComponentType.BLOODRUNE.name());

		apiInterface.registerInventoryProvider("mainInventory", player -> player.getInventory().items);
		apiInterface.registerInventoryProvider("armorInventory", player -> player.getInventory().armor);
		apiInterface.registerInventoryProvider("offHandInventory", player -> player.getInventory().offhand);
	}
}

package wayoftime.bloodmagic.api.impl;

import net.minecraft.block.Blocks;
import wayoftime.bloodmagic.altar.ComponentType;
import wayoftime.bloodmagic.api.IBloodMagicAPI;
import wayoftime.bloodmagic.common.block.BloodMagicBlocks;
import wayoftime.bloodmagic.incense.EnumTranquilityType;
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
		api.getValueManager().setTranquility(BloodMagicBlocks.LIFE_ESSENCE_BLOCK.get(), new TranquilityStack(EnumTranquilityType.WATER, 1.5D));
		api.getValueManager().setTranquility(Blocks.NETHERRACK, new TranquilityStack(EnumTranquilityType.FIRE, 0.5D));
		api.getValueManager().setTranquility(Blocks.DIRT, new TranquilityStack(EnumTranquilityType.EARTHEN, 0.25D));
		api.getValueManager().setTranquility(Blocks.FARMLAND, new TranquilityStack(EnumTranquilityType.EARTHEN, 1.0D));
		api.getValueManager().setTranquility(Blocks.POTATOES, new TranquilityStack(EnumTranquilityType.CROP, 1.0D));
		api.getValueManager().setTranquility(Blocks.CARROTS, new TranquilityStack(EnumTranquilityType.CROP, 1.0D));
		api.getValueManager().setTranquility(Blocks.WHEAT, new TranquilityStack(EnumTranquilityType.CROP, 1.0D));
		api.getValueManager().setTranquility(Blocks.NETHER_WART, new TranquilityStack(EnumTranquilityType.CROP, 1.0D));
		api.getValueManager().setTranquility(Blocks.BEETROOTS, new TranquilityStack(EnumTranquilityType.CROP, 1.0D));

		apiInterface.registerAltarComponent(Blocks.GLOWSTONE.getDefaultState(), ComponentType.GLOWSTONE.name());
		apiInterface.registerAltarComponent(Blocks.SEA_LANTERN.getDefaultState(), ComponentType.GLOWSTONE.name());
		apiInterface.registerAltarComponent(BloodMagicBlocks.BLOODSTONE.get().getDefaultState(), ComponentType.BLOODSTONE.name());
		apiInterface.registerAltarComponent(Blocks.BEACON.getDefaultState(), ComponentType.BEACON.name());

		apiInterface.registerAltarComponent(BloodMagicBlocks.BLANK_RUNE.get().getDefaultState(), ComponentType.BLOODRUNE.name());
		apiInterface.registerAltarComponent(BloodMagicBlocks.SPEED_RUNE.get().getDefaultState(), ComponentType.BLOODRUNE.name());
		apiInterface.registerAltarComponent(BloodMagicBlocks.SACRIFICE_RUNE.get().getDefaultState(), ComponentType.BLOODRUNE.name());
		apiInterface.registerAltarComponent(BloodMagicBlocks.SELF_SACRIFICE_RUNE.get().getDefaultState(), ComponentType.BLOODRUNE.name());
		apiInterface.registerAltarComponent(BloodMagicBlocks.DISPLACEMENT_RUNE.get().getDefaultState(), ComponentType.BLOODRUNE.name());
		apiInterface.registerAltarComponent(BloodMagicBlocks.CAPACITY_RUNE.get().getDefaultState(), ComponentType.BLOODRUNE.name());
		apiInterface.registerAltarComponent(BloodMagicBlocks.AUGMENTED_CAPACITY_RUNE.get().getDefaultState(), ComponentType.BLOODRUNE.name());
		apiInterface.registerAltarComponent(BloodMagicBlocks.ORB_RUNE.get().getDefaultState(), ComponentType.BLOODRUNE.name());
		apiInterface.registerAltarComponent(BloodMagicBlocks.ACCELERATION_RUNE.get().getDefaultState(), ComponentType.BLOODRUNE.name());
		apiInterface.registerAltarComponent(BloodMagicBlocks.CHARGING_RUNE.get().getDefaultState(), ComponentType.BLOODRUNE.name());
	}
}

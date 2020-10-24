package wayoftime.bloodmagic.api.impl;

import net.minecraft.block.Blocks;
import wayoftime.bloodmagic.altar.ComponentType;
import wayoftime.bloodmagic.api.IBloodMagicAPI;
import wayoftime.bloodmagic.common.block.BloodMagicBlocks;

public class BloodMagicCorePlugin
{
	public static final BloodMagicCorePlugin INSTANCE = new BloodMagicCorePlugin();

	public void register(IBloodMagicAPI apiInterface)
	{
		apiInterface.registerAltarComponent(Blocks.GLOWSTONE.getDefaultState(), ComponentType.GLOWSTONE.name());
		apiInterface.registerAltarComponent(Blocks.SEA_LANTERN.getDefaultState(), ComponentType.GLOWSTONE.name());
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

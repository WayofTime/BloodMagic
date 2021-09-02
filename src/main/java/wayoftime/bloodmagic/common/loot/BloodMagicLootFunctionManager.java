package wayoftime.bloodmagic.common.loot;

import net.minecraft.loot.ILootSerializer;
import net.minecraft.loot.LootFunctionType;
import net.minecraft.loot.functions.ILootFunction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

public class BloodMagicLootFunctionManager
{
	public static LootFunctionType SET_WILL_FRACTION;
	public static LootFunctionType SET_WILL_RANGE;

	public static void register()
	{
		SET_WILL_FRACTION = registerLootFunction("bloodmagic:set_will_fraction", new SetWillFraction.Serializer());
		SET_WILL_RANGE = registerLootFunction("bloodmagic:set_will_range", new SetWillRange.Serializer());
	}

	private static LootFunctionType registerLootFunction(String p_237451_0_, ILootSerializer<? extends ILootFunction> p_237451_1_)
	{
		return Registry.register(Registry.LOOT_FUNCTION_TYPE, new ResourceLocation(p_237451_0_), new LootFunctionType(p_237451_1_));
	}
}

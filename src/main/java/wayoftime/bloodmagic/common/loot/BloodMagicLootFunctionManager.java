package wayoftime.bloodmagic.common.loot;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;

public class BloodMagicLootFunctionManager
{
	public static LootItemFunctionType SET_WILL_FRACTION;
	public static LootItemFunctionType SET_WILL_RANGE;
	public static LootItemFunctionType SET_LIVING_UPGRADE;

	public static void register()
	{
		SET_WILL_FRACTION = registerLootFunction("bloodmagic:set_will_fraction", new SetWillFraction.Serializer());
		SET_WILL_RANGE = registerLootFunction("bloodmagic:set_will_range", new SetWillRange.Serializer());
		SET_LIVING_UPGRADE = registerLootFunction("bloodmagic:set_living_upgrade", new SetLivingUpgrade.Serializer());
	}

	private static LootItemFunctionType registerLootFunction(String name, Serializer<? extends LootItemFunction> function)
	{
		return Registry.register(Registry.LOOT_FUNCTION_TYPE, new ResourceLocation(name), new LootItemFunctionType(function));
	}
}

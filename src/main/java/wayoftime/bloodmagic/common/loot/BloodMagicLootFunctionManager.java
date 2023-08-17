package wayoftime.bloodmagic.common.loot;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import wayoftime.bloodmagic.BloodMagic;

public class BloodMagicLootFunctionManager
{
	public static final DeferredRegister<LootItemFunctionType> LOOT_FUNCTIONS = DeferredRegister.create(Registries.LOOT_FUNCTION_TYPE, BloodMagic.MODID);

	public static RegistryObject<LootItemFunctionType> SET_WILL_FRACTION = LOOT_FUNCTIONS.register("set_will_fraction", () -> new LootItemFunctionType(new SetWillFraction.Serializer()));
	public static RegistryObject<LootItemFunctionType> SET_WILL_RANGE = LOOT_FUNCTIONS.register("set_will_range", () -> new LootItemFunctionType(new SetWillRange.Serializer()));;
	public static RegistryObject<LootItemFunctionType> SET_LIVING_UPGRADE = LOOT_FUNCTIONS.register("set_living_upgrade", () -> new LootItemFunctionType(new SetLivingUpgrade.Serializer()));;

//	public static void register()
//	{
//		SET_WILL_FRACTION = registerLootFunction("bloodmagic:set_will_fraction", new SetWillFraction.Serializer());
//		SET_WILL_RANGE = registerLootFunction("bloodmagic:set_will_range", new SetWillRange.Serializer());
//		SET_LIVING_UPGRADE = registerLootFunction("bloodmagic:set_living_upgrade", new SetLivingUpgrade.Serializer());
//	}
//
//	private static LootItemFunctionType registerLootFunction(String name, Serializer<? extends LootItemFunction> function)
//	{
//		return Registry.register(Registry.LOOT_FUNCTION_TYPE, new ResourceLocation(name), new LootItemFunctionType(function));
//	}
}

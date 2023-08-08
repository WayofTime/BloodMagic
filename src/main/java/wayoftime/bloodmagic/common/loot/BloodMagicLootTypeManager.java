package wayoftime.bloodmagic.common.loot;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import wayoftime.bloodmagic.BloodMagic;

public class BloodMagicLootTypeManager
{
	public static final DeferredRegister<LootPoolEntryType> ENTRY_TYPES = DeferredRegister.create(Registries.LOOT_POOL_ENTRY_TYPE, BloodMagic.MODID);
	public static final RegistryObject<LootPoolEntryType> LOOT_TABLE = ENTRY_TYPES.register("loot_table", () -> new LootPoolEntryType(new BMTableLootEntry.Serializer()));

//	public static LootPoolEntryType LOOT_TABLE;

//	public static void register()
//	{
//		LOOT_TABLE = register("loot_table", new BMTableLootEntry.Serializer());
//	}
//
//	private static LootPoolEntryType register(String name, Serializer<? extends LootPoolEntryContainer> serializer)
//	{
//		return Registry.register(Registry.LOOT_POOL_ENTRY_TYPE, BloodMagic.rl(name), new LootPoolEntryType(serializer));
//	}
}

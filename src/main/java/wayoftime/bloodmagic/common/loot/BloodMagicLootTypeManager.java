package wayoftime.bloodmagic.common.loot;

import net.minecraft.loot.ILootSerializer;
import net.minecraft.loot.LootEntry;
import net.minecraft.loot.LootPoolEntryType;
import net.minecraft.util.registry.Registry;
import wayoftime.bloodmagic.BloodMagic;

public class BloodMagicLootTypeManager
{
	public static LootPoolEntryType LOOT_TABLE;

	public static void register()
	{
		LOOT_TABLE = register("loot_table", new BMTableLootEntry.Serializer());
	}

	private static LootPoolEntryType register(String name, ILootSerializer<? extends LootEntry> serializer)
	{
		return Registry.register(Registry.LOOT_POOL_ENTRY_TYPE, BloodMagic.rl(name), new LootPoolEntryType(serializer));
	}
}

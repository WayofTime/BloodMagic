package wayoftime.bloodmagic.common.loot;

import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryType;
import net.minecraft.core.Registry;
import wayoftime.bloodmagic.BloodMagic;

public class BloodMagicLootTypeManager
{
	public static LootPoolEntryType LOOT_TABLE;

	public static void register()
	{
		LOOT_TABLE = register("loot_table", new BMTableLootEntry.Serializer());
	}

	private static LootPoolEntryType register(String name, Serializer<? extends LootPoolEntryContainer> serializer)
	{
		return Registry.register(Registry.LOOT_POOL_ENTRY_TYPE, BloodMagic.rl(name), new LootPoolEntryType(serializer));
	}
}

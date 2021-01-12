package wayoftime.bloodmagic.loot;

import net.minecraft.loot.LootConditionType;
import net.minecraft.loot.conditions.MatchTool;
import net.minecraft.util.registry.Registry;
import wayoftime.bloodmagic.BloodMagic;

public class BloodMagicLootConditions
{
	public static final LootConditionType INVERTED = Registry.register(Registry.LOOT_CONDITION_TYPE, BloodMagic.rl("testing"), new LootConditionType(new MatchTool.Serializer()));

	static
	{

	}
}

package wayoftime.bloodmagic.common.loot;

import java.util.function.Consumer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootPoolEntryType;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.StandaloneLootEntry;
import net.minecraft.loot.ValidationTracker;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.loot.functions.ILootFunction;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;

public class BMTableLootEntry extends StandaloneLootEntry
{
	private final ResourceLocation table;

	private BMTableLootEntry(ResourceLocation tableIn, int weightIn, int qualityIn, ILootCondition[] conditionsIn, ILootFunction[] functionsIn)
	{
		super(weightIn, qualityIn, conditionsIn, functionsIn);
		this.table = tableIn;
	}

	public LootPoolEntryType getType()
	{
		return BloodMagicLootTypeManager.LOOT_TABLE;
	}

	public void createItemStack(Consumer<ItemStack> stackConsumer, LootContext context)
	{
		LootTable loottable = context.getLootTable(this.table);
		loottable.getRandomItemsRaw(context, stackConsumer);
	}

	public void validate(ValidationTracker p_225579_1_)
	{
		if (p_225579_1_.hasVisitedTable(this.table))
		{
			p_225579_1_.reportProblem("Table " + this.table + " is recursively called");
		} else
		{
			super.validate(p_225579_1_);
			LootTable loottable = p_225579_1_.resolveLootTable(this.table);
//			if (loottable == null)
//			{
//				p_225579_1_.addProblem("Unknown loot table called " + this.table);
//			} else
//			{
//				loottable.validate(p_225579_1_.enterTable("->{" + this.table + "}", this.table));
//			}

		}
	}

	public static StandaloneLootEntry.Builder<?> builder(ResourceLocation tableIn)
	{
		return simpleBuilder((weight, quality, conditions, functions) -> {
			return new BMTableLootEntry(tableIn, weight, quality, conditions, functions);
		});
	}

	public static class Serializer extends StandaloneLootEntry.Serializer<BMTableLootEntry>
	{
		public void serializeCustom(JsonObject object, BMTableLootEntry context, JsonSerializationContext conditions)
		{
			super.serializeCustom(object, context, conditions);
			object.addProperty("name", context.table.toString());
		}

		protected BMTableLootEntry deserialize(JsonObject object, JsonDeserializationContext context, int weight, int quality, ILootCondition[] conditions, ILootFunction[] functions)
		{
			ResourceLocation resourcelocation = new ResourceLocation(JSONUtils.getAsString(object, "name"));
			return new BMTableLootEntry(resourcelocation, weight, quality, conditions, functions);
		}
	}
}

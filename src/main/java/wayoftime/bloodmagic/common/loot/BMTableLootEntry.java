package wayoftime.bloodmagic.common.loot;

import java.util.function.Consumer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryType;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class BMTableLootEntry extends LootPoolSingletonContainer
{
	private final ResourceLocation table;

	private BMTableLootEntry(ResourceLocation tableIn, int weightIn, int qualityIn, LootItemCondition[] conditionsIn, LootItemFunction[] functionsIn)
	{
		super(weightIn, qualityIn, conditionsIn, functionsIn);
		this.table = tableIn;
	}

	public LootPoolEntryType getType()
	{
		return BloodMagicLootTypeManager.LOOT_TABLE.get();
	}

	public void createItemStack(Consumer<ItemStack> stackConsumer, LootContext context)
	{
		context.addDynamicDrops(this.table, stackConsumer);
	}

//	public void validate(ValidationContext p_225579_1_)
//	{
//		if (p_225579_1_.hasVisitedTable(this.table))
//		{
//			p_225579_1_.reportProblem("Table " + this.table + " is recursively called");
//		} else
//		{
//			super.validate(p_225579_1_);
////			LootTable loottable = p_225579_1_.resolveLootTable(this.table);
////			if (loottable == null)
////			{
////				p_225579_1_.addProblem("Unknown loot table called " + this.table);
////			} else
////			{
////				loottable.validate(p_225579_1_.enterTable("->{" + this.table + "}", this.table));
////			}
//
//		}
//	}

	public static LootPoolSingletonContainer.Builder<?> builder(ResourceLocation tableIn)
	{
		return simpleBuilder((weight, quality, conditions, functions) -> {
			return new BMTableLootEntry(tableIn, weight, quality, conditions, functions);
		});
	}

	public static class Serializer extends LootPoolSingletonContainer.Serializer<BMTableLootEntry>
	{
		public void serializeCustom(JsonObject object, BMTableLootEntry context, JsonSerializationContext conditions)
		{
			super.serializeCustom(object, context, conditions);
			object.addProperty("name", context.table.toString());
		}

		protected BMTableLootEntry deserialize(JsonObject object, JsonDeserializationContext context, int weight, int quality, LootItemCondition[] conditions, LootItemFunction[] functions)
		{
			ResourceLocation resourcelocation = new ResourceLocation(GsonHelper.getAsString(object, "name"));
			return new BMTableLootEntry(resourcelocation, weight, quality, conditions, functions);
		}
	}
}

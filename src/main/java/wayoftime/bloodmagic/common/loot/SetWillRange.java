package wayoftime.bloodmagic.common.loot;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootFunction;
import net.minecraft.loot.LootFunctionType;
import net.minecraft.loot.RandomValueRange;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.JSONUtils;
import wayoftime.bloodmagic.api.compat.IDemonWill;

public class SetWillRange extends LootFunction
{
	private static final Logger LOGGER = LogManager.getLogger();
	private final RandomValueRange damageRange;

	private SetWillRange(ILootCondition[] conditionsIn, RandomValueRange damageRangeIn)
	{
		super(conditionsIn);
		this.damageRange = damageRangeIn;
	}

	public LootFunctionType getType()
	{
		return BloodMagicLootFunctionManager.SET_WILL_RANGE;
	}

	public ItemStack run(ItemStack stack, LootContext context)
	{
		if (stack.getItem() instanceof IDemonWill)
		{
			float f = this.damageRange.getFloat(context.getRandom());
			return ((IDemonWill) stack.getItem()).createWill(f);
		} else
		{
			LOGGER.warn("Couldn't set will of loot item {}", (Object) stack);
		}

		return stack;
	}

	public static LootFunction.Builder<?> withRange(RandomValueRange p_215931_0_)
	{
		return simpleBuilder((p_215930_1_) -> {
			return new SetWillRange(p_215930_1_, p_215931_0_);
		});
	}

	public static class Serializer extends LootFunction.Serializer<SetWillRange>
	{
		public void serialize(JsonObject p_230424_1_, SetWillRange p_230424_2_, JsonSerializationContext p_230424_3_)
		{
			super.serialize(p_230424_1_, p_230424_2_, p_230424_3_);
			p_230424_1_.add("damage", p_230424_3_.serialize(p_230424_2_.damageRange));
		}

		public SetWillRange deserialize(JsonObject object, JsonDeserializationContext deserializationContext, ILootCondition[] conditionsIn)
		{
			return new SetWillRange(conditionsIn, JSONUtils.getAsObject(object, "damage", deserializationContext, RandomValueRange.class));
		}
	}
}

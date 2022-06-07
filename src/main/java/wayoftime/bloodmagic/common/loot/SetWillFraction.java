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
import wayoftime.bloodmagic.api.compat.EnumDemonWillType;
import wayoftime.bloodmagic.api.compat.IDemonWillGem;

public class SetWillFraction extends LootFunction
{
	private static final Logger LOGGER = LogManager.getLogger();
	private final RandomValueRange damageRange;

	private SetWillFraction(ILootCondition[] conditionsIn, RandomValueRange damageRangeIn)
	{
		super(conditionsIn);
		this.damageRange = damageRangeIn;
	}

	public LootFunctionType getFunctionType()
	{
		return BloodMagicLootFunctionManager.SET_WILL_FRACTION;
	}

	public ItemStack doApply(ItemStack stack, LootContext context)
	{
		if (stack.getItem() instanceof IDemonWillGem)
		{
			int maxWill = ((IDemonWillGem) stack.getItem()).getMaxWill(EnumDemonWillType.DEFAULT, stack);
			float f = 1.0F - this.damageRange.generateFloat(context.getRandom());
			((IDemonWillGem) stack.getItem()).setWill(EnumDemonWillType.DEFAULT, stack, maxWill * f);
		} else
		{
			LOGGER.warn("Couldn't set will of loot item {}", (Object) stack);
		}

		return stack;
	}

	public static LootFunction.Builder<?> withRange(RandomValueRange p_215931_0_)
	{
		return builder((p_215930_1_) -> {
			return new SetWillFraction(p_215930_1_, p_215931_0_);
		});
	}

	public static class Serializer extends LootFunction.Serializer<SetWillFraction>
	{
		public void serialize(JsonObject p_230424_1_, SetWillFraction p_230424_2_, JsonSerializationContext p_230424_3_)
		{
			super.serialize(p_230424_1_, p_230424_2_, p_230424_3_);
			p_230424_1_.add("damage", p_230424_3_.serialize(p_230424_2_.damageRange));
		}

		public SetWillFraction deserialize(JsonObject object, JsonDeserializationContext deserializationContext, ILootCondition[] conditionsIn)
		{
			return new SetWillFraction(conditionsIn, JSONUtils.deserializeClass(object, "damage", deserializationContext, RandomValueRange.class));
		}
	}
}

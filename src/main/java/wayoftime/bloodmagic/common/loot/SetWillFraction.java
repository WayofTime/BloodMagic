package wayoftime.bloodmagic.common.loot;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import wayoftime.bloodmagic.api.compat.EnumDemonWillType;
import wayoftime.bloodmagic.api.compat.IDemonWillGem;

public class SetWillFraction extends LootItemConditionalFunction
{
	private static final Logger LOGGER = LogManager.getLogger();
	private final UniformGenerator damageRange;

	private SetWillFraction(LootItemCondition[] conditionsIn, UniformGenerator damageRangeIn)
	{
		super(conditionsIn);
		this.damageRange = damageRangeIn;
	}

	public LootItemFunctionType getType()
	{
		return BloodMagicLootFunctionManager.SET_WILL_FRACTION.get();
	}

	public ItemStack run(ItemStack stack, LootContext context)
	{
		if (stack.getItem() instanceof IDemonWillGem)
		{
			int maxWill = ((IDemonWillGem) stack.getItem()).getMaxWill(EnumDemonWillType.DEFAULT, stack);
			float f = 1.0F - this.damageRange.getFloat(context);
			((IDemonWillGem) stack.getItem()).setWill(EnumDemonWillType.DEFAULT, stack, maxWill * f);
		} else
		{
			LOGGER.warn("Couldn't set will of loot item {}", (Object) stack);
		}

		return stack;
	}

	public static LootItemConditionalFunction.Builder<?> withRange(UniformGenerator p_215931_0_)
	{
		return simpleBuilder((p_215930_1_) -> {
			return new SetWillFraction(p_215930_1_, p_215931_0_);
		});
	}

	public static class Serializer extends LootItemConditionalFunction.Serializer<SetWillFraction>
	{
		public void serialize(JsonObject p_230424_1_, SetWillFraction p_230424_2_, JsonSerializationContext p_230424_3_)
		{
			super.serialize(p_230424_1_, p_230424_2_, p_230424_3_);
			p_230424_1_.add("damage", p_230424_3_.serialize(p_230424_2_.damageRange));
		}

		public SetWillFraction deserialize(JsonObject object, JsonDeserializationContext deserializationContext, LootItemCondition[] conditionsIn)
		{
			return new SetWillFraction(conditionsIn, GsonHelper.getAsObject(object, "damage", deserializationContext, UniformGenerator.class));
		}
	}
}

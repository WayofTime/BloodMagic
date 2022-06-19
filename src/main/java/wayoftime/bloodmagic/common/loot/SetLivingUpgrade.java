package wayoftime.bloodmagic.common.loot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import wayoftime.bloodmagic.core.living.ILivingContainer;
import wayoftime.bloodmagic.core.living.LivingStats;
import wayoftime.bloodmagic.util.Constants;

public class SetLivingUpgrade extends LootItemConditionalFunction
{
	private static final Logger LOGGER = LogManager.getLogger();
	private final UniformGenerator pointsRange;
	private final List<ResourceLocation> livingUpgrades;

	private SetLivingUpgrade(LootItemCondition[] conditionsIn, List<ResourceLocation> livingUpgrades, UniformGenerator damageRangeIn)
	{
		super(conditionsIn);
		this.pointsRange = damageRangeIn;
		this.livingUpgrades = livingUpgrades;
	}

	public LootItemFunctionType getType()
	{
		return BloodMagicLootFunctionManager.SET_LIVING_UPGRADE.get();
	}

	public ItemStack run(ItemStack stack, LootContext context)
	{
		if (stack.getItem() instanceof ILivingContainer)
		{
			Collections.shuffle(livingUpgrades);
			ResourceLocation upgrade = livingUpgrades.get(0);
			float points = pointsRange.getFloat(context);
			LivingStats stats = new LivingStats();
			stats.addExperience(upgrade, points);
			((ILivingContainer) stack.getItem()).updateLivingStats(stack, stats);
		} else
		{
			LOGGER.warn("Couldn't set will of loot item {}", (Object) stack);
		}

		return stack;
	}

	public static LootItemConditionalFunction.Builder<?> withRange(UniformGenerator p_215931_0_, ResourceLocation... livingUpgrades)
	{
		return simpleBuilder((p_215930_1_) -> {
			List<ResourceLocation> list = new ArrayList<>();
			for (ResourceLocation resource : livingUpgrades)
			{
				list.add(resource);
			}
			return new SetLivingUpgrade(p_215930_1_, list, p_215931_0_);
		});
	}

	public static class Serializer extends LootItemConditionalFunction.Serializer<SetLivingUpgrade>
	{
		public void serialize(JsonObject json, SetLivingUpgrade p_230424_2_, JsonSerializationContext context)
		{
			super.serialize(json, p_230424_2_, context);
			if (p_230424_2_.livingUpgrades.size() > 0)
			{
				JsonArray mainArray = new JsonArray();
				for (ResourceLocation ing : p_230424_2_.livingUpgrades)
				{
					mainArray.add(ing.toString());
				}

				json.add(Constants.JSON.UPGRADES, mainArray);
			}
			json.add("points", context.serialize(p_230424_2_.pointsRange));
		}

		public SetLivingUpgrade deserialize(JsonObject json, JsonDeserializationContext deserializationContext, LootItemCondition[] conditionsIn)
		{
			List<ResourceLocation> inputList = new ArrayList<ResourceLocation>();

			if (json.has(Constants.JSON.UPGRADES) && GsonHelper.isArrayNode(json, Constants.JSON.UPGRADES))
			{
				JsonArray mainArray = GsonHelper.getAsJsonArray(json, Constants.JSON.UPGRADES);

				for (JsonElement element : mainArray)
				{
					String resource = element.getAsString();
					inputList.add(new ResourceLocation(resource));
				}
			}

			return new SetLivingUpgrade(conditionsIn, inputList, GsonHelper.getAsObject(json, "points", deserializationContext, UniformGenerator.class));
		}
	}
}

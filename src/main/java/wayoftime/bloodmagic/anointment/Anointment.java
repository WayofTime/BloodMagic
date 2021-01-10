package wayoftime.bloodmagic.anointment;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.reflect.TypeToken;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.JsonAdapter;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;
import wayoftime.bloodmagic.core.living.LivingStats;
import wayoftime.bloodmagic.core.living.LivingUpgrade;
import wayoftime.bloodmagic.core.living.LivingUpgrade.Level;

@JsonAdapter(Anointment.Deserializer.class)
public class Anointment extends ForgeRegistryEntry<Anointment>
{
	public static final Anointment DUMMY = new Anointment(new ResourceLocation("dummy"));

	private final ResourceLocation key;
//	private final Set<ResourceLocation> incompatible;
	private final Map<String, Bonus> bonuses;
	private IAttributeProvider attributeProvider;
	private IDamageProvider damageProvider;

	public Anointment(ResourceLocation key)
	{
		this.key = key;
		this.bonuses = Maps.newHashMap();
	}

	public Anointment withBonusSet(String id, Consumer<List<Number>> modifiers)
	{
//		List<Number> values = DefaultedList.of();
		List<Number> values = new ArrayList<Number>();
		modifiers.accept(values);

		bonuses.put(id, new Bonus(id, values));
		return this;
	}

	public Number getBonusValue(String id, int level)
	{
		List<Number> modifiers = bonuses.getOrDefault(id, Bonus.DEFAULT).modifiers;
		if (modifiers.isEmpty() || level == 0)
			return 0;

		return modifiers.get(level - 1);
	}

	public ResourceLocation getKey()
	{
		return key;
	}

	@Override
	public String toString()
	{
		return key.toString();
	}

	public Anointment withAttributeProvider(IAttributeProvider attributeProvider)
	{
		this.attributeProvider = attributeProvider;
		return this;
	}

	public IAttributeProvider getAttributeProvider()
	{
		return attributeProvider;
	}

	public Anointment withDamageProvider(IDamageProvider damageProvider)
	{
		this.damageProvider = damageProvider;
		return this;
	}

	public IDamageProvider getDamageProvider()
	{
		return damageProvider;
	}

	public interface IAttributeProvider
	{
		void handleAttributes(AnointmentHolder holder, Multimap<Attribute, AttributeModifier> modifiers, UUID uuid, Anointment anoint, int level);
	}

	public interface IDamageProvider
	{
		double getAdditionalDamage(PlayerEntity player, ItemStack weapon, double damage, LivingStats stats, LivingEntity attacked, LivingUpgrade upgrade, int level);
	}

	public static class Bonus
	{
		private static final Bonus DEFAULT = new Bonus("null", Collections.emptyList());

		private final String id;
		private final List<Number> modifiers;

		public Bonus(String id, List<Number> modifiers)
		{
			this.id = id;
			this.modifiers = modifiers;
		}

		public String getId()
		{
			return id;
		}
	}

	public static class Deserializer implements JsonDeserializer<Anointment>
	{
		@Override
		public Anointment deserialize(JsonElement element, Type typeOfT, JsonDeserializationContext context)
				throws JsonParseException
		{
			JsonObject json = element.getAsJsonObject();
			ResourceLocation id = new ResourceLocation(json.getAsJsonPrimitive("id").getAsString());
			List<Level> levels = context.deserialize(json.getAsJsonArray("levels"), new TypeToken<List<Level>>()
			{
			}.getType());
			boolean negative = json.has("negative") && json.getAsJsonPrimitive("negative").getAsBoolean();

			Anointment upgrade = new Anointment(id);
//			if (negative)
//				upgrade.asDowngrade();

//			if (json.has("incompatibilities"))
//			{
//				String[] incompatibilities = context.deserialize(json.getAsJsonArray("incompatibilities"), String[].class);
//				for (String incompatible : incompatibilities)
//					upgrade.addIncompatibility(new ResourceLocation(incompatible));
//			}

			if (json.has("bonuses"))
			{
				Map<String, Number[]> bonuses = context.deserialize(json.getAsJsonObject("bonuses"), new TypeToken<Map<String, Number[]>>()
				{
				}.getType());
				bonuses.forEach((k, v) -> upgrade.withBonusSet(k, numbers -> Collections.addAll(numbers, v)));
			}

			return upgrade;
		}
	}
}

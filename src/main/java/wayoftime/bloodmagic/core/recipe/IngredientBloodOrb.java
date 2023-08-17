package wayoftime.bloodmagic.core.recipe;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.IIngredientSerializer;
import net.minecraftforge.common.crafting.VanillaIngredientSerializer;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.item.BloodOrb;
import wayoftime.bloodmagic.core.registry.OrbRegistry;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

public class IngredientBloodOrb extends Ingredient
{
	public static final ResourceLocation NAME = new ResourceLocation(BloodMagic.MODID, "bloodorb");

	public final int orbTier;

	public static IngredientBloodOrb fromTier(int orbTier)
	{
		return new IngredientBloodOrb(orbTier);
	}

	public static IngredientBloodOrb fromOrb(BloodOrb orb)
	{
		return new IngredientBloodOrb(orb.getTier());
	}

	protected IngredientBloodOrb(int orbTier)
	{
		super(Stream.of(new ItemList(orbTier)));
		this.orbTier = orbTier;
	}

	public net.minecraftforge.common.crafting.IIngredientSerializer<? extends Ingredient> getSerializer()
	{
		return Serializer.INSTANCE;
	}

	private static class ItemList implements Value
	{
		private final int orbTier;

		public ItemList(int orbTier)
		{
			this.orbTier = orbTier;
		}

		@Override
		public Collection<ItemStack> getItems()
		{
			List<ItemStack> orbGet = OrbRegistry.getOrbsDownToTier(orbTier);

			return orbGet;
		}

		@Override
		public JsonObject serialize()
		{
			JsonObject object = new JsonObject();
			object.addProperty("type", NAME.toString());
			object.addProperty("orb_tier", orbTier);
			return object;
		}
	}

	public static class Serializer extends VanillaIngredientSerializer
	{
		public static final IIngredientSerializer<? extends Ingredient> INSTANCE = new Serializer();

		@Override
		public Ingredient parse(JsonObject json)
		{
			return new IngredientBloodOrb(GsonHelper.getAsInt(json, "orb_tier"));
		}
	}
}

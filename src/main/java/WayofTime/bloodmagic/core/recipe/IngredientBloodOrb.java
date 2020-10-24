package wayoftime.bloodmagic.core.recipe;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import com.google.gson.JsonObject;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.IIngredientSerializer;
import net.minecraftforge.common.crafting.VanillaIngredientSerializer;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.core.registry.OrbRegistry;
import wayoftime.bloodmagic.orb.BloodOrb;

public class IngredientBloodOrb extends Ingredient
{

//	private final BloodOrb orb;
//	private NonNullList<ItemStack> orbs;
//	private IntList itemIds = null;
//	private ItemStack[] items;

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

	private static class ItemList implements IItemList
	{
		private final int orbTier;

		public ItemList(int orbTier)
		{
			this.orbTier = orbTier;
		}

		@Override
		public Collection<ItemStack> getStacks()
		{
//			System.out.println("BING BONG");
			List<ItemStack> orbGet = OrbRegistry.getOrbsDownToTier(orbTier);
//			List<ItemStack> orbGet = new ArrayList<ItemStack>();
//			orbGet.add(new ItemStack(Items.DIAMOND));
//	        orbs = NonNullList.withSize(orbGet.size(), ItemStack.EMPTY);
//	
//	        for (int i = 0; i < orbGet.size(); i++)
//	            orbs.set(i, orbGet.get(i));
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
			System.out.println("Parsing Blood Orb");
			return new IngredientBloodOrb(JSONUtils.getInt(json, "orb_tier"));
		}
	}

//
//	@Override
//	public boolean test(@Nullable ItemStack input)
//	{
//		System.out.println("Testing");
//		if (input == null || input.isEmpty())
//			return false;
//
//		if (!(input.getItem() instanceof IBloodOrb))
//			return false;
//
//		BloodOrb orb = ((IBloodOrb) input.getItem()).getOrb(input);
//		return orb != null && orb.getTier() >= this.orbTier;
//	}

}

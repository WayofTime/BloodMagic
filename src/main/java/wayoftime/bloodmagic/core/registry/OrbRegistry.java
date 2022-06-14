package wayoftime.bloodmagic.core.registry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.common.collect.ArrayListMultimap;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import wayoftime.bloodmagic.altar.AltarTier;
import wayoftime.bloodmagic.common.item.BloodOrb;

/**
 * This is only for those who wish to add a basic {@link BloodOrb}. If you need
 * custom handling, you will need your own item class.
 */
public class OrbRegistry
{
	public static ArrayListMultimap<Integer, ItemStack> tierMap = ArrayListMultimap.create();
	private static List<BloodOrb> orbs = new ArrayList<>();

	public static List<ItemStack> getOrbsForTier(int tier)
	{
		if (getTierMap().containsKey(tier))
			return getTierMap().get(tier);

		return Collections.emptyList();
	}

	public static List<ItemStack> getOrbsUpToTier(int tier)
	{
		List<ItemStack> ret = new ArrayList<>();

		for (int i = 1; i <= tier; i++) ret.addAll(getOrbsForTier(i));

		return ret;
	}

	public static List<ItemStack> getOrbsDownToTier(int tier)
	{
		List<ItemStack> ret = new ArrayList<>();

		for (int i = AltarTier.MAXTIERS; i >= tier; i--) ret.addAll(getOrbsForTier(i));

		return ret;
	}

	public static ItemStack getOrbStack(BloodOrb orb)
	{
		Item orbItem = ForgeRegistries.ITEMS.getValue(orb.getResourceLocation());
		if (orbItem == null)
			return null;

		return new ItemStack(orbItem);
	}

	public static ArrayListMultimap<Integer, ItemStack> getTierMap()
	{
		return ArrayListMultimap.create(tierMap);
	}
}
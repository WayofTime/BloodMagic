package WayofTime.bloodmagic.api.registry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import WayofTime.bloodmagic.api.BloodMagicAPI;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.altar.EnumAltarTier;
import WayofTime.bloodmagic.api.orb.BloodOrb;
import WayofTime.bloodmagic.api.orb.IBloodOrb;

import com.google.common.collect.ArrayListMultimap;

/**
 * This is only for those who wish to add a basic {@link BloodOrb}. If you need
 * custom handling, you will need your own item class.
 */
public class OrbRegistry
{
    private static List<BloodOrb> orbs = new ArrayList<BloodOrb>();
    private static ArrayListMultimap<Integer, ItemStack> tierMap = ArrayListMultimap.create();

    private static Item orbItem = Constants.BloodMagicItem.BLOOD_ORB.getItem();

    public static void registerOrb(BloodOrb orb)
    {
        if (!orbs.contains(orb))
        {
            orbs.add(orb);
            registerOrbForTier(orb.getTier(), getOrbStack(orb));
        } else
            BloodMagicAPI.getLogger().error("Error adding orb %s. Orb already exists!", orb.toString());
    }

    public static void registerOrbForTier(int tier, ItemStack stack)
    {
        if (stack.getItem() instanceof IBloodOrb)
            tierMap.put(tier, stack);
    }

    @SideOnly(Side.CLIENT)
    public static void registerOrbTexture(BloodOrb orb, ResourceLocation resourceLocation)
    {
        int meta = getIndexOf(orb);

        ModelBakery.registerItemVariants(orbItem, resourceLocation);
        ModelLoader.setCustomModelResourceLocation(orbItem, meta, new ModelResourceLocation(resourceLocation, "inventory"));
    }

    public static BloodOrb getOrb(int index)
    {
        return orbs.get(index);
    }

    public static int getIndexOf(BloodOrb orb)
    {
        return orbs.indexOf(orb);
    }

    public static boolean isEmpty()
    {
        return orbs.isEmpty();
    }

    public static int getSize()
    {
        return orbs.size();
    }

    public static List<ItemStack> getOrbsForTier(int tier)
    {
        if (getTierMap().containsKey(tier))
            return getTierMap().get(tier);

        return Collections.emptyList();
    }

    public static List<ItemStack> getOrbsUpToTier(int tier)
    {
        List<ItemStack> ret = new ArrayList<ItemStack>();

        for (int i = 1; i <= tier; i++)
            ret.addAll(getOrbsForTier(i));

        return ret;
    }

    public static List<ItemStack> getOrbsDownToTier(int tier)
    {
        List<ItemStack> ret = new ArrayList<ItemStack>();

        for (int i = EnumAltarTier.MAXTIERS; i >= tier; i--)
            ret.addAll(getOrbsForTier(i));

        return ret;
    }

    public static ItemStack getOrbStack(BloodOrb orb)
    {
        return new ItemStack(orbItem, 1, getIndexOf(orb));
    }

    public static List<BloodOrb> getOrbs()
    {
        return new ArrayList<BloodOrb>(orbs);
    }

    public static ArrayListMultimap<Integer, ItemStack> getTierMap()
    {
        return ArrayListMultimap.create(tierMap);
    }
}

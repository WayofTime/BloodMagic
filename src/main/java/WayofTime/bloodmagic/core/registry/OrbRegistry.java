package WayofTime.bloodmagic.core.registry;

import WayofTime.bloodmagic.altar.AltarTier;
import WayofTime.bloodmagic.orb.BloodOrb;
import com.google.common.collect.ArrayListMultimap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This is only for those who wish to add a basic {@link BloodOrb}. If you need
 * custom handling, you will need your own item class.
 */
@Deprecated
public class OrbRegistry {
    @GameRegistry.ObjectHolder("bloodmagic:blood_orb")
    private static final Item ORB_ITEM = null;
    public static ArrayListMultimap<Integer, ItemStack> tierMap = ArrayListMultimap.create();
    private static List<BloodOrb> orbs = new ArrayList<>();

    public static List<ItemStack> getOrbsForTier(int tier) {
        if (getTierMap().containsKey(tier))
            return getTierMap().get(tier);

        return Collections.emptyList();
    }

    public static List<ItemStack> getOrbsUpToTier(int tier) {
        List<ItemStack> ret = new ArrayList<>();

        for (int i = 1; i <= tier; i++)
            ret.addAll(getOrbsForTier(i));

        return ret;
    }

    public static List<ItemStack> getOrbsDownToTier(int tier) {
        List<ItemStack> ret = new ArrayList<>();

        for (int i = AltarTier.MAXTIERS; i >= tier; i--)
            ret.addAll(getOrbsForTier(i));

        return ret;
    }

    public static ItemStack getOrbStack(BloodOrb orb) {
        ItemStack ret = new ItemStack(ORB_ITEM);
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("orb", orb.getRegistryName().toString());
        ret.setTagCompound(tag);
        return ret;
    }

    public static ArrayListMultimap<Integer, ItemStack> getTierMap() {
        return ArrayListMultimap.create(tierMap);
    }
}

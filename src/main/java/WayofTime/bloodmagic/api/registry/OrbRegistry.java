package WayofTime.bloodmagic.api.registry;

import WayofTime.bloodmagic.api.BloodMagicAPI;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.orb.BloodOrb;
import lombok.Getter;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

/**
 * This is only for those who wish to add a basic {@link BloodOrb}. If you need
 * custom handling, you will need your own item class.
 */
public class OrbRegistry
{
    @Getter
    private static List<BloodOrb> orbs = new ArrayList<BloodOrb>();

    private static Item orbItem = Constants.BloodMagicItem.BLOOD_ORB.getItem();

    public static void registerOrb(BloodOrb orb)
    {
        if (!orbs.contains(orb))
            orbs.add(orb);
        else
            BloodMagicAPI.getLogger().error("Error adding orb %s. Orb already exists!", orb.toString());
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

    public static ItemStack getOrbStack(BloodOrb orb)
    {
        return new ItemStack(orbItem, 1, getIndexOf(orb));
    }
}

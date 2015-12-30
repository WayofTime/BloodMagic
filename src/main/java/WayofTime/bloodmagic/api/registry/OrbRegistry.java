package WayofTime.bloodmagic.api.registry;

import WayofTime.bloodmagic.api.BloodMagicAPI;
import WayofTime.bloodmagic.api.orb.BloodOrb;
import lombok.Getter;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
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

    public static void registerOrb(BloodOrb orb)
    {
        if (!orbs.contains(orb))
            orbs.add(orb);
        else
            BloodMagicAPI.getLogger().error("Error adding orb %s. Orb already exists!", orb.toString());
    }

    @SideOnly(Side.CLIENT)
    public static void registerOrbTexture(BloodOrb orb, String resourceLocation)
    {
        int meta = getIndexOf(orb);

        ModelBakery.addVariantName(BloodMagicAPI.getItem(BloodMagicAPI.ORB), resourceLocation);
        ModelLoader.setCustomModelResourceLocation(BloodMagicAPI.getItem(BloodMagicAPI.ORB), meta, new ModelResourceLocation(resourceLocation, "inventory"));
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
        return new ItemStack(BloodMagicAPI.getItem(BloodMagicAPI.ORB), 1, getIndexOf(orb));
    }
}

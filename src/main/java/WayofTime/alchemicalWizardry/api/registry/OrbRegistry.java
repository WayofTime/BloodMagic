package WayofTime.alchemicalWizardry.api.registry;

import WayofTime.alchemicalWizardry.api.AlchemicalWizardryAPI;
import WayofTime.alchemicalWizardry.api.orb.BloodOrb;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

/**
 * This is only for those who wish to add a basic {@link BloodOrb}. If you need custom handling,
 * you will need your own item class.
 */
public class OrbRegistry {

    @Getter
    private static List<BloodOrb> orbs = new ArrayList<BloodOrb>();

    public static void registerOrb(BloodOrb orb) {
        if (!orbs.contains(orb))
            orbs.add(orb);
        else
            AlchemicalWizardryAPI.getLogger().error("Error adding orb: " + orb.toString() + ". Orb already exists!");
    }

    @SideOnly(Side.CLIENT)
    public static void registerOrbTexture(BloodOrb orb, String resourceLocation) {
        int meta = getIndexOf(orb);

        RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();

        ModelBakery.addVariantName(AlchemicalWizardryAPI.getOrbItem(), resourceLocation);
        renderItem.getItemModelMesher().register(AlchemicalWizardryAPI.getOrbItem(), meta, new ModelResourceLocation(resourceLocation, "inventory"));
    }

    public static BloodOrb getOrb(int index) {
        return orbs.get(index);
    }

    public static int getIndexOf(BloodOrb orb) {
        return orbs.indexOf(orb);
    }

    public static boolean isEmpty() {
        return orbs.isEmpty();
    }

    public static int getSize() {
        return orbs.size();
    }

    public static ItemStack getOrbStack(BloodOrb orb) {
        return new ItemStack(AlchemicalWizardryAPI.getOrbItem(), 1, getIndexOf(orb));
    }
}

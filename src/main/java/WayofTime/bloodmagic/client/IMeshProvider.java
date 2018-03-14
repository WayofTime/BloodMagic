package WayofTime.bloodmagic.client;

import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.function.Consumer;

/**
 * Provides a custom {@link ItemMeshDefinition} for automatic registration of
 * renders.
 */
public interface IMeshProvider {
    /**
     * Gets the custom ItemMeshDefinition to use for the item.
     *
     * @return - the custom ItemMeshDefinition to use for the item.
     */
    @SideOnly(Side.CLIENT)
    ItemMeshDefinition getMeshDefinition();

    /**
     * Gathers all possible variants for this item
     */
    void gatherVariants(Consumer<String> variants);

    /**
     * If a custom ResourceLocation is required, return it here.
     * <p>
     * Can be null if unneeded.
     *
     * @return - The custom ResourceLocation
     */
    @Nullable
    default ResourceLocation getCustomLocation() {
        return null;
    }
}

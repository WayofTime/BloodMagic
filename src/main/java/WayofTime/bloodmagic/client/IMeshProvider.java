package WayofTime.bloodmagic.client;

import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/**
 * Provides a custom {@link ItemMeshDefinition} for automatic registration of
 * renders.
 */
public interface IMeshProvider
{

    @SideOnly(Side.CLIENT)
    ItemMeshDefinition getMeshDefinition();

    List<String> getVariants();
}

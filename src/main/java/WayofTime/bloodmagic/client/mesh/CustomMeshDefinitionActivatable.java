package WayofTime.bloodmagic.client.mesh;

import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.iface.IActivatable;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;

public class CustomMeshDefinitionActivatable implements ItemMeshDefinition
{
    private final String name;

    public CustomMeshDefinitionActivatable(String name) {
        this.name = name;
    }

    @Override
    public ModelResourceLocation getModelLocation(ItemStack stack)
    {
        if (stack != null && stack.getItem() instanceof IActivatable)
        {
            if (!((IActivatable) stack.getItem()).getActivated(stack))
                return new ModelResourceLocation(Constants.Mod.DOMAIN + name + "0", "inventory");

            return new ModelResourceLocation(Constants.Mod.DOMAIN + name + "1", "inventory");
        }
        return null;
    }
}

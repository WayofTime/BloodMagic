package WayofTime.bloodmagic.client.mesh;

import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.iface.IActivatable;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class CustomMeshDefinitionActivatable implements ItemMeshDefinition
{
    private final String name;

    public CustomMeshDefinitionActivatable(String name)
    {
        this.name = name;
    }

    @Override
    public ModelResourceLocation getModelLocation(ItemStack stack)
    {
        if (stack != null && stack.getItem() instanceof IActivatable)
            if (((IActivatable) stack.getItem()).getActivated(stack))
                return new ModelResourceLocation(new ResourceLocation(Constants.Mod.MODID, "item/" + name), "active=true");

        return new ModelResourceLocation(new ResourceLocation(Constants.Mod.MODID, "item/" + name), "active=false");
    }
}

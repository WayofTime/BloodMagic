package WayofTime.bloodmagic.client.mesh;

import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import WayofTime.bloodmagic.item.soul.ItemSentientSword;

public class ItemSentientSwordMeshDefinition implements ItemMeshDefinition
{
    @Override
    public ModelResourceLocation getModelLocation(ItemStack stack)
    {
        if (stack != null && stack.getItem() instanceof ItemSentientSword)
        {
            if (((ItemSentientSword) stack.getItem()).getActivated(stack))
            {
                return new ModelResourceLocation("bloodmagic:ItemSentientSword1", "inventory");
            } else
            {
                return new ModelResourceLocation("bloodmagic:ItemSentientSword0", "inventory");
            }
        }
        return null;
    }
}

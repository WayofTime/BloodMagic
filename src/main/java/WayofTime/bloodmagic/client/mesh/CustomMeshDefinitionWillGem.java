package WayofTime.bloodmagic.client.mesh;

import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.soul.EnumDemonWillType;
import WayofTime.bloodmagic.item.soul.ItemSoulGem;
import WayofTime.bloodmagic.registry.ModItems;

public class CustomMeshDefinitionWillGem implements ItemMeshDefinition
{
    private final String name;

    public CustomMeshDefinitionWillGem(String name)
    {
        this.name = name;
    }

    @Override
    public ModelResourceLocation getModelLocation(ItemStack stack)
    {
        if (stack != null && stack.getItem() == ModItems.SOUL_GEM)
        {
            EnumDemonWillType type = ((ItemSoulGem) stack.getItem()).getCurrentType(stack);
            return new ModelResourceLocation(new ResourceLocation(Constants.Mod.MODID, "item/" + name), "type=" + ItemSoulGem.names[stack.getItemDamage()] + "_" + type.getName().toLowerCase());
        }

        return new ModelResourceLocation(new ResourceLocation(Constants.Mod.MODID, "item/" + name), "type=petty_default");
    }
}

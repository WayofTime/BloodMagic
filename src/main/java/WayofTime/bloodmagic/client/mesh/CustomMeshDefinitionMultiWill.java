package WayofTime.bloodmagic.client.mesh;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.iface.IMultiWillTool;
import WayofTime.bloodmagic.soul.EnumDemonWillType;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class CustomMeshDefinitionMultiWill implements ItemMeshDefinition {
    private final String name;

    public CustomMeshDefinitionMultiWill(String name) {
        this.name = name;
    }

    @Override
    public ModelResourceLocation getModelLocation(ItemStack stack) {
        if (!stack.isEmpty() && stack.getItem() instanceof IMultiWillTool) {
            EnumDemonWillType type = ((IMultiWillTool) stack.getItem()).getCurrentType(stack);
            return new ModelResourceLocation(new ResourceLocation(BloodMagic.MODID, name), "type=" + type.getName().toLowerCase());
        }

        return new ModelResourceLocation(new ResourceLocation(BloodMagic.MODID, name), "type=default");
    }
}

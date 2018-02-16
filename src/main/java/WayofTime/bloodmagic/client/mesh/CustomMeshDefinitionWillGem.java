package WayofTime.bloodmagic.client.mesh;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.soul.EnumDemonWillType;
import WayofTime.bloodmagic.core.RegistrarBloodMagicItems;
import WayofTime.bloodmagic.item.soul.ItemSoulGem;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class CustomMeshDefinitionWillGem implements ItemMeshDefinition {
    private final String name;

    public CustomMeshDefinitionWillGem(String name) {
        this.name = name;
    }

    @Override
    public ModelResourceLocation getModelLocation(ItemStack stack) {
        if (!stack.isEmpty() && stack.getItem() == RegistrarBloodMagicItems.SOUL_GEM) {
            EnumDemonWillType type = ((ItemSoulGem) stack.getItem()).getCurrentType(stack);
            return new ModelResourceLocation(new ResourceLocation(BloodMagic.MODID, name), "type=" + ItemSoulGem.names[stack.getItemDamage()] + "_" + type.getName().toLowerCase());
        }

        return new ModelResourceLocation(new ResourceLocation(BloodMagic.MODID, name), "type=petty_default");
    }
}

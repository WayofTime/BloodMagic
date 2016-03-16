package WayofTime.bloodmagic.util.helper;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;

public class InventoryRenderHelperV2
{

    private final String domain;

    public InventoryRenderHelperV2(String domain)
    {
        this.domain = domain.endsWith(":") ? domain.replace(":", "") : domain;
    }

    public void registerMesher(Item item, ItemMeshDefinition meshDefinition)
    {
        ModelLoader.setCustomMeshDefinition(item, meshDefinition);
    }

    public void registerRender(Item item, int meta, String name, String variant)
    {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(new ResourceLocation(domain, "item/" + name), "type=" + variant));
    }

    public void registerRender(Item item, int meta, String variant)
    {
        registerRender(item, meta, item.getRegistryName().split(":")[1], variant);
    }

    public void registerRender(Item item, String name, String variant)
    {
        registerRender(item, 0, name, variant);
    }

    public void registerRender(Item item, String variant)
    {
        registerRender(item, item.getRegistryName().split(":")[1], variant);
    }

    public void registerRender(Block block, int meta, String name, String variant)
    {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), meta, new ModelResourceLocation(new ResourceLocation(domain, name), variant));
    }

    public void registerRender(Block block, int meta, String variant)
    {
        registerRender(block, meta, block.getRegistryName().split(":")[1], variant);
    }

    public void registerRender(Block block, String name, String variant)
    {
        registerRender(block, 0, name, variant);
    }

    public void registerRender(Block block, String variant)
    {
        registerRender(block, block.getRegistryName().split(":")[1], variant);
    }
}

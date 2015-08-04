package WayofTime.alchemicalWizardry.client;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class InventoryRenderHelper {

    private static final String resourceBase = "alchemicalwizardry:";
    
    /**
     * Registers a Model for the given Item and meta.
     *
     * @param item - Item to register Model for
     * @param meta - Meta of Item
     * @param name - Name of the model JSON
     */
    public static void itemRender(Item item, int meta, String name) {
        if (item instanceof ItemBlock && name.startsWith("ItemBlock"))
            name = name.replace("Item", "");

        RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
        String resName = resourceBase + name;

        ModelBakery.addVariantName(item, resName);
        renderItem.getItemModelMesher().register(item, meta, new ModelResourceLocation(resName, "inventory"));
    }

    /**
     * Shorthand of {@code itemRender(Item, int, String)}
     *
     * @param item - Item to register Model for
     * @param meta - Meta of Item
     */
    public static void itemRender(Item item, int meta) {
        itemRender(item, meta, item.getClass().getSimpleName() + meta);
    }

    /**
     * Shorthand of {@code itemRender(Item, int)}
     *
     * @param item - Item to register Model for
     */
    public static void itemRender(Item item) {
        itemRender(item, 0, item.getClass().getSimpleName());
    }

    /**
     * Registers a model for the item across all Meta's that get used for the item
     *
     * @param item - Item to register Model for
     */
    public static void itemRenderAll(Item item) {
        RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
        final Item toRender = item;

        renderItem.getItemModelMesher().register(item, new ItemMeshDefinition() {
            @Override
            public ModelResourceLocation getModelLocation(ItemStack stack) {
                return new ModelResourceLocation(resourceBase + toRender.getClass().getSimpleName(), "inventory");
            }
        });
    }

    /**
     *
     * @param block - Block to get Item of
     * @return      - The ItemBlock that corresponds to the Block.
     */
    public static Item getItemFromBlock(Block block) {
        return Item.getItemFromBlock(block);
    }
}

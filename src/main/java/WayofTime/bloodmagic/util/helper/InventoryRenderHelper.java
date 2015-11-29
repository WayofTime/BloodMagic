package WayofTime.bloodmagic.util.helper;

import WayofTime.bloodmagic.api.Constants;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;

/**
 * @author <a href="https://github.com/TehNut">TehNut</a>
 *         <p/>
 *         The goal of this class is to make registering the inventory renders
 *         for your Items/Blocks a much simpler and easier process.
 *         <p/>
 *         You must call this at the post initialization stage on
 *         the clientside only.
 *         <p/>
 *         If you pass a Block through here that uses the default
 *         ItemBlock, you should specify a custom name.
 */
public class InventoryRenderHelper {

    /**
     * This is the base string for your resources. It will usually be
     * your modid in all lowercase with a colon at the end.
     */
    private final String domain;

    public InventoryRenderHelper(String domain) {
        this.domain = domain;
    }

    /**
     * Registers a Model for the given Item and meta.
     *
     * @param item - Item to register Model for
     * @param meta - Meta of Item
     * @param name - Name of the model JSON
     */
    public void itemRender(Item item, int meta, String name) {
        String resName = domain + name;

        ModelBakery.addVariantName(item, resName);
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(resName, "inventory"));
    }

    /**
     * Shorthand of {@code itemRender(Item, int, String)}
     *
     * @param item - Item to register Model for
     * @param meta - Meta of Item
     */
    public void itemRender(Item item, int meta) {
        itemRender(item, meta, getClassName(item) + meta);
    }

    public void itemRender(Item item, String name) {
        itemRender(item, 0, name);
    }

    /**
     * Shorthand of {@code itemRender(Item, int)}
     *
     * @param item - Item to register Model for
     */
    public void itemRender(Item item) {
        itemRender(item, 0, getClassName(item));
    }

    /**
     * Registers a model for the item across all Meta's that get used for the item
     *
     * @param item - Item to register Model for
     */
    public void itemRenderAll(Item item) {
        final Item toRender = item;

        ModelLoader.setCustomMeshDefinition(item, new ItemMeshDefinition() {
            @Override
            public ModelResourceLocation getModelLocation(ItemStack stack) {
                return new ModelResourceLocation(domain + getClassName(toRender), "inventory");
            }
        });
    }

    public void itemRenderToggle(Item item, String name) {
        itemRender(item, 0, name + "_deactivated");
        itemRender(item, 1, name + "_activated");
    }

    public void fluidRender(Block block) {

        final Block toRender = block;

        ModelBakery.addVariantName(InventoryRenderHelper.getItemFromBlock(block));
        ModelLoader.setCustomMeshDefinition(InventoryRenderHelper.getItemFromBlock(block), new ItemMeshDefinition() {
            @Override
            public ModelResourceLocation getModelLocation(ItemStack stack) {
                return new ModelResourceLocation(Constants.Mod.DOMAIN + toRender.getClass().getSimpleName(), "fluid");
            }
        });
        ModelLoader.setCustomStateMapper(block, new StateMapperBase() {
            @Override
            protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
                return new ModelResourceLocation(domain + toRender.getClass().getSimpleName(), "fluid");
            }
        });
    }

    /**
     * @param block - Block to get Item of
     *
     * @return - The ItemBlock that corresponds to the Block.
     */
    public static Item getItemFromBlock(Block block) {
        return Item.getItemFromBlock(block);
    }

    /**
     * Finds the class name of the given Item. If handed an ItemBlock, it will
     * use the class name of the contained Block.
     *
     * @return The class name of the given Item
     */
    private static String getClassName(Item item) {
        return item instanceof ItemBlock ? Block.getBlockFromItem(item).getClass().getSimpleName() : item.getClass().getSimpleName();
    }
}

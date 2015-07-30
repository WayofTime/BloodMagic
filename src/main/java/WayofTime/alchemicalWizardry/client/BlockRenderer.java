package WayofTime.alchemicalWizardry.client;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;

public class BlockRenderer
{
    public static void registerBlock(Block block)
    {
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(block), 0, new ModelResourceLocation("alchemicalwizardry:" + block.getUnlocalizedName().substring(5), "inventory"));
    }
    
    public static void registerBlocks()
    {
    	
    }
}

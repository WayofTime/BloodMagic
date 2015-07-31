package WayofTime.alchemicalWizardry.common.harvest;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.api.harvest.IHarvestHandler;
import cpw.mods.fml.common.registry.GameRegistry;

public class AgriCraftCropHarvestHandler implements IHarvestHandler
{
    public Block harvestBlock;
    public Method isMature;
    public Method harvest;

    public AgriCraftCropHarvestHandler()
    {
        this.harvestBlock = getBlockForString("AgriCraft:crops");
        if(this.harvestBlock != null)
        {
        	try {
				Class clazz = Class.forName("com.InfinityRaider.AgriCraft.blocks.BlockCrop");
				if(clazz != null)
				{
					isMature = clazz.getMethod("isMature", World.class, int.class, int.class, int.class);
					harvest = clazz.getMethod("harvest", World.class, int.class, int.class, int.class);
				}
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    }

    public boolean isHarvesterValid()
    {
        return harvestBlock != null && isMature != null && harvest != null;
    }

    public static Block getBlockForString(String str)
    {
        String[] parts = str.split(":");
        String modId = parts[0];
        String name = parts[1];
        return GameRegistry.findBlock(modId, name);
    }

    public boolean canHandleBlock(Block block)
    {
        return block == harvestBlock;
    }

    @Override
    public boolean harvestAndPlant(World world, int xCoord, int yCoord, int zCoord, Block block, int meta)
    {
        if (!this.canHandleBlock(block))
        {
            return false;
        }
        
        try {
			return (Boolean)(isMature.invoke(block, world, xCoord, yCoord, zCoord)) && (Boolean)(harvest.invoke(block, world, xCoord, yCoord, zCoord));
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        return false;
    }
}

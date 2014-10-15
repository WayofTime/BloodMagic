package WayofTime.alchemicalWizardry.common.harvest;

import WayofTime.alchemicalWizardry.api.harvest.IHarvestHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.world.World;

public class GenericPamSeedlessFruitHarvestHandler implements IHarvestHandler
{
    public Block harvestBlock;
    public int harvestMeta;
    public int resetMeta;

    public GenericPamSeedlessFruitHarvestHandler(String block, int harvestMeta, int resetMeta)
    {
        this.harvestBlock = getBlockForString(block);
        this.harvestMeta = harvestMeta;
        this.resetMeta = resetMeta;
    }

    public boolean isHarvesterValid()
    {
        return harvestBlock != null;
    }

    public static Block getBlockForString(String str)
    {
        String[] parts = str.split(":");
        String modId = parts[0];
        String name = parts[1];
        return GameRegistry.findBlock(modId, name);
    }

    public static Item getItemForString(String str)
    {
        String[] parts = str.split(":");
        String modId = parts[0];
        String name = parts[1];
        return GameRegistry.findItem(modId, name);
    }

    public boolean canHandleBlock(Block block)
    {
        return block == harvestBlock;
    }

    public int getHarvestMeta(Block block)
    {
        return harvestMeta;
    }

    @Override
    public boolean harvestAndPlant(World world, int xCoord, int yCoord, int zCoord, Block block, int meta)
    {
        if (!this.canHandleBlock(block) || meta != this.getHarvestMeta(block))
        {
            return false;
        }

        world.func_147480_a(xCoord, yCoord, zCoord, true);

        world.setBlock(xCoord, yCoord, zCoord, harvestBlock, resetMeta, 3);

        return true;
    }
}

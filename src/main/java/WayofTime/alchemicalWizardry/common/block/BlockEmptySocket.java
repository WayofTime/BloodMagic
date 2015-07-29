package WayofTime.alchemicalWizardry.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class BlockEmptySocket extends Block
{
    public BlockEmptySocket()
    {
        super(Material.iron);
        setHardness(2.0F);
        setResistance(5.0F);
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }
}

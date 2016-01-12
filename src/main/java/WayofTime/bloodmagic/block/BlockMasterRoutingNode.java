package WayofTime.bloodmagic.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.routing.TileMasterRoutingNode;

public class BlockMasterRoutingNode extends BlockContainer
{
    public BlockMasterRoutingNode()
    {
        super(Material.rock);

        setUnlocalizedName(Constants.Mod.MODID + ".masterRouting");
        setCreativeTab(BloodMagic.tabBloodMagic);
        setHardness(2.0F);
        setResistance(5.0F);
        setHarvestLevel("pickaxe", 2);
    }

    @Override
    public int getRenderType()
    {
        return 3;
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileMasterRoutingNode();
    }
}

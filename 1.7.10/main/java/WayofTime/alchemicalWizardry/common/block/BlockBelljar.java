package WayofTime.alchemicalWizardry.common.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.common.tileEntity.TEBellJar;

public class BlockBelljar extends BlockContainer
{
	public BlockBelljar() 
	{
		super(Material.glass);
		setHardness(2.0F);
        setResistance(5.0F);
		this.setCreativeTab(AlchemicalWizardry.tabBloodMagic);
		this.setBlockName("crystalBelljar");
	}	
	
	@Override
	public TileEntity createNewTileEntity(World world, int meta) 
	{
		return new TEBellJar();
	}
	
	@Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    @Override
    public int getRenderType()
    {
        return -1;
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public boolean hasTileEntity()
    {
        return true;
    }   
    
    @Override
    public boolean canProvidePower()
    {
        return true;
    }

    @Override
    public int isProvidingWeakPower(IBlockAccess world, int x, int y, int z, int meta)
    {
    	TileEntity tile = world.getTileEntity(x, y, z);
    	if(tile instanceof TEBellJar)
    	{
    		return ((TEBellJar) tile).getRSPowerOutput();
    	}
        return 15;
    }
}

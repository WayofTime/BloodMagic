package WayofTime.alchemicalWizardry.common.demonVillage.tileEntity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.AlchemicalWizardry;

public class BlockDemonChest extends BlockChest implements IBlockPortalNode, IRoadWard
{
	public BlockDemonChest() 
	{
		super(0);
		this.setHardness(2.5F).setStepSound(soundTypeWood).setBlockName("demonChest");
		this.setCreativeTab(AlchemicalWizardry.tabBloodMagic);
	}
	
	public IInventory func_149951_m(World world, int x, int y, int z)
    {
		IInventory object = (IInventory)world.getTileEntity(x, y, z);
        
        return object;
    }
	
	@Override
    public TileEntity createNewTileEntity(World var1, int var2)
    {
        return new TEDemonChest();
    }
	
	@Override
	public void breakBlock(World world, int x, int y, int z, Block block, int meta)
	{
		TileEntity tile = world.getTileEntity(x, y, z);
		if(tile instanceof TEDemonChest)
		{
			((TEDemonChest) tile).notifyPortalOfInteraction();
		}
		super.breakBlock(world, x, y, z, block, meta);
	}
	
	@Override
	public boolean canPlaceBlockAt(World p_149742_1_, int p_149742_2_, int p_149742_3_, int p_149742_4_)
	{
		return true;
	}
}

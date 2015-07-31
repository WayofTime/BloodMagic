package WayofTime.alchemicalWizardry.common.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.common.demonVillage.tileEntity.TEDemonPortal;

public class BlockDemonPortal extends BlockContainer
{
    public BlockDemonPortal()
    {
        super(Material.rock);
        setHardness(1000);
        setResistance(10000);
        setCreativeTab(AlchemicalWizardry.tabBloodMagic);
        this.setBlockName("demonPortal");
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        this.blockIcon = iconRegister.registerIcon("AlchemicalWizardry:DemonPortal");
    }
    
    @Override
    public void onBlockHarvested(World world, int x, int y, int z, int meta, EntityPlayer player)
    {
    	TileEntity tile = world.getTileEntity(x, y, z);
		if(tile instanceof TEDemonPortal)
		{
			((TEDemonPortal) tile).notifyPortalOfBreak();
		}
		
    	super.onBlockHarvested(world, x, y, z, meta, player);
    }

    @Override
    public TileEntity createNewTileEntity(World var1, int var2)
    {
        return new TEDemonPortal();
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float what, float these, float are)
    {
        if (world.isRemote)
        {
            return false;
        }

        TEDemonPortal tileEntity = (TEDemonPortal) world.getTileEntity(x, y, z);

        tileEntity.rightClickBlock(player, side);

        return false;
    }
}

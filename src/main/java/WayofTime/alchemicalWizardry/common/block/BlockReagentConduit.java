package WayofTime.alchemicalWizardry.common.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.common.tileEntity.TEReagentConduit;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockReagentConduit extends BlockContainer
{
    public BlockReagentConduit()
    {
        super(Material.cloth);
        setHardness(2.0F);
        setResistance(5.0F);
        this.setBlockName("blockReagentConduit");
        this.setCreativeTab(AlchemicalWizardry.tabBloodMagic);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        this.blockIcon = iconRegister.registerIcon("AlchemicalWizardry:SimpleTransCircle");
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        return new TEReagentConduit();
    }

    @Override
    public boolean canProvidePower()
    {
        return true;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float what, float these, float are)
    {
        return super.onBlockActivated(world, x, y, z, player, side, what, these, are);
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
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

//    @Override
//    public AxisAlignedBB getCollisionBoundingBoxFromPool(World p_149668_1_, int p_149668_2_, int p_149668_3_, int p_149668_4_)
//    {
//        return null;
//    }
}

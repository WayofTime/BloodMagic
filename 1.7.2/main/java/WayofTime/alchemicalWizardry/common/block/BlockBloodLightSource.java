package WayofTime.alchemicalWizardry.common.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockBloodLightSource extends Block
{
    public BlockBloodLightSource()
    {
        super(Material.cloth);
        //setCreativeTab(AlchemicalWizardry.tabBloodMagic);
        this.setBlockName("blockBloodLightSource");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        this.blockIcon = iconRegister.registerIcon("AlchemicalWizardry:BlockBloodLight");
    }

    @Override
    public int getLightValue(IBlockAccess world, int x, int y, int z)
    {
        return 15;
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
    public void randomDisplayTick(World world, int x, int y, int z, Random rand)
    {
        if (rand.nextInt(3) != 0)
        {
            float f = (float) 1.0F;
            float f1 = f * 0.6F + 0.4F;
            float f2 = f * f * 0.7F - 0.5F;
            float f3 = f * f * 0.6F - 0.7F;
            world.spawnParticle("reddust", x + 0.5D + rand.nextGaussian() / 8, y + 0.5D, z + 0.5D + rand.nextGaussian() / 8, f1, f2, f3);
        }
    }

    @Override

    public void addCollisionBoxesToList(World par1World, int par2, int par3, int par4, AxisAlignedBB par5AxisAlignedBB, List par6List, Entity par7Entity)
    {
        this.setBlockBounds(0.40F, 0.40F, 0.40F, 0.60F, 0.60F, 0.60F);
        //super.addCollisionBoxesToList(par1World, par2, par3, par4, par5AxisAlignedBB, par6List, par7Entity);
    }

    public int quantityDropped(Random par1Random)
    {
        return 0;
    }
}

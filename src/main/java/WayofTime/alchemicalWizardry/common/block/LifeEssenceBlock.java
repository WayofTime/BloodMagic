package WayofTime.alchemicalWizardry.common.block;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;

public class LifeEssenceBlock extends BlockFluidClassic
{
    public LifeEssenceBlock()
    {
        super(AlchemicalWizardry.lifeEssenceFluid, Material.water);
        AlchemicalWizardry.lifeEssenceFluid.setBlock(this);
        this.setBlockName("lifeEssenceFluidBlock");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta)
    {
        return this.blockIcon;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        this.blockIcon = iconRegister.registerIcon("AlchemicalWizardry:lifeEssenceStill");
        AlchemicalWizardry.lifeEssenceFluid.setFlowingIcon(blockIcon);
        AlchemicalWizardry.lifeEssenceFluid.setStillIcon(blockIcon);
    }

    @Override
    public boolean canDisplace(IBlockAccess world, int x, int y, int z)
    {
        return !world.getBlock(x, y, z).getMaterial().isLiquid() && super.canDisplace(world, x, y, z);
    }

    @Override
    public boolean displaceIfPossible(World world, int x, int y, int z)
    {
        return !world.getBlock(x, y, z).getMaterial().isLiquid() && super.displaceIfPossible(world, x, y, z);
    }
}

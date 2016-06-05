package WayofTime.bloodmagic.block;

import java.awt.Color;

import lombok.Getter;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import WayofTime.bloodmagic.api.BloodMagicAPI;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.util.helper.TextHelper;

public class BlockLifeEssence extends BlockFluidClassic
{
    @Getter
    private static Fluid lifeEssence = new FluidLifeEssence();

    public BlockLifeEssence()
    {
        super(lifeEssence, Material.WATER);

        setUnlocalizedName(Constants.Mod.MODID + ".fluid.lifeEssence");
        getLifeEssence().setBlock(this);
        BloodMagicAPI.setLifeEssence(getLifeEssence());
    }

    @Override
    public boolean canDisplace(IBlockAccess world, BlockPos blockPos)
    {
        return !world.getBlockState(blockPos).getBlock().getMaterial(world.getBlockState(blockPos)).isLiquid() && super.canDisplace(world, blockPos);
    }

    @Override
    public boolean displaceIfPossible(World world, BlockPos blockPos)
    {
        return !world.getBlockState(blockPos).getBlock().getMaterial(world.getBlockState(blockPos)).isLiquid() && super.displaceIfPossible(world, blockPos);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, World worldIn, BlockPos pos)
    {
        return null;
    }

    public static class FluidLifeEssence extends Fluid
    {

        public FluidLifeEssence()
        {
            super("lifeEssence", new ResourceLocation(Constants.Mod.DOMAIN + "blocks/lifeEssenceStill"), new ResourceLocation(Constants.Mod.DOMAIN + "blocks/lifeEssenceFlowing"));

            setDensity(2000);
            setViscosity(2000);
        }

        @Override
        public int getColor()
        {
            return Color.RED.getRGB();
        }

        @Override
        public String getLocalizedName(FluidStack fluidStack)
        {
            return TextHelper.localize("tile.BloodMagic.fluid.lifeEssence.name");
        }
    }
}

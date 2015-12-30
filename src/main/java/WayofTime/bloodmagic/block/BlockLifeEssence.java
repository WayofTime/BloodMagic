package WayofTime.bloodmagic.block;

import WayofTime.bloodmagic.api.BloodMagicAPI;
import WayofTime.bloodmagic.api.Constants;
import lombok.Getter;
import net.minecraft.block.material.Material;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import java.awt.*;

public class BlockLifeEssence extends BlockFluidClassic
{

    @Getter
    private static Fluid lifeEssence = new FluidLifeEssence();

    public BlockLifeEssence()
    {
        super(lifeEssence, Material.water);

        setUnlocalizedName(Constants.Mod.MODID + ".fluid.lifeEssence");

        lifeEssence.setBlock(this);
        BloodMagicAPI.setLifeEssence(lifeEssence);
    }

    @Override
    public boolean canDisplace(IBlockAccess world, BlockPos blockPos)
    {
        return !world.getBlockState(blockPos).getBlock().getMaterial().isLiquid() && super.canDisplace(world, blockPos);
    }

    @Override
    public boolean displaceIfPossible(World world, BlockPos blockPos)
    {
        return !world.getBlockState(blockPos).getBlock().getMaterial().isLiquid() && super.displaceIfPossible(world, blockPos);
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
            return Color.WHITE.getRGB();
        }

        @Override
        public String getLocalizedName(FluidStack fluidStack)
        {
            return StatCollector.translateToLocal("tile.BloodMagic.fluid.lifeEssence.name");
        }
    }
}

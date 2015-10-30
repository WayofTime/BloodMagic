package WayofTime.alchemicalWizardry.block;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.api.AlchemicalWizardryAPI;
import lombok.AccessLevel;
import lombok.Getter;
import net.minecraft.block.material.Material;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class BlockLifeEssence extends BlockFluidClassic {

    @Getter(AccessLevel.PUBLIC)
    private static Fluid lifeEssence = new FluidLifeEssence("lifeEssence");

    public BlockLifeEssence() {
        super(lifeEssence, Material.water);

        lifeEssence.setBlock(this);
        AlchemicalWizardryAPI.setLifeEssence(lifeEssence);
    }

    @Override
    public boolean canDisplace(IBlockAccess world, BlockPos blockPos) {
        return !world.getBlockState(blockPos).getBlock().getMaterial().isLiquid() && super.canDisplace(world, blockPos);
    }

    @Override
    public boolean displaceIfPossible(World world, BlockPos blockPos) {
        return !world.getBlockState(blockPos).getBlock().getMaterial().isLiquid() && super.displaceIfPossible(world, blockPos);
    }

    public static class FluidLifeEssence extends Fluid {

        public FluidLifeEssence(String fluidName) {
            super(fluidName, new ResourceLocation(AlchemicalWizardry.DOMAIN + "lifeEssenceStill"), new ResourceLocation(AlchemicalWizardry.DOMAIN + "lifeEssenceFlowing"));

            setDensity(2000);
            setViscosity(2000);
        }

        @Override
        public int getColor() {
            return 0xEEEEEE;
        }

        @Override
        public String getLocalizedName(FluidStack fluidStack) {
            return "Life Essence";
        }
    }
}

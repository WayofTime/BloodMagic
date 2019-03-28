package WayofTime.bloodmagic.block;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.util.Constants;
import WayofTime.bloodmagic.util.helper.TextHelper;
import net.minecraft.block.material.Material;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import java.awt.Color;

public class BlockLifeEssence extends BlockFluidClassic {
    private static final Fluid LIFE_ESSENCE = new FluidLifeEssence();

    public BlockLifeEssence() {
        super(LIFE_ESSENCE, Material.WATER);

        setTranslationKey(BloodMagic.MODID + ".fluid.lifeEssence");
        getLifeEssence().setBlock(this);
    }

    @Override
    public boolean canDisplace(IBlockAccess world, BlockPos blockPos) {
        return !world.getBlockState(blockPos).getBlock().getMaterial(world.getBlockState(blockPos)).isLiquid() && super.canDisplace(world, blockPos);
    }

    @Override
    public boolean displaceIfPossible(World world, BlockPos blockPos) {
        return !world.getBlockState(blockPos).getBlock().getMaterial(world.getBlockState(blockPos)).isLiquid() && super.displaceIfPossible(world, blockPos);
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.SOLID;
    }

    public static Fluid getLifeEssence() {
        return LIFE_ESSENCE;
    }

    public static class FluidLifeEssence extends Fluid {

        public FluidLifeEssence() {
            super("lifeEssence", new ResourceLocation(Constants.Mod.DOMAIN + "blocks/lifeEssenceStill"), new ResourceLocation(Constants.Mod.DOMAIN + "blocks/lifeEssenceFlowing"));

            setDensity(2000);
            setViscosity(2000);
        }

        @Override
        public int getColor() {
            return Color.RED.getRGB();
        }

        @Override
        public String getLocalizedName(FluidStack fluidStack) {
            return TextHelper.localize("tile.bloodmagic.fluid.lifeEssence.name");
        }
    }
}

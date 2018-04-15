package WayofTime.bloodmagic.block;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.core.RegistrarBloodMagicItems;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockBloodLight extends Block {
    protected static final AxisAlignedBB AABB = new AxisAlignedBB(0.4, 0.4, 0.4, 0.6, 0.6, 0.6);

    public BlockBloodLight() {
        super(Material.CLOTH);

        setUnlocalizedName(BloodMagic.MODID + ".bloodLight");
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return AABB;
    }

    @Nullable
    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
        return NULL_AABB;
    }

    @Override
    public boolean isReplaceable(IBlockAccess world, BlockPos pos) {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean causesSuffocation(IBlockState state) {
        return false;
    }

    @Override
    public int getLightValue(IBlockState state) {
        return 15;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean addDestroyEffects(World world, BlockPos pos, ParticleManager particleManager) {
        if (world.getBlockState(pos).getBlock() == this) {
            Random random = new Random();
            particleManager.spawnEffectParticle(EnumParticleTypes.REDSTONE.getParticleID(), pos.getX() + 0.5D + random.nextGaussian() / 8, pos.getY() + 0.5D, pos.getZ() + 0.5D + random.nextGaussian() / 8, 0, 0, 0);
        }
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
        EntityPlayerSP player = Minecraft.getMinecraft().player;

        if (rand.nextInt(3) != 0) {
            world.spawnParticle(EnumParticleTypes.REDSTONE, pos.getX() + 0.5D + rand.nextGaussian() / 8, pos.getY() + 0.5D, pos.getZ() + 0.5D + rand.nextGaussian() / 8, 0, 0, 0, 0);
            ItemStack heldItem = player.getHeldItem(EnumHand.MAIN_HAND);

            if (heldItem.isEmpty() || heldItem.getItem() != RegistrarBloodMagicItems.SIGIL_BLOOD_LIGHT)
                return;

            for (int i = 0; i < 8; i++)
                world.spawnParticle(EnumParticleTypes.REDSTONE, pos.getX() + 0.5D + rand.nextGaussian() / 8, pos.getY() + 0.5D, pos.getZ() + 0.5D + rand.nextGaussian() / 8, 0, 0, 0, 0);
        }
    }

    @Override
    public int quantityDropped(Random par1Random) {
        return 0;
    }
}

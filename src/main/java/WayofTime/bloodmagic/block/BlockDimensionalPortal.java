package WayofTime.bloodmagic.block;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.block.base.BlockInteger;
import WayofTime.bloodmagic.ritual.IMasterRitualStone;
import WayofTime.bloodmagic.ritual.portal.LocationsHandler;
import WayofTime.bloodmagic.teleport.PortalLocation;
import WayofTime.bloodmagic.teleport.TeleportQueue;
import WayofTime.bloodmagic.teleport.Teleports;
import WayofTime.bloodmagic.tile.TileDimensionalPortal;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Random;

public class BlockDimensionalPortal extends BlockInteger {
    protected static final AxisAlignedBB AABB_0 = new AxisAlignedBB(0.0D, 0.0D, 0.375D, 1.0D, 1.0D, 0.625D);
    protected static final AxisAlignedBB AABB_1 = new AxisAlignedBB(0.375D, 0.0D, 0.0D, 0.625D, 1.0D, 1.0D);
    protected static final AxisAlignedBB AABB_DEFAULT = new AxisAlignedBB(0.375D, 0.0D, 0.375D, 0.625D, 1.0D, 0.625D);

    public BlockDimensionalPortal() {
        super(Material.PORTAL, 2);
        setTranslationKey(BloodMagic.MODID + ".dimensionalPortal");
        setBlockUnbreakable();
        setResistance(2000);
        setLightOpacity(0);
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
    public boolean causesSuffocation(IBlockState state) {
        return false;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
        return null;
    }

    public boolean isOpaqueCube() {
        return false;
    }

    public boolean isFullCube() {
        return false;
    }

    @Override
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
        return 12;
    }

    @Override
    public void onEntityCollision(World world, BlockPos pos, IBlockState blockState, Entity entity) {
        if (!world.isRemote && world.getTileEntity(pos) instanceof TileDimensionalPortal) {
            TileDimensionalPortal tile = (TileDimensionalPortal) world.getTileEntity(pos);

            ArrayList<PortalLocation> linkedLocations = LocationsHandler.getLinkedLocations(tile.portalID);

            if (linkedLocations != null && linkedLocations.size() > 1) {
                TileEntity tileEntity = world.getTileEntity(tile.getMasterStonePos());
                if (tileEntity instanceof IMasterRitualStone) {
                    IMasterRitualStone masterRitualStone = (IMasterRitualStone) tileEntity;
                    PortalLocation portal;
                    int index = linkedLocations.size() - 1; //index of most recent PortalLocation
                    if (linkedLocations.get(index).equals(new PortalLocation(masterRitualStone.getBlockPos().up(), world.provider.getDimension())))
                        index--; // if most recent PortalLocation = this, get the 2nd most recent
                    portal = linkedLocations.get(index);
                    
                    if (portal.getDimension() == world.provider.getDimension()) {
                        TeleportQueue.getInstance().addITeleport(new Teleports.TeleportSameDim(portal.getX(), portal.getY(), portal.getZ(), entity, masterRitualStone.getOwner(), false));
                    } else {
                        TeleportQueue.getInstance().addITeleport(new Teleports.TeleportToDim(portal.getX(), portal.getY(), portal.getZ(), entity, masterRitualStone.getOwner(), world, portal.getDimension(), false));
                    }
                }
            }
        }
    }

    @Override
    public int quantityDropped(Random par1Random) {
        return 0;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
        int meta = state.getBlock().getMetaFromState(state);
        if (meta == 0) {
            return AABB_0;
        } else if (meta == 1) {
            return AABB_1;
        } else {
            return AABB_DEFAULT;
        }
    }

//
//    @Override
//    public void setBlockBoundsForItemRender()
//    {
//        setBlockBounds(0f, 0f, 0.375f, 1f, 1f, 0.625f);
//    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
        this.spawnParticles(world, pos.getX(), pos.getY(), pos.getZ());
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileDimensionalPortal();
    }

    private void spawnParticles(World world, int x, int y, int z) {
        Random random = world.rand;
        double d0 = 0.0625D;
        for (int i = 0; i < 6; ++i) {
            double particleX = (double) ((float) x + random.nextFloat());
            double particleY = (double) ((float) y + random.nextFloat());
            double particleZ = (double) ((float) z + random.nextFloat());
            if (i == 0 && !world.getBlockState(new BlockPos(x, y + 1, z)).isOpaqueCube()) {
                particleY = (double) (y + 1) + d0;
            }
            if (i == 1 && !world.getBlockState(new BlockPos(x, y - 1, z)).isOpaqueCube()) {
                particleY = (double) y - d0;
            }
            if (i == 2 && !world.getBlockState(new BlockPos(x, y, z + 1)).isOpaqueCube()) {
                particleZ = (double) (z + 1) + d0;
            }
            if (i == 3 && !world.getBlockState(new BlockPos(x, y, z - 1)).isOpaqueCube()) {
                particleZ = (double) z - d0;
            }
            if (i == 4 && !world.getBlockState(new BlockPos(x + 1, y, z)).isOpaqueCube()) {
                particleX = (double) (x + 1) + d0;
            }
            if (i == 5 && !world.getBlockState(new BlockPos(x - 1, y, z)).isOpaqueCube()) {
                particleX = (double) x - d0;
            }
            if (particleX < (double) x || particleX > (double) (x + 1) || particleY < 0.0D || particleY > (double) (y + 1) || particleZ < (double) z || particleZ > (double) (z + 1)) {
                world.spawnParticle(EnumParticleTypes.REDSTONE, particleX, particleY, particleZ, 0.0D, 0.0D, 0.0D);
            }
        }
    }
}

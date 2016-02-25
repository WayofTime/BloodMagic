package WayofTime.bloodmagic.block;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.ritual.IMasterRitualStone;
import WayofTime.bloodmagic.api.teleport.PortalLocation;
import WayofTime.bloodmagic.api.teleport.TeleportQueue;
import WayofTime.bloodmagic.block.base.BlockIntegerContainer;
import WayofTime.bloodmagic.ritual.portal.LocationsHandler;
import WayofTime.bloodmagic.ritual.portal.Teleports;
import WayofTime.bloodmagic.tile.TileDimensionalPortal;

public class BlockDimensionalPortal extends BlockIntegerContainer
{

    public BlockDimensionalPortal()
    {
        super(Material.portal, 2);
        setUnlocalizedName(Constants.Mod.MODID + ".dimensionalPortal");
        setRegistryName(Constants.BloodMagicBlock.DIMENSIONAL_PORTAL.getRegName());
        setBlockUnbreakable();
        setResistance(2000);
        setLightOpacity(0);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileDimensionalPortal();
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state)
    {
        return null;
    }

    public boolean isOpaqueCube()
    {
        return false;
    }

    public boolean isFullCube()
    {
        return false;
    }

    @Override
    public int getLightValue(IBlockAccess world, BlockPos pos)
    {
        return 12;
    }

    @Override
    public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState blockState, Entity entity)
    {
        if (!world.isRemote && world.getTileEntity(pos) instanceof TileDimensionalPortal)
        {
            TileDimensionalPortal tile = (TileDimensionalPortal) world.getTileEntity(pos);

            if (LocationsHandler.getLocationsHandler() != null)
            {
                ArrayList<PortalLocation> linkedLocations = LocationsHandler.getLocationsHandler().getLinkedLocations(tile.portalID);

                if (linkedLocations != null && !linkedLocations.isEmpty() && linkedLocations.size() > 1)
                {
                    if (world.getTileEntity(tile.getMasterStonePos()) != null && world.getTileEntity(tile.getMasterStonePos()) instanceof IMasterRitualStone)
                    {
                        IMasterRitualStone masterRitualStone = (IMasterRitualStone) world.getTileEntity(tile.getMasterStonePos());
                        if (linkedLocations.get(0).equals(new PortalLocation(masterRitualStone.getBlockPos().up(), world.provider.getDimensionId())))
                        {
                            PortalLocation portal = linkedLocations.get(1);
                            if (portal.getDimension() == world.provider.getDimensionId())
                            {
                                TeleportQueue.getInstance().addITeleport(new Teleports.TeleportSameDim(portal.getX(), portal.getY(), portal.getZ(), entity, masterRitualStone.getOwner()));
                            } else
                            {
                                TeleportQueue.getInstance().addITeleport(new Teleports.TeleportToDim(portal.getX(), portal.getY(), portal.getZ(), entity, masterRitualStone.getOwner(), world, portal.getDimension()));
                            }
                        } else if (linkedLocations.get(1).equals(new PortalLocation(masterRitualStone.getBlockPos().up(), world.provider.getDimensionId())))
                        {
                            PortalLocation portal = linkedLocations.get(0);
                            if (portal.getDimension() == world.provider.getDimensionId())
                            {
                                TeleportQueue.getInstance().addITeleport(new Teleports.TeleportSameDim(portal.getX(), portal.getY(), portal.getZ(), entity, masterRitualStone.getOwner()));
                            } else
                            {
                                TeleportQueue.getInstance().addITeleport(new Teleports.TeleportToDim(portal.getX(), portal.getY(), portal.getZ(), entity, masterRitualStone.getOwner(), world, portal.getDimension()));
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public int quantityDropped(Random par1Random)
    {
        return 0;
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, BlockPos blockPos)
    {
        int meta = world.getBlockState(blockPos).getBlock().getMetaFromState(world.getBlockState(blockPos));
        if (meta == 0)
        {
            setBlockBounds(0f, 0f, 0.375f, 1f, 1f, 0.625f);
        } else if (meta == 1)
        {
            setBlockBounds(0.375f, 0f, 0f, 0.625f, 1f, 1f);
        } else
        {
            setBlockBounds(0f, 0f, 0f, 1f, 1, 1f);
        }
    }

    @Override
    public void setBlockBoundsForItemRender()
    {
        setBlockBounds(0f, 0f, 0.375f, 1f, 1f, 0.625f);
    }

    @SideOnly(Side.CLIENT)
    public EnumWorldBlockLayer getBlockLayer()
    {
        return EnumWorldBlockLayer.TRANSLUCENT;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, BlockPos pos, IBlockState state, Random rand)
    {
        this.spawnParticles(world, pos.getX(), pos.getY(), pos.getZ());
    }

    private void spawnParticles(World world, int x, int y, int z)
    {
        Random random = world.rand;
        double d0 = 0.0625D;
        for (int i = 0; i < 6; ++i)
        {
            double particleX = (double) ((float) x + random.nextFloat());
            double particleY = (double) ((float) y + random.nextFloat());
            double particleZ = (double) ((float) z + random.nextFloat());
            if (i == 0 && !world.getBlockState(new BlockPos(x, y + 1, z)).getBlock().isOpaqueCube())
            {
                particleY = (double) (y + 1) + d0;
            }
            if (i == 1 && !world.getBlockState(new BlockPos(x, y - 1, z)).getBlock().isOpaqueCube())
            {
                particleY = (double) y - d0;
            }
            if (i == 2 && !world.getBlockState(new BlockPos(x, y, z + 1)).getBlock().isOpaqueCube())
            {
                particleZ = (double) (z + 1) + d0;
            }
            if (i == 3 && !world.getBlockState(new BlockPos(x, y, z - 1)).getBlock().isOpaqueCube())
            {
                particleZ = (double) z - d0;
            }
            if (i == 4 && !world.getBlockState(new BlockPos(x + 1, y, z)).getBlock().isOpaqueCube())
            {
                particleX = (double) (x + 1) + d0;
            }
            if (i == 5 && !world.getBlockState(new BlockPos(x - 1, y, z)).getBlock().isOpaqueCube())
            {
                particleX = (double) x - d0;
            }
            if (particleX < (double) x || particleX > (double) (x + 1) || particleY < 0.0D || particleY > (double) (y + 1) || particleZ < (double) z || particleZ > (double) (z + 1))
            {
                world.spawnParticle(EnumParticleTypes.REDSTONE, particleX, particleY, particleZ, 0.0D, 0.0D, 0.0D);
            }
        }
    }
}

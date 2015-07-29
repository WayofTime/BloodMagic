package WayofTime.alchemicalWizardry.common.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.particle.EntityDiggingFX;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.common.tileEntity.TEMimicBlock;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockMimic extends BlockContainer
{
	public BlockMimic()
	{
		super(Material.water);
		setHardness(2.0F);
        setResistance(5.0F);
//        this.setBlockBounds(0, 0, 0, 0, 0, 0);
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess world, BlockPos blockPos, EnumFacing side)
	{
		TileEntity TE = world.getTileEntity(blockPos);
		if(!(TE instanceof TEMimicBlock))
		{
			return true;
		}
        TEMimicBlock mimic = (TEMimicBlock)TE;

        Block block = mimic.getBlock();
        
        return block == null || block.shouldSideBeRendered(world, blockPos, side);
	}
	
	@Override
	public boolean canCollideCheck(IBlockState blockState, boolean bool)
    {
        return blockState.getBlock().getMetaFromState(blockState) == 1;
    }

    @Override
	public boolean isOpaqueCube()
    {
        return false;
    }

	@Override
	public TileEntity createNewTileEntity(World world, int meta) 
	{
		return new TEMimicBlock();
	}
	
	@Override
    public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player)
	{
		return false;
	}
	
	@Override
    public int quantityDropped(Random random)
	{
		return 0;
	}
	
	@Override
	public boolean isBlockSolid(IBlockAccess world, BlockPos blockPos, EnumFacing side)
	{
		TileEntity tile = world.getTileEntity(blockPos);
		if(tile instanceof TEMimicBlock)
		{
			Block block = ((TEMimicBlock) tile).getBlock();
			int mimicMeta = ((TEMimicBlock) tile).getMetaOfMimic();
			
			if(block != null)
			{
				return block.isBlockSolid(world, blockPos, side); //side was mimicM
			}
		}
		return super.isBlockSolid(world, blockPos, side);
	}
	
	@Override
	public boolean addHitEffects(World world, MovingObjectPosition target, EffectRenderer effectRenderer)
    {
		TileEntity tile = world.getTileEntity(target.func_178782_a());
		
        TEMimicBlock TE = (TEMimicBlock)tile;

        if (TE != null) 
        {
            Block block = TE.getBlock();

            double xOffset = target.func_178782_a().getX() + world.rand.nextDouble() * (block.getBlockBoundsMaxX() - block.getBlockBoundsMinX() - 0.1F * 2.0F) + 0.1F + block.getBlockBoundsMinX();
            double yOffset = target.func_178782_a().getY() + world.rand.nextDouble() * (block.getBlockBoundsMaxY() - block.getBlockBoundsMinY() - 0.1F * 2.0F) + 0.1F + block.getBlockBoundsMinY();
            double zOffset = target.func_178782_a().getZ() + world.rand.nextDouble() * (block.getBlockBoundsMaxZ() - block.getBlockBoundsMinZ() - 0.1F * 2.0F) + 0.1F + block.getBlockBoundsMinZ();

            switch (target.subHit)
            {
                case 0:
                    yOffset = target.func_178782_a().getY() + block.getBlockBoundsMinY() - 0.1D;
                    break;
                case 1:
                    yOffset = target.func_178782_a().getY() + block.getBlockBoundsMaxY() + 0.1D;
                    break;
                case 2:
                    zOffset = target.func_178782_a().getZ() + block.getBlockBoundsMinZ() - 0.1D;
                    break;
                case 3:
                    zOffset = target.func_178782_a().getZ() + block.getBlockBoundsMaxZ() + 0.1D;
                    break;
                case 4:
                    xOffset = target.func_178782_a().getX() + block.getBlockBoundsMinX() - 0.1D;
                    break;
                case 5:
                    xOffset = target.func_178782_a().getX() + block.getBlockBoundsMaxX() + 0.1D;
                    break;
            }

//            BlockMimic.addHitEffect(TE, target, xOffset, yOffset, zOffset, null, effectRenderer);

            return true;

        }

        return super.addHitEffects(world, target, effectRenderer);
    }
	
	@Override
	public AxisAlignedBB getCollisionBoundingBox(World world, BlockPos blockPos, IBlockState blockState)
    {
		TEMimicBlock tile = (TEMimicBlock)world.getTileEntity(blockPos);
		Block block = tile.getBlock();
		
        return block != null ? block.getCollisionBoundingBox(world, blockPos, blockState) : super.getCollisionBoundingBox(world, blockPos, blockState);
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public int colorMultiplier(IBlockAccess blockAccess, BlockPos blockPos, int renderPass)
    {
        TEMimicBlock TE = (TEMimicBlock)blockAccess.getTileEntity(blockPos);
        if (TE != null)
        {
            Block block = TE.getBlock();
            if(block != null)
            {
                return block.colorMultiplier(blockAccess, blockPos);
            }
        }

        return super.colorMultiplier(blockAccess, blockPos);
    }
	
/*	@Override
    public void velocityToAddToEntity(World world, BlockPos, Entity entity, Vec3 vec)
	{
		TEMimicBlock TE = (TEMimicBlock)world.getTileEntity(x, y, z);
        if (TE != null)
        {
            Block block = TE.getBlock();
            if(block != null)
            {
            	block.velocityToAddToEntity(world, x, y, z, entity, vec);
            }
        }
	}
	No longer in 1.8 apparently

	public static void addHitEffect(TEMimicBlock TE, MovingObjectPosition target, double x, double y, double z, ItemStack itemStack, EffectRenderer effectRenderer)
	{ 
	    EntityDiggingFX particle = new EntityDiggingFX(TE.getWorld(), x, y, z, 0.0D, 0.0D, 0.0D, TE.getBlock(), TE.getMetaOfMimic());
	    effectRenderer.addEffect(particle.func_174846_a(target.func_178782_a()).multiplyVelocity(0.2F).multipleParticleScaleBy(0.6F));
	}
*/
	@Override
	public void onEntityCollidedWithBlock(World world, BlockPos blockPos, Entity entity)
	{
		TEMimicBlock TE = (TEMimicBlock)world.getTileEntity(blockPos);
        if (TE != null)
        {
        	if(TE.getBlockEffectWhileInside(entity, blockPos))
        	{
        		return;
        	}else
        	{
        		Block block = TE.getBlock();
        		if(block != null)
        		{
        			block.onEntityCollidedWithBlock(world, blockPos, entity);
        			return;
        		}
        	}
        }

		super.onEntityCollidedWithBlock(world, blockPos, entity);
	}
}

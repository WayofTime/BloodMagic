package wayoftime.bloodmagic.common.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BlockBloodLight extends Block
{
	protected static final VoxelShape BODY = Block.makeCuboidShape(7, 7, 7, 9, 9, 9);

	public BlockBloodLight()
	{
		super(Properties.create(Material.WOOL).doesNotBlockMovement().setLightLevel((state) -> {
			return 15;
		}));
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
	{
		return BODY;
	}

	@Override
	public BlockRenderType getRenderType(BlockState state)
	{
		return BlockRenderType.ENTITYBLOCK_ANIMATED;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState stateIn, World world, BlockPos pos, Random rand)
	{
		ClientPlayerEntity player = Minecraft.getInstance().player;

		if (rand.nextInt(3) != 0)
		{
			world.addParticle(RedstoneParticleData.REDSTONE_DUST, pos.getX() + 0.5D
					+ rand.nextGaussian() / 8, pos.getY() + 0.5D, pos.getZ() + 0.5D + rand.nextGaussian() / 8, 0, 0, 0);
			ItemStack heldItem = player.getHeldItem(Hand.MAIN_HAND);

//			if (heldItem.isEmpty() || heldItem.getItem() != RegistrarBloodMagicItems.SIGIL_BLOOD_LIGHT)
//				return;
//
//			for (int i = 0; i < 8; i++) world.addParticle(RedstoneParticleData.REDSTONE_DUST, pos.getX() + 0.5D
//					+ rand.nextGaussian() / 8, pos.getY() + 0.5D, pos.getZ() + 0.5D + rand.nextGaussian() / 8, 0, 0, 0);
		}
	}
}

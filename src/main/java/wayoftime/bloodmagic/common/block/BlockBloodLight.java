package wayoftime.bloodmagic.common.block;


import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class BlockBloodLight extends Block
{
	protected static final VoxelShape BODY = Block.box(7, 7, 7, 9, 9, 9);

	public BlockBloodLight()
	{
		super(Properties.of().noCollission().lightLevel((state) -> 15).ignitedByLava());
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context)
	{
		return BODY;
	}

	@Override
	public RenderShape getRenderShape(BlockState state)
	{
		return RenderShape.ENTITYBLOCK_ANIMATED;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState stateIn, Level world, BlockPos pos, RandomSource rand)
	{
		LocalPlayer player = Minecraft.getInstance().player;

		if (rand.nextInt(3) != 0)
		{
			world.addParticle(DustParticleOptions.REDSTONE, pos.getX() + 0.5D
					+ rand.nextGaussian() / 8, pos.getY() + 0.5D, pos.getZ() + 0.5D + rand.nextGaussian() / 8, 0, 0, 0);
			ItemStack heldItem = player.getItemInHand(InteractionHand.MAIN_HAND);

//			if (heldItem.isEmpty() || heldItem.getItem() != RegistrarBloodMagicItems.SIGIL_BLOOD_LIGHT)
//				return;
//
//			for (int i = 0; i < 8; i++) world.addParticle(RedstoneParticleData.REDSTONE_DUST, pos.getX() + 0.5D
//					+ rand.nextGaussian() / 8, pos.getY() + 0.5D, pos.getZ() + 0.5D + rand.nextGaussian() / 8, 0, 0, 0);
		}
	}
}

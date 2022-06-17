package wayoftime.bloodmagic.common.item.block;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import wayoftime.bloodmagic.anointment.AnointmentHolder;
import wayoftime.bloodmagic.common.tile.TileExplosiveCharge;
import wayoftime.bloodmagic.entity.projectile.EntityShapedCharge;

public class ItemBlockShapedCharge extends BlockItem
{
	public ItemBlockShapedCharge(Block blockIn, Properties builder)
	{
		super(blockIn, builder);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand hand)
	{
		ItemStack stack = playerIn.getItemInHand(hand);
		if (!playerIn.isCreative())
		{
			stack.shrink(1);
		}

		worldIn.playSound((Player) null, playerIn.getX(), playerIn.getY(), playerIn.getZ(), SoundEvents.SNOWBALL_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (worldIn.random.nextFloat() * 0.4F + 0.8F));

		if (!worldIn.isClientSide)
		{
//			EntitySoulSnare snare = new EntitySoulSnare(worldIn, playerIn);
			EntityShapedCharge charge = new EntityShapedCharge(worldIn, this.getBlock(), playerIn);
			charge.shootFromRotation(playerIn, playerIn.getXRot(), playerIn.getYRot(), 0.0F, 1.5F, 1.0F);
			charge.setAnointmentHolder(AnointmentHolder.fromItemStack(stack));
			worldIn.addFreshEntity(charge);
//			
//			SnowballEntity snowballentity = new SnowballEntity(worldIn, playerIn);
//	         snowballentity.setItem(itemstack);
//	         snowballentity.shootFromRotation(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, 1.5F, 1.0F);
//	         worldIn.addEntity(snowballentity);
		}

		return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
	}

	@Override
	public InteractionResult place(BlockPlaceContext context)
	{
		InteractionResult result = super.place(context);

		AnointmentHolder holder = AnointmentHolder.fromItemStack(context.getItemInHand());
		if (holder != null)
		{
			BlockEntity tile = context.getLevel().getBlockEntity(context.getClickedPos());
			if (tile instanceof TileExplosiveCharge)
			{
				((TileExplosiveCharge) tile).setAnointmentHolder(holder);
			}
		}

		return result;
	}
}

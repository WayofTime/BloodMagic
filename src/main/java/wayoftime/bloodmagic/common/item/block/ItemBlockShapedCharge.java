package wayoftime.bloodmagic.common.item.block;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import wayoftime.bloodmagic.anointment.AnointmentHolder;
import wayoftime.bloodmagic.entity.projectile.EntityShapedCharge;
import wayoftime.bloodmagic.tile.TileExplosiveCharge;

public class ItemBlockShapedCharge extends BlockItem
{
	public ItemBlockShapedCharge(Block blockIn, Properties builder)
	{
		super(blockIn, builder);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand hand)
	{
		ItemStack stack = playerIn.getHeldItem(hand);
		if (!playerIn.isCreative())
		{
			stack.shrink(1);
		}

		worldIn.playSound((PlayerEntity) null, playerIn.getPosX(), playerIn.getPosY(), playerIn.getPosZ(), SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));

		if (!worldIn.isRemote)
		{
//			EntitySoulSnare snare = new EntitySoulSnare(worldIn, playerIn);
			EntityShapedCharge charge = new EntityShapedCharge(worldIn, this.getBlock(), playerIn);
			charge.func_234612_a_(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, 1.5F, 1.0F);
			charge.setAnointmentHolder(AnointmentHolder.fromItemStack(stack));
			worldIn.addEntity(charge);
//			
//			SnowballEntity snowballentity = new SnowballEntity(worldIn, playerIn);
//	         snowballentity.setItem(itemstack);
//	         snowballentity.func_234612_a_(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, 1.5F, 1.0F);
//	         worldIn.addEntity(snowballentity);
		}

		return new ActionResult<>(ActionResultType.SUCCESS, stack);
	}

	@Override
	public ActionResultType tryPlace(BlockItemUseContext context)
	{
		ActionResultType result = super.tryPlace(context);

		AnointmentHolder holder = AnointmentHolder.fromItemStack(context.getItem());
		if (holder != null)
		{
			TileEntity tile = context.getWorld().getTileEntity(context.getPos());
			if (tile instanceof TileExplosiveCharge)
			{
				((TileExplosiveCharge) tile).setAnointmentHolder(holder);
			}
		}

		return result;
	}
}

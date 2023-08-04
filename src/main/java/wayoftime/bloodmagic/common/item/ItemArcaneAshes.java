package wayoftime.bloodmagic.common.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.block.BloodMagicBlocks;
import wayoftime.bloodmagic.common.tile.TileAlchemyArray;

import java.util.List;

public class ItemArcaneAshes extends Item
{
	public ItemArcaneAshes()
	{
		super(new Item.Properties().stacksTo(1).durability(20));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, Level world, List<Component> tooltip, TooltipFlag flag)
	{
		tooltip.add(Component.translatable("tooltip.bloodmagic.arcaneAshes").withStyle(ChatFormatting.GRAY));
	}

	@Override
	public InteractionResult useOn(UseOnContext context)
	{
		ItemStack stack = context.getItemInHand();
		BlockPos newPos = context.getClickedPos().relative(context.getClickedFace());
		Level world = context.getLevel();
		Player player = context.getPlayer();

		if (world.isEmptyBlock(newPos))
		{
			if (!world.isClientSide)
			{
				Direction rotation = Direction.fromYRot(player.getYHeadRot());
				world.setBlockAndUpdate(newPos, BloodMagicBlocks.ALCHEMY_ARRAY.get().defaultBlockState());
				BlockEntity tile = world.getBlockEntity(newPos);
				if (tile instanceof TileAlchemyArray)
				{
					((TileAlchemyArray) tile).setRotation(rotation);
				}

//				PickaxeItem d;
				stack.hurtAndBreak(1, player, (entity) -> {
					entity.broadcastBreakEvent(EquipmentSlot.MAINHAND);
				});

			}

			return InteractionResult.SUCCESS;
		}

		return InteractionResult.FAIL;
	}

//	@Override
//	public ActionResultType onItemUse(PlayerEntity player, World world, BlockPos blockPos, Hand hand, Direction side, float hitX, float hitY, float hitZ)
//	{
//		ItemStack stack = player.getHeldItem(hand);
//		BlockPos newPos = blockPos.offset(side);
//
//		if (world.isAirBlock(newPos))
//		{
//			if (!world.isRemote)
//			{
//				Direction rotation = Direction.fromAngle(player.getRotationYawHead());
//				world.setBlockState(newPos, RegistrarBloodMagicBlocks.ALCHEMY_ARRAY.getDefaultState());
//				TileEntity tile = world.getTileEntity(newPos);
//				if (tile instanceof TileAlchemyArray)
//				{
//					((TileAlchemyArray) tile).setRotation(rotation);
//				}
//
//				stack.damageItem(1, player);
//			}
//
//			return ActionResultType.SUCCESS;
//		}
//
//		return ActionResultType.FAIL;
//	}

}

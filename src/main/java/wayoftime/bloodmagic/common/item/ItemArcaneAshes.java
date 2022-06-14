package wayoftime.bloodmagic.common.item;

import java.util.List;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.block.BloodMagicBlocks;
import wayoftime.bloodmagic.tile.TileAlchemyArray;

public class ItemArcaneAshes extends Item
{
	public ItemArcaneAshes()
	{
		super(new Item.Properties().stacksTo(1).tab(BloodMagic.TAB).durability(20));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, World world, List<ITextComponent> tooltip, ITooltipFlag flag)
	{
		tooltip.add(new TranslationTextComponent("tooltip.bloodmagic.arcaneAshes").withStyle(TextFormatting.GRAY));
	}

	@Override
	public ActionResultType useOn(ItemUseContext context)
	{
		ItemStack stack = context.getItemInHand();
		BlockPos newPos = context.getClickedPos().relative(context.getClickedFace());
		World world = context.getLevel();
		PlayerEntity player = context.getPlayer();

		if (world.isEmptyBlock(newPos))
		{
			if (!world.isClientSide)
			{
				Direction rotation = Direction.fromYRot(player.getYHeadRot());
				world.setBlockAndUpdate(newPos, BloodMagicBlocks.ALCHEMY_ARRAY.get().defaultBlockState());
				TileEntity tile = world.getBlockEntity(newPos);
				if (tile instanceof TileAlchemyArray)
				{
					((TileAlchemyArray) tile).setRotation(rotation);
				}

//				PickaxeItem d;
				stack.hurtAndBreak(1, player, (entity) -> {
					entity.broadcastBreakEvent(EquipmentSlotType.MAINHAND);
				});

			}

			return ActionResultType.SUCCESS;
		}

		return ActionResultType.FAIL;
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

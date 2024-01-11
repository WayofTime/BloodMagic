package wayoftime.bloodmagic.common.item;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.block.enums.BloodRuneType;
import wayoftime.bloodmagic.common.block.BlockBloodRune;
import wayoftime.bloodmagic.common.block.BloodMagicBlocks;
import wayoftime.bloodmagic.util.BMLog;

public class ItemRuneEtcher extends Item
{

	public ItemRuneEtcher()
	{
		super(new Item.Properties().stacksTo(1).tab(BloodMagic.TAB));
	}

	@Override
	public boolean hasContainerItem(ItemStack stack)
	{
		return true;
	}

	@Override
	public ItemStack getContainerItem(ItemStack stack)
	{
		return stack.copy();
	}

	@Override
	public InteractionResult useOn(UseOnContext context)
	{
		BlockPos pos = context.getClickedPos();
		Level world = context.getLevel();
		Player player = context.getPlayer();
		BlockState state = world.getBlockState(pos);

		if (state.getBlock() instanceof BlockBloodRune)
		{
			BlockBloodRune rune = (BlockBloodRune) state.getBlock();
			if (canDowngrade(rune))
			{
				player.addItem(getUpgradeKit(rune));
				downgrade(rune, pos, world);
				return InteractionResult.SUCCESS;
			}
		}

		return InteractionResult.FAIL;
	}

	public void downgrade(BlockBloodRune rune, BlockPos pos, Level level)
	{
		Block runeBlock = (Block) rune;
		switch (rune.getBloodRune(level, pos))
		{
		case ACCELERATION:
			runeBlock = BloodMagicBlocks.SPEED_RUNE.get();
			break;

		case AUGMENTED_CAPACITY:
			runeBlock = BloodMagicBlocks.CAPACITY_RUNE.get();
			break;

		case CAPACITY:
		case CHARGING:
		case DISPLACEMENT:
		case ORB:
		case SACRIFICE:
		case SELF_SACRIFICE:
		case SPEED:
			runeBlock = BloodMagicBlocks.BLANK_RUNE.get();
			break;

		case EFFICIENCY:
			// no corresponding rune block
		default:
			break;
		}

		BlockState newState = runeBlock.defaultBlockState();
		level.setBlockAndUpdate(pos, newState);
	}

	public ItemStack getUpgradeKit(BlockBloodRune rune)
	{
		switch (rune.getBloodRune(null, null))
		{
		case ACCELERATION:
			return new ItemStack(BloodMagicItems.ACCELERATION_RUNE_KIT_ITEM.get());
		case AUGMENTED_CAPACITY:
			return new ItemStack(BloodMagicItems.AUGMENTED_CAPACITY_RUNE_KIT_ITEM.get());
		case CAPACITY:
			return new ItemStack(BloodMagicItems.CAPACITY_RUNE_KIT_ITEM.get());
		case CHARGING:
			return new ItemStack(BloodMagicItems.CHARGING_RUNE_KIT_ITEM.get());
		case DISPLACEMENT:
			return new ItemStack(BloodMagicItems.DISPLACEMENT_RUNE_KIT_ITEM.get());
		case EFFICIENCY:
			// no corresponding rune block
			break;
		case ORB:
			return new ItemStack(BloodMagicItems.ORB_RUNE_KIT_ITEM.get());
		case SACRIFICE:
			return new ItemStack(BloodMagicItems.SACRIFICE_RUNE_KIT_ITEM.get());
		case SELF_SACRIFICE:
			return new ItemStack(BloodMagicItems.SELF_SACRIFICE_RUNE_KIT_ITEM.get());
		case SPEED:
			return new ItemStack(BloodMagicItems.SPEED_RUNE_KIT_ITEM.get());
		default:
			BMLog.DEFAULT.error(String.format("rune type %s not supported", rune.getBloodRune(null, null).name()));
			break;
		}

		return ItemStack.EMPTY;
	}

	public boolean canDowngrade(BlockBloodRune rune)
	{
		if (rune.getRuneCount(null, null) == 2)
		{
			return false;
		}

		if (rune.getBloodRune(null, null) == BloodRuneType.BLANK)
		{
			return false;
		}

		return true;
	}
}

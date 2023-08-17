package wayoftime.bloodmagic.common.block;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import wayoftime.bloodmagic.ritual.EnumRuneType;
import wayoftime.bloodmagic.ritual.IRitualStone;

import javax.annotation.Nullable;
import java.util.List;

public class BlockRitualStone extends Block implements IRitualStone
{
	private final EnumRuneType type;

	public BlockRitualStone(EnumRuneType type)
	{
		super(Properties.of().strength(2.0F, 5.0F).sound(SoundType.STONE).requiresCorrectToolForDrops());
		this.type = type;
//	.harvestTool(ToolType.PICKAXE).harvestLevel(2)
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable BlockGetter world, List<Component> tooltip, TooltipFlag flag)
	{
		tooltip.add(Component.translatable("tooltip.bloodmagic.decoration.safe").withStyle(ChatFormatting.GRAY));
		super.appendHoverText(stack, world, tooltip, flag);
	}

//	@Override
//	public int damageDropped(BlockState state)
//	{
//		return 0;
//	}
//
//	@Override
//	public boolean canSilkHarvest(World world, BlockPos pos, BlockState state, PlayerEntity player)
//	{
//		return false;
//	}

	@Override
	public boolean isRuneType(Level world, BlockPos pos, EnumRuneType runeType)
	{
		return type.equals(runeType);
	}

	@Override
	public void setRuneType(Level world, BlockPos pos, EnumRuneType runeType)
	{
		Block runeBlock = this;
		switch (runeType)
		{
		case AIR:
			runeBlock = BloodMagicBlocks.AIR_RITUAL_STONE.get();
			break;
		case BLANK:
			runeBlock = BloodMagicBlocks.BLANK_RITUAL_STONE.get();
			break;
		case DAWN:
			runeBlock = BloodMagicBlocks.DAWN_RITUAL_STONE.get();
			break;
		case DUSK:
			runeBlock = BloodMagicBlocks.DUSK_RITUAL_STONE.get();
			break;
		case EARTH:
			runeBlock = BloodMagicBlocks.EARTH_RITUAL_STONE.get();
			break;
		case FIRE:
			runeBlock = BloodMagicBlocks.FIRE_RITUAL_STONE.get();
			break;
		case WATER:
			runeBlock = BloodMagicBlocks.WATER_RITUAL_STONE.get();
			break;
		}

		BlockState newState = runeBlock.defaultBlockState();
		world.setBlockAndUpdate(pos, newState);
	}
}
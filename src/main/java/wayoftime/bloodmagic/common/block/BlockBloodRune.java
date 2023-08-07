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
import wayoftime.bloodmagic.altar.IBloodRune;
import wayoftime.bloodmagic.block.enums.BloodRuneType;

import javax.annotation.Nullable;
import java.util.List;

public class BlockBloodRune extends Block implements IBloodRune
{
	private final BloodRuneType type;
	private final int strength;

	public BlockBloodRune(BloodRuneType type, int strength)
	{
		super(Properties.of().strength(2.0F, 5.0F).sound(SoundType.STONE).requiresCorrectToolForDrops());
		this.type = type;
		this.strength = strength;
	}

	public BlockBloodRune(BloodRuneType type)
	{
		this(type, 1);
	}

	@Nullable
	@Override
	public BloodRuneType getBloodRune(Level world, BlockPos pos)
	{
		return type;
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable BlockGetter world, List<Component> tooltip, TooltipFlag flag)
	{
		tooltip.add(Component.translatable("tooltip.bloodmagic.decoration.safe").withStyle(ChatFormatting.GRAY));
		super.appendHoverText(stack, world, tooltip, flag);
	}

	@Override
	public int getRuneCount(Level world, BlockPos pos)
	{
		return strength;
	}
}

package wayoftime.bloodmagic.common.block;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import wayoftime.bloodmagic.altar.IBloodRune;
import wayoftime.bloodmagic.block.enums.BloodRuneType;

public class BlockBloodRune extends Block implements IBloodRune
{
	private final BloodRuneType type;

	public BlockBloodRune(BloodRuneType type)
	{
		super(Properties.of(Material.STONE).strength(2.0F, 5.0F).sound(SoundType.STONE));
		this.type = type;
//	.harvestTool(ToolType.PICKAXE).harvestLevel(2)
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
		tooltip.add(new TranslatableComponent("tooltip.bloodmagic.decoration.safe").withStyle(ChatFormatting.GRAY));
		super.appendHoverText(stack, world, tooltip, flag);
	}
}

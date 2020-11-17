package wayoftime.bloodmagic.common.block;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import wayoftime.bloodmagic.block.enums.BloodRuneType;
import wayoftime.bloodmagic.api.tile.IBloodRune;

public class BlockBloodRune extends Block implements IBloodRune
{
	private final BloodRuneType type;

	public BlockBloodRune(BloodRuneType type)
	{
		super(Properties.create(Material.ROCK).hardnessAndResistance(2.0F, 5.0F).harvestTool(ToolType.PICKAXE).harvestLevel(2).sound(SoundType.STONE));
		this.type = type;
	}

	@Nullable
	@Override
	public BloodRuneType getBloodRune(World world, BlockPos pos)
	{
		return type;
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable IBlockReader world, List<ITextComponent> tooltip,
			ITooltipFlag flag)
	{
		tooltip.add(new TranslationTextComponent("tooltip.bloodmagic.decoration.safe"));
		super.addInformation(stack, world, tooltip, flag);
	}
}

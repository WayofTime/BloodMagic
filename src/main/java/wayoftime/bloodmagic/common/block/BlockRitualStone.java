package wayoftime.bloodmagic.common.block;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import wayoftime.bloodmagic.ritual.EnumRuneType;
import wayoftime.bloodmagic.ritual.IRitualStone;

public class BlockRitualStone extends Block implements IRitualStone
{
	private final EnumRuneType type;

	public BlockRitualStone(EnumRuneType type)
	{
		super(Properties.create(Material.ROCK).hardnessAndResistance(2.0F, 5.0F).sound(SoundType.STONE).harvestTool(ToolType.PICKAXE).harvestLevel(2).setRequiresTool());
		this.type = type;
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable IBlockReader world, List<ITextComponent> tooltip, ITooltipFlag flag)
	{
		tooltip.add(new TranslationTextComponent("tooltip.bloodmagic.decoration.safe").mergeStyle(TextFormatting.GRAY));
		super.addInformation(stack, world, tooltip, flag);
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
	public boolean isRuneType(World world, BlockPos pos, EnumRuneType runeType)
	{
		return type.equals(runeType);
	}

	@Override
	public void setRuneType(World world, BlockPos pos, EnumRuneType runeType)
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

		BlockState newState = runeBlock.getDefaultState();
		world.setBlockState(pos, newState);
	}
}
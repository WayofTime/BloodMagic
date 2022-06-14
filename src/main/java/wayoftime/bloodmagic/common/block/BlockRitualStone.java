package wayoftime.bloodmagic.common.block;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ToolType;
import wayoftime.bloodmagic.ritual.EnumRuneType;
import wayoftime.bloodmagic.ritual.IRitualStone;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class BlockRitualStone extends Block implements IRitualStone
{
	private final EnumRuneType type;

	public BlockRitualStone(EnumRuneType type)
	{
		super(Properties.of(Material.STONE).strength(2.0F, 5.0F).sound(SoundType.STONE).harvestTool(ToolType.PICKAXE).harvestLevel(2).requiresCorrectToolForDrops());
		this.type = type;
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable BlockGetter world, List<Component> tooltip, TooltipFlag flag)
	{
		tooltip.add(new TranslatableComponent("tooltip.bloodmagic.decoration.safe").withStyle(ChatFormatting.GRAY));
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
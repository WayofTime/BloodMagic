package wayoftime.bloodmagic.common.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;

public class BloodstoneBlock extends Block
{
	public BloodstoneBlock()
	{
		super(Properties.of().strength(2.0F, 5.0F).sound(SoundType.STONE).requiresCorrectToolForDrops());
//.harvestTool(ToolType.PICKAXE).harvestLevel(1)
		// TODO Auto-generated constructor stub
	}
}

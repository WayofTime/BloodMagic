package wayoftime.bloodmagic.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.AbstractBlock.Properties;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;

public class BloodstoneBlock extends Block
{
	public BloodstoneBlock() 
	{
		super(Properties
				.create(Material.ROCK)
				.hardnessAndResistance(2.0F, 5.0F)
				.sound(SoundType.STONE)
				.harvestTool(ToolType.PICKAXE)
				.harvestLevel(1));

		// TODO Auto-generated constructor stub
	}
}

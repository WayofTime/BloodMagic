package WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.earth;


import java.util.LinkedList;
import java.util.List;


import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.tool.ItemManipulator;


public class ToolOffensiveEarth extends ItemManipulator
{
	public static Block[] mundaneList = new Block[]{Blocks.stone,Blocks.cobblestone,Blocks.sand,Blocks.gravel,Blocks.netherrack,Blocks.dirt};


	public ToolOffensiveEarth(int power, int potency, int cost) 
	{
		super(power, potency, cost);
	}


	@Override
	public List<ItemStack> handleItemsOnBlockBroken(ItemStack toolStack, List<ItemStack> itemList) 
	{
		List<ItemStack> newList = new LinkedList();


		for(ItemStack stack : itemList)
		{
			if(stack != null && stack.getItem() instanceof ItemBlock && !this.isMundaneBlock(((ItemBlock)stack.getItem()).field_150939_a))
			{
				newList.add(stack);
			}
		}


		return newList;
	}


	public boolean isMundaneBlock(Block block)
	{
		for(Block test : mundaneList)
		{
			if(test.equals(block))
			{
				return true;
			}
		}


		return false;
	}
}

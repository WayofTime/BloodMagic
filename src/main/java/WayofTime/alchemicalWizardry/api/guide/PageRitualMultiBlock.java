package WayofTime.alchemicalWizardry.api.guide;

import java.util.List;

import net.minecraft.item.ItemStack;
import WayofTime.alchemicalWizardry.api.rituals.RitualComponent;
import WayofTime.alchemicalWizardry.api.rituals.Rituals;

public class PageRitualMultiBlock extends PageMultiBlock 
{
	private static ItemStack blankStone;
	private static ItemStack waterStone;
	private static ItemStack fireStone;
	private static ItemStack earthStone;
	private static ItemStack airStone;
	private static ItemStack duskStone;
	private static ItemStack dawnStone;
	static
	{
//		blankStone = new ItemStack(ModBlocks.ritualStone, 1, RitualComponent.BLANK);
//		waterStone = new ItemStack(ModBlocks.ritualStone, 1, RitualComponent.WATER);
//		fireStone = new ItemStack(ModBlocks.ritualStone, 1, RitualComponent.FIRE);
//		earthStone = new ItemStack(ModBlocks.ritualStone, 1, RitualComponent.EARTH);
//		airStone = new ItemStack(ModBlocks.ritualStone, 1, RitualComponent.AIR);
//		duskStone = new ItemStack(ModBlocks.ritualStone, 1, RitualComponent.DUSK);
//		dawnStone = new ItemStack(ModBlocks.ritualStone, 1, RitualComponent.DAWN);
	}
	
	private PageRitualMultiBlock(ItemStack[][][] structure)
	{
    	super(structure);
	}
	
	public static PageRitualMultiBlock getPageForRitual(String ritualID)
	{
		return getPageForRitual(Rituals.getRitualList(ritualID));
	}
	
	public static PageRitualMultiBlock getPageForRitual(List<RitualComponent> ritualComponents)
	{
		int minX = 0;
		int minY = 0;
		int minZ = 0;
		
		int maxX = 0;
		int maxY = 0;
		int maxZ = 0;
		
		for(RitualComponent comp : ritualComponents)
		{
			minX = Math.min(comp.getX(), minX);
			minY = Math.min(comp.getY(), minY);
			minZ = Math.min(comp.getZ(), minZ);

			maxX = Math.max(comp.getX(), maxX);
			maxY = Math.max(comp.getY(), maxY);
			maxZ = Math.max(comp.getZ(), maxZ);
		}
		
		System.out.println("Min: (" + minX + ", " + minY + ", " + minZ + "), Max: (" + maxX + ", " + maxY + ", " + maxZ + ")");
		
		ItemStack[][][] tempStructure = new ItemStack[maxY-minY+1][maxX-minX+1][maxZ-minZ+1]; //First value is vertical, second is down to the left, third is down to the right
		
		for(RitualComponent comp : ritualComponents)
		{
			tempStructure[comp.getY() - minY][comp.getX() - minX][comp.getZ() - minZ] = getStackForRitualStone(comp.getStoneType());
		}
		
//		tempStructure[-minY][-minX][-minZ] = new ItemStack(ModBlocks.blockMasterStone);

		return new PageRitualMultiBlock(tempStructure);
	}
	
	private static ItemStack getStackForRitualStone(int type)
	{
		switch(type)
		{
		case RitualComponent.BLANK:
			return blankStone;
		case RitualComponent.WATER:
			return waterStone;
		case RitualComponent.FIRE:
			return fireStone;
		case RitualComponent.EARTH:
			return earthStone;
		case RitualComponent.AIR:
			return airStone;
		case RitualComponent.DUSK:
			return duskStone;
		case RitualComponent.DAWN:
			return dawnStone;
		}
		return blankStone;
	}
}

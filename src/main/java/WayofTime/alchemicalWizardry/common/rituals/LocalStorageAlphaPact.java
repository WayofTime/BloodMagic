//package WayofTime.alchemicalWizardry.common.rituals;
//
//import java.util.HashSet;
//import java.util.Set;
//
//import WayofTime.alchemicalWizardry.api.Int3;
//import WayofTime.alchemicalWizardry.api.rituals.LocalRitualStorage;
//import WayofTime.alchemicalWizardry.common.demonVillage.demonHoard.demon.IHoardDemon;
//
//public class LocalStorageAlphaPact extends LocalRitualStorage
//{	
//    public static Set<IHoardDemon> hoardList = new HashSet();
//	
//	public void thrallDemon(IHoardDemon demon)
//	{
//		if(demon instanceof IHoardDemon)
//    	{
//    		boolean enthrall = ((IHoardDemon) demon).thrallDemon(new Int3(this.xCoord, this.yCoord, this.zCoord));
//    		if(enthrall)
//    		{
//        		this.hoardList.add((IHoardDemon)demon);
//    		}
//    	}
//	}
//}

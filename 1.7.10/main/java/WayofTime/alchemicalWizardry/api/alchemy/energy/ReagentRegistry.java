package WayofTime.alchemicalWizardry.api.alchemy.energy;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.minecraft.item.ItemStack;
import WayofTime.alchemicalWizardry.ModItems;

public class ReagentRegistry 
{
	public static Map<String, Reagent> reagentList = new HashMap();
	public static Map<ItemStack, ReagentStack> itemToReagentMap = new HashMap();
	
	public static Reagent sanctusReagent;
	public static Reagent incendiumReagent;
	public static Reagent aquasalusReagent;
	public static Reagent magicalesReagent;
	public static Reagent aetherReagent;
	public static Reagent crepitousReagent;
	public static Reagent crystallosReagent;
	public static Reagent terraeReagent;
	public static Reagent tenebraeReagent;
	
	public static Reagent offensaReagent;
	public static Reagent praesidiumReagent;
	public static Reagent orbisTerraeReagent;
	public static Reagent virtusReagent;
	public static Reagent reductusReagent;
	public static Reagent potentiaReagent;
	
	public static void initReagents()
	{
		sanctusReagent = new Reagent("sanctus");
		incendiumReagent = new Reagent("incendium");
		aquasalusReagent = new Reagent("aquasalus");
		magicalesReagent = new Reagent("magicales");
		aetherReagent = new Reagent("aether");
		crepitousReagent = new Reagent("crepitous");
		crystallosReagent = new Reagent("crystallos");
		terraeReagent = new Reagent("terrae");
		tenebraeReagent = new Reagent("tenebrae");
		offensaReagent = new Reagent("offensa");
		praesidiumReagent = new Reagent("praesidium");
		orbisTerraeReagent = new Reagent("orbisTerrae");
		virtusReagent = new Reagent("virtus");
		reductusReagent = new Reagent("reductus");
		potentiaReagent = new Reagent("potentia");
		
		sanctusReagent.setColour(255, 255, 0, 255);
		incendiumReagent.setColour(255, 0, 0, 255);
		aquasalusReagent.setColour(47, 0, 196, 255);
		magicalesReagent.setColour(150, 0, 146, 255);
		aetherReagent.setColour(105, 223, 86, 255);
		crepitousReagent.setColour(145, 145, 145, 255);
		crystallosReagent.setColour(135, 255, 231, 255);
		terraeReagent.setColour(147, 48, 13, 255);
		tenebraeReagent.setColour(86, 86, 86, 255);
		offensaReagent.setColour(126, 0, 0, 255);
		praesidiumReagent.setColour(135, 135, 135, 255);
		orbisTerraeReagent.setColour(32, 94, 14, 255);
		virtusReagent.setColour(180, 0, 0, 255);
		reductusReagent.setColour(20, 93, 2, 255);
		potentiaReagent.setColour(64, 81, 208, 255);
		
		registerReagent("sanctus", sanctusReagent);
		registerReagent("incendium", incendiumReagent);
		registerReagent("aquasalus", aquasalusReagent);
		registerReagent("magicales", magicalesReagent);
		registerReagent("aether", aetherReagent);
		registerReagent("crepitous", crepitousReagent);
		registerReagent("crystallos", crystallosReagent);
		registerReagent("terrae", terraeReagent);
		registerReagent("tenebrae", tenebraeReagent);
		registerReagent("offensa", offensaReagent);
		registerReagent("praesidium", praesidiumReagent);
		registerReagent("orbisTerrae", orbisTerraeReagent);
		registerReagent("virtus", virtusReagent);
		registerReagent("reductus", reductusReagent);
		registerReagent("potentia", potentiaReagent);
		
		ReagentRegistry.registerItemAndReagent(new ItemStack(ModItems.sanctus), new ReagentStack(sanctusReagent, 1000));
		ReagentRegistry.registerItemAndReagent(new ItemStack(ModItems.incendium), new ReagentStack(incendiumReagent, 1000));
		ReagentRegistry.registerItemAndReagent(new ItemStack(ModItems.aquasalus), new ReagentStack(aquasalusReagent, 1000));
		ReagentRegistry.registerItemAndReagent(new ItemStack(ModItems.magicales), new ReagentStack(magicalesReagent, 1000));
		ReagentRegistry.registerItemAndReagent(new ItemStack(ModItems.aether), new ReagentStack(aetherReagent, 1000));
		ReagentRegistry.registerItemAndReagent(new ItemStack(ModItems.crepitous), new ReagentStack(crepitousReagent, 1000));
		ReagentRegistry.registerItemAndReagent(new ItemStack(ModItems.crystallos), new ReagentStack(crystallosReagent, 1000));
		ReagentRegistry.registerItemAndReagent(new ItemStack(ModItems.terrae), new ReagentStack(terraeReagent, 1000));
		ReagentRegistry.registerItemAndReagent(new ItemStack(ModItems.tennebrae), new ReagentStack(tenebraeReagent, 1000));
		ReagentRegistry.registerItemAndReagent(new ItemStack(ModItems.baseAlchemyItems,1,0), new ReagentStack(offensaReagent, 1000));
		ReagentRegistry.registerItemAndReagent(new ItemStack(ModItems.baseAlchemyItems,1,1), new ReagentStack(praesidiumReagent, 1000));
		ReagentRegistry.registerItemAndReagent(new ItemStack(ModItems.baseAlchemyItems,1,2), new ReagentStack(orbisTerraeReagent, 1000));
		ReagentRegistry.registerItemAndReagent(new ItemStack(ModItems.baseAlchemyItems,1,6), new ReagentStack(virtusReagent, 1000));
		ReagentRegistry.registerItemAndReagent(new ItemStack(ModItems.baseAlchemyItems,1,7), new ReagentStack(reductusReagent, 1000));
		ReagentRegistry.registerItemAndReagent(new ItemStack(ModItems.baseAlchemyItems,1,8), new ReagentStack(potentiaReagent, 1000));
	}
	
	public static boolean registerReagent(String key, Reagent reagent)
	{
		if(reagentList.containsKey(key) || reagent == null)
		{
			return false;
		}
		
		reagentList.put(key, reagent);
		
		return true;
	}
	
	public static Reagent getReagentForKey(String key)
	{
		if(reagentList.containsKey(key))
		{
			return reagentList.get(key);
		}
		
		return null;
	}
	
	public static String getKeyForReagent(Reagent reagent)
	{
		if(reagentList.containsValue(reagent))
		{
			Set<Entry<String, Reagent>> set = reagentList.entrySet();
			for(Entry<String, Reagent> entry : set)
			{
				if(entry.getValue().equals(reagent))
				{
					return entry.getKey();
				}
			}
		}
		
		return "";
	}
	
	public static void registerItemAndReagent(ItemStack stack, ReagentStack reagentStack)
	{
		itemToReagentMap.put(stack, reagentStack);
	}
	
	public static ReagentStack getReagentStackForItem(ItemStack stack)
	{
		if(stack == null)
		{
			return null;
		}
		
		for(Entry<ItemStack, ReagentStack> entry : itemToReagentMap.entrySet())
		{
			if(entry.getKey() != null && entry.getKey().isItemEqual(stack))
			{
				if(entry.getValue() == null)
				{
					return null;
				}else
				{
					return entry.getValue().copy();
				}
			}
		}
		
		return null;
	}
	
	public static ItemStack getItemForReagent(Reagent reagent)
	{
		if(reagent == null)
		{
			return null;
		}
		
		for(Entry<ItemStack, ReagentStack> entry : itemToReagentMap.entrySet())
		{
			if(entry.getValue() != null && entry.getValue().reagent == reagent)
			{
				if(entry.getKey() == null)
				{
					return null;
				}else
				{
					return entry.getKey().copy();
				}
			}
		}
		
		return null;
	}
}

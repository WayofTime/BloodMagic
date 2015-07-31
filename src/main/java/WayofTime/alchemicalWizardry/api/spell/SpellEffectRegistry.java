package WayofTime.alchemicalWizardry.api.spell;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class SpellEffectRegistry 
{
	public static Map<Class<? extends SpellParadigm>, List<ComplexSpellEffect>> effectRegistry = new HashMap();
	public static Map<String, ComplexSpellType> typeRegistry = new HashMap();
	public static Map<String, ComplexSpellModifier> modifierRegistry = new HashMap();
	
	public static void registerSpellEffect(Class<? extends SpellParadigm> paraClass, ComplexSpellEffect effect)
	{
		if(paraClass == null || effect == null)
		{
			return;
		}
		
		if(effectRegistry.containsKey(paraClass))
		{
			List<ComplexSpellEffect> effectList = effectRegistry.get(paraClass);
			ComplexSpellType type = effect.getType();
			ComplexSpellModifier modifier = effect.getModifier();
			
			if(type == null || modifier == null)
			{
				return;
			}
			
			for(ComplexSpellEffect eff : effectList)
			{
				if(type.equals(eff.getType()) && modifier.equals(eff.getModifier()))
				{
					effectList.remove(eff);
					effectList.add(effect);
					return;
				}
			}
			
			effectList.add(effect);
		}else
		{
			List<ComplexSpellEffect> effectList = new LinkedList();
			effectList.add(effect);
			effectRegistry.put(paraClass, effectList);
		}
	}
	
	/**
	 * 
	 * @param paraClass
	 * @param type
	 * @param mod
	 * @return				A copy of the spell effect
	 */
	public static ComplexSpellEffect getSpellEffect(Class<? extends SpellParadigm> paraClass, ComplexSpellType type, ComplexSpellModifier mod)
	{
		return SpellEffectRegistry.getSpellEffect(paraClass, type, mod, 0, 0, 0);
	}
	
	public static ComplexSpellEffect getSpellEffect(Class<? extends SpellParadigm> paraClass, ComplexSpellType type, ComplexSpellModifier mod, int power, int potency, int cost)
	{
		if(paraClass == null || type == null || mod == null)
		{
			return null;
		}
		
		List<ComplexSpellEffect> list = effectRegistry.get(paraClass);
		
		if(list == null || list.isEmpty())
		{
			return null;
		}
		
		for(ComplexSpellEffect effect : list)
		{
			if(effect != null && type.equals(effect.type) && mod.equals(effect.modifier))
			{
				return effect.copy(power, cost, potency);
			}
		}
				
		return null;
	}
	
	public static void registerSpellType(String key, ComplexSpellType type)
	{
		typeRegistry.put(key, type);
	}
	
	public static void registerSpellModifier(String key, ComplexSpellModifier modifier)
	{
		modifierRegistry.put(key, modifier);
	}
	
	public static ComplexSpellType getTypeForKey(String key)
	{
		return typeRegistry.get(key);
	}
	
	public static String getKeyForType(ComplexSpellType type)
	{
		if(type == null)
		{
			return "";
		}
		
		for(Entry<String, ComplexSpellType> entry : typeRegistry.entrySet())
		{
			if(type.equals(entry.getValue()))
			{
				return entry.getKey();
			}
		}
		
		return "";
	}
	
	public static ComplexSpellModifier getModifierForKey(String key)
	{
		return modifierRegistry.get(key);
	}
	
	public static String getKeyForModifier(ComplexSpellModifier modifier)
	{
		if(modifier == null)
		{
			return "";
		}
		
		for(Entry<String, ComplexSpellModifier> entry : modifierRegistry.entrySet())
		{
			if(modifier.equals(entry.getValue()))
			{
				return entry.getKey();
			}
		}
		
		return "";
	}
	
	public static void initiateRegistry()
	{
		SpellEffectRegistry.registerSpellType("FIRE", ComplexSpellType.FIRE);
		SpellEffectRegistry.registerSpellType("ICE", ComplexSpellType.ICE);
		SpellEffectRegistry.registerSpellType("EARTH", ComplexSpellType.EARTH);
		SpellEffectRegistry.registerSpellType("WIND", ComplexSpellType.WIND);

		SpellEffectRegistry.registerSpellModifier("DEFAULT", ComplexSpellModifier.DEFAULT);
		SpellEffectRegistry.registerSpellModifier("OFFENSIVE", ComplexSpellModifier.OFFENSIVE);
		SpellEffectRegistry.registerSpellModifier("DEFENSIVE", ComplexSpellModifier.DEFENSIVE);
		SpellEffectRegistry.registerSpellModifier("ENVIRONMENTAL", ComplexSpellModifier.ENVIRONMENTAL);
	}
}
package WayofTime.alchemicalWizardry.api.spell;


public abstract class ComplexSpellEffect
{
	public final ComplexSpellType type;
	public final ComplexSpellModifier modifier;
	
	protected int powerEnhancement;
    protected int costEnhancement;
    protected int potencyEnhancement;
	
	public ComplexSpellEffect(ComplexSpellType type, ComplexSpellModifier modifier)
	{
		this.type = type;
		this.modifier = modifier;
	}
	
	public ComplexSpellEffect(ComplexSpellType type, ComplexSpellModifier modifier, int power, int cost, int potency)
	{
		this(type, modifier);
		
		this.powerEnhancement = power;
		this.costEnhancement = cost;
		this.potencyEnhancement = potency;
	}
	
	public abstract void modifyParadigm(SpellParadigm parad);
	
	public ComplexSpellType getType()
	{
		return this.type;
	}
	
	public ComplexSpellModifier getModifier()
	{
		return this.modifier;
	}
	
	public abstract ComplexSpellEffect copy(int power, int cost, int potency);
	
	public abstract int getCostOfEffect();
	
//	public NBTTagCompound getTag()
//    {
//        NBTTagCompound tag = new NBTTagCompound();
//
//        tag.setString("Class", this.getClass().getName());
//        tag.setInteger("modifier", modifierState);
//        tag.setInteger("power", powerEnhancement);
//        tag.setInteger("cost", costEnhancement);
//        tag.setInteger("potency", potencyEnhancement);
//
//        return tag;
//    }
//
//    public static SpellEffect getEffectFromTag(NBTTagCompound tag)
//    {
//        try
//        {
//            Class clazz = Class.forName(tag.getString("Class"));
//            if (clazz != null)
//            {
//                try
//                {
//                    Object obj = clazz.newInstance();
//                    if (obj instanceof SpellEffect)
//                    {
//                        SpellEffect eff = (SpellEffect) obj;
//
//                        eff.modifierState = tag.getInteger("modifier");
//                        eff.powerEnhancement = tag.getInteger("power");
//                        eff.costEnhancement = tag.getInteger("cost");
//                        eff.potencyEnhancement = tag.getInteger("potency");
//
//                        return eff;
//                    }
//                } catch (InstantiationException e)
//                {
//                    e.printStackTrace();
//                } catch (IllegalAccessException e)
//                {
//                    e.printStackTrace();
//                }
//            }
//        } catch (ClassNotFoundException e)
//        {
//            e.printStackTrace();
//        }
//        return null;
//    }
}
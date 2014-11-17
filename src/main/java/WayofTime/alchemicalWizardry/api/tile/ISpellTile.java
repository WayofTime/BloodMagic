package WayofTime.alchemicalWizardry.api.tile;

import net.minecraftforge.common.util.ForgeDirection;
import WayofTime.alchemicalWizardry.api.spell.SpellParadigm;

public interface ISpellTile 
{
	public void modifySpellParadigm(SpellParadigm parad);	
	
	public boolean canInputRecieveOutput(ForgeDirection output);
}

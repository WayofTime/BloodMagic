package WayofTime.alchemicalWizardry.api.tile;

import net.minecraftforge.common.util.ForgeDirection;
import WayofTime.alchemicalWizardry.api.spell.SpellParadigm;

public interface ISpellTile 
{
	void modifySpellParadigm(SpellParadigm parad);
	
	boolean canInputRecieveOutput(ForgeDirection output);
}

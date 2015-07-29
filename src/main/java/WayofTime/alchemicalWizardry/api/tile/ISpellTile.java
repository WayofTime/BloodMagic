package WayofTime.alchemicalWizardry.api.tile;

import net.minecraft.util.EnumFacing;
import WayofTime.alchemicalWizardry.api.spell.SpellParadigm;

public interface ISpellTile 
{
	void modifySpellParadigm(SpellParadigm parad);
	
	boolean canInputRecieveOutput(EnumFacing output);
}

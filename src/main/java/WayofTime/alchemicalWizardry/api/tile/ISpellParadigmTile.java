package WayofTime.alchemicalWizardry.api.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface ISpellParadigmTile extends ISpellTile
{
	void castSpell(World world, EntityPlayer entity, ItemStack spellCasterStack);
}

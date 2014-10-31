package WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.tool;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.common.items.spell.ItemSpellMultiTool;

public interface IDigAreaEffect 
{
	public abstract int digSurroundingArea(ItemStack container, World world, EntityPlayer player, MovingObjectPosition blockPos, String usedToolClass, float blockHardness, int harvestLvl, ItemSpellMultiTool itemTool);
}

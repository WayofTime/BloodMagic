package WayofTime.alchemicalWizardry.api.spell;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public interface ISelfSpellEffect
{
    public void onSelfUse(World world, EntityPlayer player);
}

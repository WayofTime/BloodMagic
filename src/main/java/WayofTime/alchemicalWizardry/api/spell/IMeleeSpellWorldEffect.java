package WayofTime.alchemicalWizardry.api.spell;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public interface IMeleeSpellWorldEffect
{
    public void onWorldEffect(World world, EntityPlayer entityPlayer);
}

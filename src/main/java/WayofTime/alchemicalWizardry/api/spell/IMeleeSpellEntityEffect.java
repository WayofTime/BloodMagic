package WayofTime.alchemicalWizardry.api.spell;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public interface IMeleeSpellEntityEffect
{
    public void onEntityImpact(World world, EntityPlayer entityPlayer);
}

package WayofTime.alchemicalWizardry.api.spell;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public interface IMeleeSpellWorldEffect
{
    void onWorldEffect(World world, EntityPlayer entityPlayer);
}

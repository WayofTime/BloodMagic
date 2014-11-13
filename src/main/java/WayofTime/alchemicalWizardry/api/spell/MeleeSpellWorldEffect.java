package WayofTime.alchemicalWizardry.api.spell;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public abstract class MeleeSpellWorldEffect implements IMeleeSpellWorldEffect
{
    protected int powerUpgrades;
    protected int potencyUpgrades;
    protected int costUpgrades;

    public MeleeSpellWorldEffect(int power, int potency, int cost)
    {
        this.powerUpgrades = power;
        this.potencyUpgrades = potency;
        this.costUpgrades = cost;
    }

    @Override
    public abstract void onWorldEffect(World world, EntityPlayer entityPlayer);
}

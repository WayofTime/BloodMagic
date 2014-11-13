package WayofTime.alchemicalWizardry.api.spell;

import net.minecraft.entity.Entity;

public interface ISpecialDamageEffect
{
    public float getDamageForEntity(Entity entity);

    public String getKey();
}

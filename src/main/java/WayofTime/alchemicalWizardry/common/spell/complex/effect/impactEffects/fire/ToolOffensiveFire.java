package WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.fire;

import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.tool.LeftClickEffect;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public class ToolOffensiveFire extends LeftClickEffect
{
    public ToolOffensiveFire(int power, int potency, int cost)
    {
        super(power, potency, cost);
    }

    @Override
    public int onLeftClickEntity(ItemStack stack, EntityLivingBase attacked, EntityLivingBase weilder)
    {
        attacked.setFire(3 + 4 * this.powerUpgrades);

        return (int) (10 * (1 + this.powerUpgrades) * Math.pow(0.85, this.costUpgrades));
    }
}

package WayofTime.alchemicalWizardry.api.spell;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public abstract class SpellParadigm
{
    protected List<SpellEffect> bufferedEffectList = new LinkedList();

    public void addBufferedEffect(SpellEffect effect)
    {
        if (effect != null)
        {
            this.bufferedEffectList.add(effect);
        }
    }

    public void modifyBufferedEffect(ComplexSpellModifier modifier)
    {
        SpellEffect effect = this.getBufferedEffect();
        if (effect != null)
        {
            effect.modifyEffect(modifier);
        }
    }

    public void applyEnhancement(SpellEnhancement enh)
    {
        if (enh != null)
        {
            if (bufferedEffectList.isEmpty())
            {
                this.enhanceParadigm(enh);
            } else
            {
                SpellEffect effect = this.getBufferedEffect();
                if (effect != null)
                {
                    effect.enhanceEffect(enh);
                }
            }
        }

    }

    public abstract void enhanceParadigm(SpellEnhancement enh);

    public abstract void castSpell(World world, EntityPlayer entityPlayer, ItemStack itemStack);

    public void applySpellEffect(SpellEffect effect)
    {
        effect.modifyParadigm(this);
    }

    public void applyAllSpellEffects()
    {
        for (SpellEffect effect : bufferedEffectList)
        {
            this.applySpellEffect(effect);
        }
    }

    public SpellEffect getBufferedEffect()
    {
        if (bufferedEffectList.isEmpty())
        {
            return null;
        } else
        {
            return bufferedEffectList.get(bufferedEffectList.size() - 1);
        }
    }

    public int getTotalCost()
    {
        int cost = 0;
        if (this.bufferedEffectList != null && !this.bufferedEffectList.isEmpty())
        {
        	for(SpellEffect effect : bufferedEffectList)
        	{
        		cost += effect.getCostOfEffect(this);
        	}

            return (int) (cost * Math.sqrt(this.bufferedEffectList.size()));
        }

        return getDefaultCost();
    }

    public abstract int getDefaultCost();

    public int getBufferedEffectPower()
    {
        SpellEffect eff = this.getBufferedEffect();

        if (eff != null)
        {
            return eff.getPowerEnhancements();
        }

        return 0;
    }

    public int getBufferedEffectCost()
    {
        SpellEffect eff = this.getBufferedEffect();

        if (eff != null)
        {
            return eff.getCostEnhancements();
        }

        return 0;
    }

    public int getBufferedEffectPotency()
    {
        SpellEffect eff = this.getBufferedEffect();

        if (eff != null)
        {
            return eff.getPotencyEnhancements();
        }

        return 0;
    }
}

package WayofTime.alchemicalWizardry.common.spell.complex;

import WayofTime.alchemicalWizardry.common.items.EnergyItems;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.ISelfSpellEffect;
import WayofTime.alchemicalWizardry.common.spell.complex.enhancement.SpellEnhancement;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class SpellParadigmSelf extends SpellParadigm
{
    public List<ISelfSpellEffect> selfSpellEffectList;

    public SpellParadigmSelf()
    {
        selfSpellEffectList = new ArrayList();
    }

    @Override
    public void enhanceParadigm(SpellEnhancement enh)
    {

    }

    @Override
    public void castSpell(World world, EntityPlayer entityPlayer, ItemStack itemStack)
    {
        this.applyAllSpellEffects();
        
        int cost = this.getTotalCost();
        
        if(!EnergyItems.syphonBatteries(itemStack, entityPlayer, cost))
        {
        	return;
        }

        for (ISelfSpellEffect eff : selfSpellEffectList)
        {
            eff.onSelfUse(world, entityPlayer);
        }
    }

    public void addSelfSpellEffect(ISelfSpellEffect eff)
    {
        if (eff != null)
        {
            this.selfSpellEffectList.add(eff);
        }
    }

    @Override
    public int getDefaultCost()
    {
        return 100;
    }

}

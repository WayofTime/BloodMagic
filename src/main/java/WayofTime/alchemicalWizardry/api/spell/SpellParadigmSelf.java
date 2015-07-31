package WayofTime.alchemicalWizardry.api.spell;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.api.soulNetwork.SoulNetworkHandler;

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
        
        if(!SoulNetworkHandler.syphonAndDamageFromNetwork(itemStack, entityPlayer, cost))
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

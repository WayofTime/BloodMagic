package WayofTime.alchemicalWizardry.api.spell;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.api.soulNetwork.SoulNetworkHandler;

public class SpellParadigmMelee extends SpellParadigm
{
    private List<IMeleeSpellEntityEffect> entityEffectList;
    private List<IMeleeSpellWorldEffect> worldEffectList;

    public SpellParadigmMelee()
    {
        this.entityEffectList = new ArrayList();
        this.worldEffectList = new ArrayList();
    }

    @Override
    public void enhanceParadigm(SpellEnhancement enh)
    {

    }

    @Override
    public void castSpell(World world, EntityPlayer entityPlayer, ItemStack itemStack)
    {
    	int cost = this.getTotalCost();
        
        if(!SoulNetworkHandler.syphonAndDamageFromNetwork(itemStack, entityPlayer, cost))
        {
        	return;
        }
                
        for (IMeleeSpellEntityEffect effect : entityEffectList)
        {
            effect.onEntityImpact(world, entityPlayer);
        }

        for (IMeleeSpellWorldEffect effect : worldEffectList)
        {
            effect.onWorldEffect(world, entityPlayer);
        }
    }

    public void addEntityEffect(IMeleeSpellEntityEffect eff)
    {
        if (eff != null)
        {
            this.entityEffectList.add(eff);
        }
    }

    public void addWorldEffect(IMeleeSpellWorldEffect eff)
    {
        if (eff != null)
        {
            this.worldEffectList.add(eff);
        }
    }

    @Override
    public int getDefaultCost()
    {
        return 0;
    }
}

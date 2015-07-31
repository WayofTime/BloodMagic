package WayofTime.alchemicalWizardry.api.spell;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.api.soulNetwork.SoulNetworkHandler;

public class SpellParadigmProjectile extends SpellParadigm
{
    public DamageSource damageSource;
    public float damage;
    public int cost;
    public List<IProjectileImpactEffect> impactList;
    public List<IProjectileUpdateEffect> updateEffectList;
    public boolean penetration;
    public int ricochetMax;
    public boolean isSilkTouch;

    public SpellParadigmProjectile()
    {
        this.damageSource = DamageSource.generic;
        this.damage = 1;
        this.cost = 0;
        this.impactList = new ArrayList();
        this.updateEffectList = new ArrayList();
        this.penetration = false;
        this.ricochetMax = 0;
        this.isSilkTouch = false;
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
        
        EntitySpellProjectile proj = new EntitySpellProjectile(world, entityPlayer);
        this.prepareProjectile(proj);
        world.spawnEntityInWorld(proj);
    }

    public static SpellParadigmProjectile getParadigmForEffectArray(List<SpellEffect> effectList)
    {
        SpellParadigmProjectile parad = new SpellParadigmProjectile();

        for (SpellEffect eff : effectList)
        {
            parad.addBufferedEffect(eff);
        }

        return parad;
    }

    public void prepareProjectile(EntitySpellProjectile proj)
    {
        proj.setDamage(damage);
        proj.setImpactList(impactList);
        proj.setUpdateEffectList(updateEffectList);
        proj.setPenetration(penetration);
        proj.setRicochetMax(ricochetMax);
        proj.setIsSilkTouch(isSilkTouch);
        proj.setSpellEffectList(bufferedEffectList);
    }

    public void addImpactEffect(IProjectileImpactEffect eff)
    {
        if (eff != null)
        {
            this.impactList.add(eff);
        }
    }

    public void addUpdateEffect(IProjectileUpdateEffect eff)
    {
        if (eff != null)
        {
            this.updateEffectList.add(eff);
        }
    }

    @Override
    public int getDefaultCost()
    {
        return 50;
    }

}

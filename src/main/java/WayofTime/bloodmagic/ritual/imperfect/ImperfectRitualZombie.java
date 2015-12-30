package WayofTime.bloodmagic.ritual.imperfect;

import WayofTime.bloodmagic.api.BlockStack;
import WayofTime.bloodmagic.api.ritual.imperfect.IImperfectRitualStone;
import WayofTime.bloodmagic.api.ritual.imperfect.ImperfectRitual;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class ImperfectRitualZombie extends ImperfectRitual
{

    public ImperfectRitualZombie()
    {
        super("zombie", new BlockStack(Blocks.coal_block), 5000);
    }

    @Override
    public boolean onActivate(IImperfectRitualStone imperfectRitualStone, EntityPlayer player)
    {
        EntityZombie zombie = new EntityZombie(imperfectRitualStone.getWorld());
        zombie.setPosition(imperfectRitualStone.getPos().getX() + 0.5, imperfectRitualStone.getPos().getY() + 2.1, imperfectRitualStone.getPos().getZ() + 0.5);
        zombie.addPotionEffect(new PotionEffect(Potion.fireResistance.getId(), 2000));
        zombie.addPotionEffect(new PotionEffect(Potion.damageBoost.getId(), 20000, 7));
        zombie.addPotionEffect(new PotionEffect(Potion.resistance.getId(), 20000, 3));

        if (!imperfectRitualStone.getWorld().isRemote)
            imperfectRitualStone.getWorld().spawnEntityInWorld(zombie);

        return true;
    }
}

package WayofTime.bloodmagic.ritual.types.imperfect;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.ritual.RitualRegister;
import WayofTime.bloodmagic.ritual.imperfect.IImperfectRitualStone;
import WayofTime.bloodmagic.ritual.imperfect.ImperfectRitual;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.block.Blocks;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;

@RitualRegister.Imperfect("zombie")
public class ImperfectRitualZombie extends ImperfectRitual {
    public ImperfectRitualZombie() {
        super("zombie", s -> s.getBlock() == Blocks.COAL_BLOCK, 5000, "ritual." + BloodMagic.MODID + ".imperfect.zombie");
    }

    @Override
    public boolean onActivate(IImperfectRitualStone imperfectRitualStone, PlayerEntity player) {
        ZombieEntity zombie = new ZombieEntity(imperfectRitualStone.getRitualWorld());
        zombie.setPosition(imperfectRitualStone.getRitualPos().getX() + 0.5, imperfectRitualStone.getRitualPos().getY() + 2.1, imperfectRitualStone.getRitualPos().getZ() + 0.5);
        zombie.addPotionEffect(new EffectInstance(Effects.FIRE_RESISTANCE, 2000));
        zombie.addPotionEffect(new EffectInstance(Effects.STRENGTH, 20000, 7));
        zombie.addPotionEffect(new EffectInstance(Effects.RESISTANCE, 20000, 3));

        if (!imperfectRitualStone.getRitualWorld().isRemote)
            imperfectRitualStone.getRitualWorld().spawnEntity(zombie);

        return true;
    }
}

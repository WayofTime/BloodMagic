package WayofTime.bloodmagic.item.sigil.sigil;

import WayofTime.bloodmagic.core.RegistrarBloodMagic;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.List;

public class SigilEnderSeverance implements ISigil.Toggle {

    @Override
    public void onUpdate(@Nonnull ItemStack stack, @Nonnull EntityPlayer player, @Nonnull World world, int itemSlot, boolean isHeld) {
        List<Entity> entityList = world.getEntitiesWithinAABB(Entity.class, new net.minecraft.util.math.AxisAlignedBB(player.posX - 4.5, player.posY - 4.5, player.posZ - 4.5, player.posX + 4.5, player.posY + 4.5, player.posZ + 4.5));
        entityList.stream().filter(e -> e instanceof EntityEnderman).forEach(e -> ((EntityEnderman) e).addPotionEffect(new PotionEffect(RegistrarBloodMagic.PLANAR_BINDING, 40, 0)));
    }

    @Override
    public int getCost() {
        return 200;
    }
}

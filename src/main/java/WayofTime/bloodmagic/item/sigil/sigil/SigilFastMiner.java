package WayofTime.bloodmagic.item.sigil.sigil;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class SigilFastMiner implements ISigil.Toggle {

    @Override
    public void onUpdate(@Nonnull ItemStack stack, @Nonnull EntityPlayer player, @Nonnull World world, int itemSlot, boolean isHeld) {
        player.addPotionEffect(new PotionEffect(MobEffects.HASTE, 2, 0, true, false));
    }

    @Override
    public int getCost() {
        return 100;
    }

//    @Override
//    public boolean performArrayEffect(World world, BlockPos pos) {
//        double radius = 10;
//        int ticks = 600;
//        int potionPotency = 2;
//
//        AxisAlignedBB bb = new AxisAlignedBB(pos).grow(radius);
//        List<EntityPlayer> playerList = world.getEntitiesWithinAABB(EntityPlayer.class, bb);
//        for (EntityPlayer player : playerList) {
//            if (!player.isPotionActive(MobEffects.HASTE) || (player.isPotionActive(MobEffects.HASTE) && player.getActivePotionEffect(MobEffects.HASTE).getAmplifier() < potionPotency)) {
//                player.addPotionEffect(new PotionEffect(MobEffects.HASTE, ticks, potionPotency));
//                if (!player.capabilities.isCreativeMode) {
//                    player.hurtResistantTime = 0;
//                    player.attackEntityFrom(BloodMagicAPI.damageSource, 1.0F);
//                }
//            }
//        }
//
//        return false;
//    }
//
//    @Override
//    public boolean hasArrayEffect() {
//        return true;
//    }
}

package WayofTime.bloodmagic.item.sigil;

import WayofTime.bloodmagic.core.RegistrarBloodMagic;
import WayofTime.bloodmagic.core.data.SoulTicket;
import WayofTime.bloodmagic.iface.ISentientSwordEffectProvider;
import WayofTime.bloodmagic.iface.ISigil;
import WayofTime.bloodmagic.soul.EnumDemonWillType;
import WayofTime.bloodmagic.util.helper.NetworkHelper;
import WayofTime.bloodmagic.util.helper.PlayerHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effects;
import net.minecraft.util.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.*;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ItemSigilAir extends ItemSigilBase implements ISentientSwordEffectProvider {
    public ItemSigilAir() {
        super("air", 50);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (stack.getItem() instanceof ISigil.Holding)
            stack = ((Holding) stack.getItem()).getHeldItem(stack, player);
        if (PlayerHelper.isFakePlayer(player))
            return ActionResult.newResult(ActionResultType.FAIL, stack);

        boolean unusable = isUnusable(stack);
        if (world.isRemote && !unusable) {
            Vec3d vec = player.getLookVec();
            double wantedVelocity = 1.7;

            // TODO - Revisit after potions
            if (player.isPotionActive(RegistrarBloodMagic.BOOST)) {
                int amplifier = player.getActivePotionEffect(RegistrarBloodMagic.BOOST).getAmplifier();
                wantedVelocity += (1 + amplifier) * (0.35);
            }

            player.motionX = vec.x * wantedVelocity;
            player.motionY = vec.y * wantedVelocity;
            player.motionZ = vec.z * wantedVelocity;
            world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);
        }

        if (!world.isRemote) {
            if (!player.capabilities.isCreativeMode)
                this.setUnusable(stack, !NetworkHelper.getSoulNetwork(getBinding(stack)).syphonAndDamage(player, SoulTicket.item(stack, world, player, getLpUsed())).isSuccess());

            if (!unusable)
                player.fallDistance = 0;
        }

        return super.onItemRightClick(world, player, hand);
    }

    @Override
    public boolean applyOnHitEffect(EnumDemonWillType type, ItemStack swordStack, ItemStack providerStack, LivingEntity attacker, LivingEntity target) {
        target.addPotionEffect(new EffectInstance(Effects.LEVITATION, 200, 0));
        return true;
    }

    @Override
    public boolean providesEffectForWill(EnumDemonWillType type) {
        return false;
    }
}

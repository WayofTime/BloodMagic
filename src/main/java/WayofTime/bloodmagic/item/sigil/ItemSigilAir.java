package WayofTime.bloodmagic.item.sigil;

import WayofTime.bloodmagic.core.RegistrarBloodMagic;
import WayofTime.bloodmagic.core.data.SoulTicket;
import WayofTime.bloodmagic.iface.ISentientSwordEffectProvider;
import WayofTime.bloodmagic.iface.ISigil;
import WayofTime.bloodmagic.soul.EnumDemonWillType;
import WayofTime.bloodmagic.util.helper.NetworkHelper;
import WayofTime.bloodmagic.util.helper.PlayerHelper;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ItemSigilAir extends ItemSigilBase implements ISentientSwordEffectProvider {
    public ItemSigilAir() {
        super("air", 50);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (stack.getItem() instanceof ISigil.Holding)
            stack = ((Holding) stack.getItem()).getHeldItem(stack, player);
        if (PlayerHelper.isFakePlayer(player))
            return ActionResult.newResult(EnumActionResult.FAIL, stack);

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
    public boolean applyOnHitEffect(EnumDemonWillType type, int willLevel, ItemStack swordStack, ItemStack providerStack, EntityLivingBase attacker, EntityLivingBase target) {
	//to ensure it will not return 0
        willLevel += 1;
        int LPUsage = getLpUsed() * willLevel;

        if (NetworkHelper.getSoulNetwork(getBinding(providerStack)).syphonAndDamage((EntityPlayer) attacker, SoulTicket.item(providerStack, attacker.getEntityWorld(), attacker, LPUsage)).isSuccess()) {
            int knockbackLevel = EnchantmentHelper.getKnockbackModifier(attacker);
            target.addVelocity(-0.2 * attacker.motionX, 0.075 * (willLevel + knockbackLevel), -0.2 * attacker.motionZ);
            target.addPotionEffect(new PotionEffect(RegistrarBloodMagic.HEAVY_HEART, 30 * (willLevel + knockbackLevel), 1));
            return true;
        }
        return false;
    }
}

package WayofTime.bloodmagic.item.sigil;

import WayofTime.bloodmagic.api.util.helper.PlayerHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import WayofTime.bloodmagic.api.iface.ISentientSwordEffectProvider;
import WayofTime.bloodmagic.api.soul.EnumDemonWillType;
import WayofTime.bloodmagic.api.util.helper.NetworkHelper;
import WayofTime.bloodmagic.registry.ModPotions;

public class ItemSigilAir extends ItemSigilBase implements ISentientSwordEffectProvider
{
    public ItemSigilAir()
    {
        super("air", 50);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand)
    {
        if (PlayerHelper.isFakePlayer(player))
            return ActionResult.newResult(EnumActionResult.FAIL, stack);

        boolean unusable = isUnusable(stack);
        if (world.isRemote && !unusable)
        {
            Vec3d vec = player.getLookVec();
            double wantedVelocity = 1.7;

            // TODO - Revisit after potions
            if (player.isPotionActive(ModPotions.boost))
            {
                int amplifier = player.getActivePotionEffect(ModPotions.boost).getAmplifier();
                wantedVelocity += (1 + amplifier) * (0.35);
            }

            player.motionX = vec.xCoord * wantedVelocity;
            player.motionY = vec.yCoord * wantedVelocity;
            player.motionZ = vec.zCoord * wantedVelocity;
            world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);
        }

        if (!world.isRemote)
        {
            if (!player.capabilities.isCreativeMode)
                this.setUnusable(stack, !NetworkHelper.getSoulNetwork(player).syphonAndDamage(player, getLpUsed()));

            if (!unusable)
                player.fallDistance = 0;
        }

        return super.onItemRightClick(stack, world, player, hand);
    }

    @Override
    public boolean applyOnHitEffect(EnumDemonWillType type, ItemStack swordStack, ItemStack providerStack, EntityLivingBase attacker, EntityLivingBase target)
    {
        target.addPotionEffect(new PotionEffect(MobEffects.LEVITATION, 200, 0));
        return true;
    }

    @Override
    public boolean providesEffectForWill(EnumDemonWillType type)
    {
        return false;
    }
}

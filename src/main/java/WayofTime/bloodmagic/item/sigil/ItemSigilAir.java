package WayofTime.bloodmagic.item.sigil;

import WayofTime.bloodmagic.api.util.helper.NetworkHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import WayofTime.bloodmagic.api.Constants;

public class ItemSigilAir extends ItemSigilBase
{
    public ItemSigilAir()
    {
        super("air", 50);
        setRegistryName(Constants.BloodMagicItem.SIGIL_AIR.getRegName());
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand)
    {
        if (!world.isRemote && !isUnusable(stack))
        {
            Vec3d vec = player.getLookVec();
            double wantedVelocity = 1.7;

            // TODO - Revisit after potions
            // if (player.isPotionActive(ModPotions.customPotionBoost)) {
            // int amplifier =
            // player.getActivePotionEffect(ModPotions.customPotionBoost).getAmplifier();
            // wantedVelocity += (1 + amplifier) * (0.35);
            // }

            player.motionX = vec.xCoord * wantedVelocity;
            player.motionY = vec.yCoord * wantedVelocity;
            player.motionZ = vec.zCoord * wantedVelocity;
            player.velocityChanged = true;
            world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.block_fire_extinguish, SoundCategory.BLOCKS, 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);

            player.fallDistance = 0;

            if (!player.capabilities.isCreativeMode)
                this.setUnusable(stack, !NetworkHelper.getSoulNetwork(player).syphonAndDamage(player, getLpUsed()));
        }

        return super.onItemRightClick(stack, world, player, hand);
    }
}

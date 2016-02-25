package WayofTime.bloodmagic.item.sigil;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Vec3;
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
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
    {
        if (!world.isRemote && !isUnusable(stack))
        {
            Vec3 vec = player.getLookVec();
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
            world.playSoundEffect((double) ((float) player.posX + 0.5F), (double) ((float) player.posY + 0.5F), (double) ((float) player.posZ + 0.5F), "random.fizz", 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);
            player.fallDistance = 0;

            if (!player.capabilities.isCreativeMode)
                this.setUnusable(stack, !syphonNetwork(stack, player, getLPUsed()));
        }

        return super.onItemRightClick(stack, world, player);
    }
}

package WayofTime.bloodmagic.item.sigil.sigil;

import WayofTime.bloodmagic.api.util.helper.NetworkHelper;
import WayofTime.bloodmagic.core.RegistrarBloodMagicBlocks;
import WayofTime.bloodmagic.entity.projectile.EntityBloodLight;
import WayofTime.bloodmagic.util.Utils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.CooldownTracker;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class SigilBloodLight implements ISigil {

    @Nonnull
    @Override
    public EnumActionResult onRightClick(@Nonnull ItemStack stack, @Nonnull EntityPlayer player, @Nonnull World world, @Nonnull EnumHand hand) {
        Item item = stack.getItem();
        RayTraceResult mop = Utils.rayTrace(player, false);

        CooldownTracker cooldownTracker = player.getCooldownTracker();
        if (mop != null && mop.typeOfHit == RayTraceResult.Type.BLOCK) {
            BlockPos blockPos = mop.getBlockPos().offset(mop.sideHit);

            if (world.getBlockState(blockPos).getBlock().isReplaceable(world, blockPos)) {
                world.setBlockState(blockPos, RegistrarBloodMagicBlocks.BLOOD_LIGHT.getDefaultState());
                if (!world.isRemote)
                    NetworkHelper.getSoulNetwork(getOwnerUUID(stack)).syphonAndDamage(player, getCost());
                player.swingArm(hand);
                cooldownTracker.setCooldown(item, 10);
                return EnumActionResult.SUCCESS;
            }
        } else {
            if (!world.isRemote) {
                world.spawnEntity(new EntityBloodLight(world, player));
                NetworkHelper.getSoulNetwork(getOwnerUUID(stack)).syphonAndDamage(player, getCost());
            }
            cooldownTracker.setCooldown(item, 10);
            return EnumActionResult.SUCCESS;
        }

        return EnumActionResult.PASS;
    }

    @Override
    public int getCost() {
        return 10;
    }
}

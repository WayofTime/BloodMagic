package WayofTime.bloodmagic.item;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.client.IVariantProvider;
import WayofTime.bloodmagic.core.data.SoulNetwork;
import WayofTime.bloodmagic.util.helper.NetworkHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class ItemBloodOrbTesting extends Item implements IVariantProvider {

    public ItemBloodOrbTesting() {
        setTranslationKey(BloodMagic.MODID + ".orb.testing");
        setCreativeTab(BloodMagic.TAB_BM);
        setMaxStackSize(1);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        SoulNetwork network = NetworkHelper.getSoulNetwork(player);
        if (!world.isRemote)
            if (player.isSneaking()) {
                network.setCurrentEssence(Integer.MAX_VALUE);
                player.sendMessage(new TextComponentTranslation("commands.bloodmagic.network.fill.success", player.getDisplayName()));
            } else {
                network.setCurrentEssence(NetworkHelper.getMaximumForTier(network.getOrbTier()));
                player.sendMessage(new TextComponentTranslation("commands.bloodmagic.network.cap.success", player.getDisplayName()));
            }
        return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
    }

    @Override
    public boolean onEntitySwing(EntityLivingBase entityLiving, ItemStack stack) {
        if (!entityLiving.world.isRemote && entityLiving instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entityLiving;
            SoulNetwork network = NetworkHelper.getSoulNetwork(player);

            RayTraceResult ray = this.rayTrace(player.getEntityWorld(), player, false);
            if (ray != null && ray.typeOfHit == RayTraceResult.Type.BLOCK) {
                return false;
            }

            if (!player.isSwingInProgress) {
                if (player.isSneaking()) {
                    network.setCurrentEssence(0);
                    player.sendMessage(new TextComponentTranslation("commands.bloodmagic.network.set.success", player.getDisplayName(), 0));
                } else {
                    player.sendMessage(new TextComponentTranslation("tooltip.bloodmagic.sigil.divination.currentEssence", network.getCurrentEssence()));
                }
            }
        }

        return false;
    }

    @Override
    public ItemStack getContainerItem(ItemStack stack) {
        return stack.copy();
    }

    @Override
    public boolean hasContainerItem(ItemStack stack) {
        return true;
    }
}

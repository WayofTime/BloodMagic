package WayofTime.bloodmagic.item.sigil.sigil;

import WayofTime.bloodmagic.api.teleport.ITeleport;
import WayofTime.bloodmagic.api.teleport.TeleportQueue;
import WayofTime.bloodmagic.ritual.portal.Teleports;
import WayofTime.bloodmagic.tile.TileTeleposer;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ItemSigilTeleposition extends ItemSigil {

    public ItemSigilTeleposition() {
        super(new SigilTeleposition(), "teleposition");
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);

        if (!stack.hasTagCompound() || !stack.getTagCompound().hasKey("dim"))
            return;

        NBTTagCompound tag = stack.getTagCompound();
        BlockPos pos = BlockPos.fromLong(tag.getLong("pos"));

        tooltip.add("");
        tooltip.add(I18n.format("tooltip.bloodmagic.telepositionFocus.coords", pos.getX(), pos.getY(), pos.getZ()));
        tooltip.add(I18n.format("tooltip.bloodmagic.telepositionFocus.dimension", tag.getInteger("dim")));
    }

    public static class SigilTeleposition implements ISigil {

        @Override
        public int getCost() {
            return 0;
        }

        @Nonnull
        @Override
        public EnumActionResult onRightClick(@Nonnull ItemStack stack, @Nonnull EntityPlayer player, @Nonnull World world, @Nonnull EnumHand hand) {
            if (world.isRemote || !stack.hasTagCompound() || !stack.getTagCompound().hasKey("dim"))
                return EnumActionResult.PASS;

            NBTTagCompound tag = stack.getTagCompound();
            BlockPos pos = BlockPos.fromLong(tag.getLong("pos"));
            int dimension = tag.getInteger("dim");

            ITeleport teleport;
            if (world.provider.getDimension() == dimension)
                teleport = new Teleports.TeleportSameDim(pos, player, getOwnerUUID(stack), true);
            else
                teleport = new Teleports.TeleportToDim(pos, player, getOwnerUUID(stack), world, dimension, true);

            TeleportQueue.getInstance().addITeleport(teleport);

            return EnumActionResult.SUCCESS;
        }

        @Override
        public EnumActionResult onInteract(@Nonnull ItemStack stack, @Nonnull EntityPlayer player, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull EnumFacing side, @Nonnull EnumHand hand) {
            if (!stack.hasTagCompound())
                stack.setTagCompound(new NBTTagCompound());

            if (!(world.getTileEntity(pos) instanceof TileTeleposer))
                return EnumActionResult.FAIL;

            NBTTagCompound tag = stack.getTagCompound();
            tag.setLong("pos", pos.toLong());
            tag.setInteger("dim", world.provider.getDimension());


            return EnumActionResult.SUCCESS;
        }
    }
}

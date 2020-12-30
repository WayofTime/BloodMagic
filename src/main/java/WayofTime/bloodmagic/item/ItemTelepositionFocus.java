package WayofTime.bloodmagic.item;

import WayofTime.bloodmagic.core.data.Binding;
import WayofTime.bloodmagic.util.Constants;
import WayofTime.bloodmagic.iface.IBindable;
import WayofTime.bloodmagic.util.helper.NBTHelper;
import WayofTime.bloodmagic.core.RegistrarBloodMagicItems;
import WayofTime.bloodmagic.item.types.ISubItem;
import WayofTime.bloodmagic.util.helper.TextHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class ItemTelepositionFocus extends ItemEnum.Variant<ItemTelepositionFocus.FocusType> implements IBindable {

    public ItemTelepositionFocus() {
        super(FocusType.class, "focus");

        setMaxStackSize(1);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        if (player.isSneaking()) {
            RayTraceResult mop = rayTrace(world, player, false);

            if (mop != null && mop.typeOfHit == RayTraceResult.Type.BLOCK) {
                setBlockPos(player.getHeldItem(hand), world, mop.getBlockPos());
            }
        }

        return super.onItemRightClick(world, player, hand);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag) {
        tooltip.addAll(Arrays.asList(TextHelper.cutLongString(TextHelper.localize("tooltip.bloodmagic.telepositionFocus." + getItemType(stack).getInternalName()))));

        super.addInformation(stack, world, tooltip, flag);
        Binding binding = getBinding(stack);
        if (binding != null)
            tooltip.add(TextHelper.localizeEffect("tooltip.bloodmagic.currentOwner", binding.getOwnerName()));

        if (!stack.hasTagCompound())
            return;

        NBTTagCompound tag = stack.getTagCompound();
        BlockPos coords = getBlockPos(stack);

        if (coords != null && tag != null) {
            tooltip.add(TextHelper.localizeEffect("tooltip.bloodmagic.telepositionFocus.coords", coords.getX(), coords.getY(), coords.getZ()));
            tooltip.add(TextHelper.localizeEffect("tooltip.bloodmagic.telepositionFocus.dimension", tag.getInteger(Constants.NBT.DIMENSION_ID)));
        }
    }

    public World getWorld(ItemStack stack) {
        stack = NBTHelper.checkNBT(stack);
        return DimensionManager.getWorld(stack.getTagCompound().getInteger(Constants.NBT.DIMENSION_ID));
    }

    public BlockPos getBlockPos(ItemStack stack) {
        stack = NBTHelper.checkNBT(stack);
        return new BlockPos(stack.getTagCompound().getInteger(Constants.NBT.X_COORD), stack.getTagCompound().getInteger(Constants.NBT.Y_COORD), stack.getTagCompound().getInteger(Constants.NBT.Z_COORD));
    }

    public ItemStack setBlockPos(ItemStack stack, World world, BlockPos pos) {
        stack = NBTHelper.checkNBT(stack);
        NBTTagCompound itemTag = stack.getTagCompound();
        itemTag.setInteger(Constants.NBT.X_COORD, pos.getX());
        itemTag.setInteger(Constants.NBT.Y_COORD, pos.getY());
        itemTag.setInteger(Constants.NBT.Z_COORD, pos.getZ());
        itemTag.setInteger(Constants.NBT.DIMENSION_ID, world.provider.getDimension());
        return stack;
    }

    public enum FocusType implements ISubItem {

        WEAK,
        ENHANCED,
        REINFORCED,
        DEMONIC,
        ;

        @Nonnull
        @Override
        public String getInternalName() {
            return name().toLowerCase(Locale.ROOT);
        }

        @Nonnull
        @Override
        public ItemStack getStack(int count) {
            return new ItemStack(RegistrarBloodMagicItems.TELEPOSITION_FOCUS, count, ordinal());
        }
    }
}

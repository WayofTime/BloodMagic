package WayofTime.bloodmagic.item.alchemy;

import WayofTime.bloodmagic.iface.ICustomAlchemyConsumable;
import WayofTime.bloodmagic.item.ItemEnum;
import WayofTime.bloodmagic.item.types.ISubItem;
import WayofTime.bloodmagic.util.helper.NBTHelper;
import WayofTime.bloodmagic.core.RegistrarBloodMagicItems;
import WayofTime.bloodmagic.util.helper.TextHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Locale;

public class ItemCuttingFluid extends ItemEnum.Variant<ItemCuttingFluid.FluidType> implements ICustomAlchemyConsumable {

    public ItemCuttingFluid() {
        super(FluidType.class, "cutting_fluid");

        setMaxStackSize(1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag) {
        if (!stack.hasTagCompound())
            return;
        int max = getMaxUsesForFluid(stack);
        tooltip.add(TextHelper.localize("tooltip.bloodmagic.cuttingFluidRatio", max - getDamageOfFluid(stack), max));
    }

    public int getDamageOfFluid(ItemStack stack) {
        NBTHelper.checkNBT(stack);
        NBTTagCompound tag = stack.getTagCompound();

        return tag.getInteger("used");
    }

    public void applyDamageToFluid(ItemStack stack) {
        int damage = Math.min(getDamageOfFluid(stack) + 1, getMaxUsesForFluid(stack));
        NBTTagCompound tag = stack.getTagCompound();

        tag.setInteger("used", damage);
    }

    public int getMaxUsesForFluid(ItemStack stack) {
        switch (stack.getMetadata()) {
            case 0:
                return 16;
            case 1:
                return 64;
            default:
                return 1;
        }
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        return (double) (getDamageOfFluid(stack)) / (double) (getMaxUsesForFluid(stack));
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return getDamageOfFluid(stack) > 0;
    }

    @Override
    public ItemStack drainUseOnAlchemyCraft(ItemStack stack) {
        applyDamageToFluid(stack);
        if (getDamageOfFluid(stack) >= getMaxUsesForFluid(stack)) {
            return ItemStack.EMPTY;
        }

        return stack;
    }

    public enum FluidType implements ISubItem {
        BASIC,
        EXPLOSIVE,
        ;

        @Nonnull
        @Override
        public String getInternalName() {
            return name().toLowerCase(Locale.ROOT);
        }

        @Nonnull
        @Override
        public ItemStack getStack(int count) {
            return new ItemStack(RegistrarBloodMagicItems.CUTTING_FLUID, count, ordinal());
        }
    }
}

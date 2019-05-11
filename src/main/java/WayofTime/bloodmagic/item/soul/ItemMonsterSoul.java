package WayofTime.bloodmagic.item.soul;

import WayofTime.bloodmagic.util.Constants;
import WayofTime.bloodmagic.soul.EnumDemonWillType;
import WayofTime.bloodmagic.soul.IDemonWill;
import WayofTime.bloodmagic.util.helper.NBTHelper;
import WayofTime.bloodmagic.core.RegistrarBloodMagicItems;
import WayofTime.bloodmagic.item.ItemEnum;
import WayofTime.bloodmagic.item.types.ISubItem;
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

public class ItemMonsterSoul extends ItemEnum.Variant<ItemMonsterSoul.WillType> implements IDemonWill {

    public ItemMonsterSoul() {
        super(WillType.class, "monster_soul");

        setMaxStackSize(1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag) {
        if (!stack.hasTagCompound())
            return;
        tooltip.add(TextHelper.localize("tooltip.bloodmagic.will", getWill(getType(stack), stack)));

        super.addInformation(stack, world, tooltip, flag);
    }

    @Override
    public EnumDemonWillType getType(ItemStack stack) {
        return EnumDemonWillType.values()[stack.getItemDamage() % 5];
    }

    @Override
    public double getWill(EnumDemonWillType type, ItemStack soulStack) {
        if (type != this.getType(soulStack)) {
            return 0;
        }

        NBTHelper.checkNBT(soulStack);

        NBTTagCompound tag = soulStack.getTagCompound();

        return tag.getDouble(Constants.NBT.SOULS);
    }

    @Override
    public void setWill(EnumDemonWillType type, ItemStack soulStack, double souls) {
        NBTHelper.checkNBT(soulStack);

        NBTTagCompound tag = soulStack.getTagCompound();

        soulStack.setItemDamage(type.ordinal());

        tag.setDouble(Constants.NBT.SOULS, souls);
    }

    @Override
    public double drainWill(EnumDemonWillType type, ItemStack soulStack, double drainAmount) {
        double souls = getWill(type, soulStack);

        double soulsDrained = Math.min(drainAmount, souls);
        setWill(type, soulStack, souls - soulsDrained);

        return soulsDrained;
    }

    @Override
    public ItemStack createWill(int meta, double number) {
        ItemStack soulStack = new ItemStack(this, 1, meta % 5);
        setWill(getType(soulStack), soulStack, number);
        return soulStack;
    }

    @Override
    public double getWill(ItemStack willStack) {
        return this.getWill(EnumDemonWillType.DEFAULT, willStack);
    }

    @Override
    public void setWill(ItemStack willStack, double will) {
        this.setWill(EnumDemonWillType.DEFAULT, willStack, will);
    }

    @Override
    public double drainWill(ItemStack willStack, double drainAmount) {
        return this.drainWill(EnumDemonWillType.DEFAULT, willStack, drainAmount);
    }

    public enum WillType implements ISubItem {

        RAW,
        CORROSIVE,
        DESTRUCTIVE,
        VENGEFUL,
        STEADFAST,
        ;

        @Nonnull
        @Override
        public String getInternalName() {
            return name().toLowerCase(Locale.ROOT);
        }

        @Nonnull
        @Override
        public ItemStack getStack(int count) {
            return new ItemStack(RegistrarBloodMagicItems.MONSTER_SOUL, count, ordinal());
        }
    }
}

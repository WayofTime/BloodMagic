package wayoftime.bloodmagic.common.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import wayoftime.bloodmagic.common.datacomponent.EnumWillType;

import java.util.List;

public class DemonCrystalItem extends Item {
    private final EnumWillType willType;
    private final double willPerCrystal;

    public DemonCrystalItem(EnumWillType willType) {
        this(willType, 50.0);
    }

    public DemonCrystalItem(EnumWillType willType, double willPerCrystal) {
        super(new Properties());
        this.willType = willType;
        this.willPerCrystal = willPerCrystal;
    }

    public EnumWillType getWillType() {
        return willType;
    }

    public double getWill(ItemStack stack) {
        return willPerCrystal * stack.getCount();
    }

    public double getWillPerCrystal() {
        return willPerCrystal;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.bloodmagic.current_type." + willType.getSerializedName()).withStyle(ChatFormatting.GRAY));
        super.appendHoverText(stack, context, tooltip, flag);
    }
}

package com.wayoftime.bloodmagic.item;

import com.wayoftime.bloodmagic.core.network.Binding;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemBindable extends ItemMundane implements IBindable {

    public ItemBindable(String name) {
        super(name);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        Binding binding = getBinding(stack);
        if (binding == null)
            return;

        tooltip.add(I18n.format("tooltip.bloodmagic:bound_owner", binding.getOwnerName()));
    }
}

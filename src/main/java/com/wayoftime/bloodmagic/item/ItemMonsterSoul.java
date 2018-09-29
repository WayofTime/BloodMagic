package com.wayoftime.bloodmagic.item;

import com.wayoftime.bloodmagic.BloodMagic;
import com.wayoftime.bloodmagic.core.util.register.IModelLocator;
import com.wayoftime.bloodmagic.core.will.DemonWill;
import com.wayoftime.bloodmagic.core.will.DemonWillHolder;
import com.wayoftime.bloodmagic.core.will.IWillContainer;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ItemMonsterSoul extends ItemMundane implements IWillContainer, IModelLocator {

    public ItemMonsterSoul() {
        super("monster_soul");

        setMaxStackSize(1);
        addPropertyOverride(new ResourceLocation(BloodMagic.MODID, "will"), (stack, worldIn, entityIn) -> {
            DemonWillHolder holder = getDemonWill(stack);
            return holder == null || holder.getType() == DemonWill.RAW ? 0F : Float.parseFloat("0." + holder.getType().ordinal());
        });
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        for (DemonWill will : DemonWill.VALUES) {
            ItemStack stack = new ItemStack(this);
            applyDemonWill(stack, new DemonWillHolder(will, 0));
            items.add(stack);
        }
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        IWillContainer.appendTooltip(stack, tooltip);
    }

    @Override
    public double getMaxContained(ItemStack stack) {
        DemonWillHolder holder = getDemonWill(stack);
        return holder == null ? Double.MAX_VALUE : holder.getAmount();
    }

    @Nonnull
    @Override
    public ResourceLocation getModelPath() {
        return new ResourceLocation(BloodMagic.MODID, "soul/monster_soul");
    }
}

package com.wayoftime.bloodmagic.item;

import com.wayoftime.bloodmagic.BloodMagic;
import com.wayoftime.bloodmagic.core.util.register.IModelLocator;
import com.wayoftime.bloodmagic.core.will.DemonWill;
import com.wayoftime.bloodmagic.core.will.ISentientEquipment;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class ItemSentientSword extends ItemSword implements ISentientEquipment, IModelLocator {

    public ItemSentientSword() {
        super(ToolMaterial.IRON);

        setRegistryName("sentient_sword");
        setTranslationKey(BloodMagic.MODID + ":sentient_sword");
        setCreativeTab(BloodMagic.TAB_BM);

        addPropertyOverride(new ResourceLocation(BloodMagic.MODID, "will"), (stack, worldIn, entityIn) -> Float.parseFloat("0." + getDemonWill(stack).ordinal()));
        addPropertyOverride(new ResourceLocation(BloodMagic.MODID, "active"), (stack, worldIn, entityIn) -> isActivated(stack) ? 1.0F : 0.0F);
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        for (DemonWill will : DemonWill.VALUES) {
            ItemStack stack = new ItemStack(this);
            NBTTagCompound tag = new NBTTagCompound();
            tag.setString("willType", will.name());
            stack.setTagCompound(tag);
            items.add(stack);
        }
    }

    @Override
    public double getWillDropAmount(ItemStack stack) {
        return 1;
    }

    @Nonnull
    @Override
    public ResourceLocation getModelPath() {
        return new ResourceLocation(BloodMagic.MODID, "soul/sentient_sword");
    }
}

package com.wayoftime.bloodmagic.core.will;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nullable;

public class DemonWillHolder implements INBTSerializable<NBTTagCompound> {

    private DemonWill type;
    private double amount;

    public DemonWillHolder(DemonWill type, double amount) {
        this.type = type;
        this.amount = amount;
    }

    private DemonWillHolder() {
        // No-op
    }

    public DemonWill getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void modifyAmount(double change) {
        this.amount += change;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("type", type.ordinal());
        tag.setDouble("amount", amount);
        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        this.type = DemonWill.VALUES[nbt.getInteger("type")];
        this.amount = nbt.getDouble("amount");
    }

    @Nullable
    public static DemonWillHolder fromStack(ItemStack stack) {
        if (!stack.hasTagCompound()) // Definitely hasn't been bound yet.
            return null;

        NBTBase willTag = stack.getTagCompound().getTag("demonWill");
        if (willTag.getId() != 10 || willTag.isEmpty()) // Make sure it's both a tag compound and that it has actual data.
            return null;

        DemonWillHolder holder = new DemonWillHolder();
        holder.deserializeNBT((NBTTagCompound) willTag);
        return holder;
    }
}

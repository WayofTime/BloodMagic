package com.wayoftime.bloodmagic.core.network;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class NetworkInteraction {

    private static final ITextComponent EMPTY = new TextComponentString("");

    private final ITextComponent description;
    private final int amount;
    private boolean syphon;

    public NetworkInteraction(ITextComponent description, int amount) {
        this.description = description;
        this.amount = amount;
    }

    public NetworkInteraction(int amount) {
        this(EMPTY, amount);
    }

    public boolean isSyphon() {
        return syphon || amount < 0;
    }

    public ITextComponent getDescription() {
        return description;
    }

    public int getAmount() {
        return amount;
    }

    public NetworkInteraction syphon() {
        this.syphon = true;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof NetworkInteraction)
            return ((NetworkInteraction) o).getDescription().equals(description);

        return false;
    }

    @Override
    public int hashCode() {
        return description.hashCode();
    }

    public static NetworkInteraction asBlockInfo(World world, BlockPos pos, int amount) {
        return new NetworkInteraction(new TextComponentString("block|" + world.provider.getDimension() + "|" + pos.toLong()), amount);
    }

    public static NetworkInteraction asItemInfo(ItemStack itemStack, World world, Entity entity, int amount) {
        return new NetworkInteraction(new TextComponentString("item|" + itemStack.getItem().getRegistryName() + "|" + world.provider.getDimension() + "|" + entity.getPersistentID()), amount);
    }

    public static NetworkInteraction asItemInfo(ItemStack itemStack, World world, BlockPos pos, int amount) {
        return new NetworkInteraction(new TextComponentString("item|" + itemStack.getItem().getRegistryName() + "|" + world.provider.getDimension() + "|" + pos.toLong()), amount);
    }

    public static NetworkInteraction asItemInfo(ItemStack itemStack, int amount) {
        return new NetworkInteraction(new TextComponentString("item|" + itemStack.getItem().getRegistryName()), amount);
    }

    public static NetworkInteraction asCommandInfo(ICommandSender sender, String command, int amount) {
        return new NetworkInteraction(new TextComponentString("command|" + command + "|" + sender.getName()), amount);
    }
}

package wayoftime.bloodmagic.util;

import net.minecraft.commands.CommandSource;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;


public class SoulTicket {
    private static final Component EMPTY = Component.literal("");

    private final Component description;
    private final int amount;

    private SoulTicket(Component description, int amount) {
        this.description = description;
        this.amount = amount;
    }

    private SoulTicket(int amount) {
        this(EMPTY, amount);
    }

    public static SoulTicket block(Level level, BlockPos pos, int amount) {
        return new SoulTicket(Component.literal("block|" + level.dimension().location() + "|" + pos.asLong()), amount);
    }

    public static SoulTicket item(ItemStack stack, int amount) {
        return new SoulTicket(Component.literal("item|" + BuiltInRegistries.ITEM.getKey(stack.getItem())), amount);
    }

    public static SoulTicket item(ItemStack stack, Level level, BlockPos pos, int amount) {
        return new SoulTicket(Component.literal("item|" + BuiltInRegistries.ITEM.getKey(stack.getItem()) + "|" + level.dimension().location() + "|" + pos.asLong()), amount);
    }

    public static SoulTicket item(ItemStack stack, Level level, Entity entity, int amount) {
        return new SoulTicket(Component.literal("item|" + BuiltInRegistries.ITEM.getKey(stack.getItem()) + "|" + level.dimension().location() + "|" + entity.getStringUUID()), amount);
    }

    public static SoulTicket command(CommandSource sender, String command, int amount) {
        return new SoulTicket(Component.literal("command|" + command + "|" + sender.toString()), amount);
    }

    public Component getDescription() {
        return description;
    }

    public int getAmount() {
        return amount;
    }

    public boolean isSyphon() {
        return amount < 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (o instanceof SoulTicket other)
            return other.getDescription().equals(description);

        return false;
    }

    @Override
    public int hashCode() {
        return description.hashCode();
    }
}

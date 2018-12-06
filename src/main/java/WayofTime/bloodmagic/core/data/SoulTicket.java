package WayofTime.bloodmagic.core.data;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class SoulTicket {

    private static final ITextComponent EMPTY = new TextComponentString("");

    private final ITextComponent description;
    private final int amount;

    public SoulTicket(ITextComponent description, int amount) {
        this.description = description;
        this.amount = amount;
    }

    public SoulTicket(int amount) {
        this(EMPTY, amount);
    }

    public boolean isSyphon() {
        return amount < 0;
    }

    public ITextComponent getDescription() {
        return description;
    }

    public int getAmount() {
        return amount;
    }

    /**
     * @return A description in the format block|dimensionID|pos
     */
    public static SoulTicket block(World world, BlockPos pos, int amount) {
        return new SoulTicket(new TextComponentString("block|" + world.provider.getDimension() + "|" + pos.toLong()), amount);
    }

    /**
     * @return A description in the format item|item registry name|dimensionID|entityName|entityPos
     */
    public static SoulTicket item(ItemStack itemStack, World world, Entity entity, int amount) {
        return new SoulTicket(new TextComponentString("item|" + itemStack.getItem().getRegistryName() + "|" + world.provider.getDimension() + "|" + entity.getPersistentID()), amount);
    }

    /**
     * @return A description in the format item|item registry name|dimensionID|pos
     */
    public static SoulTicket item(ItemStack itemStack, World world, BlockPos pos, int amount) {
        return new SoulTicket(new TextComponentString("item|" + itemStack.getItem().getRegistryName() + "|" + world.provider.getDimension() + "|" + pos.toLong()), amount);
    }

    /**
     * @return A description in the format item|item registry name|dimensionID
     */
    public static SoulTicket item(ItemStack itemStack, int amount) {
        return new SoulTicket(new TextComponentString("item|" + itemStack.getItem().getRegistryName()), amount);
    }

    public static SoulTicket command(ICommandSender sender, String command, int amount) {
        return new SoulTicket(new TextComponentString("command|" + command + "|" + sender.getName()), amount);
    }

    // TODO maybe make it check the amount??
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof SoulTicket)
            return ((SoulTicket) o).getDescription().equals(description);

        return false;
    }

    @Override
    public int hashCode() {
        return description.hashCode();
    }
}

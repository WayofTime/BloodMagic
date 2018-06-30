package WayofTime.bloodmagic.core.data;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

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
}

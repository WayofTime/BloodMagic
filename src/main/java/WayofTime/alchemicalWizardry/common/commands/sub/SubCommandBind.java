package WayofTime.alchemicalWizardry.common.commands.sub;

import WayofTime.alchemicalWizardry.api.command.SubCommandBase;
import WayofTime.alchemicalWizardry.api.items.interfaces.IBindable;
import WayofTime.alchemicalWizardry.common.items.EnergyItems;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.StatCollector;

public class SubCommandBind extends SubCommandBase {

    public SubCommandBind(ICommand parent) {
        super(parent, "bind");
    }

    @Override
    public String getArgUsage(ICommandSender commandSender) {
        return StatCollector.translateToLocal("commands.bind.usage");
    }

    @Override
    public String getHelpText() {
        return StatCollector.translateToLocal("commands.bind.help");
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public void processSubCommand(ICommandSender commandSender, String[] args) {
        super.processSubCommand(commandSender, args);

        EntityPlayer player = getCommandSenderAsPlayer(commandSender);
        String playerName = player.getCommandSenderName();
        ItemStack held = player.getHeldItem();
        boolean bind = true;

        if (held != null && held.getItem() instanceof IBindable) {
            if (args.length > 0) {

                if (args[0].equalsIgnoreCase("help"))
                    return;

                if (isBoolean(args[0])) {
                    bind = Boolean.parseBoolean(args[0]);

                    if (args.length > 2)
                        playerName = args[1];
                } else {
                    playerName = args[0];
                }
            }

            if (bind) {
                EnergyItems.setItemOwner(held, playerName);
                commandSender.addChatMessage(new ChatComponentTranslation("commands.bind.success"));
            } else {
                if (!EnergyItems.getOwnerName(held).isEmpty()) {
                    held.stackTagCompound.removeTag("ownerName");
                    commandSender.addChatMessage(new ChatComponentTranslation("commands.bind.remove.success"));
                }
            }
        }
    }

    private boolean isBoolean(String string) {
        return string.equalsIgnoreCase("true") || string.equalsIgnoreCase("false");
    }
}

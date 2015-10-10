package WayofTime.alchemicalWizardry.common.commands.sub;

import WayofTime.alchemicalWizardry.api.command.ISubCommand;
import WayofTime.alchemicalWizardry.api.command.SubCommandBase;
import WayofTime.alchemicalWizardry.common.commands.CommandBloodMagic;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;

public class SubCommandHelp extends SubCommandBase {

    public SubCommandHelp(ICommand parent) {
        super(parent, "help");
    }

    @Override
    public String getArgUsage(ICommandSender commandSender) {
        return "/bloodmagic help";
    }

    @Override
    public String getHelpText() {
        return "Displays the help information for the \"/bloodmagic\" command.";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public void processSubCommand(ICommandSender commandSender, String[] args) {
        super.processSubCommand(commandSender, args);

        if (args.length > 0)
            return;

        for (ISubCommand subCommand : ((CommandBloodMagic)getParentCommand()).getSubCommands().values())
            commandSender.addChatMessage(new ChatComponentText(String.format("%s - %s", capitalizeFirstLetter(subCommand.getSubCommandName()), subCommand.getArgUsage(commandSender))).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GREEN)));
    }
}

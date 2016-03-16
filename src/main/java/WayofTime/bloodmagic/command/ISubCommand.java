package WayofTime.bloodmagic.command;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;

public interface ISubCommand
{

    String getSubCommandName();

    ICommand getParentCommand();

    String getArgUsage(ICommandSender commandSender);

    String getHelpText();

    void processSubCommand(ICommandSender commandSender, String[] args);
}
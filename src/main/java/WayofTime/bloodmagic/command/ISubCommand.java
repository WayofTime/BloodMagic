package WayofTime.bloodmagic.command;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public interface ISubCommand
{

    String getSubCommandName();

    ICommand getParentCommand();

    String getArgUsage(ICommandSender commandSender);

    String getHelpText();

    void processSubCommand(MinecraftServer server, ICommandSender commandSender, String[] args);
}
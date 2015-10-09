package WayofTime.alchemicalWizardry.api.command;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;

public interface ISubCommand {

    String getSubCommandName();

    ICommand getParentCommand();

    String getArgUsage(ICommandSender commandSender);

    String getHelpText();

    int getRequiredPermissionLevel();

    boolean canSenderUseSubCommand(ICommandSender commandSender);

    void processSubCommand(ICommandSender commandSender, String[] args);
}

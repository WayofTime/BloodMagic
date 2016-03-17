package WayofTime.bloodmagic.command.sub;

import WayofTime.bloodmagic.command.CommandBloodMagic;
import WayofTime.bloodmagic.command.ISubCommand;
import WayofTime.bloodmagic.command.SubCommandBase;
import WayofTime.bloodmagic.util.helper.TextHelper;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

public class SubCommandHelp extends SubCommandBase
{

    public SubCommandHelp(ICommand parent)
    {
        super(parent, "help");
    }

    @Override
    public String getArgUsage(ICommandSender commandSender)
    {
        return TextHelper.localize("commands.help.usage");
    }

    @Override
    public String getHelpText()
    {
        return TextHelper.localizeEffect("commands.help.help");
    }

    @Override
    public void processSubCommand(MinecraftServer server, ICommandSender commandSender, String[] args)
    {
        super.processSubCommand(server, commandSender, args);

        if (args.length > 0)
            return;

        for (ISubCommand subCommand : ((CommandBloodMagic) getParentCommand()).getSubCommands().values())
            commandSender.addChatMessage(new TextComponentString(TextHelper.localizeEffect("commands.format.help", capitalizeFirstLetter(subCommand.getSubCommandName()), subCommand.getArgUsage(commandSender))));
    }
}

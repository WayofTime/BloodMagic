package WayofTime.bloodmagic.command.sub;

import WayofTime.bloodmagic.command.CommandBloodMagic;
import WayofTime.bloodmagic.command.ISubCommand;
import WayofTime.bloodmagic.command.SubCommandBase;
import WayofTime.bloodmagic.util.helper.TextHelper;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;

public class SubCommandHelp extends SubCommandBase
{

    public SubCommandHelp(ICommand parent)
    {
        super(parent, "help");
    }

    @Override
    public String getArgUsage(ICommandSender commandSender)
    {
        return StatCollector.translateToLocal("commands.help.usage");
    }

    @Override
    public String getHelpText()
    {
        return StatCollector.translateToLocal("commands.help.help");
    }

    @Override
    public void processSubCommand(ICommandSender commandSender, String[] args)
    {
        super.processSubCommand(commandSender, args);

        if (args.length > 0)
            return;

        for (ISubCommand subCommand : ((CommandBloodMagic) getParentCommand()).getSubCommands().values())
            commandSender.addChatMessage(new ChatComponentText(TextHelper.localizeEffect("commands.format.help", capitalizeFirstLetter(subCommand.getSubCommandName()), subCommand.getArgUsage(commandSender))));
    }
}

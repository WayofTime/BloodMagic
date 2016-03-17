package WayofTime.bloodmagic.command;

import WayofTime.bloodmagic.util.helper.TextHelper;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

import java.util.Locale;

public abstract class SubCommandBase implements ISubCommand
{

    private ICommand parent;
    private String name;

    public SubCommandBase(ICommand parent, String name)
    {
        this.parent = parent;
        this.name = name;
    }

    @Override
    public String getSubCommandName()
    {
        return name;
    }

    @Override
    public ICommand getParentCommand()
    {
        return parent;
    }

    @Override
    public void processSubCommand(MinecraftServer server, ICommandSender commandSender, String[] args)
    {

        if (args.length == 0 && !getSubCommandName().equals("help"))
            displayErrorString(commandSender, String.format(TextHelper.localizeEffect("commands.format.error"), capitalizeFirstLetter(getSubCommandName()), getArgUsage(commandSender)));

        if (isBounded(0, 2, args.length) && args[0].equals("help"))
            displayHelpString(commandSender, String.format(TextHelper.localizeEffect("commands.format.help"), capitalizeFirstLetter(getSubCommandName()), getHelpText()));
    }

    protected String capitalizeFirstLetter(String toCapital)
    {
        return String.valueOf(toCapital.charAt(0)).toUpperCase(Locale.ENGLISH) + toCapital.substring(1);
    }

    protected boolean isBounded(int low, int high, int given)
    {
        return given > low && given < high;
    }

    protected void displayHelpString(ICommandSender commandSender, String display, Object... info)
    {
        commandSender.addChatMessage(new TextComponentString(TextHelper.localizeEffect(display, info)));
    }

    protected void displayErrorString(ICommandSender commandSender, String display, Object... info)
    {
        commandSender.addChatMessage(new TextComponentString(TextHelper.localizeEffect(display, info)));
    }

    protected void displaySuccessString(ICommandSender commandSender, String display, Object... info)
    {
        commandSender.addChatMessage(new TextComponentString(TextHelper.localizeEffect(display, info)));
    }
}

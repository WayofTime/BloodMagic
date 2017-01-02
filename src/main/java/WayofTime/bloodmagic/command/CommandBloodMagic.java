package WayofTime.bloodmagic.command;

import WayofTime.bloodmagic.command.sub.SubCommandBind;
import WayofTime.bloodmagic.command.sub.SubCommandNetwork;
import WayofTime.bloodmagic.command.sub.SubCommandOrb;
import WayofTime.bloodmagic.util.helper.TextHelper;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.server.command.CommandTreeBase;

public class CommandBloodMagic extends CommandTreeBase
{
    public CommandBloodMagic()
    {
        addSubcommand(new SubCommandBind());
        addSubcommand(new SubCommandNetwork());
        addSubcommand(new SubCommandOrb());
    }

    @Override
    public String getName()
    {
        return "bloodmagic";
    }

    @Override
    public String getUsage(ICommandSender sender)
    {
        return "/bloodmagic help";
    }

    public static void displayHelpString(ICommandSender commandSender, String display, Object... info)
    {
        commandSender.sendMessage(new TextComponentString(TextHelper.localizeEffect(display, info)));
    }

    public static void displayErrorString(ICommandSender commandSender, String display, Object... info)
    {
        commandSender.sendMessage(new TextComponentString(TextHelper.localizeEffect(display, info)));
    }

    public static void displaySuccessString(ICommandSender commandSender, String display, Object... info)
    {
        commandSender.sendMessage(new TextComponentString(TextHelper.localizeEffect(display, info)));
    }
}

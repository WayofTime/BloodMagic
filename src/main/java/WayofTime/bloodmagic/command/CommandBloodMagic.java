package WayofTime.bloodmagic.command;

import WayofTime.bloodmagic.command.sub.SubCommandBind;
import WayofTime.bloodmagic.command.sub.SubCommandHelp;
import WayofTime.bloodmagic.command.sub.SubCommandNetwork;
import WayofTime.bloodmagic.command.sub.SubCommandOrb;
import WayofTime.bloodmagic.util.helper.TextHelper;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

import java.util.*;

public class CommandBloodMagic extends CommandBase
{

    private final List<String> aliases = new ArrayList<String>();
    private final Map<String, ISubCommand> subCommands = new HashMap<String, ISubCommand>();

    public CommandBloodMagic()
    {
        aliases.add("BloodMagic");
        aliases.add("bloodmagic");
        aliases.add("bloodMagic");
        aliases.add("bm");

        subCommands.put("help", new SubCommandHelp(this));
        subCommands.put("network", new SubCommandNetwork(this));
        subCommands.put("bind", new SubCommandBind(this));
        subCommands.put("orb", new SubCommandOrb(this));
    }

    @Override
    public String getCommandName()
    {
        return "/bloodmagic";
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 3;
    }

    @Override
    public String getCommandUsage(ICommandSender commandSender)
    {
        return getCommandName() + " help";
    }

    @Override
    public List<String> getCommandAliases()
    {
        return aliases;
    }

    @Override
    public void processCommand(ICommandSender commandSender, String[] args)
    {
        if (args.length > 0 && subCommands.containsKey(args[0]))
        {

            ISubCommand subCommand = subCommands.get(args[0]);
            String[] subArgs = Arrays.copyOfRange(args, 1, args.length);
            subCommand.processSubCommand(commandSender, subArgs);
        } else
        {
            commandSender.addChatMessage(new ChatComponentText(TextHelper.localizeEffect("commands.error.unknown")));
        }
    }

    public Map<String, ISubCommand> getSubCommands()
    {
        return subCommands;
    }
}

package WayofTime.alchemicalWizardry.common.commands;

import WayofTime.alchemicalWizardry.api.command.ISubCommand;
import WayofTime.alchemicalWizardry.common.commands.sub.SubCommandBind;
import WayofTime.alchemicalWizardry.common.commands.sub.SubCommandHelp;
import WayofTime.alchemicalWizardry.common.commands.sub.SubCommandNetwork;
import WayofTime.alchemicalWizardry.common.commands.sub.SubCommandOrb;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;

import java.util.*;

public class CommandBloodMagic extends CommandBase {

    private final List aliases = new ArrayList();
    private final Map<String, ISubCommand> subCommands = new HashMap<String, ISubCommand>();

    @SuppressWarnings("unchecked")
    public CommandBloodMagic() {
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
    public String getCommandName() {
        return "/bloodmagic";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 3;
    }

    @Override
    public String getCommandUsage(ICommandSender commandSender) {
        return getCommandName() + " help";
    }

    @Override
    public List getCommandAliases() {
        return aliases;
    }

    @Override
    public void processCommand(ICommandSender commandSender, String[] args) {
        if (args.length > 0 && subCommands.containsKey(args[0])) {

            ISubCommand subCommand = subCommands.get(args[0]);
            String[] subArgs = Arrays.copyOfRange(args, 1, args.length);

            if (subCommand.canSenderUseSubCommand(commandSender))
                subCommand.processSubCommand(commandSender, subArgs);
            else
                commandSender.addChatMessage(new ChatComponentTranslation("commands.error.perm").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
        } else {
            commandSender.addChatMessage(new ChatComponentTranslation("commands.error.unknown").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
        }
    }

    public Map<String, ISubCommand> getSubCommands() {
        return subCommands;
    }
}

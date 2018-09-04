package WayofTime.bloodmagic.command;

import WayofTime.bloodmagic.command.sub.SubCommandBind;
import WayofTime.bloodmagic.command.sub.SubCommandNetwork;
import WayofTime.bloodmagic.command.sub.SubCommandOrb;
import WayofTime.bloodmagic.command.sub.SubCommandRitual;
import WayofTime.bloodmagic.util.helper.TextHelper;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.server.command.CommandTreeBase;
import net.minecraftforge.server.command.CommandTreeHelp;

public class CommandBloodMagic extends CommandTreeBase {
    public CommandBloodMagic() {
        addSubcommand(new SubCommandBind());
        addSubcommand(new SubCommandNetwork());
        addSubcommand(new SubCommandOrb());
        addSubcommand(new SubCommandRitual());
        addSubcommand(new CommandTreeHelp(this));
    }

    @Override
    public String getName() {
        return "bloodmagic";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/bloodmagic help";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    public static void displayHelpString(ICommandSender commandSender, String display, Object... info) {
        commandSender.sendMessage(new TextComponentString(TextHelper.localizeEffect(display, info)));
    }

    public static void displayErrorString(ICommandSender commandSender, String display, Object... info) {
        commandSender.sendMessage(new TextComponentString(TextHelper.localizeEffect(display, info)));
    }

    public static void displaySuccessString(ICommandSender commandSender, String display, Object... info) {
        commandSender.sendMessage(new TextComponentString(TextHelper.localizeEffect(display, info)));
    }


}
